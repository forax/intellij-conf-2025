package com.github.forax.valhalla.benchmark;

import com.github.forax.valhalla.MandelbrotSet;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;

import java.util.concurrent.TimeUnit;

// identity
// Benchmark                                      Mode  Cnt    Score   Error  Units
// ScalarizationBench.computeMandelbrot           avgt    5  474.451 ± 5.279  ms/op
// ScalarizationBench.computeMandelbrotPrimitive  avgt    5  258.706 ± 1.109  ms/op

// value
// Benchmark                                      Mode  Cnt    Score   Error  Units
// ScalarizationBench.computeMandelbrot           avgt    5  258.697 ± 1.038  ms/op
// ScalarizationBench.computeMandelbrotPrimitive  avgt    5  259.169 ± 1.162  ms/op

// export JAVA_HOME=/Users/forax/valhalla-live2/valhalla/build/macosx-aarch64-server-release/images/jdk/
// $JAVA_HOME/bin/java -jar target/benchmarks.jar
@Warmup(iterations = 5, time = 2, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 2, timeUnit = TimeUnit.SECONDS)
@Fork(value = 1, jvmArgs = { "--enable-preview" })
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
public class ScalarizationBench {

  int width = 800;
  int height = 600;
  double xMin = -2.0;
  double xMax = 1.0;
  double yMin = -1.5;
  double yMax = 1.5;

  @Benchmark
  public int computeMandelbrot() {
    var sum = 0;
    for (int row = 0; row < height; row++) {
      for (int col = 0; col < width; col++) {
        var x = xMin + (xMax - xMin) * col / width;
        var y = yMin + (yMax - yMin) * row / height;
        sum += MandelbrotSet.computeMandelbrot(x, y);
      }
    }
    return sum;
  }

  @Benchmark
  public int computeMandelbrotPrimitive() {
    var sum = 0;
    for (int row = 0; row < height; row++) {
      for (int col = 0; col < width; col++) {
        var x = xMin + (xMax - xMin) * col / width;
        var y = yMin + (yMax - yMin) * row / height;
        sum += MandelbrotSet.computeMandelbrotPrimitive(x, y);
      }
    }
    return sum;
  }
}
