package interpolation;

import java.util.Arrays;

public class InterpolationMethod2D {

    /**
     * The 1D interpolation method, which we will use to build our 2D interpolation method.
     */
    private final InterpolationMethod im;

    /**
     * Grid point x coordinates, we assume a cartesian grid (x, y).
     */
    private double[] x;

    /**
     * Grid point y coordinates, we assume a cartesian grid (x, y).
     */
    private double[] y;

    /**
     * Values to interpolate f(x[i], y[j]) = z[i][j].
     */
    private double[][] z;

    /**
     * Dimension of x-axis.
     */
    private int m;

    /**
     * Dimension of y-axis.
     */
    private int n;

    /**
     * Initialize a 2D interpolation method, by setting the 1D interpolation method to build on.
     * @param im Interpolation method to use.
     */
    public InterpolationMethod2D(InterpolationMethod im) {
        this.im = im;
    }

    /**
     * Initialize 2D interpolation problem on a cartesian grid:
     * f(x[i], y[j]) = z[i][j]
     * @param newX array of x coordinates
     * @param newY array of y coordinates
     * @param newZ array of function values
     */
    public void init(double[] newX, double[] newY, double[][] newZ) {
        this.x = Arrays.copyOf(newX, newX.length);
        this.y = Arrays.copyOf(newY, newY.length);
        this.z = Arrays.stream(newZ).map(double[]::clone).toArray(double[][]::new);
        this.m = newX.length;
        this.n = newY.length;
    }

    /**
     * Add your implementation below.
     */

    /**
     * Evaluate the 2D interpolation function at the new grid points s, t.
     * @param s evaluation points along the x-axis.
     * @param t evaluation points along the y-axis.
     * @return 2D array z, with f(s[i], t[j]) = z[i][j]
     */
    public double[][] evaluate(double[] s, double[] t) {
        int k = s.length;
        int l = t.length;

        // Temporary storage for intermediate results
        double[][] tempResult = new double[m][l];

        // interpolate along y-axis for each x
        for (int i = 0; i < m; i++) {
            double[] newY = new double[n];
            System.arraycopy(z[i], 0, newY, 0, n);
            im.init(y[0], y[n - 1], newY);
            for (int j = 0; j < l; j++) {
                tempResult[i][j] = im.evaluate(t[j]);
            }
        }

        // Final results
        double[][] finalResults = new double[k][l];

        // interpolate along x-axis
        for (int j = 0; j < l; j++) {
            double[] temp = new double[m];
            for (int i = 0; i < m; i++) {
                temp[i] = tempResult[i][j];
            }
            im.init(x[0], x[m - 1], temp);
            for (int i = 0; i < k; i++) {
                finalResults[i][j] = im.evaluate(s[i]);
            }
        }

        return finalResults;
    }
}
