package interpolation;

public class NearestNeighbor extends InterpolationMethod {
    double[] x;

    /**
     * Precompute x values
     */
    @Override
    public void init(double newA, double newB, double[] newY) {
        super.init(newA, newB, newY);
        x = new double[y.length];
        for (int i = 0; i < y.length; i++) {
            x[i] = a + i * h;
        }
    }

    /**
     * For a given z, search the nearest grid point and return the value.
     * If z is exactly in the middle of two grid points, return the value from the right.
     */
    @Override
    public double evaluate(double z) {
        double distance = Double.POSITIVE_INFINITY;
        double result = Double.POSITIVE_INFINITY;

        for (int i = 0; i < y.length; i++) {
            double currentDistance = Math.abs(x[i] - z);
            if (currentDistance <= distance) {
                distance = currentDistance;
                result = y[i];
            }
        }
        return result;
    }
}

