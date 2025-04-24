package interpolation;

import java.util.Arrays;

public abstract class InterpolationMethod {
    /**
     * Leftmost grid point.
     */
    protected double a;

    /**
     * Rightmost grid point.
     */
    protected double b;

    /**
     * Number of intervals.
     */
    protected int n;

    /**
     * Width of intervals.
     */
    protected double h;

    /**
     * Values to interpolate.
     */
    protected double[] y;

    /**
     * Initialize the interpolation method with equidistant grid points.
     * The parameters have to fulfill:
     * a < b
     * @param newA leftmost grid point
     * @param newB rightmost grid point
     * @param newY values to interpolate
     */
    public void init(double newA, double newB, double[] newY) {
        assert newA < newB;
        this.a = newA;
        this.b = newB;
        this.n = newY.length - 1;
        this.h = (newB - newA) / n;
        this.y = Arrays.copyOf(newY, newY.length);
    }

    /**
     * Evaluates the interpolating function at a point z.
     */
    public abstract double evaluate(double z);
}

