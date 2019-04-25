package io.github.mapogolions.maybedi;

import java.util.function.*;
import java.util.Map;
import java.util.HashMap;

import io.github.mapogolions.maybedi.UnknownIdentifierException;
import io.github.mapogolions.maybedi.FrozenServiceException;


public class Container {
  final private Map<Class<?>, Function<Container, ?>> services = new HashMap<>();
  final private Map<Class<?>, Object> assemblies = new HashMap<>();
  final private Map<Class<?>, Boolean> factories = new HashMap<>();
  final private Map<String, Object> namespace = new HashMap<>();

  public <T> T get(Class<T> type) throws UnknownIdentifierException {
    if (!services.containsKey(type)) {
      throw new UnknownIdentifierException(type.getName());
    }
    if (factories.containsKey(type)) {
      return type.cast(services.get(type).apply(this));
    }
    if (assemblies.containsKey(type)) {
      return type.cast(assemblies.get(type));
    }
    T service = type.cast(services.get(type).apply(this));
    assemblies.put(type, service);
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
      assemblies.remove(type);
      return true;
    }
    return false;
  }

  public <T> boolean contains(Class<T> type) {
    return services.containsKey(type);
  }

  public <T> Container assemblyLine(Class<T> type, Function<Container, T> service) {
    if (services.containsKey(type)) {
      throw new FrozenServiceException(service.toString());
    }
    services.put(type, service);
    factories.put(type, true);
    return this;
  }

  public <T> Container pollute(String id, T item) {
    namespace.put(id, item);
    return this;
  }

  public <T> Object global(String id) {
    if (!namespace.containsKey(id)) {
      throw new UnknownIdentifierException(id);
    }
    return namespace.get(id);
  }
}
