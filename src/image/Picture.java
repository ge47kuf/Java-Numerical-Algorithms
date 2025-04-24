package image;
import interpolation.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;

/**
 * This class wraps an image in RBG format. It is possible to scale the image.
 */
public class Picture {
    /**
     * The different RBG color indices.
     */
    public enum RBG_COLORS {
        RED,
        GREEN,
        BLUE
    }


    /**
     * Member variable to store the image.
     */
    private BufferedImage img;

    /**
     * Deep copy of a buffered image.
     * https://stackoverflow.com/a/3514297
     */
    static BufferedImage deepCopy(BufferedImage bi) {
        ColorModel cm = bi.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = bi.copyData(null);
        return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
    }

    /**
     * Size of the image.
     */
    private int width;
    private int height;

    /**
     * Load an image from a file.
     */
    public Picture(String filename) throws IOException {
        File f = new File(filename);
        img = ImageIO.read(f);
        if (!isNull()) {
            width = img.getWidth();
            height = img.getHeight();
        }
    }

    /**
     * Initialize an empty image with given dimension.
     */
    public Picture(int width, int height) {
        img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        this.width = width;
        this.height = height;
    }

    /**
     * Copys a picture.
     */
    public Picture(BufferedImage newImg) {
        if (newImg != null) {
            this.img = deepCopy(newImg);
            width = img.getWidth();
            height = img.getHeight();
        }
    }

    /**
     * Returns the width of the image.
     */
    public int getWidth() {
        return width;
    }

    /**
     * Returns the height of the image.
     */
    public int getHeight() {
        return height;
    }

    /**
     * Checks, whether there is currently an image loaded.
     */
    public boolean isNull() {
        return img == null;
    }

    /**
     * Returns the image.
     */
    public final BufferedImage getImage() {
        return deepCopy(this.img);
    }

    /**
     * Saves the image.
     */
    public void save(String filename, String formatName) throws IOException {
        File f = new File(filename);
        ImageIO.write(img, formatName, f);
    }

    /**
     * Returns a string representation with all rbg values for each pixel.
     */
    public String toString() {
        RBG_COLORS[] rbgColors = {RBG_COLORS.RED, RBG_COLORS.GREEN, RBG_COLORS.GREEN};
        DecimalFormat df = new DecimalFormat("#,##0.00");
        StringBuilder result = new StringBuilder();
        for (RBG_COLORS c : rbgColors) {
            result.append("The ").append(c).append(" values of the image: \n");
            for (int x = 1; x <= width; x++) {
                for (int y = 1; y <= height; y++) {
                    result.append(df.format(getPixel(x, y, c)));
                }
                result.append("\n");
            }
            result.append("\n");
        }
        return result.toString();
    }

    /**
     * Returns the color value of color rbgColor at position (x,y).
     * @param x horizontal position of the pixel from 1 to n.
     * @param y vertical position of the pixel from 1 to n.
     * @param rbgColor specify the color channel.
     * @return brightness of pixel at (x,y) between [0,1] filtered by color channel.
     */
    public double getPixel(int x, int y, RBG_COLORS rbgColor) {
        x = x - 1;
        y = y - 1;

        int rgb = img.getRGB(x, y);
        Color col = new Color(rgb);
        double c = switch (rbgColor) {
            case RED -> col.getRed();
            case GREEN -> col.getGreen();
            case BLUE -> col.getBlue();
        };

        return c / 255.;
    }

    /**
     * Sets the color at position (x, y) to the given rbg values.
     * If the color is not  within [0,1], set it to either 0 or 1 respectively.
     */
    public void setPixel(int x, int y, double r, double g, double b) {
        r = Math.max(Math.min(r, 1.0), 0.0);
        b = Math.max(Math.min(b, 1.0), 0.0);
        g = Math.max(Math.min(g, 1.0), 0.0);

        Color col = new Color((int) Math.round(r * 255.), (int) Math.round(g * 255.), (int) Math.round(b * 255.));
        img.setRGB(x - 1, y - 1, col.getRGB());
    }

    /**
     * Computes the position of the pixel centers, for n pixels.
     */
    private double[] pixelCenters(int n) {
        double[] x = new double[n];
        double dx = 1.0 / n;
        x[0] = 0.5 * dx;
        for (int i = 1; i < n; i++) {
            x[i] = x[i - 1] + dx;
        }
        return x;
    }
    /**
     * Scales the image to dimension (newWidth, newHeight).
     */
    public void scale(int newWidth, int newHeight, InterpolationMethod2D im2d) {
        // Set up old coordinates between 0 and 1
        double[] x = pixelCenters(width);
        double[] y = pixelCenters(height);
        // Set up new coordinates between 0 and 1
        double[] xNew = pixelCenters(newWidth);
        double[] yNew = pixelCenters(newHeight);

        // reserve space for the result
        double[][][] resultImageArray = new double[newWidth][newHeight][3];
        // Treat all colours independently
        RBG_COLORS[] colors = {RBG_COLORS.RED, RBG_COLORS.GREEN, RBG_COLORS.BLUE};
        for (RBG_COLORS c : colors) {
            // First gather the old color values to interpolate
            double[][] z = new double[width][height];
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    z[i][j] = getPixel(i + 1, j + 1, c);
                }
            }
            // Evaluate the 2D interpolation
            im2d.init(x, y, z);
            double[][] result = im2d.evaluate(xNew, yNew);
            // Store color values in result array
            for (int i = 0; i < newWidth; i++) {
                for (int j = 0; j < newHeight; j++) {
                    resultImageArray[i][j][c.ordinal()] = result[i][j];
                }
            }
        }
        // Create new picture.
        this.img = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
        this.img.createGraphics();
        width = newWidth;
        height = newHeight;

        for (int i = 1; i <= newWidth; i++) {
            for (int j = 1; j <= newHeight; j++) {
                double red = resultImageArray[i - 1][j - 1][0];
                double green = resultImageArray[i - 1][j - 1][1];
                double blue = resultImageArray[i - 1][j - 1][2];

                this.setPixel(i, j, red, green, blue);
            }
        }

    }
}

