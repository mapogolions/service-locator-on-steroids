package io.github.mapogolions.fixtures;


public class FkCacheItem<K, V> {
  final private K key;
  final private V value;

  public FkCacheItem(K key, V value) {
    this.key = key;
    this.value = value;
  }

  public K getKey() {
    return key;
  }

  public V getValue() {
    return value;
  }
}