package com.example.shoestore.core.discount.discountperiodtype.service.ipml;

import com.example.shoestore.core.common.BaseService;
import com.example.shoestore.core.common.dto.response.ResponseDTO;
import com.example.shoestore.core.common.mapper.DiscountPeriodMapper;
import com.example.shoestore.core.discount.discountperiodtype.dto.request.DiscountPeriodRequest;
import com.example.shoestore.core.discount.discountperiodtype.dto.request.DiscountPeriodSearch;
import com.example.shoestore.core.discount.discountperiodtype.dto.response.DiscountPeriodResponsePrejection;
import com.example.shoestore.core.discount.discountperiodtype.repository.AdminDiscountPeriodRepository;
import com.example.shoestore.core.discount.discountperiodtype.service.AdminDiscoutPeriodService;
import com.example.shoestore.entity.Discount;
import com.example.shoestore.entity.DiscountPeriod;
import com.example.shoestore.infrastructure.constants.*;
import com.example.shoestore.infrastructure.exception.ValidateException;
import com.example.shoestore.infrastructure.utils.DataBaseUtils;
import com.example.shoestore.infrastructure.utils.DataFormatUtils;
import com.example.shoestore.infrastructure.utils.DateUtils;
import com.example.shoestore.infrastructure.utils.MessageUtils;
import lombok.SneakyThrows;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@EnableScheduling
@Service
public class AdminDiscountPeriodIpml implements AdminDiscoutPeriodService {

    @Autowired
    AdminDiscountPeriodRepository adminDiscountPeriodRepository;

    @Autowired
    DiscountPeriodMapper discountPeriodMapper;

    private DiscountPeriod discountPeriod;

    @Scheduled(cron = "1 0 0 * * MON-SUN")
    @Override
    public ResponseDTO getAllIsActive() {
        System.out.println("Đang chạy");
        return ResponseDTO.success(adminDiscountPeriodRepository.getAll());
    }

    @Scheduled(cron = "1 0 0 * * MON-SUN")
//    @Scheduled(fixedDelay = 10000)
    private void thread() {
        System.out.println("Đang chạy");
        timerTaskStart(adminDiscountPeriodRepository.getAllBe());
        timerTaskEnd(adminDiscountPeriodRepository.getAllBe());
    }


    @Override
    public Page<DiscountPeriodResponsePrejection> search(String code, String name, Integer minPercent, Integer maxPercent, LocalDate fromDate,
                                                         LocalDate toDate, Integer status, Integer isDelete, Integer typePeriod, int page, int size, String sortField, String sortOrder) throws ValidateException {
        System.out.println(minPercent);
        System.out.println(maxPercent);
        this.validateSearch(fromDate, toDate);
        this.validatePrice(minPercent, maxPercent);
        LocalDateTime toDateFrom;
        if (toDate != null) {
            toDateFrom = LocalDateTime.of(toDate.getYear(), toDate.getMonth(), toDate.getDayOfMonth(), 23, 59, 59);
        } else {
            toDateFrom = null;
        }
        PageRequest pageRequest = BaseService.createPageRequest(page, size, sortField, sortOrder);
        return adminDiscountPeriodRepository.search(DataBaseUtils.getLikeCondition(code), DataBaseUtils.getLikeCondition(name),
                minPercent, maxPercent, fromDate, toDateFrom, status, isDelete, typePeriod, pageRequest);
    }

    @SneakyThrows
    @Override
    public ResponseDTO createDiscountPeriod(DiscountPeriodRequest createDiscountPeriodDTO) {
        if (createDiscountPeriodDTO.getTypePeriod() == 0) {
            this.validateCommon(createDiscountPeriodDTO);
        } else {
            this.validateCommonFree(createDiscountPeriodDTO);
        }
        DiscountPeriod cdiscountPeriod = new DiscountPeriod();
        cdiscountPeriod = discountPeriodMapper.createDTOToEntity(createDiscountPeriodDTO);
        DataFormatUtils.trimStringFields(cdiscountPeriod);
        cdiscountPeriod.setCreatedTime(LocalDateTime.now());
        cdiscountPeriod.setIsDeleted(IsDeleted.UNDELETED.getValue());
        DiscountPeriod saveDiscountPeriod = adminDiscountPeriodRepository.save(cdiscountPeriod);
        return ResponseDTO.success(discountPeriodMapper.entityToDTO(saveDiscountPeriod));
    }

