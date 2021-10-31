package fr.ldivad.dumpling;

import com.google.common.collect.ImmutableList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.beam.sdk.transforms.Combine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommandAggregator extends Combine.CombineFn<Command, CartAccumulator, Cart> {

  private final Logger logger = LoggerFactory.getLogger(CommandAggregator.class);

  private Product deriveProduct(final Command command) {
    return Product
        .newBuilder()
        .setItemId(command.getItemId())
        .setQuantity(command.getOperation() == Command.Operation.ADD ? 1 : -1)
        .build();
  }

  @Override
  public CartAccumulator createAccumulator() {
    return CartAccumulator
        .newBuilder()
        .build();
  }

  @Override
  public CartAccumulator addInput(final CartAccumulator accumulator, final Command input) {

    final Product product = deriveProduct(input);
    final List<Product> products = Optional.of(accumulator)
        .map(CartAccumulator::getProductsList)
        .orElse(Collections.emptyList());

    final ImmutableList<Product> plusOneProduct =
        ImmutableList.<Product>builder().addAll(products).add(product).build();

    return CartAccumulator
        .newBuilder()
        .addAllProducts(plusOneProduct)
        .build();
  }

  @Override
  public CartAccumulator mergeAccumulators(final Iterable<CartAccumulator> accumulators) {

    final CartAccumulator.Builder accumulatorBuilder = CartAccumulator.newBuilder();
    accumulators.iterator().forEachRemaining((accumulator) -> {
      accumulatorBuilder.addAllProducts(accumulator.getProductsList());
    });

    return accumulatorBuilder.build();
  }

  @Override
  public Cart extractOutput(final CartAccumulator accumulator) {
    final Optional<List<Product>> maybeProductList = Optional
        .of(accumulator)
        .map(CartAccumulator::getProductsList);

    maybeProductList.ifPresentOrElse((p) -> {}, () -> logger.warn("Empty accumulator"));

    final List<Product> productsList = maybeProductList.orElse(Collections.emptyList());

    final Map<String, Product> cart = productsList.stream().collect(Collectors.collectingAndThen(

        Collectors.groupingBy(Product::getItemId),
        (groupedProducts) -> groupedProducts.entrySet().stream().flatMap((itemEntry) -> {
          final String itemId = itemEntry.getKey();
          final List<Product> items = itemEntry.getValue();

          final Long itemCount = items
              .stream()
              .map(Product::getQuantity)
              .reduce(Long::sum)
              .orElse(0L);

          if (itemCount <= 0) {
            logger.warn("Number of item is going back to zero.");
            return Stream.empty();
          } else {
            final Product result = Product
                .newBuilder()
                .setItemId(itemId)
                .setQuantity(itemCount)
                .setPrice(MockedCatalog.getProductPriceById(itemId))
                .build();

            return Stream.of(Map.entry(itemId, result));
          }

        }).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue))
    ));

    final Long total = cart
        .values()
        .stream()
        .map(MockedCatalog::computeTotalPrice)
        .reduce(Long::sum)
        .orElse(0L);

    return Cart
        .newBuilder()
        .setTotal(total)
        .putAllProducts(cart)
        .build();
  }
}
