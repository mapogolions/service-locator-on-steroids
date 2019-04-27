package io.github.mapogolions.eel;

import java.util.function.*;
import java.util.Map;
import java.util.HashMap;


public class Container {
  final private Map<Class<?>, Function<Container, ?>> services = new HashMap<>();
  final private Map<Class<?>, Object> assemblies = new HashMap<>();
  final private Map<Class<?>, Boolean> shared = new HashMap<>();
  final private Map<String, Object> namespace = new HashMap<>();

  public <T> T get(Class<T> type) throws UnknownIdentifierException {
    if (!services.containsKey(type)) {
      throw new UnknownIdentifierException(type.getName());
    }
    if (shared.containsKey(type)) {
      if (assemblies.containsKey(type)) {
        return type.cast(assemblies.get(type));
      }
      T service = type.cast(services.get(type).apply(this));
      assemblies.put(type, service);
      return service;
    }
    return type.cast(services.get(type).apply(this));
  }

  public <T> Container put(Class<T> type, Function<Container, T> service) 
    throws FrozenServiceException {
    if (services.containsKey(type)) {
      throw new FrozenServiceException(type.getName());
    }
    services.put(type, service);
    return this;
  }

  public <T> boolean remove(Class<T> type) {
    if (services.containsKey(type)) {
      if (shared.containsKey(type)) {
        shared.remove(type);
      }
      services.remove(type);
      assemblies.remove(type);
      return true;
    }
    return false;
  }

  public <T> boolean contains(Class<T> type) {
    return services.containsKey(type);
  }

  public <T> Container share(Class<T> type, Function<Container, T> service) 
    throws FrozenServiceException {
    if (services.containsKey(type)) {
      throw new FrozenServiceException(service.toString());
    }
    services.put(type, service);
    shared.put(type, true);
    return this;
  }

  public <T> Container extend(Class<T> type, BiFunction<T, Container, T> decorator) 
    throws UnknownIdentifierException, FrozenServiceException {
    if (!services.containsKey(type)) {
      throw new UnknownIdentifierException(type.getName());
    }
    if (assemblies.containsKey(type)) {
      throw new FrozenServiceException(type.getName());
    }
    T entity = type.cast(services.get(type).apply(this));
    Function<Container, T> service = c -> decorator.apply(entity, this);
    services.put(type, service);
    return this;  
  }

  public <T> Container define(String id, T item) {
    namespace.put(id, item);
    return this;
  }

  public <T> Object variable(String id) throws UnknownIdentifierException {
    if (!namespace.containsKey(id)) {
      throw new UnknownIdentifierException(id);
    }
    return namespace.get(id);
  }

  public <T> T variable(String id, Class<T> type) throws UnknownIdentifierException {
    return type.cast(variable(id));
  }

  public <T> boolean delete(String id) {
    if (namespace.containsKey(id)) {
      namespace.remove(id);
      return true;
    }
    return false;
  }
}