    @SneakyThrows
    @Override
    public ResponseDTO updateDiscountPeriod(DiscountPeriodRequest updateDiscountPeriodDTO) {
        if (updateDiscountPeriodDTO.getTypePeriod() == 0) {
            this.validateCommonUp(updateDiscountPeriodDTO);
        } else {
            this.validateCommonFreeUp(updateDiscountPeriodDTO);
        }
        discountPeriod = new DiscountPeriod();
        discountPeriod = discountPeriodMapper.updateDTOToEntity(updateDiscountPeriodDTO);
        System.out.println(discountPeriod);
        DataFormatUtils.trimStringFields(discountPeriod);
        discountPeriod.setUpdatedTime(LocalDateTime.now());
        discountPeriod.setIsDeleted(IsDeleted.UNDELETED.getValue());
        DiscountPeriod updatedDiscountPeriod = adminDiscountPeriodRepository.save(discountPeriod);
        return ResponseDTO.success(discountPeriodMapper.entityToDTO(updatedDiscountPeriod));
    }

    @Override
    public ResponseDTO stopDiscountPeriod(Long id) {
        adminDiscountPeriodRepository.setDiscountPeriodById(DiscountStatus.STOP.getValue(), id);
        getAllIsActive();
        return ResponseDTO.success("Stop success !");
    }

    @Override
    public ResponseDTO setDiscountPeriodRunNow(Long id) {
        Optional<DiscountPeriod> optionalDiscountPeriod = adminDiscountPeriodRepository.findById(id);
        validateRunNowPeriod(optionalDiscountPeriod);
        adminDiscountPeriodRepository.setDiscountPeriodRunNowById(LocalDate.now(), id);
        return ResponseDTO.success("Run Now success !");
    }

//    @Override
//    public ResponseDTO restoreDiscountPeriod(DiscountPeriodRequest restoreDiscountPeriodDTO) {
//        discountPeriod = new DiscountPeriod();
//        discountPeriod = discountPeriodMapper.updateDTOToEntity(restoreDiscountPeriodDTO);
//        discountPeriod.setStatus(Status.Ư.getValue());
//        DiscountPeriod restoreDiscountPeriod = adminDiscountPeriodRepository.save(discountPeriod);
//        getAllIsActive();
//        return ResponseDTO.success(discountPeriodMapper.entityToDTO(restoreDiscountPeriod));
//    }

    @Override
    public ResponseDTO deleteDiscountPeriod(Long id) {
        adminDiscountPeriodRepository.deleteDiscountPeriodById(id);
        return ResponseDTO.success("Delete success !");
    }

    @Override
    public ResponseDTO deleteAll(List<Long> deleteAllDiscountPeriod) {
        deleteAllDiscountPeriod.stream().forEach(d ->
                adminDiscountPeriodRepository.deleteDiscountPeriodById(d));
        return ResponseDTO.success("Delete success !");
    }

    @SneakyThrows
    private void validateExistedVoucherPeriodName(String voucherName) {
        if (adminDiscountPeriodRepository.existsByName(voucherName) > 0) {
            throw new ValidateException(MessageCode.Commom.EXISTED,
                    MessageCode.DiscountPeriod.ENTITY);
        }
    }

