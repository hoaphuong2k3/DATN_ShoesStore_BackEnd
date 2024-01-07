package com.example.shoestore.core.statistics.service.ipml;

import com.example.shoestore.core.common.dto.response.ResponseDTO;
import com.example.shoestore.core.statistics.dto.Seo;
import com.example.shoestore.core.statistics.dto.TopProduct;
import com.example.shoestore.core.statistics.repository.AdminClientRepository;
import com.example.shoestore.core.statistics.repository.AdminOrdersRepository;
import com.example.shoestore.core.statistics.service.StatisticsService;
import com.example.shoestore.infrastructure.constants.MessageCode;
import com.example.shoestore.infrastructure.exception.ValidateException;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.*;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

@Service
public class StatisticsIpml implements StatisticsService {

    @Autowired
    AdminOrdersRepository adminOrdersRepository;

    @Autowired
    AdminClientRepository adminClientRepository;

    private LocalDate today = LocalDate.now();

    @Override
    public ResponseDTO getAllQuantity(LocalDate fromDate,
                                      LocalDate toDate) {
        this.validateSearch(fromDate, toDate);
        LocalDateTime toDateFrom;
        if (toDate != null) {
            toDateFrom = LocalDateTime.of(toDate.getYear(), toDate.getMonth(), toDate.getDayOfMonth(), 23, 59, 59);
        } else {
            toDateFrom = null;
        }
        return ResponseDTO.success(adminOrdersRepository.getQuantityAndMoney(fromDate.atStartOfDay(), toDateFrom));
    }

