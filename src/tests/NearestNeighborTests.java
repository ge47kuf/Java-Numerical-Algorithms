package tests;

import interpolation.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class NearestNeighborTests {
    @Test
    void nearestTest_1() {
        NearestNeighbor n = new NearestNeighbor();
        n.init(-2.6, 4.6, new double[]{1.0, 1.0, 2.0, 1.0, 2.0, 4.0});
        assertEquals(4.0, n.evaluate(4.64821924770869));
        assertEquals(4.0, n.evaluate(6));
        assertEquals(1.0, n.evaluate(-100));
    }

    @Test
    void nearestTest_2() {
        NearestNeighbor n = new NearestNeighbor();
        n.init(-4.1, 3.0999999999999996, new double[]{4.0, 5.0, -4.0, -5.0, -3.0, 3.0});
        assertEquals(-3.0, n.evaluate(2.36097369802821));
    }

    @Test
    void nearestTest_3() {
        NearestNeighbor n = new NearestNeighbor();
        n.init(-3.4, -1.2999999999999998, new double[]{1.0, 3.0, 4.0, 1.0, 1.0, -1.0});
        assertEquals(1.0, n.evaluate(-2.205093946309468));
    }

    @Test
    void exactLowerBoundary() {
        NearestNeighbor n = new NearestNeighbor();
        n.init(-3.4, -1.2999999999999998, new double[]{1.0, 3.0, 4.0, 1.0, 1.0, -1.0});
        assertEquals(1.0, n.evaluate(-3.4));
    }

    @Test
    void exactUpperBoundary() {
        NearestNeighbor n = new NearestNeighbor();
        n.init(-3.4, -1.2999999999999998, new double[]{1.0, 3.0, 4.0, 1.0, 1.0, -1.0});
        assertEquals(-1.0, n.evaluate(-1.2999999999999998));
    }
}