    @SneakyThrows
    private void validateCommon(DiscountPeriodRequest createDiscountPeriodDTO) {
        List<DiscountPeriod> listRunning = adminDiscountPeriodRepository.getAllIsRunning();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DatePattern.DATE);
        LocalDate startDate = LocalDate.parse(createDiscountPeriodDTO.getStartDate(), formatter);
        LocalDate endDate = LocalDate.parse(createDiscountPeriodDTO.getEndDate(), formatter);
        Integer listWaiting = adminDiscountPeriodRepository.getAllIsWaiting(startDate, endDate);
        if (createDiscountPeriodDTO.getSalePercent().trim().isEmpty()) {
            throw new ValidateException(MessageCode.DiscountPeriod.SALE_PERCENT_NOT_NULL, MessageCode.DiscountPeriod.ENTITY);
        }
        if (createDiscountPeriodDTO.getMinPrice().trim().isEmpty()) {
            throw new ValidateException(MessageCode.DiscountPeriod.MINPRICE_NOT_NULL, MessageCode.DiscountPeriod.ENTITY);
        }

        if (Integer.parseInt(createDiscountPeriodDTO.getSalePercent()) > 100 || Integer.parseInt(createDiscountPeriodDTO.getSalePercent()) < 1) {
            throw new ValidateException(MessageCode.DiscountPeriod.SALE_MIN_MAX, MessageCode.DiscountPeriod.ENTITY);
        }

        if (startDate.isAfter(endDate)) {
            throw new ValidateException(MessageCode.DiscountPeriod.START_DATE_IS_AFTER_END_DATE, MessageCode.DiscountPeriod.ENTITY);
        }

