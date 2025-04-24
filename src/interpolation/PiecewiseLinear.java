package interpolation;

public class PiecewiseLinear extends InterpolationMethod {
    double[] x;
    double[] m;
    double[] t;

    /**
     * Precompute x values
     */
    @Override
    public void init(double newA, double newB, double[] newY) {
        super.init(newA, newB, newY);
        x = new double[y.length];
        m = new double[y.length - 1];
        t = new double[y.length - 1];

        for (int i = 0; i < y.length; i++) {
            x[i] = a + i * h;
        }
        for (int i = 0; i < m.length; i++) {
            m[i] = (y[i + 1] - y[i]) / h;
            t[i] = y[i] - m[i] * x[i];
        }
    }

    /**
     * For a given z, find the left and right grid points. Evaluate the linear interpolation between the left and the
     * right values. If z is outside [a, b], return y[0] or y[n] respectively.
     */
    @Override
    public double evaluate(double z) {
        if (z < a) {
            return y[0];
        } else if (z > b) {
            return y[y.length - 1];
        }

        int pieceIndex = 0;

        for (int i = 0; i < y.length - 1; i++) {
            double lowerBoundary = x[i];
            double upperBoundary = x[i + 1];

            if (z >= lowerBoundary && z <= upperBoundary) {
                pieceIndex = i;
                break;
            }
        }

        return m[pieceIndex] * z + t[pieceIndex];
    }
}

