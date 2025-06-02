package com.github.forax.valhalla;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class MandelbrotSet {
  private static final int MAX_ITERATIONS = 1_000;
  private static final double ESCAPE_RADIUS = 2.0;

  public static int computeMandelbrot(double x, double y) {
    var c = new Complex(x, y);
    var z = new Complex(0.0, 0.0);

    for (var iteration = 0; iteration < MAX_ITERATIONS; iteration++) {
      z = z.multiply(z).add(c);
      if (z.magnitudeSquared() > ESCAPE_RADIUS * ESCAPE_RADIUS) {
        return iteration;
      }
    }
    return MAX_ITERATIONS;
  }

  public static int computeMandelbrotPrimitive(double x, double y) {
    double zRe = 0.0;
    double zIm = 0.0;

    for (var iteration = 0; iteration < MAX_ITERATIONS; iteration++) {
      // Calculate z * z using the Complex multiply logic:
      double newZRe = zRe * zRe - zIm * zIm;
      double newZIm = zRe * zIm + zIm * zRe;

      // Add c (which is the input point (x,y)) using Complex add logic:
      zRe = newZRe + x;
      zIm = newZIm + y;

      // Check magnitude squared (re * re + im * im)
      if (zRe * zRe + zIm * zIm > ESCAPE_RADIUS * ESCAPE_RADIUS) {
        return iteration;
      }
    }
    return MAX_ITERATIONS;
  }

  public static BufferedImage computeImage(int width, int height,
                                          double xMin, double xMax,
                                          double yMin, double yMax) {
    var image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    for (int row = 0; row < height; row++) {
      for (int col = 0; col < width; col++) {
        var x = xMin + (xMax - xMin) * col / width;
        var y = yMin + (yMax - yMin) * row / height;
        var iteration = computeMandelbrot(x, y);
        image.setRGB(col, row, getColor(iteration));
      }
    }
    return image;
  }

  private static int getColor(int iteration) {
    if (iteration == MAX_ITERATIONS) {
      return Color.BLACK.getRGB();
    }
    var smooth = iteration + 1 - Math.log(Math.log(ESCAPE_RADIUS)) / Math.log(2);
    var brightness = Math.pow(smooth / MAX_ITERATIONS, 0.5);
    var gradient = (int) (255 * brightness);
    var value = Math.clamp(gradient, 0, 255);
    return new Color(value, value, value).getRGB();
  }

// export JAVA_HOME=/Users/forax/valhalla-live/valhalla/build/macosx-aarch64-server-release/images/jdk/
// $JAVA_HOME/bin/java --enable-preview -cp target/fosdem-2025-valhalla-1.0.jar com.github.forax.valhalla.MandelbrotSet
  void main() throws IOException {
    var width = 800;
    var height = 600;
    var xMin = -2.0;
    var xMax = 1.0;
    var yMin = -1.5;
    var yMax = 1.5;

    var path = Path.of("mandelbrot.png");
    var image = computeImage(width, height, xMin, xMax, yMin, yMax);
    try(var output = Files.newOutputStream(path)) {
      ImageIO.write(image, "png", output);
    }
  }
}