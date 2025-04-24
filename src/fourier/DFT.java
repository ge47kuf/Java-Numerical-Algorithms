package fourier;

public final class DFT {
    private DFT() { }


    /**
     * Compute the discrete Fourier transform of the vector v.
     * @param v
     * @return dft(v)
     */
    public static Complex[] dft(double[] v) {
        int n = v.length;
        Complex[] result = new Complex[n];

        Complex omega_strich = Complex.fromPolar(1, -2 * Math.PI / n);

        for (int k = 0; k < n; k++) {
            Complex sum = new Complex();

            for (int j = 0; j < n; j++) {
                Complex omega_jk = omega_strich.power(j * k);
                sum = sum.add(new Complex(v[j]).mul(omega_jk));
            }

            result[k] = sum.mul(new Complex(1.0/n));
        }

        return result;
    }

}

