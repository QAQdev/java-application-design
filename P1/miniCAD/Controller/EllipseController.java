package Controller;

import Shapes.Ellipse;
import View.Canvas;

import java.awt.Point;
import java.awt.event.MouseEvent;

public class EllipseController extends Controller {
    @Override
    public void mousePressed(Canvas canvas, MouseEvent event) {
        canvas.setCurShape(new Ellipse());
        canvas.getCurShape().setColor(canvas.getSelectedColor());
        canvas.getCurShape().setWeight(canvas.getSelectedWeight());
        canvas.getCurShape().setFirst(new Point(event.getX(), event.getY()));
    }

    @Override
    public void mouseReleased(Canvas canvas, MouseEvent event) {
        canvas.getShapesList().add(canvas.getCurShape());
        canvas.setCurShape(null);
    }

    @Override
    public void mouseClicked(Canvas canvas, MouseEvent event) {
    }

    @Override
    public void mouseDragged(Canvas canvas, MouseEvent event) {
        canvas.getCurShape().setSecond(new Point(event.getX(), event.getY()));
        canvas.repaint();
    }
}