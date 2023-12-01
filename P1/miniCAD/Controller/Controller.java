package Controller;

import View.Canvas;
import java.awt.event.MouseEvent;

public abstract class Controller {
    public abstract void mousePressed(Canvas canvas, MouseEvent event);

    public abstract void mouseReleased(Canvas canvas, MouseEvent event);

    public abstract void mouseClicked(Canvas canvas, MouseEvent event);

    public abstract void mouseDragged(Canvas canvas, MouseEvent event);
}