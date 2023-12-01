package Controller;

import Shapes.Shape;
import View.Canvas;
import View.Mode;

import java.awt.Point;
import java.awt.event.MouseEvent;

public class ModeController extends Controller {
    Point reference_point = null;

    private void findTargetShape(Canvas canvas, MouseEvent event) {
        for (Shape shape : canvas.getShapesList()) {
            if (shape.isSelected(new Point(event.getX(), event.getY()))) {
                canvas.setSelectedShape(shape);
                break;
            }
        }
    }

    @Override
    public void mousePressed(Canvas canvas, MouseEvent event) {
        reference_point = new Point(
                event.getX(),
                event.getY()
        );

        findTargetShape(canvas, event);
        Shape selected_shape = canvas.getSelectedShape();

        if (canvas.getMode() == Mode.DELETE) {
            if (selected_shape != null) {
                canvas.getShapesList().remove(selected_shape);
            }
            canvas.repaint();
        }
    }

    @Override
    public void mouseReleased(Canvas canvas, MouseEvent event) {
        reference_point = null;
    }

    @Override
    public void mouseClicked(Canvas canvas, MouseEvent event) {

        findTargetShape(canvas, event);

        Shape selected_shape = canvas.getSelectedShape();
        if (canvas.getMode() == Mode.DELETE) {
            if (selected_shape != null) {
                canvas.getShapesList().remove(selected_shape);
            }
            canvas.repaint();
        }
    }

    @Override
    public void mouseDragged(Canvas canvas, MouseEvent event) {

        findTargetShape(canvas, event);

        Shape selected_shape = canvas.getSelectedShape();

        switch (canvas.getMode()) {
            case SELECT -> {
                if (selected_shape != null) {
                    int x_ofs = event.getX() - reference_point.x;
                    int y_ofs = event.getY() - reference_point.y;
                    selected_shape.setSrcByOfs(x_ofs, y_ofs);
                    selected_shape.setDstByOfs(x_ofs, y_ofs);

                    reference_point.x += x_ofs;
                    reference_point.y += y_ofs;
                    canvas.repaint();
                }
            }

            case RESIZE -> {
                if (selected_shape != null) {

                    double dis2src = Math.pow(event.getX() - selected_shape.getDiagonalSrc().x, 2)
                            + Math.pow(event.getY() - selected_shape.getDiagonalSrc().y, 2);

                    double dis2dst = Math.pow(event.getX() - selected_shape.getDiagonalDst().x, 2)
                            + Math.pow(event.getY() - selected_shape.getDiagonalDst().y, 2);

                    if (dis2src <= dis2dst) {
                        selected_shape.setFirst(new Point(event.getX(), event.getY()));
                    } else {
                        selected_shape.setSecond(new Point(event.getX(), event.getY()));
                    }
                    canvas.repaint();
                }
            }
        }
    }
}