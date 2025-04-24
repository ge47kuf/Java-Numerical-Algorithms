package ui;

import image.Picture;
import interpolation.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;

public class ImageViewer extends JFrame implements ActionListener, MouseWheelListener {
    /**
     * ViewComponent controls the canvas for the image.
     */
    static class ViewComponent extends JComponent {
        // The picture to show.
        private Picture pic;

        // The last picture for the undo operation.
        private Picture oldPic;

        ViewComponent() {
            super();
        }

        /**
         * Loads a picture from file.
         */
        public void setImage(File file) {
            if (pic != null) {
                oldPic = new Picture(pic.getImage());
            }
            if (file == null || !file.isFile()) {
                return;
            }
            try {
                pic = new Picture(file.getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (!pic.isNull()) {
                repaint();
            }
        }

        /**
         * Stores the image in file.
         */
        public void saveImage(File file) {
            if (pic == null || pic.isNull()) {
                return;
            }
            try {
                pic.save(file.getAbsolutePath(), "png");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        /**
         * Scales the image to new dimensions with the interpolation method im.
         */
        public void scaleImage(int newWidth, int newHeight, InterpolationMethod2D im) {
            if (newHeight < 1 || newWidth < 1) {
                System.out.println("Image is too small");
                return;
            }
            oldPic = new Picture(pic.getImage());
            pic.scale(newWidth, newHeight, im);
            repaint();
        }

        /**
         * Scales the image by the factor scale with the interpolation method im.
         */
        public void scaleImage(double scale, InterpolationMethod2D im) {
            if (pic != null && !pic.isNull()) {
                int newWidth = (int) Math.round(pic.getWidth() * scale);
                int newHeight = (int) Math.round(pic.getHeight() * scale);
                this.scaleImage(newWidth, newHeight, im);
            }
        }

        /**
         * Reverts the last image action.
         */
        public void undo() {
            if (oldPic != null && !oldPic.isNull()) {
                pic = new Picture(oldPic.getImage());
                oldPic = null;
                repaint();
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            if (pic != null && !pic.isNull()) {
                g.drawImage(pic.getImage(), 0, 0, this);
            }
        }
    }

    /**
     * List of available interpolation methods.
     */
    private final String[] modeStrings = {"Nearest", "Linear", "Newton", "Cubic"};

    /**
     * Parse an integer to an Interpolation method, such that modeStrings[i] describes the return value.
     */
    public InterpolationMethod parseMode(int i) {
        return switch (i) {
            case 0 -> new NearestNeighbor();
            case 1 -> new PiecewiseLinear();
            case 2 -> new NewtonPolynomial();
            case 3 -> new CubicSpline();
            default -> null;
        };
    }

    private final ViewComponent viewComponent = new ViewComponent();
    private JMenuBar mBar = new JMenuBar();

    // File Menu
    private JMenu fileMenu = new JMenu("File");
    private JMenuItem openItem = new JMenuItem("Open");
    private JMenuItem saveItem = new JMenuItem("Save");
    private JMenuItem undoItem = new JMenuItem("Undo");

    // Combo box for the interpolation methods
    private JComboBox<String> interpolationModeBox = new JComboBox<>(modeStrings);

    // Text field to add the scale factor
    private JTextField scaleFactorField = new JTextField(1 + "", 4);

    // The scale button
    private JButton scaleButton = new JButton("Scale");

    /**
     * Constructor, puts everything together.
     */
    public ImageViewer() {
        super("Image Viewer");

        openItem.addActionListener(this);
        saveItem.addActionListener(this);
        undoItem.addActionListener(this);
        fileMenu.add(openItem);
        fileMenu.add(saveItem);
        fileMenu.add(undoItem);

        interpolationModeBox.setSelectedIndex(0);
        scaleButton.addActionListener(this);

        mBar.add(fileMenu);
        mBar.add(interpolationModeBox);
        mBar.add(scaleFactorField);
        mBar.add(scaleButton);

        scaleFactorField.addActionListener(this);

        setJMenuBar(mBar);
        add(viewComponent);

        // Mouse wheel listener
        this.addMouseWheelListener(this);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
    }

    /**
     * Process user actions.
     */
    public void actionPerformed(ActionEvent e) {
        JComponent source = (JComponent) (e.getSource());

        // Open new image.
        if (source == openItem) {
            JFileChooser d = new JFileChooser();
            d.setCurrentDirectory(new File("./"));

            d.setFileFilter(new PictureFilter());
            int returnVal = d.showOpenDialog(null);

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = d.getSelectedFile();
                viewComponent.setImage(file);
                System.out.println("Opening: " + file.getName() + ".\n");
            } else {
                System.out.println("Open command cancelled by user.\n");
            }
        }
        // Save current image.
        else if (source == saveItem) {
            JFileChooser d = new JFileChooser();
            d.setCurrentDirectory(new File("./"));

            d.setFileFilter(new PictureFilter());
            int returnVal = d.showSaveDialog(null);

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = d.getSelectedFile();
                viewComponent.saveImage(file);
                System.out.println("Saving: " + file.getName() + ".\n");
            } else {
                System.out.println("Open command cancelled by user.\n");
            }
            // Undo
        } else if (source == undoItem) {
            viewComponent.undo();
            // Scale the image
        } else if (source == scaleButton
                || source == scaleFactorField) {
            int mode = interpolationModeBox.getSelectedIndex();
            InterpolationMethod2D im2d = new InterpolationMethod2D(parseMode(mode));
            try {
                double scaleFactor = Double.parseDouble(scaleFactorField.getText());
                if (scaleFactor >= 0) {
                    viewComponent.scaleImage(scaleFactor, im2d);
                }
                else {
                    System.out.println("Bitte positive Zahl eingeben!");
                }
            } catch (NumberFormatException except) {
                System.out.println("Add a number");
            }
        }
    }

    /**
     * Scale by 10% with every mouse wheel notch.
     */
    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        int mode = interpolationModeBox.getSelectedIndex();
        InterpolationMethod2D im2d = new InterpolationMethod2D(parseMode(mode));

        int notches = e.getWheelRotation();
        if (notches < 0) {
            viewComponent.scaleImage(Math.pow(0.8, -notches), im2d);
        } else {
            viewComponent.scaleImage(Math.pow(1.2, notches), im2d);
        }
    }

    /**
     * Filters filenames with typical picture endings.
     */
    static class PictureFilter extends FileFilter {
        @Override
        public boolean accept(File f) {
            return f.isDirectory()
                    || f.getName().toLowerCase().endsWith(".jpg")
                    || f.getName().toLowerCase().endsWith(".gif")
                    || f.getName().toLowerCase().endsWith(".bmp")
                    || f.getName().toLowerCase().endsWith(".png");
        }

        @Override
        public String getDescription() {
            return "*.jpg;*.gif;*.bmp;*.png";
        }

    }

    /**
     * Start the GUI.
     */
    public static void main(String[] args) {
        new ImageViewer().setVisible(true);
    }

}
