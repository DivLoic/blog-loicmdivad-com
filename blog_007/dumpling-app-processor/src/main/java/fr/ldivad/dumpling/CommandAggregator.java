package fr.ldivad.dumpling;

import fr.ldivad.dumpling.Cart.Product;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.apache.beam.sdk.transforms.Combine;

public class CommandAggregator extends Combine.CombineFn<Command, Cart, Cart> {

  @Override
  public Cart createAccumulator() {
    return Cart.newBuilder()
        .setCartId("")
        .setTotal(0)
        .build();
  }

  @Override
  public Cart addInput(final Cart mutableAccumulator, final Command input) {

    if (mutableAccumulator.getProductsMap().containsKey(input.getItemId())) {
      final Product product = mutableAccumulator.getProductsMap().get(input.getItemId());
      long newCount = (input.getOperation() == Command.Operation.ADD) ? product.getQuantity() + 1
                                                                      : product.getQuantity() - 1;

      if(newCount > 0) {
        final Product newProduct = product
            .newBuilderForType()
            .setQuantity(newCount)
            .build();
        mutableAccumulator.getProductsMap().put(input.getItemId(), newProduct);

      } else {
        mutableAccumulator.getProductsMap().remove(input.getItemId());
      }



    } else {
      mutableAccumulator
          .getProductsMap()
          .put(
              input.getItemId(),
              Product
                  .newBuilder()
                  .setQuantity(1L)
                  .setPrice(10)
                  .setItemId(input.getItemId())
                  .setItemName(input.getItemName())
                  .build()
          );
    }

    mutableAccumulator
        .newBuilderForType()
        .setCartId(input.getSession().getStoreId())
        .build();

    return mutableAccumulator;
  }

  @Override
  public Cart mergeAccumulators(final Iterable<Cart> accumulators) {
    final Map<String, List<Product>> cartRows = StreamSupport
        .stream(accumulators.spliterator(), false)
        .flatMap((maybeCart) -> Optional
            .ofNullable(maybeCart)
            .map(Cart::getProductsMap)
            .map(Map::entrySet)
            .orElseGet(Collections::emptySet)
            .stream()
        )

        .collect(
            Collectors.collectingAndThen(Collectors.groupingBy(Map.Entry::getKey),
                (entries) -> entries.entrySet().stream().collect(
                    Collectors.toMap(Map.Entry::getKey, (entry) -> entry.getValue().stream().map(
                        Map.Entry::getValue).collect(Collectors.toList())))));

    final Map<String, Product> newProductMap = cartRows.entrySet().stream().flatMap((row) -> {
      final Optional<Long> quantityInARow =
          row.getValue().stream().map(Product::getQuantity).reduce(Long::sum);

      return row
          .getValue()
          .stream()
          .findFirst()
          .flatMap((product) ->
              quantityInARow.map((quantity) ->
                  product.newBuilderForType().setQuantity(quantity).build()
              )
          ).stream();

    }).collect(Collectors.toMap(Product::getItemId, (product) -> product));

    final Cart newCart = StreamSupport
        .stream(accumulators.spliterator(), false)
        .findAny()
        .orElseGet(() -> Cart.newBuilder().build());

    newCart.getProductsMap().clear();
    newCart.getProductsMap().putAll(newProductMap);

    return newCart;
  }

  @Override
  public Cart extractOutput(final Cart accumulator) {
    final Optional<Long> maybeTotal = Optional
        .ofNullable(accumulator)
        .flatMap((acc) -> acc
            .getProductsMap()
            .values()
            .stream()
            .map((product) -> product.getQuantity() * product.getPrice())
            .reduce(Long::sum)
        );

    return accumulator.newBuilderForType().setTotal(maybeTotal.orElse(0L)).build();
  }
}
