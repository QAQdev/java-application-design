package Shapes;

import java.awt.*;

public class Line extends Shape {

    @Override
    public void paint(Graphics2D g) {
        this.setGraphicAttr(g);
        g.drawLine(
                this.diagonal_src.x, this.diagonal_src.y,
                this.diagonal_dst.x, this.diagonal_dst.y
        );
    }
}
