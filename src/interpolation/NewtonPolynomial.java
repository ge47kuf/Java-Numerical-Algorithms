package interpolation;

public class NewtonPolynomial extends InterpolationMethod {
    /**
     * Coefficients of the Newton Polynomial p(x) = a0 + a1*(x-x0) + a2*(x-x0)*(x-x1)+...
     */
    private double[] coefficients;

    /**
     * Grid points x0, x1, ..., xn.
     */
    private double[] x;

    /**
     * Add your implementation below.
     */


    /**
     * Compute the coefficients of the Newton polynomials
     * Newton-Polynom berechnet.
     */
    @Override
    public void init(double newA, double newB, double[] newY) {
        super.init(newA, newB, newY);

        // int x value
        this.x = new double[y.length];
        for (int i = 0; i < x.length; i++) {
            x[i] = newA + i * h;
        }

        computeCoefficients(y);
    }

    /**
     * Compute the coefficients using the triangle scheme and store them.
     * @param y Interpolation values
     */
    private void computeCoefficients(double[] y) {
        // Hint: this function might be useful.
        int n = y.length;
        this.coefficients = new double[n];
        double[][] triangleM = new double[n][n];

        // c_i,0 spalte
        for (int i = 0; i < n; i++) {
            triangleM[i][0] = y[i];
        }

        // idea: spalte fur spalte iter
        for (int k = 1; k < n; k++) {
            for (int i = 0; i < n - k; i++) {
                // c_i,k formel
                triangleM[i][k] = (triangleM[i + 1][k - 1] - triangleM[i][k - 1]) / (x[i + k] - x[i]);
            }
        }

        // copy erste reihe
        for (int i = 0; i < n; i++) {
            this.coefficients[i] = triangleM[0][i];
        }
    }

    /**
     * Evaluate the Newton polynomial efficiently in a similar manner as the Horner scheme.
     */
    // p(x) = a0 + (x - x0) * (a1 + (x - x1) * ...)
    @Override
    public double evaluate(double z) {
        double acc = 0;
        double acc2 = 1;
        int i = 0;

        while (i < y.length) {
            acc = acc + coefficients[i] * acc2;
            acc2 = acc2 * (z - x[i]);
            i++;
        }
        return acc;
    }
    // test-------------------------------------------------
    public static void main(String[] args) {
        NewtonPolynomial newtonPoly = new NewtonPolynomial();
        double[] y = {3,0,1};
        double a = 0; // start of interval
        double b = 2; // end of interval

        newtonPoly.init(a, b, y);

        double result = newtonPoly.evaluate(5); // Evaluate at some point should be 28
        System.out.println("Polynomial at 5: " + result);
    }

}
