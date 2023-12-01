package View;

import javax.swing.JFrame;
import java.awt.*;

public class Frame extends JFrame {
    private Canvas canvas = new Canvas();
    private SideBar sidebar = new SideBar(canvas);
    private MenuBar menubar = new MenuBar(canvas);

    public Frame() {
        setTitle("miniCAD");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1024, 640);
        setLayout(new BorderLayout());

        add(sidebar, BorderLayout.EAST);
        add(canvas, BorderLayout.CENTER);
        setJMenuBar(menubar);
    }

    public static void main(String[] args) {
        Frame frame = new Frame();
        frame.setVisible(true);
    }
}
