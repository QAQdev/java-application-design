package Controller;

import Shapes.Text;
import View.Canvas;

import javax.swing.JOptionPane;
import java.awt.Point;
import java.awt.event.MouseEvent;

public class TextController extends Controller {

    @Override
    public void mousePressed(Canvas canvas, MouseEvent event) {
    }

    @Override
    public void mouseReleased(Canvas canvas, MouseEvent event) {

    }

    @Override
    public void mouseClicked(Canvas canvas, MouseEvent event) {
        Text text_shape = new Text();
        String temp = JOptionPane.showInputDialog(canvas,
                "Enter the text",
                "Text Input Box", JOptionPane.PLAIN_MESSAGE);

        if (temp != null) {
            text_shape.setText(temp);
            text_shape.setColor(canvas.getSelectedColor());
            text_shape.setWeight(canvas.getSelectedWeight());
            text_shape.setFirst(new Point(event.getX(), event.getY()));
            canvas.getShapesList().add(text_shape);
            canvas.repaint();
        }
    }

    @Override
    public void mouseDragged(Canvas canvas, MouseEvent event) {
    }
}
