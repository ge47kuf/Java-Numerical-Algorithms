# Interpolation, Linear Algebra, and Fourier Transform Library

This Java project provides a collection of algorithms and applications for various mathematical and image processing tasks.

## Overview

The project encompasses the following main functionalities:

* **Interpolation Methods:** Implementations of various interpolation algorithms.
* **Linear Algebra:** Specifically, handling tridiagonal matrices and their solution.
* **Fourier Transforms:** Implementations of the Discrete Fourier Transform (DFT) and the Fast Fourier Transform (FFT).
* **Image Processing:** A GUI application for image scaling using interpolation methods.

## Features

### Interpolation Methods

The project contains implementations of the following interpolation methods (all in `src/interpolation`):

* **Nearest Neighbor Interpolation:** (`NearestNeighbor.java`) Finds the closest grid point for a given point and returns its value.
* **Piecewise Linear Interpolation:** (`PiecewiseLinear.java`) Performs linear interpolation between adjacent data points.
* **Cubic Spline Interpolation:** (`CubicSpline.java`) Uses cubic splines to interpolate a smooth function through given points.
* **Newton Polynomial Interpolation:** (`NewtonPolynomial.java`) Computes the Newton polynomial for interpolation.
* **2D Interpolation:** (`InterpolationMethod2D.java`) Enables the application of 1D interpolation methods to 2D data, e.g., for image scaling.
* **Base Class:** (`InterpolationMethod.java`) Defines the common interface for all interpolation methods.

### Linear Algebra

* **Tridiagonal Matrix Solver:** (`src/linalg/TridiagonalMatrix.java`) Implements a solver for linear systems with tridiagonal matrices using the Thomas algorithm (Gaussian elimination without pivoting).

### Fourier Transforms

* **Discrete Fourier Transform (DFT):** (`src/fourier/DFT.java`) Computes the DFT of a vector.
* **Fast Fourier Transform (FFT) and Inverse FFT (IFFT):** (`src/fourier/IFFT.java`) Implementations of the FFT and IFFT for efficient frequency analysis.
* **Complex Numbers:** (`src/fourier/Complex.java`) A utility class for representing complex numbers.

### Image Processing

* **Image Scaling:** (`src/image/Picture.java`, `src/ui/ImageViewer.java`) A GUI application that allows scaling images using various interpolation methods. The application (`ImageViewer.java`) provides a user interface for loading, saving, and scaling images.

### Utilities

* **Plotter:** (`src/ui/Plotter.java`) A utility for visualizing data points and interpolation results.

### Tests

* JUnit tests are available for some of the interpolation methods (`src/tests`).

## Usage Examples

### Interpolation

```java
double[] x = {0, 1, 2, 3, 4};
double[] y = {1, 3, 2, 4, 1};

InterpolationMethod method = new CubicSpline();
method.init(0, 4, y);

double interpolatedValue = method.evaluate(2.5);
System.out.println("Interpolated Value: " + interpolatedValue);
