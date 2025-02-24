package com.moldavets.microservices.report_generator_service.service.Impl;

import com.moldavets.microservices.report_generator_service.service.ImageGeneratorService;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.data.category.DefaultCategoryDataset;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Map;

@Service
public class ImageGeneratorServiceImpl implements ImageGeneratorService {

    private final int WIDTH = 1920;
    private final int HEIGHT = 1080;

    @Override
    public byte[] getImageAsByteArray(Map<String, Integer> skills) {

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        for(String tempSkill : skills.keySet()) {
            dataset.addValue(skills.get(tempSkill), "Skills", tempSkill);
        }

        JFreeChart barChart = ChartFactory.createBarChart(
                "Analytics",
                "Tech",
                "Count",
                dataset
        );

        CategoryPlot plot = barChart.getCategoryPlot();
        CategoryAxis axis = plot.getDomainAxis();

        axis.setCategoryLabelPositions(CategoryLabelPositions.UP_90);

        NumberAxis yAxis = (NumberAxis) plot.getRangeAxis();
        yAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            ChartUtilities.writeChartAsPNG(outputStream, barChart, WIDTH, HEIGHT);
            return outputStream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
