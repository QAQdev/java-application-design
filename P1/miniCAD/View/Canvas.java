package View;

import Controller.*;
import Shapes.Shape;

import javax.swing.JPanel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;

public class Canvas extends JPanel {
    private ArrayList<Shape> shapes_list = new ArrayList<>();

    private Color selected_color = Color.BLACK;

    private Float selected_weight = 1.0f;

    Mode mode = null;

    private Shape selected_shape = null;

    private Shape cur_shape = null;

    private Controller ctrl = new SilentController();

    public Mode getMode() {
        return mode;
    }

    public void setMode(Mode mode) {
        this.mode = mode;
    }

    public Shape getCurShape() {
        return cur_shape;
    }

    public Shape getSelectedShape() {
        return this.selected_shape;
    }

    public void setShapesList(ArrayList<Shape> shapes_list) {
        this.shapes_list = shapes_list;
    }


    public void setCurShape(Shape shape) {
        this.cur_shape = shape;
    }

    public void setSelectedShape(Shape shape) {
        this.selected_shape = shape;
    }

    public ArrayList<Shape> getShapesList() {
        return shapes_list;
    }

    public Color getSelectedColor() {
        return selected_color;
    }

    public Float getSelectedWeight() {
        return selected_weight;
    }

    public void setCtrl(Controller ctrl) {
        this.ctrl = ctrl;
    }

    public void setSelectedColor(Color color) {
        this.selected_color = color;
    }

    public void setSelectedWeight(Float weight) {
        this.selected_weight = weight;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        for (Shape shape : shapes_list) {
            shape.paint((Graphics2D) g);
        }

        if (cur_shape != null) {
            cur_shape.paint((Graphics2D) g);
        }
    }

    public Canvas() {
        setBackground(Color.WHITE);

        // add listeners to canvas
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                ctrl.mouseClicked(Canvas.this, e);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                ctrl.mousePressed(Canvas.this, e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                ctrl.mouseReleased(Canvas.this, e);
            }

        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                ctrl.mouseDragged(Canvas.this, e);
            }
        });
    }
}