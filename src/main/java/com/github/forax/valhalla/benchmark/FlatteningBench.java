package com.github.forax.valhalla.benchmark;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

// with sort
// Benchmark                             Mode  Cnt    Score   Error  Units
// FlatteningBench.identities            avgt    5  332,901 ± 0,818  us/op
// FlatteningBench.nullRestrictedValues  avgt    5  292,112 ± 2,189  us/op
// FlatteningBench.values                avgt    5  335,689 ± 9,755  us/op

// no sort
// Benchmark                             Mode  Cnt    Score   Error  Units
// FlatteningBench.identities            avgt    5  334,449 ± 1,835  us/op
// FlatteningBench.nullRestrictedValues  avgt    5  292,082 ± 2,957  us/op
// FlatteningBench.values                avgt    5  332,883 ± 0,507  us/op

// export JAVA_HOME=/Users/forax/valhalla-live2/valhalla/build/macosx-aarch64-server-release/images/jdk/
// $JAVA_HOME/bin/java -jar target/benchmarks.jar
@Warmup(iterations = 5, time = 2, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 2, timeUnit = TimeUnit.SECONDS)
@Fork(value = 1, jvmArgs = { "--enable-preview", "--add-exports=java.base/jdk.internal.value=ALL-UNNAMED" })
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@State(Scope.Benchmark)
public class FlatteningBench {

  /*not-value*/ class IdentityInteger {
    final int value;

    public IdentityInteger(int value) {
      this.value = value;
    }
  }

  IdentityInteger[] identities;
  {
    identities = new IdentityInteger[1_000_000];
    Arrays.setAll(identities, IdentityInteger::new);
    //Collections.shuffle(Arrays.asList(identities));
  }

  value class ValueInteger {
    final int value;

    public ValueInteger(int value) {
      this.value = value;
    }
  }

  ValueInteger[] values;
  {
    //values = new ValueInteger[1_000_000];
    values = (ValueInteger[]) jdk.internal.value.ValueClass.newNullableAtomicArray(ValueInteger.class, 1_000_000);
    Arrays.setAll(values, ValueInteger::new);
    //Collections.shuffle(Arrays.asList(values));
  }

  ValueInteger[] nullRestrictedValues;
  {
    //values = new ValueInteger[1_000_000];
    nullRestrictedValues = (ValueInteger[]) jdk.internal.value.ValueClass.newNullRestrictedAtomicArray(ValueInteger.class, 1_000_000, new ValueInteger(0));
    Arrays.setAll(nullRestrictedValues, ValueInteger::new);
    //Collections.shuffle(Arrays.asList(nullRestrictedValues));
  }

  @Benchmark
  public int identities() {
    var sum = 0;
    for (IdentityInteger identity : identities) {
      sum += identity.value;
    }
    return sum;
  }

  @Benchmark
  public int values() {
    var sum = 0;
    for (ValueInteger value : values) {
      sum += value.value;
    }
    return sum;
  }

  @Benchmark
  public int nullRestrictedValues() {
    var sum = 0;
    for (ValueInteger value : nullRestrictedValues) {
      sum += value.value;
    }
    return sum;
  }
}