    @Override
    public ResponseDTO getAllPerWeek() {
        LocalDate firstDayOfWeek = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDateTime startOfWeek = LocalDateTime.of(firstDayOfWeek, LocalTime.of(0, 0, 1));
        LocalDate lastDayOfWeek = today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));
        LocalDateTime endOfWeek = LocalDateTime.of(lastDayOfWeek, LocalTime.of(23,59,59));
        return ResponseDTO.success(adminOrdersRepository.getQuantityAndMoney(startOfWeek, endOfWeek));
    }

    @Override
    public ResponseDTO getAllPerWeekBefore() {
        LocalDate previousWeek = today.minusWeeks(1);

        LocalDate firstDayOfWeek = previousWeek.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDateTime startOfWeek = LocalDateTime.of(firstDayOfWeek, LocalTime.of(0, 0, 1)); // Thời gian bắt đầu của tuần trước

        LocalDate lastDayOfWeek = previousWeek.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));
        LocalDateTime endOfWeek = LocalDateTime.of(lastDayOfWeek, LocalTime.of(23, 59, 59)); // Thời gian kết thúc của tuần trước
        return ResponseDTO.success(adminOrdersRepository.getQuantityAndMoney(startOfWeek, endOfWeek));
    }

    @SneakyThrows
    @Override
    public ResponseDTO getAllPerMonth() {
//        while ((month  != null && year == null) || (month  == null && year != null)) {
//            if (year != null && month == null) {
//                throw new ValidateException("Vui lòng chọn tháng");
//            } else if (year > today.getYear()) {
//                throw new ValidateException("Vui lòng chọn năm nhỏ hơn hoặc bằng năm hiện tại");
//            } else if (year == null && month != null){
//                throw new ValidateException("Vui lòng chọn năm");
//            }
//            break;
//        }
//
//        while (month  == null && year != null) {
//            if (year > today.getYear()) {
//                throw new ValidateException("Vui lòng chọn năm nhỏ hơn hoặc bằng năm hiện tại");
//            }
//            if (year < 1970) {
//                throw new ValidateException("Vui lòng chọn năm lớn hơn năm 1970");
//            }
//            break;
//        }
//
//        if (month == null && year == null) {
//            month = today.getMonthValue();
//            year = today.getYear();
//        }
//
//        LocalDate startDayOfMonth = LocalDate.of(year, month, 1);
//        LocalDateTime startOfMonth = startDayOfMonth.atTime(0, 0, 0);
//
//        LocalDate lastDayOfMonth = startDayOfMonth.with(TemporalAdjusters.lastDayOfMonth());
//        LocalDateTime endOfMonth = lastDayOfMonth.atTime(23, 59, 59);
        LocalDate startDayOfMonth = today.with(TemporalAdjusters.firstDayOfMonth());
        LocalDateTime startOfMonth = startDayOfMonth.atTime(00,00,00);
        LocalDate lastDayOfMonth = today.with(TemporalAdjusters.lastDayOfMonth());
        LocalDateTime endOfMonth = lastDayOfMonth.atTime(23,59,59);
        return ResponseDTO.success(adminOrdersRepository.getQuantityAndMoney(startOfMonth, endOfMonth));
    }

    @Override
    public ResponseDTO getAllPerMonthBefore() {
        LocalDate startOfMonth = today.with(TemporalAdjusters.firstDayOfMonth());
        LocalDate startOfPreviousMonth = startOfMonth.minusMonths(1); // Lấy tháng trước đó
        LocalDate endOfPreviousMonth = startOfMonth.minusDays(1); // Lấy ngày cuối cùng của tháng trước

        LocalDateTime startTimeOfPreviousMonth = startOfPreviousMonth.atTime(0, 0, 1); // Thời gian bắt đầu của tháng trước
        LocalDateTime endTimeOfPreviousMonth = endOfPreviousMonth.atTime(23, 59, 59); // Thời gian kết thúc của tháng trước
        return ResponseDTO.success(adminOrdersRepository.getQuantityAndMoney(startTimeOfPreviousMonth, endTimeOfPreviousMonth));
    }

    @SneakyThrows
    @Override
    public ResponseDTO getAllPerYear() {
//        while (year != null) {
//            if (year > today.getYear()) {
//                throw new ValidateException("Vui lòng chọn năm nhỏ hơn hoặc bằng năm hiện tại");
//            }
//            if (year < 1970) {
//                throw new ValidateException("Vui lòng chọn năm lớn hơn năm 1970");
//            }
//            break;
//        }
//
//        if (year == null) {
//            year = today.getYear();
//        }
//
//        LocalDate startDayOfYear = LocalDate.of(year, 1, 1);
//        LocalDateTime startOfYear = startDayOfYear.atTime(0, 0, 0);
//
//        LocalDate lastDayOfYear = startDayOfYear.with(TemporalAdjusters.lastDayOfYear());
//        LocalDateTime endOfYear = lastDayOfYear.atTime(23, 59, 59);
        LocalDate startDayOfYear = today.with(TemporalAdjusters.firstDayOfYear());
        LocalDateTime startOfYear = startDayOfYear.atTime(00,00,010);
        LocalDate lastDayOfYear = today.with(TemporalAdjusters.lastDayOfYear());
        LocalDateTime endOfYear = lastDayOfYear.atTime(23,59,59);
        return ResponseDTO.success(adminOrdersRepository.getQuantityAndMoney(startOfYear, endOfYear));
    }

    @Override
    public ResponseDTO getAllPerYearBefore() {
        LocalDate previousYear = today.minusYears(1);

        LocalDate firstDayOfYear = previousYear.with(TemporalAdjusters.firstDayOfYear());
        LocalDateTime startOfYear = LocalDateTime.of(firstDayOfYear, LocalTime.of(0, 0, 1)); // Thời gian bắt đầu của năm trước

        LocalDate lastDayOfYear = previousYear.with(TemporalAdjusters.lastDayOfYear());
        LocalDateTime endOfYear = LocalDateTime.of(lastDayOfYear, LocalTime.of(23, 59, 59)); // Thời gian kết thúc của năm trước
        return ResponseDTO.success(adminOrdersRepository.getQuantityAndMoney(startOfYear, endOfYear));
    }

    @Override
    public ResponseDTO getAllClientSignUp() {
        LocalDateTime startDate = today.atTime(00,00,00);
        LocalDateTime endDate = today.atTime(23, 59, 59);
        return ResponseDTO.success(adminClientRepository.getALLClientSighUpCount(startDate,endDate));
    }

    @Override
    public ResponseDTO getAllTopProduct(LocalDate fromDate, LocalDate toDate) {
        LocalDate startDayOfYear = today.with(TemporalAdjusters.firstDayOfYear());
        LocalDateTime startOfYear = startDayOfYear.atTime(00,00,010);
        LocalDate lastDayOfYear = today.with(TemporalAdjusters.lastDayOfYear());
        LocalDateTime endOfYear = lastDayOfYear.atTime(23,59,59);
        if (fromDate == null && toDate == null){
            return ResponseDTO.success(adminOrdersRepository.getTop5Product(startOfYear,endOfYear));
        } else {
            validateDate(fromDate, toDate);
            LocalDateTime startOfDay = fromDate.atStartOfDay();
            LocalDateTime endOfDay = toDate.atTime(LocalTime.MAX);
            return ResponseDTO.success(adminOrdersRepository.getTop5Product(startOfDay,endOfDay));
        }
    }

    @Override
    public ResponseDTO getAllBottomProduct(LocalDate fromDate, LocalDate toDate) {
        LocalDate startDayOfYear = today.with(TemporalAdjusters.firstDayOfYear());
        LocalDateTime startOfYear = startDayOfYear.atTime(00,00,010);
        LocalDate lastDayOfYear = today.with(TemporalAdjusters.lastDayOfYear());
        LocalDateTime endOfYear = lastDayOfYear.atTime(23,59,59);
        if (fromDate == null && toDate == null){
            return ResponseDTO.success(adminOrdersRepository.getBottom5Product(startOfYear,endOfYear));
        } else {
            validateDate(fromDate, toDate);
            LocalDateTime startOfDay = fromDate.atStartOfDay();
            LocalDateTime endOfDay = toDate.atTime(LocalTime.MAX);
            return ResponseDTO.success(adminOrdersRepository.getBottom5Product(startOfDay,endOfDay));
        }
    }

    @SneakyThrows
    @Override
    public ResponseDTO getDataPerMonth(Integer month, Integer year) {
        while ((month  != null && year == null) ) {
            if (month < 1 || month > 12) {
                throw new ValidateException("Tháng không hợp lệ");
            }
            if (year == null && month != null){
                throw new ValidateException("Vui lòng chọn năm");
            }
            break;
        }

        while (month  == null && year != null) {
            if (year > today.getYear()) {
                throw new ValidateException("Vui lòng chọn năm nhỏ hơn hoặc bằng năm hiện tại");
            }
            if (year != null && month == null) {
                throw new ValidateException("Vui lòng chọn tháng");
            }
            if (year > today.getYear()) {
                throw new ValidateException("Vui lòng chọn năm nhỏ hơn hoặc bằng năm hiện tại");
            }
            if (year < 1970) {
                throw new ValidateException("Vui lòng chọn năm lớn hơn năm 1970");
            }
            break;
        }

        if (month == null || year == null) {
            month = today.getMonthValue();
            year = today.getYear();
        }


        LocalDate fromDate = LocalDate.of(year, month, 1);
        LocalDate toDate = fromDate.withDayOfMonth(fromDate.lengthOfMonth());


        return ResponseDTO.success(adminOrdersRepository.getQuantityAndMoneyByDateRangeMonth(fromDate.atStartOfDay(), toDate.atStartOfDay()));

//        LocalDate fromDate = LocalDate.of(today.getYear(), today.getMonth(), 1);
//        LocalDate toDate = fromDate.withDayOfMonth(fromDate.lengthOfMonth());
//        return ResponseDTO.success(adminOrdersRepository.getQuantityAndMoneyByDateRangeMonth(fromDate.atStartOfDay(), toDate.atStartOfDay()));
    }

    @SneakyThrows
    @Override
    public ResponseDTO getDataPerYear(Integer year) {
        while (year != null) {
            if (year > today.getYear()) {
                throw new ValidateException("Vui lòng chọn năm nhỏ hơn hoặc bằng năm hiện tại");
            }

            if (year < 1970) {
                throw new ValidateException("Vui lòng chọn năm lớn hơn năm 1970");
            }

            break;
        }
        // Nếu year không được chọn, sử dụng năm hiện tại
        if (year == null) {
            year = LocalDate.now().getYear();
        }

        // Kiểm tra xác thực năm

        List<ResponseDTO> result = new ArrayList<>();

        for (Month month : Month.values()) {
            LocalDate startDayOfMonth = LocalDate.of(year, month, 1);
            LocalDate lastDayOfMonth = startDayOfMonth.withDayOfMonth(startDayOfMonth.lengthOfMonth());

            ZonedDateTime startOfMonth = startDayOfMonth.atStartOfDay(ZoneId.systemDefault());
            ZonedDateTime endOfMonth = lastDayOfMonth.atTime(23, 59, 59).atZone(ZoneId.systemDefault());

            List<Seo> dataForMonth = adminOrdersRepository.getQuantityAndMoneyByDateRange(startOfMonth.toLocalDateTime(), endOfMonth.toLocalDateTime());

            // Check if there is data for the month
            if (dataForMonth != null && !dataForMonth.isEmpty()) {
                ResponseDTO responseDTO = ResponseDTO.success(dataForMonth);
                result.add(responseDTO);
            }
        }
        return ResponseDTO.success(result);
    }

    @Override
    public ResponseDTO getDetailShoe(Long shoeId) {
        return ResponseDTO.success(adminOrdersRepository.getDetailShoe(shoeId));
    }

    @Override
    public ResponseDTO getTotalPayment(Integer saleStatus, Integer status) {
        return ResponseDTO.success(adminOrdersRepository.getTotalPayment(saleStatus, status, LocalDate.now()));
    }

    @Override
    public Page<?> searchInvoice(String code, String name, Integer minPrice, Integer maxPrice, LocalDate fromDate, LocalDate toDate, Integer status, Integer type, Integer is_delete, int page, int size, String sortField, String sortOrder) {
        return null;
    }

    @Override
    public Page<?> searchSale(String code, String name, Integer minPrice, Integer maxPrice, LocalDate fromDate, LocalDate toDate, Integer status, Integer type, Integer is_delete, int page, int size, String sortField, String sortOrder) {
        return null;
    }

    @SneakyThrows
    private void validateDate(LocalDate fromDate, LocalDate toDate){
        if (fromDate == null || toDate == null){
            throw new ValidateException(MessageCode.Discount.DATE_NOT_NULL, MessageCode.Discount.ENTITY);
        }
        if (fromDate.isAfter(toDate)) {
            throw new ValidateException(MessageCode.Discount.START_DATE_ISAFTER_END_DATE, MessageCode.Discount.ENTITY);
        }
    }

    @SneakyThrows
    void validateSearch(LocalDate fromDate, LocalDate toDate) {
        while (fromDate != null && toDate != null) {
            if (toDate.isBefore(fromDate)) {
                throw new ValidateException(MessageCode.Discount.START_DATE_ISAFTER_END_DATE, MessageCode.Discount.ENTITY);
            }
            break;
        }
    }
}
