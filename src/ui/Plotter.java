package ui;

import interpolation.*;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.text.DecimalFormat;
import java.util.Arrays;
import javax.swing.*;

public class Plotter extends JPanel {
    // Top, bottom, left and right margin of the coordinate system in the plot.
    private static final int PADDING = 20;

    // Class members. Are set in class constructor.
    private final double[] xData;
    private final double[] yData;
    private double minX = Double.MAX_VALUE;
    private double minY = Double.MAX_VALUE;
    private double maxX = -Double.MAX_VALUE;
    private double maxY = -Double.MAX_VALUE;

    /**
     * Constructor of this class. Assigns the passed x- and y-values of the
     * points to plot to the internal private member variables.
     *
     * @param xData x-values of the points which should be plotted by this class.
     * @param yData y-values of the points which should be plotted by this class.
     * @throws InstantiationException The lengths of the x- and y-coordinates arrays have to be equal. Else an exception is thrown.
     */
    public Plotter(double[] xData, double[] yData) throws InstantiationException {
        // Make sure the arrays which contain the x- and y-coordinates which should be plotted by this class have the same length.
        if (xData.length != yData.length) {
            throw new InstantiationException("The arrays for the x- and y-components of the coordinates have to be of the same length.");
        }

        this.xData = Arrays.copyOf(xData, xData.length);
        this.yData = Arrays.copyOf(yData, yData.length);

        // Determine the smallest and largest value which should be plotted by this class.
        // These values are the boundaries of the axes of the coordinate system which will be plotted.
        this.minX = Arrays.stream(xData).min().getAsDouble();
        this.maxX = Arrays.stream(xData).max().getAsDouble();
        this.minY = Arrays.stream(yData).min().getAsDouble();
        this.maxY = Arrays.stream(yData).max().getAsDouble();
    }

    /**
     * paint method, draws the coordinate axes, axes labels, the given data and the reference solution.
     */
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D graphics = (Graphics2D) g;
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int height = getHeight();
        int width = getWidth();
        DecimalFormat df = new DecimalFormat("0.00");

        // draw x-axis
        graphics.draw(new Line2D.Double(
                PADDING, PADDING,
                PADDING, height - PADDING));
        graphics.draw(new Line2D.Double(
                PADDING + 0.25 * (width - 2 * PADDING), height - PADDING - 5,
                PADDING + 0.25 * (width - 2 * PADDING), height - PADDING + 5));
        graphics.draw(new Line2D.Double(
                0.5 * width, height - PADDING - 5,
                0.5 * width, height - PADDING + 5));
        graphics.draw(new Line2D.Double(
                PADDING + 0.75 * (width - 2 * PADDING), height - PADDING - 5,
                PADDING + 0.75 * (width - 2 * PADDING), height - PADDING + 5));
        graphics.draw(new Line2D.Double(
                width - PADDING, height - PADDING - 5,
                width - PADDING, height - PADDING + 5));

        // draw x-axis caption
        graphics.drawString(df.format(minX),
                PADDING + 2, height - PADDING + 12);
        graphics.drawString(df.format(0.25 * maxX + 0.75 * minX),
                PADDING + 0.25f * (width - 2 * PADDING) + 2, height - PADDING + 12);
        graphics.drawString(df.format(0.5 * (maxX + minX)),
                0.5f * width + 2, height - PADDING + 12);
        graphics.drawString(df.format(0.75 * maxX + 0.25 * minX),
                PADDING + 0.75f * (width - 2 * PADDING) + 2, height - PADDING + 12);
        graphics.drawString(df.format(maxX),
                width - PADDING + 2, height - PADDING + 12);

        // draw y-axis
        graphics.draw(new Line2D.Double(
                PADDING, height - PADDING,
                width - PADDING, height - PADDING));
        graphics.draw(new Line2D.Double(
                PADDING - 5, height - (PADDING + 0.25 * (height - 2 * PADDING)),
                PADDING + 5, height - (PADDING + 0.25 * (height - 2 * PADDING))));
        graphics.draw(new Line2D.Double(
                PADDING - 5, 0.5 * height,
                PADDING + 5, 0.5 * height));
        graphics.draw(new Line2D.Double(
                PADDING - 5, height - (PADDING + 0.75 * (height - 2 * PADDING)),
                PADDING + 5, height - (PADDING + 0.75 * (height - 2 * PADDING))));
        graphics.draw(new Line2D.Double(
                PADDING - 5, height - (PADDING + (height - 2 * PADDING)),
                PADDING + 5, height - (PADDING + (height - 2 * PADDING))));

        // draw y-axis caption
        graphics.drawString(df.format(minY),
                PADDING + 2, (height - 2 * PADDING) + PADDING - 2);
        graphics.drawString(
                df.format(0.25 * maxY + 0.75 * minY),
                PADDING + 2, height - (PADDING + 0.25f * (height - 2 * PADDING)) - 2);
        graphics.drawString(df.format(0.5 * (maxY + minY)),
                PADDING + 2, 0.5f * height - 2);
        graphics.drawString(
                df.format(0.75 * maxY + 0.25 * minY),
                PADDING + 2, height - (PADDING + 0.75f * (height - 2 * PADDING)) - 2);
        graphics.drawString(df.format(maxY),
                PADDING + 2, PADDING - 2);

