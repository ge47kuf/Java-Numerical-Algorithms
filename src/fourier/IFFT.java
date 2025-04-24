package fourier;

public final class IFFT {

    private IFFT() { }
    /**
     * Compute the fast inverse Fourier transform of the vector c.
     * Assume that the length of c is a power of two, i.e. there exists some m, such that c.length == 2^m
     * @param c Vector, of which we want to compute the inverse fast fourier transform.
     * @return inverse fourier transform of the vector c.
     */
    public static Complex[] ifft(Complex[] c) {
        int n = c.length;

        for (int i = 0; i < n; i++) {
            c[i] = c[i].conjugate();
        }

        Complex[] result = fft_helper(c);

        for (int i = 0; i < n; i++) {
            result[i] = new Complex(result[i].getReal(),-result[i].getImaginary());
        }

        return result;
    }

    /**
     * Compute the fast Fourier transform of the vector v.
     * Assume that the length of v is a power of two, i.e. there exists some m, such that v.length == 2^m
     * @param v Vector, of which we want to compute the fast fourier transform.
     * @return  fourier transform of the vector c.
     */
    public static Complex[] fft(Complex[] v) {
        Complex[] result = fft_helper(v);
        for (int i = 0; i < result.length; i++){
            result[i] = result[i].mul(new Complex(1.0/ result.length));
        }
        return result;
    }

    public static Complex[] fft_helper(Complex[] v) {
        int n = v.length;

        if (n == 1) {
            return v;
        }

        Complex[] even = new Complex[n / 2];
        Complex[] odd = new Complex[n / 2];

        for (int i = 0; i < n / 2; i++) {
            even[i] = v[2 * i];
            odd[i] = v[2 * i + 1];
        }

        Complex[] evenFFT = fft_helper(even);
        Complex[] oddFFT = fft_helper(odd);

        Complex[] result = new Complex[n];

        for (int k = 0; k < n / 2; k++) {

            Complex omega = oddFFT[k].mul(Complex.fromPolar(1, -2 * Math.PI * k / n));
            result[k] = evenFFT[k].add(omega);
            result[k + n / 2] = evenFFT[k].sub(omega);

        }

        return result;
    }
}

