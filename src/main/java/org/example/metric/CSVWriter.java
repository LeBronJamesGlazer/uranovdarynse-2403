package org.example.metric;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

public class CSVWriter implements AutoCloseable {

    private final PrintWriter out;
    private boolean headerWritten = false;

    public CSVWriter(String filePath) throws IOException {
        this.out = new PrintWriter(new FileWriter(filePath, true)); // append mode
    }

    public void write(Metrics metrics) {
        if (!headerWritten) {
            writeHeader(metrics);
            headerWritten = true;
        }
        writeRow(metrics);
    }

    private void writeHeader(Metrics metrics) {
        StringBuilder sb = new StringBuilder();
        sb.append("Algorithm;RunTime(ns);Counter;Allocations;MaxDepth");
        for (String key : metrics.getCustom().keySet()) {
            sb.append(";").append(key);
        }
        out.println(sb.toString());
    }

    private void writeRow(Metrics metrics) {
        StringBuilder sb = new StringBuilder();
        sb.append(metrics.getAlgorithmName()).append(";");
        sb.append(metrics.getRunTime()).append(";");
        sb.append(metrics.getCounter()).append(";");
        sb.append(metrics.getAllocations()).append(";");
        sb.append(metrics.getMaxDepth());

        for (Map.Entry<String, Long> entry : metrics.getCustom().entrySet()) {
            sb.append(";").append(entry.getValue());
        }
        out.println(sb.toString());
        out.flush(); // make sure it writes immediately
    }

    @Override
    public void close() {
        out.close();
    }
}