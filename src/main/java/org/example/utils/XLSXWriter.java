package org.example.utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.example.metrics.Metrics;

import java.io.FileOutputStream;
import java.io.IOException;

public class XLSXWriter implements AutoCloseable {
    private final Workbook workbook;
    private final Sheet sheet;
    private final String filename;
    private int rowNum = 0;

    public XLSXWriter(String filename) {
        this.filename = filename;
        this.workbook = new XSSFWorkbook();
        this.sheet = workbook.createSheet("Metrics");

        // Header
        Row header = sheet.createRow(rowNum++);
        String[] cols = {"Algorithm", "RunTime(ms)", "Counter", "Allocations", "MaxDepth"};
        for (int i = 0; i < cols.length; i++) {
            Cell cell = header.createCell(i);
            cell.setCellValue(cols[i]);
        }
    }

    public void write(Metrics metrics) {
        Row row = sheet.createRow(rowNum++);
        row.createCell(0).setCellValue(metrics.getAlgorithmName());
        row.createCell(1).setCellValue(metrics.getRunTime());
        row.createCell(2).setCellValue(metrics.getCounter());
        row.createCell(3).setCellValue(metrics.getAllocations());
        row.createCell(4).setCellValue(metrics.getMaxDepth());
    }

    @Override
    public void close() throws IOException {
        try (FileOutputStream out = new FileOutputStream(filename)) {
            workbook.write(out);
        }
        workbook.close();
    }
}