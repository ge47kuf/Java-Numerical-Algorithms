package tests;

import interpolation.PiecewiseLinear;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PiecewiseLinearTests {
    @Test
    void linearTest_1() {
        PiecewiseLinear l = new PiecewiseLinear();
        l.init(-1.7000000000000002, -0.09999999999999964, new double[]{-3.0, -3.0, 4.0, 4.0, 4.0, 4.0});
        assertEquals(-0.5134994638239996, l.evaluate(-1.2663314040605258), 0.001);
        assertEquals(4.0, l.evaluate(-0.03479104253794274), 0.001);
    }

    @Test
    void linearTest_2() {
        PiecewiseLinear l = new PiecewiseLinear();
        l.init(0.5999999999999996, 4.699999999999999, new double[]{4.0, 4.0, 3.0, 0.0, 3.0, -2.0});
        assertEquals(0.09071325446926058, l.evaluate(3.0847949562215975), 0.001);
        assertEquals(-2.0, l.evaluate(4.879468962378141), 0.001);
    }

    @Test
    void linearTest_3() {
        PiecewiseLinear l = new PiecewiseLinear();
        l.init(-4.8, -3.7, new double[]{-2.0, -4.0, 1.0, -4.0, -2.0, 5.0});
        assertEquals(-3.021699819774409, l.evaluate(-4.536954792070074), 0.001);
        assertEquals(5.0, l.evaluate(-3.651599788142515), 0.001);
    }
}
