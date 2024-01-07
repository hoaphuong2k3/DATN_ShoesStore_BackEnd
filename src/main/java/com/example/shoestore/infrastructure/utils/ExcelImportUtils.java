package com.example.shoestore.infrastructure.utils;

import com.example.shoestore.infrastructure.constants.MessageCode;
import com.example.shoestore.infrastructure.exception.ValidateException;
import com.example.shoestore.infrastructure.exception.ValidationError;
import lombok.SneakyThrows;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.*;

@Service
public class ExcelImportUtils<T> {

    public static <T> Pair<List<T>, List<ValidationError<T>>> importFromExcel(MultipartFile multipartFile, Class<T> objectType) throws IOException {
        List<T> dataList = new ArrayList<>();
        List<ValidationError<T>> errorList = new ArrayList<>();
        Map<Integer, ValidationError<T>> sttErrorMap = new HashMap<>();

        try (InputStream inputStream = multipartFile.getInputStream();
             Workbook workbook = new XSSFWorkbook(inputStream)) {
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();

            // Bỏ qua hàng đầu tiên vì nó chứa tiêu đề cột
            if (rowIterator.hasNext()) {
                rowIterator.next();
            }

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();

                // Nếu cột STT trống thì ngừng duyệt
                Cell sequenceCell = row.getCell(0);
                if (sequenceCell == null || sequenceCell.getCellType() == CellType.BLANK) {
                    break;
                }

                T myObject = getObjectInstance(objectType);

                int columnIndex = 0;
                Iterator<Cell> cellIterator = row.iterator();

                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();
                    try {
                        validateExcelData(myObject, cell, columnIndex, objectType);
                        setPropertyFromCell(myObject, cell, columnIndex, objectType);
                    } catch (ValidateException e) {
                        int stt = getStt(myObject); // Sửa hàm này để trả về giá trị của STT

                        ValidationError<T> error;
                        if (sttErrorMap.containsKey(stt)) {
                            // Nếu stt đã tồn tại trong Map, lấy ValidationError tương ứng
                            error = sttErrorMap.get(stt);
                        } else {
                            // Nếu stt chưa tồn tại trong Map, tạo mới ValidationError và đặt vào Map
                            error = new ValidationError<>(myObject, new ArrayList<>());
                            sttErrorMap.put(stt, error);
                            errorList.add(error);
                        }

                        // Cộng thêm vào danh sách lỗi của ValidationError
                        error.getErrors().add(e.getMessage());
                    }
                    columnIndex++;
                }

                dataList.add(myObject);
            }
        }

        return Pair.of(dataList, errorList);
    }

    // Hàm này trả về giá trị của trường STT từ đối tượng T
    private static <T> int getStt(T myObject) {
        try {
            Method method = myObject.getClass().getMethod("getStt");
            return (int) method.invoke(myObject);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace(); // Xử lý lỗi nếu cần
            return 0; // Giá trị mặc định hoặc xử lý lỗi khác
        }
    }


    private static <T> T getObjectInstance(Class<T> objectType) {
        try {
            return objectType.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Failed to create object instance.", e);
        }
    }

    // Validate null, max length, min, max, dataType
    public static <T> void validateExcelData(T myObject, Cell cell, int columnIndex, Class<T> objectType) throws ValidateException {
        Field[] fields = objectType.getDeclaredFields();
        for (Field field : fields) {
            ExcelColumn excelColumn = field.getAnnotation(ExcelColumn.class);
            if (excelColumn != null && excelColumn.value() == columnIndex) {
                field.setAccessible(true);
                validateCellType(cell, field.getType(), excelColumn, excelColumn.nullable());
                validatePropertyFromCell(excelColumn, field, cell);
            }
        }
    }

    private static void validateCellType(Cell cell, Class<?> targetType, ExcelColumn excelColumn, boolean nullable) throws ValidateException {
        // Kiểm tra null
        if (!nullable && (cell == null || cell.getCellType() == CellType.BLANK)) {
            String mess = MessageUtils.getMessage(MessageCode.Excel.FIELD_NOT_BLANK, excelColumn.header());
            throw new ValidateException(mess);
        }

        // Kiểm tra kiểu dữ liệu
        switch (cell.getCellType()) {
            case STRING:
                if (!String.class.isAssignableFrom(targetType)) {
                    String errorMessage = MessageUtils.getMessage(MessageCode.Commom.NOT_INVALID_TYPE_STRING, excelColumn.header(), targetType.getSimpleName());
                    throw new ValidateException(errorMessage);
                }
                break;
            case NUMERIC:
                if (!Number.class.isAssignableFrom(targetType)) {
                    String errorMessage = MessageUtils.getMessage(MessageCode.Commom.NOT_INVALID_TYPE_NUMBER, excelColumn.header(), targetType.getSimpleName());
                    throw new ValidateException(errorMessage);
                }
                break;
            default:
                throw new ValidateException("Loại ô không được hỗ trợ");
        }
    }


    private static void validatePropertyFromCell(ExcelColumn excelColumn, Field field, Cell cell) throws ValidateException {
        if (field.getType() == String.class) {
            validateNotNullNotBlank(excelColumn, field, cell);
            validateStringLength(excelColumn, field, cell);
        } else if (field.getType() == Long.class || field.getType() == Integer.class||field.getType()== BigDecimal.class) {
            validateNotNull(excelColumn, field, cell);
            validateNumericRange(excelColumn, field, cell);
        }
    }

    private static void validateNumericRange(ExcelColumn excelColumn, Field field, Cell cell) throws ValidateException {
        if (excelColumn.min() > Long.MIN_VALUE || excelColumn.max() < Long.MAX_VALUE) {
            long numericValue = 0;
            if (cell != null && cell.getCellType() != CellType.BLANK) {
                numericValue = (long) cell.getNumericCellValue();
            }
            if (numericValue < excelColumn.min() || numericValue > excelColumn.max()) {
                String mess = MessageUtils.getMessage(MessageCode.Excel.FIELD_MIN_TO_MAX, excelColumn.header(), excelColumn.min(), excelColumn.max());
                throw new ValidateException(mess);
            }
        }
    }

    private static void validateNotNullNotBlank(ExcelColumn excelColumn, Field field, Cell cell) throws ValidateException {
        if (!excelColumn.nullable()) {
            if (cell == null || cell.getCellType() == CellType.BLANK) {
                String mess = MessageUtils.getMessage(MessageCode.Excel.FIELD_NOT_BLANK, excelColumn.header());
                throw new ValidateException(mess);
            }

            if (cell.getCellType() == CellType.STRING) {
                // Kiểm tra trường rỗng sau khi trim để đảm bảo rằng nó không chỉ chứa khoảng trắng
                if (cell.getStringCellValue().trim().isEmpty()) {
                    String mess = MessageUtils.getMessage(MessageCode.Excel.FIELD_NOT_BLANK, excelColumn.header());
                    throw new ValidateException(mess);
                }
            }
        }
    }

    private static void validateNotNull(ExcelColumn excelColumn, Field field, Cell cell) throws ValidateException {
        if (!excelColumn.nullable() && (cell == null || cell.getCellType() == CellType.BLANK)) {
            String mess = MessageUtils.getMessage(MessageCode.Excel.FIELD_NOT_BLANK, excelColumn.header());
            throw new ValidateException(mess);
        }
    }

    private static void validateStringLength(ExcelColumn excelColumn, Field field, Cell cell) throws ValidateException {
        if (excelColumn.maxLength() > 0) {
            String cellValue = getCellValueAsString(cell);
            if (cellValue.length() > excelColumn.maxLength()) {
                String mess = MessageUtils.getMessage(MessageCode.Excel.FIELD_MAX_LENGTH, excelColumn.header(), excelColumn.maxLength());
                throw new ValidateException(mess);
            }
        }
    }

    private static <T> String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return "";
        }

        if (cell.getCellType() == CellType.STRING) {
            return cell.getStringCellValue();
        } else if (cell.getCellType() == CellType.NUMERIC) {
            return String.valueOf((long) cell.getNumericCellValue());
        } else {
            return "";
        }
    }

    //  chuyển đổi giá trị của ô tương ứng với kiểu dữ liệu của trường trong đối tượng
    @SneakyThrows
    private static <T> void setPropertyFromCell(T myObject, Cell cell, int columnIndex, Class<T> objectType) {
        Field[] fields = objectType.getDeclaredFields();
        for (Field field : fields) {
            ExcelColumn excelColumn = field.getAnnotation(ExcelColumn.class);
            if (excelColumn != null && excelColumn.value() == columnIndex) {
                field.setAccessible(true);

                Class<?> fieldType = field.getType();

                if (fieldType == String.class) {
                    setStringProperty(myObject, field, cell);
                } else if (fieldType == Long.class) {
                    setLongProperty(myObject, field, cell);
                } else if (fieldType == BigDecimal.class) {
                    setBigDecimalProperty(myObject, field, cell);
                } else if (fieldType == Boolean.class) {
                    setBooleanProperty(myObject, field, cell);
                } else if (fieldType == Integer.class) {
                    setIntProperty(myObject, field, cell);
                }

            }
        }
    }

    private static <T> void setStringProperty(T myObject, Field field, Cell cell) {
        try {
            field.set(myObject, getStringValue(cell));
        } catch (IllegalAccessException e) {
            // Xử lý lỗi nếu cần
        }
    }

    private static <T> void setBooleanProperty(T myObject, Field field, Cell cell) {
        try {
            String cellValue = getStringValue(cell).toLowerCase();
            if ("true".equals(cellValue) || "1".equals(cellValue)) {
                field.set(myObject, true);
            } else if ("false".equals(cellValue) || "0".equals(cellValue)) {
                field.set(myObject, false);
            }
        } catch (IllegalAccessException e) {
            // Xử lý lỗi nếu cần
        }
    }

    private static <T> void setIntProperty(T myObject, Field field, Cell cell) {
        try {
            field.set(myObject, getIntValue(cell));
        } catch (IllegalAccessException e) {
            // Xử lý lỗi nếu cần
        }
    }

    private static <T> void setLongProperty(T myObject, Field field, Cell cell) {
        try {
            field.set(myObject, getLongValue(cell));
        } catch (IllegalAccessException e) {
            // Xử lý lỗi nếu cần
        }
    }

    private static <T> void setBigDecimalProperty(T myObject, Field field, Cell cell) {
        try {
            field.set(myObject, getBigDecimalValue(cell));
        } catch (IllegalAccessException e) {
            // Xử lý lỗi nếu cần
        }
    }

    private static String getStringValue(Cell cell) {
        if (cell.getCellType() == CellType.STRING) {
            return cell.getStringCellValue();
        } else if (cell.getCellType() == CellType.NUMERIC) {
            return String.valueOf(cell.getNumericCellValue());
        }
        return "";
    }

    private static int getIntValue(Cell cell) {
        if (cell.getCellType() == CellType.NUMERIC) {
            return (int) cell.getNumericCellValue();
        } else if (cell.getCellType() == CellType.STRING) {
            try {
                return Integer.parseInt(cell.getStringCellValue());
            } catch (NumberFormatException e) {
                // Xử lý lỗi nếu cần
            }
        }
        return 0; // Giá trị mặc định hoặc xử lý lỗi khác
    }

    private static long getLongValue(Cell cell) {
        if (cell.getCellType() == CellType.NUMERIC) {
            return (long) cell.getNumericCellValue();
        } else if (cell.getCellType() == CellType.STRING) {
            try {
                String stringValue = cell.getStringCellValue();
                if (stringValue != null && !stringValue.isEmpty()) {
                    return Long.parseLong(stringValue);
                }
            } catch (NumberFormatException e) {
                // Xử lý lỗi nếu cần
            }
        }
        return 0L; // Giá trị mặc định hoặc xử lý lỗi khác
    }

    private static BigDecimal getBigDecimalValue(Cell cell) {
        if (cell.getCellType() == CellType.NUMERIC) {
            return BigDecimal.valueOf(cell.getNumericCellValue());
        } else if (cell.getCellType() == CellType.STRING) {
            try {
                return new BigDecimal(cell.getStringCellValue());
            } catch (NumberFormatException e) {
                // Xử lý lỗi nếu cần
            }
        }
        return BigDecimal.ZERO; // Giá trị mặc định hoặc xử lý lỗi khác
    }
}
