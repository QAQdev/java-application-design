package View;

import Shapes.Shape;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;

public class MenuBar extends JMenuBar {
    private JMenu file = new JMenu("File");

    private JMenuItem open_btn = new JMenuItem("Open");

    private JMenuItem save_btn = new JMenuItem("Save");

    public MenuBar(Canvas canvas) {
        file.setSize(200, 100);
        add(file);
        file.add(open_btn);
        file.add(save_btn);

        open_btn.addActionListener(e -> {
            JFileChooser opener = new JFileChooser();
            int val = opener.showOpenDialog(opener);
            String file_name = "";

            if (val == JFileChooser.APPROVE_OPTION) {
                file_name = opener.getSelectedFile().getAbsolutePath();
            }
            if (!file_name.isEmpty()) {
                ObjectInputStream inputShapeStream;
                try {
                    inputShapeStream = new ObjectInputStream(new FileInputStream(file_name));
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                try {
                    canvas.setShapesList((ArrayList<Shape>) inputShapeStream.readObject());
                } catch (IOException | ClassNotFoundException ex) {
                    throw new RuntimeException(ex);
                }
                canvas.repaint();
            }
        });

        save_btn.addActionListener(e -> {
            JFileChooser saver = new JFileChooser();
            saver.showSaveDialog(canvas);
            File file = saver.getSelectedFile();
            String file_name = file.getPath();

            if (!file_name.isEmpty()) {
                ObjectOutputStream out;
                try {
                    out = new ObjectOutputStream(new FileOutputStream(file_name));
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }

                try {
                    out.writeObject(canvas.getShapesList());
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }

                try {
                    out.close();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }
}