package com.moldavets.microservices.report_generator_service.service.Impl;

import com.moldavets.microservices.report_generator_service.exception.ConvertImageException;
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
import java.io.IOException;
import java.time.LocalDate;
import java.util.Map;

@Service
public class ImageGeneratorServiceImpl implements ImageGeneratorService {

    @Override
    public byte[] getImageAsByteArray(Map<String, Integer> skills, String tech) {

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        for(Map.Entry<String, Integer> entry : skills.entrySet()) {
            String key = entry.getKey();
            Integer value = entry.getValue();
            dataset.addValue(value, "Skills", key);
        }

        JFreeChart barChart = ChartFactory.createBarChart(
                "Analytics - " + tech.toUpperCase() + " - " + LocalDate.now().getYear() + "/" + LocalDate.now().getMonth(),
                "Skills",
                "Count",
                dataset
        );

        CategoryPlot plot = barChart.getCategoryPlot();
        CategoryAxis axis = plot.getDomainAxis();

        axis.setCategoryLabelPositions(CategoryLabelPositions.UP_90);

        NumberAxis yAxis = (NumberAxis) plot.getRangeAxis();
        yAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            ChartUtilities.writeChartAsPNG(outputStream, barChart, 1920, 1080);
            return outputStream.toByteArray();
        } catch (IOException e) {
            throw new ConvertImageException(e.getMessage());
        }
    }
}