        if (startDate.isBefore(LocalDate.now())) {
            throw new ValidateException(MessageCode.DiscountPeriod.START_DATE_IS_EQUAL_OR_BIGGER, MessageCode.DiscountPeriod.ENTITY);
        }
        while (listRunning.size() > 0) {
            listRunning.stream().forEach(dto -> {
                if (startDate.isBefore(dto.getEndDate()) || startDate.isEqual(dto.getEndDate())) {
                    try {
                        throw new ValidateException(MessageCode.DiscountPeriod.DISCOUNT_IS_RUNNING, MessageCode.DiscountPeriod.ENTITY);
                    } catch (ValidateException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
            break;
        }
//
        while (listWaiting != null) {
            if (listWaiting > 0) {
                try {
                    throw new ValidateException(MessageCode.DiscountPeriod.DISCOUNT_IS_WAITING, MessageCode.DiscountPeriod.ENTITY);
                } catch (ValidateException e) {
                    throw new RuntimeException(e);
                }
            }
            break;
        }
    }

    @SneakyThrows
    private void validateCommonUp(DiscountPeriodRequest createDiscountPeriodDTO) {
        List<DiscountPeriod> listRunning = adminDiscountPeriodRepository.getAllIsRunningUp(createDiscountPeriodDTO.getId());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DatePattern.DATE);
        LocalDate startDate = LocalDate.parse(createDiscountPeriodDTO.getStartDate(), formatter);
        LocalDate endDate = LocalDate.parse(createDiscountPeriodDTO.getEndDate(), formatter);
        Integer listWaiting = adminDiscountPeriodRepository.getAllIsWaitingUp(createDiscountPeriodDTO.getId(), startDate, endDate);
        System.out.println(listWaiting);

        if (createDiscountPeriodDTO.getSalePercent().trim().isEmpty()) {
            throw new ValidateException(MessageCode.DiscountPeriod.SALE_PERCENT_NOT_NULL, MessageCode.DiscountPeriod.ENTITY);
        }

        if (createDiscountPeriodDTO.getMinPrice().trim().isEmpty()) {
            throw new ValidateException(MessageCode.DiscountPeriod.MINPRICE_NOT_NULL, MessageCode.DiscountPeriod.ENTITY);
        }

        if (Integer.parseInt(createDiscountPeriodDTO.getSalePercent()) > 100 || Integer.parseInt(createDiscountPeriodDTO.getSalePercent()) < 1) {
            throw new ValidateException(MessageCode.DiscountPeriod.SALE_MIN_MAX, MessageCode.DiscountPeriod.ENTITY);
        }
        if (startDate.isAfter(endDate)) {
            throw new ValidateException(MessageCode.DiscountPeriod.START_DATE_IS_AFTER_END_DATE, MessageCode.DiscountPeriod.ENTITY);
        }
        if (startDate.isBefore(LocalDate.now())) {
            throw new ValidateException(MessageCode.DiscountPeriod.START_DATE_IS_EQUAL_OR_BIGGER, MessageCode.DiscountPeriod.ENTITY);
        }
        while (listRunning.size() > 0) {
            listRunning.stream().forEach(dto -> {
                if (startDate.isBefore(dto.getEndDate()) || startDate.isEqual(dto.getEndDate())) {
                    try {
                        throw new ValidateException(MessageCode.DiscountPeriod.DISCOUNT_IS_RUNNING, MessageCode.DiscountPeriod.ENTITY);
                    } catch (ValidateException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
            break;
        }

        while (listWaiting != null) {
            if (listWaiting > 0) {
                try {
                    throw new ValidateException(MessageCode.DiscountPeriod.DISCOUNT_IS_WAITING, MessageCode.DiscountPeriod.ENTITY);
                } catch (ValidateException e) {
                    throw new RuntimeException(e);
                }
            }
            break;
        }
    }

    @SneakyThrows
    private void validateCommonFreeUp(DiscountPeriodRequest createDiscountPeriodDTO) {
        System.out.println(createDiscountPeriodDTO);
        List<DiscountPeriod> listRunning = adminDiscountPeriodRepository.getAllIsRunningUp(createDiscountPeriodDTO.getId());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DatePattern.DATE);
        LocalDate startDate = LocalDate.parse(createDiscountPeriodDTO.getStartDate(), formatter);
        LocalDate endDate = LocalDate.parse(createDiscountPeriodDTO.getEndDate(), formatter);
        Integer listWaiting = adminDiscountPeriodRepository.getAllIsWaitingUp(createDiscountPeriodDTO.getId(), startDate, endDate);
        if (startDate.isAfter(endDate)) {
            throw new ValidateException(MessageCode.DiscountPeriod.START_DATE_IS_AFTER_END_DATE, MessageCode.DiscountPeriod.ENTITY);
        }
        if (startDate.isBefore(LocalDate.now())) {
            throw new ValidateException(MessageCode.DiscountPeriod.START_DATE_IS_EQUAL_OR_BIGGER, MessageCode.DiscountPeriod.ENTITY);
        }
        while (listRunning.size() > 0) {
            listRunning.stream().forEach(dto -> {
                if (startDate.isBefore(dto.getEndDate()) || startDate.isEqual(dto.getEndDate())) {
                    try {
                        throw new ValidateException(MessageCode.DiscountPeriod.DISCOUNT_IS_RUNNING, MessageCode.DiscountPeriod.ENTITY);
                    } catch (ValidateException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
            break;
        }

        while (listWaiting != null) {
            if (listWaiting > 0) {
                try {
                    throw new ValidateException(MessageCode.DiscountPeriod.DISCOUNT_IS_WAITING, MessageCode.DiscountPeriod.ENTITY);
                } catch (ValidateException e) {
                    throw new RuntimeException(e);
                }
            }
            break;
        }
    }

    @SneakyThrows
    private void validateCommonFree(DiscountPeriodRequest createDiscountPeriodDTO) {
        System.out.println(createDiscountPeriodDTO);
        List<DiscountPeriod> listRunning = adminDiscountPeriodRepository.getAllIsRunning();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DatePattern.DATE);
        LocalDate startDate = LocalDate.parse(createDiscountPeriodDTO.getStartDate(), formatter);
        LocalDate endDate = LocalDate.parse(createDiscountPeriodDTO.getEndDate(), formatter);
        Integer listWaiting = adminDiscountPeriodRepository.getAllIsWaiting(startDate, endDate);
        if (startDate.isAfter(endDate)) {
            throw new ValidateException(MessageCode.DiscountPeriod.START_DATE_IS_AFTER_END_DATE, MessageCode.DiscountPeriod.ENTITY);
        }
        if (startDate.isBefore(LocalDate.now())) {
            throw new ValidateException(MessageCode.DiscountPeriod.START_DATE_IS_EQUAL_OR_BIGGER, MessageCode.DiscountPeriod.ENTITY);
        }
        while (listRunning.size() > 0) {
            listRunning.stream().forEach(dto -> {
                if (startDate.isBefore(dto.getEndDate()) || startDate.isEqual(dto.getEndDate())) {
                    try {
                        throw new ValidateException(MessageCode.DiscountPeriod.DISCOUNT_IS_RUNNING, MessageCode.DiscountPeriod.ENTITY);
                    } catch (ValidateException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
            break;
        }

        while (listWaiting != null) {
            if (listWaiting > 0) {
                try {
                    throw new ValidateException(MessageCode.DiscountPeriod.DISCOUNT_IS_WAITING, MessageCode.DiscountPeriod.ENTITY);
                } catch (ValidateException e) {
                    throw new RuntimeException(e);
                }
            }
            break;
        }
    }


    @SneakyThrows
    private void validatePrice(Integer minPrice, Integer maxPrice) {
        while (minPrice != null) {
            if (minPrice < 0) {
                throw new ValidateException(MessageCode.Discount.MINPRICE_IS_BIGGER_ZERO, MessageCode.Discount.ENTITY);
            }
            break;
        }
        while (minPrice != null && maxPrice != null) {
            if (minPrice > (maxPrice)) {
                throw new ValidateException(MessageCode.DiscountPeriod.MINPRICE_IS_SMALLER_MAXPRICE, MessageCode.DiscountPeriod.ENTITY);
            }
            break;
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

    @SneakyThrows
    private void validateRunNowPeriod(Optional<DiscountPeriod> optionalDiscount) {
        List<DiscountPeriod> list = adminDiscountPeriodRepository.getAllIsRunning();
        if (optionalDiscount.get().getEndDate().isBefore(LocalDate.now())) {
            throw new ValidateException(MessageCode.Discount.END_DATE_IS_EXPIRED, MessageCode.DiscountPeriod.ENTITY);
        }
        if (list.size() > 0) {
//            list.stream().forEach(dto -> {
//                if (optionalDiscount.get().getStartDate().isBefore(dto.getEndDate()) || optionalDiscount.get().getStartDate().isEqual(dto.getEndDate())) {
                    try {
                        throw new ValidateException(MessageCode.DiscountPeriod.DISCOUNT_IS_RUNNING, MessageCode.DiscountPeriod.ENTITY);
                    } catch (ValidateException e) {
                        throw new RuntimeException(e);
                    }

//                }
//            });
//            break;
        }
    }

    @SneakyThrows
    private synchronized void timerTaskStart(List<DiscountPeriod> list) {
        if (list.size() == 0) {
            return;
        }
        for (DiscountPeriod entry : list) {
            System.out.println("Đã cập nhật");
            adminDiscountPeriodRepository.setDiscountPeriodById(DiscountStatus.RUNNING.getValue(), entry.getId());
            getAllIsActive();
            list.remove(entry);
            break;
        }
    }

    @SneakyThrows
    private synchronized void timerTaskEnd(List<DiscountPeriod> list) {
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        if (list.size() == 0) {
            return;
        }
        for (DiscountPeriod entry : list) {
            System.out.println(entry.getEndDate());
            long delay = LocalDateTime.now().until(entry.getEndDate().plusDays(1).atTime(23, 59, 59), ChronoUnit.SECONDS); // Khoảng thời gian giữa thời điểm hiện tại và thời điểm cần chạy tác vụ (tính bằng giây)
            executor.schedule(() -> {
                System.out.println("Đã cập nhật");
                adminDiscountPeriodRepository.updateYears(entry.getId());
                getAllIsActive();
                list.remove(entry);
            }, delay, TimeUnit.SECONDS);
        }
    }

}
