package io.github.mapogolions.maybedi;

import java.util.function.*;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.github.mapogolions.maybedi.UnknownIdentifierException;
import io.github.mapogolions.maybedi.FrozenServiceException;


public class Container {
  private Map<Class<?>, Function<Container, ?>> services = new HashMap<>();
  private Map<Class<?>, Object> instances = new HashMap<>();
  private List<Function<Container, ?>> factories = new ArrayList<>();
  private Map<String, Object> params = new HashMap<>();

  public <T> T get(Class<T> type) throws UnknownIdentifierException {
    if (!services.containsKey(type)) {
      throw new UnknownIdentifierException(type.getName());
    }
    if (instances.containsKey(type)) {
      return type.cast(instances.get(type));
    }
    if (factories.contains(services.get(type))) {
      return type.cast(services.get(type).apply(this));
    }
    T service = type.cast(services.get(type).apply(this));
    instances.put(type, service);
    return service;
  }

  public <T> Container put(Class<T> type, Function<Container, T> service) {
    if (services.containsKey(type)) {
      throw new FrozenServiceException(type.getName());
    }
    services.put(type, service);
    return this;
  }

  public <T> boolean remove(Class<T> type) {
    if (services.containsKey(type)) {
      services.remove(type);
      instances.remove(type);
      return true;
    }
    return false;
  }

  public <T> boolean contains(Class<T> type) {
    return services.containsKey(type);
  }

  public <T> Container assemblyLine(Function<Container, T> service) {
    if (factories.contains(service)) {
      throw new FrozenServiceException(service.toString());
    }
    factories.add(service);
    return this;
  }

  public <T> Container pollute(String id, T item) {
    params.put(id, item);
    return this;
  }

  public <T> Object global(String id) {
    if (!params.containsKey(id)) {
      throw new UnknownIdentifierException(id);
    }
    return params.get(id);
  }
}
