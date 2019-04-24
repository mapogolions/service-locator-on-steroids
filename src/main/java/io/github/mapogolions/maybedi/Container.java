package io.github.mapogolions.maybedi;

import java.util.function.*;
import java.util.Map;
import java.util.HashMap;

public class Container {
  private Map<String, Function<Container, ? extends Object>> services = new HashMap<>();

  public <T> Container putService(String id, Function<Container, T> service) {
      services.put(id, service);
      return this;
  }

  public Object getService(String id) {
      Function<Container, ? extends Object> func = services.get(id);
      return func.apply(this);
  }
}
