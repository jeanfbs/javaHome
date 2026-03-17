package br.com.javahome.ui;

import javax.swing.*;
import javax.swing.plaf.basic.BasicComboBoxUI;
import java.awt.*;

import static br.com.javahome.ui.RoomUI.DEFAULT;

public class ComboBoxUI extends JComboBox<String> {

    public ComboBoxUI() {
        setUI(createCustomUI());
        setRenderer(createCustomRenderer());
    }

    public BasicComboBoxUI createCustomUI(){
        return new BasicComboBoxUI(){

            @Override
            protected JButton createArrowButton() {

                JButton button = new JButton("▼");
                button.setForeground(DEFAULT);
                Font font = button.getFont();
                button.setFont(new Font(font.getFontName(), font.getStyle(), 12));

                button.setBackground(Color.BLACK);
                button.setBorder(null);
                button.setFocusPainted(false);

                return button;
            }

            @Override
            public void installUI(JComponent c) {
                super.installUI(c);

                JComboBox<?> combo = (JComboBox<?>) c;
                Font font = combo.getFont();
                combo.setFont(new Font(font.getFontName(), font.getStyle(), 12));
                combo.setBackground(Color.BLACK);
                combo.setForeground(DEFAULT);
                combo.setBorder(BorderFactory.createLineBorder(DEFAULT, 1));
                combo.setFocusable(false);

            }
        };
    }

    public DefaultListCellRenderer createCustomRenderer(){
        return new DefaultListCellRenderer() {

            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                          boolean isSelected, boolean cellHasFocus) {

                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                label.setBackground(isSelected ? DEFAULT : Color.BLACK);
                label.setForeground(isSelected ? Color.BLACK : DEFAULT);
                label.setBorder(BorderFactory.createEmptyBorder(5,10,5,10));
                return label;
            }
        };
    }
}