        // draw assigned values
        graphics.setPaint(Color.red);

        for (int i = 0; i < xData.length; i++) {
            double xLin = scaleX(xData[i], width);
            double yLin = scaleY(yData[i], height);
            graphics.fill(new Ellipse2D.Double(xLin - 1.0f, yLin - 1.0f, 2.0f, 2.0f));
        }
    }

    /**
     * This method scales x-values linearly scaled for drawing. The padding of the coordinate system is respected.
     *
     * @param x Value which should be scaled linearly.
     * @param width Height of the plottable area.
     * @return Linearly scaled value of x.
     */
    private double scaleX(double x, double width) {
        double xScale = (x - minX) / (maxX - minX);
        return PADDING + xScale * (width - 2 * PADDING);
    }

    /**
     * This method scales y-values linearly scaled for drawing. The padding of the coordinate system is respected.
     *
     * @param y Value which should be scaled linear.
     * @param height Height of the plottable area.
     * @return Linearly scaled value of y.
     */
    private double scaleY(double y, double height) {
        double yScale = (y - minY) / (maxY - minY);
        return height - PADDING - yScale * (height - 2 * PADDING);
    }

    /**
     * Uses some interpolation method to interpolate a set of given points and plot the result.
     */
    public static void main(String[] args) {
        double l = 0.;
        double r = 5.;
        double w = r - l;

        /*
        // Set up interpolation problem
        //double[] y = {0.0, 1.0, 0.0, -1.0, 0.0};
        double[] y = {4.0, 1.0, -5.0, 2.0, 3.0, 1.0};

        int numOfSamplingPoints = 1001;
        double[] xData = new double[numOfSamplingPoints];
        double[] yData = new double[numOfSamplingPoints];

        //InterpolationMethod im = new NearestNeighbor();
        //InterpolationMethod im = new PiecewiseLinear();
        //InterpolationMethod im = new CubicSpline();
        //InterpolationMethod im = new NewtonPolynomial();
        //im.init(l, r, y);
        // calculate data to plot
        for (int i = 0; i < numOfSamplingPoints; i++) {
            xData[i] = l - 0.1 * w + 1.0 * i / numOfSamplingPoints * 1.2 * w;
            yData[i] = im.evaluate(xData[i]);
        }

        // initialize plotter
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        try {
            frame.add(new Plotter(xData, yData));
        } catch (InstantiationException exception) {
            exception.printStackTrace();
            System.exit(1);
        }
        frame.setSize(960, 720);
        frame.setLocation(0, 0);
        frame.setVisible(true);

         */

        // test -----------------------------------------------------
        double[] y = {0.0, 1.0, 0.0, -1.0, 0.0};        // sinus
        //double[] y = {4.0, 1.0, -5.0, 2.0, 3.0, 1.0}; // randon polynom
        //double[] y = {0.0, 1.0, 2.0, 3.0, 4.0};       // linear
        //double[] y = {0.0, 0.0, 0.0, 0.0, 0.0};       // null function

        int numOfSamplingPoints = 1001;
        double[] xData = new double[numOfSamplingPoints];
        double[] yNN = new double[numOfSamplingPoints];
        double[] yPL = new double[numOfSamplingPoints];
        double[] yCS = new double[numOfSamplingPoints];
        double[] yNP = new double[numOfSamplingPoints];

        InterpolationMethod imNN = new NearestNeighbor();
        InterpolationMethod imPL = new PiecewiseLinear();
        InterpolationMethod imCS = new CubicSpline();
        InterpolationMethod imNP = new NewtonPolynomial();

        imNN.init(l, r, y);
        imPL.init(l, r, y);
        imCS.init(l, r, y);
        imNP.init(l, r, y);

        // calculate data to plot
        for (int i = 0; i < numOfSamplingPoints; i++) {
            xData[i] = l - 0.1 * w + 1.0 * i / numOfSamplingPoints * 1.2 * w;
            //xData[i] = i;

            yNN[i] = imNN.evaluate(xData[i]);
            yPL[i] = imPL.evaluate(xData[i]);
            yCS[i] = imCS.evaluate(xData[i]);
            yNP[i] = imNP.evaluate(xData[i]);
        }

        // initialize plotter
        JFrame frameNN = new JFrame();
        JFrame framePL = new JFrame();
        JFrame frameCS = new JFrame();
        JFrame frameNP = new JFrame();

        frameNN.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        framePL.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frameCS.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frameNP.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        try {
            frameNN.add(new Plotter(xData, yNN));
            framePL.add(new Plotter(xData, yPL));
            frameCS.add(new Plotter(xData, yCS));
            frameNP.add(new Plotter(xData, yNP));
        } catch (InstantiationException exception) {
            exception.printStackTrace();
            System.exit(1);
        }

        frameNN.setSize(800, 500);
        frameNN.setLocation(0, 0);
        frameNN.setVisible(true);

        framePL.setSize(800, 500);
        framePL.setLocation(800, 0);
        framePL.setVisible(true);

        frameCS.setSize(800, 500);
        frameCS.setLocation(0, 500);
        frameCS.setVisible(true);

        frameNP.setSize(800, 500);
        frameNP.setLocation(800, 500);
        frameNP.setVisible(true);
        // end of test -------------------------------------------------
    }
}

