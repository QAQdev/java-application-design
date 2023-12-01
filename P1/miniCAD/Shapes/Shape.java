package Shapes;

import java.awt.*;
import java.io.Serializable;

public abstract class Shape implements Serializable {
    Point diagonal_src, diagonal_dst;
    Color shape_color = Color.BLACK;
    Float shape_weight = 1.0f;

    public void setColor(Color c) {
        this.shape_color = c;
    }

    public void setSrcByOfs(int dx, int dy) {
        diagonal_src.x += dx;
        diagonal_src.y += dy;
    }

    public void setDstByOfs(int dx, int dy) {
        diagonal_dst.x += dx;
        diagonal_dst.y += dy;
    }

    public void setWeight(Float w) {
        this.shape_weight = w;
    }

    public void setFirst(Point p) {
        this.diagonal_src = p;
    }

    public void setSecond(Point p) {
        this.diagonal_dst = p;
    }

    protected void setGraphicAttr(Graphics2D g) {
        Stroke stroke = new BasicStroke(
                shape_weight,
                BasicStroke.CAP_ROUND,
                BasicStroke.JOIN_ROUND,
                3f,
                new float[]{1, 0},
                0f);
        g.setStroke(stroke);
        g.setColor(shape_color);
        g.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );
    }

    public abstract void paint(Graphics2D g);

    public Point getDiagonalSrc() {
        return diagonal_src;
    }

    public Point getDiagonalDst() {
        return diagonal_dst;
    }

    public boolean isSelected(Point p) {
        return p.x <= Math.max(diagonal_src.x, diagonal_dst.x) && p.x >= Math.min(diagonal_src.x, diagonal_dst.x)
                && p.y <= Math.max(diagonal_src.y, diagonal_dst.y) && p.y >= Math.min(diagonal_src.y, diagonal_dst.y);
    }
}