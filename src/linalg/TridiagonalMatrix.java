package linalg;

import java.text.DecimalFormat;
import java.util.Arrays;

public class TridiagonalMatrix {
    /**
     * Dimension, can't be changed, once it's set.
     */
    private final int n;

    /**
     * Values on the lower diagonal, has length n-1.
     */
    private double[] lower;

    /**
     * Values on the diagonal, has length n.
     */
    private double[] diagonal;

    /**
     * Values on the upper diagonal, has length n-1.
     */
    private double[] upper;

    /**
     * Creates an empty tridiagonal matrix.
     */
    public TridiagonalMatrix(int n) {
        this.n = n;
        lower = new double[n - 1];
        diagonal = new double[n];
        upper = new double[n - 1];
    }

    /**
     * Copies the tridiagonal matrix tri.
     */
    public TridiagonalMatrix(TridiagonalMatrix tri) {
        n = tri.n;
        this.lower = Arrays.copyOf(tri.lower, n - 1);
        this.diagonal = Arrays.copyOf(tri.diagonal, n);
        this.upper = Arrays.copyOf(tri.upper, n - 1);
    }

    /**
     * Set's all entries on the lower diagonal to d.
     */
    public void setLower(double d) {
        for (int i = 0; i < n - 1; i++) {
            this.lower[i] = d;
        }
    }

    /**
     * Set's all entries on the diagonal to d.
     */
    public void setDiagonal(double d) {
        for (int i = 0; i < n; i++) {
            this.diagonal[i] = d;
        }
    }

    /**
     * Set's all entries on the upper diagonal to d.
     */
    public void setUpper(double d) {
        for (int i = 0; i < n - 1; i++) {
            this.upper[i] = d;
        }
    }
    /**
     * Create a string representation of the matrix..
     */
    public String toString() {
        DecimalFormat df = new DecimalFormat(" #,##0.00;-#");
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i == j + 1) {
                    str.append(df.format(lower[j])).append(" ");
                } else if (i == j) {
                    str.append(df.format(diagonal[i])).append(" ");
                } else if (i == j - 1) {
                    str.append(df.format(upper[i])).append(" ");
                } else {
                    str.append("      ");
                }
            }
            str.append("\n");
        }
        return str.toString();
    }

    /**
     * Solve the linear system Ax = b with the tridiagonal matrix A. Uses Gaussian elimination without pivoting (Thomas algorithm).
     */
    public double[] solveLinearSystem(double[] b) {
        double[] l = Arrays.copyOf(lower, n - 1);
        double[] d = Arrays.copyOf(diagonal, n);
        double[] u = Arrays.copyOf(upper, n - 1);
        double[] br = Arrays.copyOf(b, n);

        // solution vector
        double[] x = new double[n];

        // Get rid of lower diagonal.
        for (int i = 0; i < n - 1; i++) {
            double factor = l[i] / d[i];
            d[i + 1] -= factor * u[i];
            br[i + 1] -= factor * br[i];
        }

        // back substitution
        x[n - 1] = br[n - 1] / d[n - 1];
        for (int i = n - 2; i >= 0; i--) {
            x[i] = (br[i] - u[i] * x[i + 1]) / d[i];
        }

        return x;
    }

}
