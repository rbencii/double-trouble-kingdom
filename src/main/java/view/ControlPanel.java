package view;

import javax.swing.*;
import java.awt.*;

public class ControlPanel extends JPanel {

    private final Color backgroundColor;
    private final Color borderColor;
    private final JRoundedButton[] buttons = new JRoundedButton[6]; //for multiple actionListener support

    public ControlPanel() {
        super();

        setLayout(new FlowLayout(FlowLayout.CENTER, 20, 8));
        backgroundColor = new Color(185, 185, 185, 200);
        borderColor = Color.white;
        setOpaque(false);
        createButtons();
        repaint();
    }

    public ControlPanel(Color bg, Color br) {
        super(new FlowLayout());
        backgroundColor = bg;
        borderColor = br;
        setOpaque(false);
        createButtons();
    }

    private void createButtons() {
        System.out.println("size: " + getSize());
        /*JRoundedButton[] controls = new JRoundedButton[6];
        for (JRoundedButton e : controls) {
            e = new JRoundedButton("0", 50, 50);
            this.add(e);
        }*/
        String[] texts = new String[]{"1", "2", "3", "4", "5", "6"};
        for (int i = 0; i < texts.length; i++) {
            buttons[i] = new JRoundedButton(texts[i], 50, 50);
            this.add(buttons[i]);
        }

        //TESTING PURPOSES
        for (int i = 3; i < buttons.length; i++) {
            buttons[i].setEnabled(false);
        }

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(backgroundColor);
        g2d.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 60, 100);
        g2d.setColor(borderColor);
        g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 60, 100);
        g2d.dispose();
    }

    private static class JRoundedButton extends JButton {
        private final Color selectedColor = new Color(92, 200, 230, 185);
        private final Color notEnabled_bgColor = new Color(46, 154, 200, 130);
        private final Color enabled_bgColor = new Color(16, 124, 170, 185);

        public JRoundedButton(String text, int w, int h) {
            super(text);
            setPreferredSize(new Dimension(w, h));
            setOpaque(false);
            setFocusPainted(false);
            setBorderPainted(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            if (!this.isEnabled()) {
                g2d.setColor(notEnabled_bgColor);
            } else if (!this.getModel().isArmed()) {
                g2d.setColor(enabled_bgColor);
            } else {
                g2d.setColor(selectedColor);
            }
            g2d.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 25, 25);
            g2d.dispose();
        }
    }
}
