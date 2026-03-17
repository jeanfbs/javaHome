package br.com.javahome.ui;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.CategoryItemRenderer;

import java.awt.*;

import static br.com.javahome.ui.RoomUI.DEFAULT;
import static java.awt.Color.BLACK;
import static java.awt.Color.GREEN;

public class ChartPanelUI extends ChartPanel {

    public ChartPanelUI(final JFreeChart chart) {
        super(chart);
        configure(chart);
    }

    private void configure(final JFreeChart chart) {
        chart.setBackgroundPaint(BLACK);
        chart.getTitle().setPaint(DEFAULT);

        Font basicFont = new Font(getFont().getFontName(), Font.BOLD, 12);
        chart.getTitle().setFont(basicFont);

        CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(BLACK);

        plot.setOutlinePaint(DEFAULT);
        plot.setOutlineStroke(new BasicStroke(1f));

        plot.setRangeGridlinePaint(DEFAULT);
        plot.setDomainGridlinePaint(DEFAULT);


        CategoryAxis domainAxis = plot.getDomainAxis();
        domainAxis.setLabelPaint(DEFAULT);
        domainAxis.setTickLabelPaint(DEFAULT);
        domainAxis.setAxisLinePaint(DEFAULT);
        domainAxis.setLabelFont(basicFont);

        ValueAxis rangeAxis = plot.getRangeAxis();
        rangeAxis.setLabelPaint(DEFAULT);
        rangeAxis.setTickLabelPaint(DEFAULT);
        rangeAxis.setAxisLinePaint(DEFAULT);
        rangeAxis.setLabelFont(basicFont);

        CategoryItemRenderer renderer = plot.getRenderer();
        renderer.setSeriesPaint(0, GREEN);
    }
}
