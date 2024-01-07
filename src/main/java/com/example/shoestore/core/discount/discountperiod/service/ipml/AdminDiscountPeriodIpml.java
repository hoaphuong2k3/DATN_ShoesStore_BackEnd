//package com.example.shoestore.core.discount.discountperiod.service.ipml;
//
//import com.example.shoestore.core.common.dto.response.ResponseDTO;
//import com.example.shoestore.core.common.mapper.DiscountPeriodMapper;
//import com.example.shoestore.core.discount.discountperiod.dto.request.DiscountPeriodRequest;
//import com.example.shoestore.core.discount.discountperiod.repository.AdminDiscountPeriodRepository;
//import com.example.shoestore.core.discount.discountperiod.service.AdminDiscoutPeriodService;
//import com.example.shoestore.entity.DiscountPeriod;
//import com.example.shoestore.infrastructure.constants.*;
//import com.example.shoestore.infrastructure.exception.ValidateException;
//import com.example.shoestore.infrastructure.utils.DataBaseUtils;
//import com.example.shoestore.infrastructure.utils.DataFormatUtils;
//import com.example.shoestore.infrastructure.utils.DateUtils;
//import com.example.shoestore.infrastructure.utils.MessageUtils;
//import lombok.SneakyThrows;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.scheduling.annotation.EnableScheduling;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Service;
//
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//import java.time.temporal.ChronoUnit;
//import java.util.List;
//import java.util.Map;
//import java.util.TreeMap;
//import java.util.concurrent.Executors;
//import java.util.concurrent.ScheduledExecutorService;
//import java.util.concurrent.TimeUnit;
//
//@EnableScheduling
//@Service
//public class AdminDiscountPeriodIpml implements AdminDiscoutPeriodService {
//
//    @Autowired
//    AdminDiscountPeriodRepository adminDiscountPeriodRepository;
//
//    @Autowired
//    DiscountPeriodMapper discountPeriodMapper;
//
//    private DiscountPeriod discountPeriod;
//
//    @Scheduled(cron = "0 0 0 * * MON-SUN")
//    @Override
//    public ResponseDTO getAllIsActive() {
//        System.out.println("Đang chạy");
//        List<DiscountPeriod> list = adminDiscountPeriodRepository.getAll();
//        timerTaskEnd(list);
//        return ResponseDTO.success(getAll());
//    }
//
//    private ResponseDTO getAll() {
//        return ResponseDTO.success(discountPeriodMapper.entityListToDTOList(adminDiscountPeriodRepository.getAll()));
//    }
//
//    @Override
//    public Page<DiscountPeriod> search(String code, String name, Integer minPercent, Integer maxPercent, LocalDate fromDate,
//                                       LocalDate toDate, Integer status, Integer is_delete, int page, int size) throws ValidateException {
//        this.validateSearch(fromDate, toDate);
//        this.validatePrice(minPercent, maxPercent);
//        LocalDateTime toDateFrom ;
//        if (toDate != null) {
//            toDateFrom = LocalDateTime.of(toDate.getYear(), toDate.getMonth(), toDate.getDayOfMonth(), 23, 59, 59);
//        } else {
//            toDateFrom = null;
//        }
//        return adminDiscountPeriodRepository.search(DataBaseUtils.getLikeCondition(code), DataBaseUtils.getLikeCondition(name),
//                minPercent, maxPercent, fromDate, toDateFrom, status, is_delete, PageRequest.of(page, size));
//    }
//
//    @SneakyThrows
//    @Override
//    public ResponseDTO createDiscountPeriod(DiscountPeriodRequest createDiscountPeriodDTO) {
//        this.validateCommon(createDiscountPeriodDTO);
//        this.validateSalePercent(createDiscountPeriodDTO);
//        discountPeriod = new DiscountPeriod();
//        discountPeriod = discountPeriodMapper.createDTOToEntity(createDiscountPeriodDTO);
//        this.validateExistedVoucherPeriodName(discountPeriod.getName());
//        discountPeriod.setStatus(Status.ACTIVATE.getValue());
//        discountPeriod.setCreatedTime(LocalDateTime.now());
//        discountPeriod.setIsDeleted(IsDeleted.UNDELETED.getValue());
//        DataFormatUtils.trimStringFields(discountPeriod);
//        DiscountPeriod saveDiscountPeriod = adminDiscountPeriodRepository.save(discountPeriod);
//        return ResponseDTO.success(discountPeriodMapper.entityToDTO(saveDiscountPeriod));
//    }
//
//    @SneakyThrows
//    @Override
//    public ResponseDTO updateDiscountPeriod(DiscountPeriodRequest updateDiscountPeriodDTO) {
//        this.validateCommon(updateDiscountPeriodDTO);
//        discountPeriod = new DiscountPeriod();
//        discountPeriod = discountPeriodMapper.updateDTOToEntity(updateDiscountPeriodDTO);
//        DataFormatUtils.trimStringFields(discountPeriod);
//        discountPeriod.setUpdatedTime(LocalDateTime.now());
//        discountPeriod.setIsDeleted(IsDeleted.UNDELETED.getValue());
//        DiscountPeriod updatedDiscountPeriod = adminDiscountPeriodRepository.save(discountPeriod);
//        getAllIsActive();
//        return ResponseDTO.success(discountPeriodMapper.entityToDTO(updatedDiscountPeriod));
//    }
//
//    @Override
//    public ResponseDTO stopDiscountPeriod(Long id) {
//        adminDiscountPeriodRepository.setDiscountPeriodById(Status.INACTIVATE.getValue(), id);
//        getAllIsActive();
//        return ResponseDTO.success("Stop success !");
//    }
//
//    @Override
//    public ResponseDTO restoreDiscountPeriod(DiscountPeriodRequest restoreDiscountPeriodDTO) {
//        discountPeriod = new DiscountPeriod();
//        discountPeriod = discountPeriodMapper.updateDTOToEntity(restoreDiscountPeriodDTO);
//        discountPeriod.setStatus(Status.ACTIVATE.getValue());
//        DiscountPeriod restoreDiscountPeriod = adminDiscountPeriodRepository.save(discountPeriod);
//        getAllIsActive();
//        return ResponseDTO.success(discountPeriodMapper.entityToDTO(restoreDiscountPeriod));
//    }
//
//    @Override
//    public ResponseDTO deleteDiscountPeriod(Long id) {
//        adminDiscountPeriodRepository.deleteDiscountPeriodById(id);
//        return ResponseDTO.success("Delete success !");
//    }
//
//    @SneakyThrows
//    private void validateExistedVoucherPeriodName(String voucherName) {
//        if (adminDiscountPeriodRepository.existsByName(voucherName) > 0) {
//            throw new ValidateException(MessageUtils.getMessage(MessageCode.Commom.EXISTED,
//                    MessageUtils.getMessage(MessageCode.Discount.ENTITY)));
//        }
//    }
//
//    @SneakyThrows
//    private void validateCommon(DiscountPeriodRequest createDiscountPeriodDTO) {
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DatePattern.DATE);
//        LocalDate startDate = LocalDate.parse(createDiscountPeriodDTO.getStartDate(), formatter);
//        LocalDate endDate = LocalDate.parse(createDiscountPeriodDTO.getEndDate(), formatter);
//        if (startDate.isAfter(endDate)) {
//            throw new ValidateException(MessageUtils.getMessage(MessageCode.Discount.START_DATE_ISAFTER_END_DATE, MessageUtils.getMessage(MessageCode.Discount.ENTITY)));
//        }
//        if (startDate.isBefore(LocalDate.now())) {
//            throw new ValidateException(MessageUtils.getMessage(MessageCode.Discount.START_DATE_ISEQUAL_OR_BIGGER, MessageUtils.getMessage(MessageCode.Discount.ENTITY)));
//        }
//    }
//
//    @SneakyThrows
//    private void validateSalePercent(DiscountPeriodRequest createPeriodRequestDTO) {
//        if (!DateUtils.isNotNumber(createPeriodRequestDTO.getSalePercent())) {
//            throw new ValidateException(MessageUtils.getMessage(MessageCode.Discount.IS_NUMBER, MessageUtils.getMessage(MessageCode.Discount.ENTITY)));
//        }
//        if (Integer.parseInt(createPeriodRequestDTO.getSalePercent()) <= 0) {
//            throw new ValidateException(MessageUtils.getMessage(MessageCode.Discount.GREATER_THAN_ZERO, MessageUtils.getMessage(MessageCode.Discount.ENTITY)));
//        }
//        if (Integer.parseInt(createPeriodRequestDTO.getSalePercent()) >= 50) {
//            throw new ValidateException(MessageUtils.getMessage(MessageCode.Discount.GREATER_THAN_FIFTYPERCENT, MessageUtils.getMessage(MessageCode.Discount.ENTITY)));
//        }
//    }
//
//    @SneakyThrows
//    private void validatePrice(Integer minPrice, Integer maxPrice) {
//        while (minPrice != null && maxPrice != null) {
//            if (minPrice > (maxPrice)) {
//                throw new ValidateException(MessageUtils.getMessage(MessageCode.DiscountPeriod.MINPRICE_IS_SMALLER_MAXPRICE, MessageUtils.getMessage(MessageCode.DiscountPeriod.ENTITY)));
//            }
//            break;
//        }
//    }
//
//    @SneakyThrows
//    void validateSearch(LocalDate fromDate, LocalDate toDate) {
//        while (fromDate != null && toDate != null) {
//            if (toDate.isBefore(fromDate)) {
//                throw new ValidateException(MessageUtils.getMessage(MessageCode.Discount.START_DATE_ISAFTER_END_DATE, MessageUtils.getMessage(MessageCode.Discount.ENTITY)));
//            }
//            break;
//        }
//    }
//
//    @SneakyThrows
//    private synchronized void timerTaskEnd(List<DiscountPeriod> list) {
//        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
//        if (list == null) {
//            return;
//        }
//        for (DiscountPeriod entry : list) {
//            System.out.println(entry.getEndDate());
//            long delay = LocalDate.now().until(entry.getEndDate(), ChronoUnit.SECONDS); // Khoảng thời gian giữa thời điểm hiện tại và thời điểm cần chạy tác vụ (tính bằng giây)
//            executor.schedule(() -> {
//                System.out.println("Đã cập nhật");
//                adminDiscountPeriodRepository.updateYears(entry.getId());
//                getAllIsActive();
//                list.remove(entry);
//            }, delay, TimeUnit.SECONDS);
//        }
//    }
//
//}
