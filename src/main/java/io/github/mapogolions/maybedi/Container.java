package io.github.mapogolions.maybedi;

import java.util.function.*;
import java.util.Map;
import java.util.HashMap;

// import io.github.mapogolions.maybedi.UnknownIdentifierException;

public class Container {
  private Map<Class<?>, Function<Container, ?>> services = new HashMap<>();
  private Map<Class<?>, Boolean> frozenServies = new HashMap<>();
  private Map<Class<?>, Object> instantiatedServices = new HashMap<>();

  public <T> Container putService(Class<T> type, Function<Container, T> service) {
    services.put(type, service);
    return this;
  }

  public <T> T getService(Class<T> type) {
    return type.cast(services.get(type).apply(this));
  }

  public <T> T offsetGet(Class<T> type) throws UnknownIdentifierException {
    if (!services.containsKey(type)) {
      throw new UnknownIdentifierException(type.getName());
    }
    T service = type.cast(services.get(type).apply(this));
    frozenServies.put(type, true);
    instantiatedServices.put(type, service);
    return service;
  }
}
