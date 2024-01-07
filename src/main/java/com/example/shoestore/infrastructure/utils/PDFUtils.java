package com.example.shoestore.infrastructure.utils;

import com.example.shoestore.core.common.dto.response.FileResponseDTO;
import com.example.shoestore.infrastructure.constants.HeaderConstants;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.io.ByteArrayResource;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Field;
import java.util.List;

public class PDFUtils {

    public static void setPDFResponseHeaders(HttpServletResponse response, String fileName) {
        response.setContentType(HeaderConstants.CONTENT_TYPE_OCTET_STREAM);
        String headerValue = HeaderConstants.CONTENT_DISPOSITION_ATTACHMENT + "; filename=" + fileName;
        response.setHeader(HeaderConstants.CONTENT_DISPOSITION, headerValue);
    }

    public static PdfPTable createPDFHeader(List<String> headers) {
        int numberOfColumns = headers.size();

        PdfPTable table = new PdfPTable(numberOfColumns);
        table.setWidthPercentage(100);

        for (String header : headers) {
            PdfPCell cell = createHeaderCell(header);
            table.addCell(cell);
        }

        return table;
    }

    public static void exportPDFData(Document document, List<?> data, int numberOfColumns) throws DocumentException {
        if (!data.isEmpty()) {
            PdfPTable table = new PdfPTable(numberOfColumns);
            table.setWidthPercentage(100);

            // Sử dụng Reflection để đọc và thêm dữ liệu từ danh sách
            for (Object item : data) {
                Class<?> itemClass = item.getClass();
                Field[] fields = itemClass.getDeclaredFields();

                for (Field field : fields) {
                    field.setAccessible(true);
                    try {
                        Object value = field.get(item);
                        PdfPCell cell = createCenteredCell(value != null ? value.toString() : "");
                        table.addCell(cell);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }

            document.add(table);
        }
    }

    public static PdfPCell createHeaderCell(String text) {
        PdfPCell cell = new PdfPCell();
        cell.setPhrase(new Phrase(text, new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD))); // Đặt font chữ in đậm cho tiêu đề
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setNoWrap(false);
        return cell;
    }

    public static PdfPCell createCenteredCell(String text) {
        PdfPCell cell = new PdfPCell();
        cell.setPhrase(new Phrase(text, new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL)));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setNoWrap(false);
        return cell;
    }

    public static FileResponseDTO exportToPDF(List<String> headers, List<?> data, String fileName) throws  DocumentException {
        Document document = new Document();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter pdfWriter = PdfWriter.getInstance(document, baos);

        document.open();
        document.setPageSize(PageSize.A4);

        PdfPTable table = createPDFHeader(headers);
        document.add(table);
        exportPDFData(document, data, headers.size());
        document.close();
        pdfWriter.close();

        byte[] byteArray = baos.toByteArray();
        ByteArrayResource byteArrayResource = new ByteArrayResource(byteArray);

        return new FileResponseDTO(byteArrayResource,fileName);
    }

}
