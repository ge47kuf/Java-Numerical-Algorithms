package fourier;

public class Complex {
    private double real;
    private double imaginary;

    public Complex() {
        this(0, 0);
    }
    public Complex(double r) {
        this(r, 0);
    }

    public Complex(double real, double imaginary) {
        this.real = real;
        this.imaginary = imaginary;
    }

    /**
     * Copy another complex number.
     * @param z
     */
    public Complex(Complex z) {
        this.real = z.real;
        this.imaginary = z.imaginary;
    }

    /**
     * Creates a new complex number from radius and angle.
     * @param r radius of the new complex number
     * @param phi angle of the new complex number
     * @return "r * exp(i phi)"
     */
    public static Complex fromPolar(double r, double phi) {
        return new Complex(r * Math.cos(phi), r * Math.sin(phi));
    }

    /**
     * @return String representation of the number
     */
    public String toString() {
        return real + " + " + imaginary + "i";
    }

    /**
     * Returns the real part of the complex number.
     * @return "Re(this)"
     */
    public double getReal() {
        return real;
    }

    /**
     * Returns the imaginary part of the complex number.
     * @return "Im(this)"
     */
    public double getImaginary() {
        return imaginary;
    }

    /**
     * Add your implementation below.
     */

    /**
     * Adds two complex numbers.
     * @param other The other operand to add.
     * @return "this + other"
     */
    public Complex add(Complex other) {
        return new Complex(this.real + other.real, this.imaginary + other.imaginary);
    }

    /**
     * Subtracts two complex numbers.
     * @param other The other operand to subtract.
     * @return "this - other"
     */
    public Complex sub(Complex other) {
        return new Complex(this.real - other.real, this.imaginary - other.imaginary);
    }

    /**
     * Multiplies two complex numbers.
     * @param other The other operand to multiply.
     * @return "this * other"
     */
    public Complex mul(Complex other) {
        return new Complex((this.real * other.real) - (this.imaginary * other.imaginary), (this.real * other.imaginary) + (this.imaginary * other.real));
    }

    /**
     * Computes the nth power.
     * @param n Exponent of the power
     * @return "this ^ n"
     */
    public Complex power(int n) {
        return fromPolar(Math.pow(getAbs(), n), (getPhi() * n));
    }

    /**
     * Returns the complex conjugate.
     * @return "this bar"
     */
    public Complex conjugate() {
        return new Complex(this.real, -this.imaginary);
    }

    /**
     * Returns the absolute value of the real number.
     * @return "|this|"
     */
    public double getAbs() {
        return Math.sqrt(Math.pow(this.real, 2) + Math.pow(this.imaginary, 2));
    }

    /**
     * Computes the angle of the complex number (between -pi and pi).
     * @return "arg(this)"
     */
    public double getPhi() {
        return Math.atan2(this.imaginary, this.real);
    }
}

