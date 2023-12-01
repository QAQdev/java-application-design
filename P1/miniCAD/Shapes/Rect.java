package Shapes;

import java.awt.Graphics2D;

public class Rect extends Shape {
    @Override
    public void paint(Graphics2D g) {
        this.setGraphicAttr(g);
        g.drawRect(
                Math.min(diagonal_src.x, diagonal_dst.x),
                Math.min(diagonal_src.y, diagonal_dst.y),
                Math.abs(diagonal_dst.x - diagonal_src.x),
                Math.abs(diagonal_dst.y - diagonal_src.y)
        );
    }
}
