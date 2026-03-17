package br.com.javahome.ui;

import javax.swing.*;
import javax.swing.plaf.basic.BasicTableUI;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;

import static br.com.javahome.ui.RoomUI.DEFAULT;

public class TableUI extends JTable {

    public TableUI() {
        configure();
    }

    private void configure() {

        setUI(new GreenBlackTableUI());
        packColumns();

        setFont(new Font(getFont().getFontName(), getFont().getStyle(), 10));
        setFillsViewportHeight(true);
        setDefaultRenderer(Object.class, new GreenBlackCellRenderer());
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);


        JTableHeader header = getTableHeader();
        header.setDefaultRenderer(new GreenBlackHeaderRenderer());
        header.setBackground(Color.BLACK);
        header.setForeground(DEFAULT);
        header.setReorderingAllowed(false);

        setBorder(BorderFactory.createLineBorder(DEFAULT));
    }

    private void packColumns() {

        for (int col = 0; col < getColumnCount(); col++) {

            int maxWidth = 50;
            TableCellRenderer headerRenderer = getTableHeader().getDefaultRenderer();
            Component headerComp = headerRenderer.getTableCellRendererComponent(this, getColumnName(col), false, false, -1, col);
            maxWidth = Math.max(maxWidth, headerComp.getPreferredSize().width);

            for (int row = 0; row < getRowCount(); row++) {
                TableCellRenderer renderer = getCellRenderer(row, col);
                Component comp = prepareRenderer(renderer, row, col);
                maxWidth = Math.max(maxWidth, comp.getPreferredSize().width);
            }

            TableColumn column = getColumnModel().getColumn(col);
            column.setPreferredWidth(maxWidth + 10);
        }
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }

    static class GreenBlackTableUI extends BasicTableUI {

        @Override
        public void installUI(javax.swing.JComponent c) {
            super.installUI(c);

            table.setBackground(Color.BLACK);
            table.setForeground(DEFAULT);

            table.setGridColor(DEFAULT);
            table.setShowGrid(true);

            table.setRowHeight(24);
        }

        @Override
        public void paint(Graphics g, javax.swing.JComponent c) {
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, c.getWidth(), c.getHeight());
            super.paint(g, c);
        }
    }

    static class GreenBlackCellRenderer extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                       boolean hasFocus, int row, int column) {

            Component c = super.getTableCellRendererComponent(table, value, isSelected, false, row, column);

            if (isSelected) {
                c.setBackground(Color.decode("#004207"));
                c.setForeground(Color.BLACK);
            } else {
                c.setBackground(Color.BLACK);
                c.setForeground(DEFAULT);
            }

            return c;
        }
    }

    static class GreenBlackHeaderRenderer extends DefaultTableCellRenderer {

        public GreenBlackHeaderRenderer() {
            setHorizontalAlignment(CENTER);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                       boolean hasFocus, int row, int column) {

            super.getTableCellRendererComponent(table, value, false, false, row, column);
            setBackground(Color.BLACK);
            setForeground(DEFAULT);
            setBorder(BorderFactory.createLineBorder(DEFAULT));
            return this;
        }
    }
}
