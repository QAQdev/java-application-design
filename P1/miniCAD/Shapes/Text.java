package Shapes;

import java.awt.*;

public class Text extends Shape {
    private String text;

    @Override
    public void paint(Graphics2D g) {
        this.setGraphicAttr(g);

        FontMetrics font_metrics;

        if (diagonal_dst == null) {
            g.setFont(new Font(Font.SERIF, Font.PLAIN, (int) (10 * shape_weight)));
            font_metrics = g.getFontMetrics();
            diagonal_dst = new Point(
                    diagonal_src.x + font_metrics.stringWidth(text),
                    diagonal_src.y + font_metrics.getAscent()
            );
        } else {
            int width = Math.abs(diagonal_src.x - diagonal_dst.x);
            int font_weight = 1;

            do {
                g.setFont(new Font(Font.SERIF, Font.PLAIN, font_weight));
                font_metrics = g.getFontMetrics();
                font_weight++;
            } while (font_metrics.stringWidth(text) <= width);

            g.setFont(new Font(Font.SERIF, Font.PLAIN, font_weight));
            font_metrics = g.getFontMetrics();

        }
        g.drawString(
                text,
                Math.min(diagonal_src.x, diagonal_dst.x),
                Math.min(diagonal_src.y, diagonal_dst.y) + font_metrics.getAscent()
        );
    }

    public void setText(String text) {
        this.text = text;
    }
}
