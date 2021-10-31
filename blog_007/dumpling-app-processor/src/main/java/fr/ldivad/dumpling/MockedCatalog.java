package fr.ldivad.dumpling;

import java.util.HashMap;
import java.util.Map;

public class MockedCatalog {
  static class Item {
    Long price;
    String name;

    private Item(final String name, final Long price) {
      this.name = name;
      this.price = price;
    }

    public Long getPrice() {
      return price;
    }

    public String getName() {
      return name;
    }
  }

  public static final Map<String, Item> ITEMS = new HashMap<>() {{
    put("1", new Item("Shui Jiao", 5L));
    put("2", new Item("Xiao Long Bao", 2L));
    put("3", new Item("Guo Tie", 10L));
    put("4", new Item("Wonton", 15L));
  }};

  public static Long getProductPriceById(String productId) {
    return ITEMS.get(productId).price;
  }
  public static Long getProductPrice(Product product) {
    return ITEMS.get(product.getItemId()).price;
  }
  public static Long computeTotalPrice(Product product) {
    return product.getQuantity() * getProductPrice(product);
  }
}
