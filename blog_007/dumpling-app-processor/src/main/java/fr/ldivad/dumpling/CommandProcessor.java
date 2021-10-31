package fr.ldivad.dumpling;

import com.google.datastore.v1.ArrayValue;
import com.google.datastore.v1.Entity;
import com.google.datastore.v1.Key.PathElement;
import com.google.datastore.v1.Value;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.transforms.MapElements;
import org.apache.beam.sdk.transforms.windowing.AfterProcessingTime;
import org.apache.beam.sdk.transforms.windowing.FixedWindows;
import org.apache.beam.sdk.transforms.windowing.Repeatedly;
import org.apache.beam.sdk.transforms.windowing.Window;
import org.apache.beam.sdk.values.KV;
import org.apache.beam.sdk.values.TypeDescriptor;
import org.apache.beam.sdk.values.TypeDescriptors;
import org.joda.time.Duration;

public class CommandProcessor {

  final static Duration WINDOW_SIZE = Duration.standardHours(3);
  final static Duration WINDOW_GRACE = Duration.standardSeconds(3);

  static class KeyValueCommandMapper {

    public KeyValueCommandMapper() {}

    private final TypeDescriptor<KV<String, Command>> keyValueDescriptor =
        TypeDescriptors.kvs(TypeDescriptors.strings(), TypeDescriptor.of(Command.class));

    private MapElements<?, KV<String, Command>> getMapElements() {
      return MapElements.into(keyValueDescriptor);
    }

    public MapElements<Command, KV<String, Command>> map() {
      return getMapElements().via((command) -> KV.of(command.getSession().getUserId(), command));
    }
  }

  public static Window<KV<String, Command>> windowingCommand() {

    final FixedWindows window = FixedWindows.of(WINDOW_SIZE);

    final AfterProcessingTime processingTime = AfterProcessingTime.pastFirstElementInPane();

    final Repeatedly repeatedly = Repeatedly.forever(processingTime.plusDelayOf(WINDOW_GRACE));

    return Window.<KV<String, Command>>into(window)
        .triggering(repeatedly)
        .withAllowedLateness(WINDOW_GRACE)
        .accumulatingFiredPanes();
  }

  static class CartEntityMapper extends DoFn<KV<String, Cart>, Entity> {
    private Value datastore(String value) {
      return Value.newBuilder().setStringValue(value).build();
    }

    private Value datastore(Long value) {
      return Value.newBuilder().setIntegerValue(value).build();
    }

    private Value datastore(ArrayValue.Builder value) {
      return Value.newBuilder().setArrayValue(value).build();
    }

    private ArrayValue.Builder wrapProductInArrayValue(Collection<Product> products) {
      final List<Value> productValues = products.stream().map((product) -> {

        final HashMap<String, Value> productMap = new HashMap<>() {{
          put("id", datastore(product.getItemId()));
          put("name", datastore(product.getItemName()));
          put("quantity", datastore(product.getQuantity()));
          put("price", datastore(product.getPrice()));
        }};

        final Entity.Builder productEntity = Entity.newBuilder().putAllProperties(productMap);
        return Value.newBuilder().setEntityValue(productEntity).build();

      }).collect(Collectors.toList());

      return ArrayValue.newBuilder().addAllValues(productValues);
    }

    @DoFn.ProcessElement
    public void processElement(@Element KV<String, Cart> cart, OutputReceiver<Entity> out) {
      final PathElement pathElement = PathElement
          .newBuilder()
          .setKind("customer-cart")
          .setName(cart.getKey())
          .build();

      final com.google.datastore.v1.Key key = com.google.datastore.v1.Key
          .newBuilder()
          .addPath(pathElement)
          .build();

      final ArrayValue.Builder productArray =
          wrapProductInArrayValue(cart.getValue().getProductsMap().values());

      final HashMap<String, Value> hashMap = new HashMap<>() {{
        put("user", datastore(cart.getKey()));
        put("total", datastore(cart.getValue().getTotal()));
        put("cart", datastore(productArray));
      }};

      final Entity entity = Entity
          .newBuilder()
          .setKey(key)
          .putAllProperties(hashMap)
          .build();

      out.output(entity);
    }
  }
}
