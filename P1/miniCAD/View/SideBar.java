package View;

import Controller.*;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

import javax.swing.*;

public class SideBar extends JPanel {
    Canvas canvas;
    private JButton line_btn = new JButton("Line");
    private JButton rect_btn = new JButton("Rect");
    private JButton ellipse_btn = new JButton("Ellipse");
    private JButton text_btn = new JButton("Text");
    private JButton select_btn = new JButton("Select");
    private JButton resize_btn = new JButton("Resize");
    private JButton delete_btn = new JButton("Delete");
    private Map<Color, JButton> color_btn = new HashMap<>();
    private JSlider line_weight_btn = new JSlider(5, 30, 10);

    public SideBar(Canvas canvas) {
        this.canvas = canvas;
        this.setSideBarLayout();
        line_btn.setSelected(true); // default choice

        // add listener to line button
        line_btn.addActionListener(e -> SideBar.this.canvas.setCtrl(new LineController()));

        // add listener to rect button
        rect_btn.addActionListener(e -> SideBar.this.canvas.setCtrl(new RectController()));

        // add listener to ellipse button
        ellipse_btn.addActionListener(e -> SideBar.this.canvas.setCtrl(new EllipseController()));

        // add listener to text button
        text_btn.addActionListener(e -> SideBar.this.canvas.setCtrl(new TextController()));

        /* add listener to mode button (select, resize, delete and set) */
        select_btn.addActionListener(e -> {
            SideBar.this.canvas.setCtrl(new ModeController());
            canvas.setMode(Mode.SELECT);
        });

        resize_btn.addActionListener(e -> {
            SideBar.this.canvas.setCtrl(new ModeController());
            canvas.setMode(Mode.RESIZE);
        });

        delete_btn.addActionListener(e -> {
            SideBar.this.canvas.setCtrl(new ModeController());
            canvas.setMode(Mode.DELETE);
        });

        // add listener to color buttons
        for (Map.Entry<Color, JButton> entry : color_btn.entrySet()) {
            entry.getValue().addActionListener(e -> SideBar.this.canvas.setSelectedColor(entry.getKey()));
        }

        // add listener to line weight
        line_weight_btn.addChangeListener(e -> SideBar.this.canvas.setSelectedWeight((float) line_weight_btn.getValue() / 10));
    }

    private void setSideBarLayout() {
        color_btn.put(Color.RED, new JButton());
        color_btn.put(Color.PINK, new JButton());
        color_btn.put(Color.ORANGE, new JButton());
        color_btn.put(Color.MAGENTA, new JButton());
        color_btn.put(Color.GREEN, new JButton());
        color_btn.put(Color.YELLOW, new JButton());
        color_btn.put(Color.BLUE, new JButton());
        color_btn.put(Color.BLACK, new JButton());
        color_btn.put(Color.CYAN, new JButton());

        GridBagLayout gbag_layout = new GridBagLayout();
        GridBagConstraints constraints = new GridBagConstraints();
        setLayout(gbag_layout);

        constraints.fill = GridBagConstraints.BOTH;
        constraints.weightx = 0.3;
        constraints.weighty = 0.15;
        constraints.gridwidth = GridBagConstraints.REMAINDER;

        gbag_layout.setConstraints(line_btn, constraints);
        add(line_btn);

        gbag_layout.setConstraints(rect_btn, constraints);
        add(rect_btn);

        gbag_layout.setConstraints(ellipse_btn, constraints);
        add(ellipse_btn);

        gbag_layout.setConstraints(text_btn, constraints);
        add(text_btn);

        gbag_layout.setConstraints(select_btn, constraints);
        add(select_btn);

        gbag_layout.setConstraints(resize_btn, constraints);
        add(resize_btn);

        gbag_layout.setConstraints(delete_btn, constraints);
        add(delete_btn);

        constraints.weightx = 0.3;
        constraints.weighty = 0.15;

        constraints.gridwidth = 1;
        gbag_layout.setConstraints(color_btn.get(Color.RED), constraints);
        color_btn.get(Color.RED).setBackground(Color.RED);
        add(color_btn.get(Color.RED));
        gbag_layout.setConstraints(color_btn.get(Color.PINK), constraints);
        color_btn.get(Color.PINK).setBackground(Color.PINK);
        add(color_btn.get(Color.PINK));
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        gbag_layout.setConstraints(color_btn.get(Color.ORANGE), constraints);
        color_btn.get(Color.ORANGE).setBackground(Color.ORANGE);
        add(color_btn.get(Color.ORANGE));

        constraints.gridwidth = 1;
        gbag_layout.setConstraints(color_btn.get(Color.MAGENTA), constraints);
        color_btn.get(Color.MAGENTA).setBackground(Color.MAGENTA);
        add(color_btn.get(Color.MAGENTA));
        gbag_layout.setConstraints(color_btn.get(Color.GREEN), constraints);
        color_btn.get(Color.GREEN).setBackground(Color.GREEN);
        add(color_btn.get(Color.GREEN));
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        gbag_layout.setConstraints(color_btn.get(Color.YELLOW), constraints);
        color_btn.get(Color.YELLOW).setBackground(Color.YELLOW);
        add(color_btn.get(Color.YELLOW));

        constraints.gridwidth = 1;
        gbag_layout.setConstraints(color_btn.get(Color.BLUE), constraints);
        color_btn.get(Color.BLUE).setBackground(Color.BLUE);
        add(color_btn.get(Color.BLUE));
        gbag_layout.setConstraints(color_btn.get(Color.BLACK), constraints);
        color_btn.get(Color.BLACK).setBackground(Color.BLACK);
        add(color_btn.get(Color.BLACK));
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        gbag_layout.setConstraints(color_btn.get(Color.CYAN), constraints);
        color_btn.get(Color.CYAN).setBackground(Color.CYAN);
        add(color_btn.get(Color.CYAN));


        gbag_layout.setConstraints(line_weight_btn, constraints);
        line_weight_btn.setMajorTickSpacing(5);
        line_weight_btn.setMinorTickSpacing(1);
        line_weight_btn.setPaintLabels(true);
        line_weight_btn.setPaintTicks(true);
        add(line_weight_btn);
    }
}