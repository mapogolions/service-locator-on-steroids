package io.github.mapogolions.fixtures;

import java.util.List;
import java.util.ArrayList;


public class FkCache<K, V> {
  final private List<FkCacheItem<K, V>> store = new ArrayList<>();

  public FkCache<K, V> save(FkCacheItem<K, V> item) {
    store.add(item);
    return this;
  }

  public FkCacheItem<K, V> obtain(int index) {
    return store.get(index);
  }
}