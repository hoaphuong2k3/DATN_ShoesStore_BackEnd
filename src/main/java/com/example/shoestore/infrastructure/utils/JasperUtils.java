package com.example.shoestore.infrastructure.utils;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import org.springframework.core.io.ByteArrayResource;

import java.io.ByteArrayOutputStream;
import java.util.Map;

public class JasperUtils {

    public static ByteArrayResource exportJasperReportToDoc(JasperReport jasperReport, Map<String, Object> parameters, ByteArrayOutputStream bos) {
        try {
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, new JREmptyDataSource());
            JRDocxExporter exporter = new JRDocxExporter();
            exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
            exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(bos));
            exporter.exportReport();
            return new ByteArrayResource(bos.toByteArray());
        } catch (Exception e) {
            return null;
        }
    }

    public static ByteArrayResource exportJasperReportToPdf(JasperReport jasperReport, Map<String, Object> parameters,ByteArrayOutputStream bos) {
        try {
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, new JREmptyDataSource());
            JasperExportManager.exportReportToPdfStream(jasperPrint, bos);
            return new ByteArrayResource(bos.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
