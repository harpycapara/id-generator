package common.utils.map;

import com.google.common.collect.ImmutableMap;

import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class NullFilterMapBuilder<K, V> {

  private final BiConsumer<K, V> putFunction;
  private final Supplier<Map<K, V>> buildFunction;

  public NullFilterMapBuilder(Map<K, V> map) {
    this.putFunction = map::put;
    this.buildFunction = () -> map;
  }

  public NullFilterMapBuilder(ImmutableMap.Builder<K, V> builder) {
    this.putFunction = builder::put;
    this.buildFunction = builder::build;
  }

  public NullFilterMapBuilder<K, V> put(K key, V value) {
    if (value != null) {
      this.putFunction.accept(key, value);
    }
    return this;
  }

  public Map<K, V> build() {
    return this.buildFunction.get();
  }

}
