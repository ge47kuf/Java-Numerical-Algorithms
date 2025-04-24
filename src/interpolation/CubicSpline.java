package interpolation;

import linalg.TridiagonalMatrix;

public class CubicSpline extends InterpolationMethod {
    // Derivatives at the grid points
    private double[] yPrime;
    private double[] x;

    @Override
    public void init(double a, double b, double[] y) {
        super.init(a, b, y);

        this.x = new double[y.length];
        for (int i = 0; i < x.length; i++) {
            x[i] = a + i * h;
        }

        computeDerivatives();
    }

    /**
     * Computes the derivatives at the grid points.
     * Therefore, we construct a linear system Ax = b and solve it.
     * We solve the system and store the derivatives in yPrime.
     */
    public void computeDerivatives() {
        // Hint: this function might be useful.
        // y array:= [y0,y1,...,yn]-> size = n+1
        int matrixSize =  y.length - 2;
        double h = (b - a) / n;
        double[] tmp = new double[matrixSize];
        yPrime = new double[y.length];
        yPrime[0] = yPrime[yPrime.length - 1] = 0;

        // left side of equation init
        TridiagonalMatrix leftSideMatrix = new TridiagonalMatrix(matrixSize);
        leftSideMatrix.setDiagonal(4.0);
        leftSideMatrix.setUpper(1.0);
        leftSideMatrix.setLower(1.0);

        // right side of equation init
        double factor = 3/h;
        double[] rightSideVector = new double[matrixSize];

        // y'0 and y'n = 0, fill in the first and last value
        rightSideVector[0] = factor * (y[2] - y[0]);
        rightSideVector[matrixSize - 1] = factor * (y[n] - y[n-2]);

        // fill in rightSideVector
        for (int i = 1; i < matrixSize - 1; i++) {
            rightSideVector[i] = factor * (y[i+2] - y[i]);
        }

        tmp = leftSideMatrix.solveLinearSystem(rightSideVector);

        System.arraycopy(tmp, 0, yPrime, 1, tmp.length);
    }

    /**
     * For a given z, find the left and right grid points. Evaluate the cubic interpolation between the left and the
     * right values. If z is outside [a, b], return y[0] or y[n] respectively.
     */
    @Override
    public double evaluate(double z) {
        if (yPrime == null) {
            computeDerivatives();
        }
        // edge case: z outside interval
        if (z < a) return y[0];
        if (z > b) return y[y.length - 1];

        // Find the interval [x_i, x_i+1] that contains z
        int i = Math.min(Math.max((int) Math.floor((z - a) / h), 0), y.length - 2);

        // Transform z to the interval [0, 1]
        double t = (z - x[i]) / h;

        // Evaluate the polynomial q at t
        double H0 = 1 - 3 * (t * t) + 2 * (t * t * t);
        double H1 = 3 * (t * t) - 2 * (t * t * t);
        double H2 = t - 2 * (t * t) + (t * t * t);
        double H3 = -(t * t) + (t * t * t);

        return y[i] * H0 + y[i + 1] * H1 + h * yPrime[i] * H2 + h * yPrime[i + 1] * H3;
    }

    // testing------------------------------------------------------
    public static void main(String[] args) {
        CubicSpline cubicSpline = new CubicSpline();

        // Example data
        double a = 0; // starting x value
        double b = 10; // ending x value
        double[] y = {1,10, 5, -1, 0, 11}; // example y values

        cubicSpline.init(a, b, y); // Initialize with data

        // Example evaluation
        double result = cubicSpline.evaluate(6); // Evaluate at x = 5
        System.out.println("Result: " + result);
    }
}
