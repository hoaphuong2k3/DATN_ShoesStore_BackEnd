package com.example.shoestore.infrastructure.utils;

import com.example.shoestore.core.common.dto.response.FileResponseDTO;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ByteArrayResource;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ExelExportUtils {

    public static FileResponseDTO exportToExcel(List<String> headers, List<?> data, String fileName, String sheetName) throws IOException {
        Workbook workbook = createExcelWorkbook(headers, data, sheetName);

        // Ghi Workbook vào một ByteArrayOutputStream
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            workbook.write(baos);
            byte[] byteArray = baos.toByteArray();
            ByteArrayResource byteArrayResource = new ByteArrayResource(byteArray);
            return new FileResponseDTO(byteArrayResource,fileName);
        }
    }

    // Tạo tệp Excel
    public static Workbook createExcelWorkbook(List<String> headers, List<?> data, String sheetName) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet(sheetName);

        createHeaderToExportExcel(sheet, headers);

        createDataToExportExcel(sheet, data);

        return workbook;
    }

    // Tạo header
    public static void createHeaderToExportExcel(Sheet sheet, List<String> headers) {
        CellStyle headerStyle = sheet.getWorkbook().createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.AQUA.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < headers.size(); i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers.get(i));
            cell.setCellStyle(headerStyle);
        }
    }

    // Tạo dữ liệu
    public static void createDataToExportExcel(Sheet sheet, List<?> data) {
        int rowIndex = 1;
        if (DataUtils.isNotEmpty(data)) {
            for (Object item : data) {
                Class<?> itemClass = item.getClass();
                Field[] fields = itemClass.getDeclaredFields();

                Row row = sheet.createRow(rowIndex++);
                int cellIndex = 0;

                for (Field field : fields) {
                    field.setAccessible(true);
                    try {
                        Object value = field.get(item);
                        Cell cell = row.createCell(cellIndex++);
                        setCellValue(cell, value);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    // Thiết lập giá trị cho một ô trong tệp Excel
    public static void setCellValue(Cell cell, Object cellValue) {
        if (cellValue == null) {
            cell.setCellValue("");
        } else if (cellValue instanceof String) {
            cell.setCellValue((String) cellValue);
        } else if (cellValue instanceof Integer) {
            cell.setCellValue((Integer) cellValue);
        } else if (cellValue instanceof Double) {
            cell.setCellValue((Double) cellValue);
        } else if (cellValue instanceof Float) {
            cell.setCellValue((Float) cellValue);
        } else if (cellValue instanceof Long) {
            cell.setCellValue((Long) cellValue);
        } else if (cellValue instanceof Boolean) {
            cell.setCellValue((Boolean) cellValue);
        } else if (cellValue instanceof BigDecimal) {
            cell.setCellValue(((BigDecimal) cellValue).doubleValue());
        } else if (cellValue instanceof java.util.Date) {
            cell.setCellValue((java.util.Date) cellValue);
        } else if (cellValue instanceof java.util.Calendar) {
            cell.setCellValue((java.util.Calendar) cellValue);
        } else if (cellValue instanceof LocalDateTime) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            cell.setCellValue(((LocalDateTime) cellValue).format(formatter));
        }
    }

}

