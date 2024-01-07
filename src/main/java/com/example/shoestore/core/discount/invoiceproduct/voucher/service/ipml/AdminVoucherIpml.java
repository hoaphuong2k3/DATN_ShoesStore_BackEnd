package com.example.shoestore.core.discount.invoiceproduct.voucher.service.ipml;

import com.example.shoestore.core.common.BaseService;
import com.example.shoestore.core.common.dto.response.ResponseDTO;
import com.example.shoestore.core.common.mapper.DiscountMapper;
import com.example.shoestore.core.discount.invoiceproduct.discountDto.request.DiscountRequest;
import com.example.shoestore.core.discount.invoiceproduct.discountDto.response.DiscountProjection;
import com.example.shoestore.core.discount.invoiceproduct.discountDto.response.DiscountResponse;
import com.example.shoestore.core.discount.invoiceproduct.voucher.service.AdminVoucherService;
import com.example.shoestore.core.discount.invoiceproduct.repository.AdminDiscountRepository;
import com.example.shoestore.entity.Discount;
import com.example.shoestore.entity.MemberAccount;
import com.example.shoestore.infrastructure.constants.*;
import com.example.shoestore.infrastructure.exception.ValidateException;
import com.example.shoestore.infrastructure.utils.*;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
@EnableScheduling
public class AdminVoucherIpml implements AdminVoucherService {

    @Autowired
    AdminDiscountRepository adminVouchersRepository;

    @Autowired
    private DiscountMapper discountMapper;

    private Discount discount;

//    LocalDateTime currentDateTime = LocalDateTime.now();

    private final Map<Long, LocalDateTime> listStart = new TreeMap<>();

    private final Map<Long, LocalDateTime> listEnd = new TreeMap<>();

    @Override
    public Page<DiscountResponse> getAll(String code, String name, Integer minPrice, Integer maxPrice, LocalDate fromDate,
                                           LocalDate toDate, Integer status, Integer type, Integer isDelete, int page, int size,
                                           String sortField, String sortOrder) {
        this.validateSearch(fromDate, toDate);
        this.validatePrice(minPrice, maxPrice);
        LocalDateTime toDateFrom;
        if (toDate != null) {
            toDateFrom = LocalDateTime.of(toDate.getYear(), toDate.getMonth(), toDate.getDayOfMonth(), 23, 59, 59);
        } else {
            toDateFrom = null;
        }
        PageRequest pageRequest = BaseService.createPageRequest(page, size, sortField, sortOrder);
        Page<Discount> result = adminVouchersRepository.searchVoucher(DataBaseUtils.getLikeCondition(code), DataBaseUtils.getLikeCondition(name),
                minPrice, maxPrice, fromDate, toDateFrom, status, type, isDelete, pageRequest);
        return discountMapper.pageEntityTODTO(result);
    }

    @Override
    public ResponseDTO getAllIsActive(Long id) {
        return ResponseDTO.success(discountMapper.entityListToDTOList(adminVouchersRepository.getAllIsActiveVoucher(id)));
    }

    @SneakyThrows
    @Override
    public ResponseDTO createVoucher(DiscountRequest createVoucherDTO) {
        this.validateCommonCreate(createVoucherDTO);
        discount = new Discount();
        this.validateSaleNotNull(createVoucherDTO.getSale());
        if (createVoucherDTO.getSale() == true) {
            discount.setSalePrice(null);
            this.validateSalePercent(createVoucherDTO);
        } else {
            discount.setSalePercent(null);
            this.validateSalePrice(createVoucherDTO);
        }
        discount = discountMapper.createDTOToEntity(createVoucherDTO);
        DataFormatUtils.trimStringFields(discount);
        Discount saveVoucher = adminVouchersRepository.save(discount);
        listStart.put(saveVoucher.getId(), saveVoucher.getStartDate());
        listEnd.put(saveVoucher.getId(), saveVoucher.getEndDate());
        timerTaskStart();
        timerTaskEnd();
        return ResponseDTO.success(discountMapper.entityToDTO(saveVoucher));
    }

    @SneakyThrows
    @Override
    public ResponseDTO setVoucherRunNow(Long id) {
        Discount discountTest = adminVouchersRepository.findByIdDiscount(id);
        System.out.println(discountTest.toString());
        validateRunNowVoucher(adminVouchersRepository.findByIdDiscount(id));
        adminVouchersRepository.setDiscountById(id);
        return ResponseDTO.success("Set successfully");
    }

    @SneakyThrows
    @Override
    public ResponseDTO updateVoucher(DiscountRequest updateVoucherDTO) {
        this.validateUpdate(updateVoucherDTO);
        discount = new Discount();
        discount = discountMapper.updateDTOToEntity(updateVoucherDTO);
        DataFormatUtils.trimStringFields(discount);
        discount.setUpdatedTime(LocalDateTime.now());
        discount.setIsDeleted(IsDeleted.UNDELETED.getValue());
        discount.setDiscountType(DiscountType.VOUCHERS.getValue());
        Discount saveVoucher = adminVouchersRepository.save(discount);
        return ResponseDTO.success(discountMapper.entityToDTO(saveVoucher));
    }

    @Override
    public ResponseDTO stopVoucher(Long id) {
        discount = new Discount();
        adminVouchersRepository.stopVoucher(id);
//        getAllIsActive();
        return ResponseDTO.success("Stop successfully");
    }

    @Override
    public ResponseDTO restoreVoucher(DiscountRequest restoreVoucherDTO) {
        this.validateCommonCreate(restoreVoucherDTO);
        discount = new Discount();
        discount = discountMapper.updateDTOToEntity(restoreVoucherDTO);
        Discount restoreVoucher = adminVouchersRepository.restoreVoucher(DiscountStatus.WAITING.getValue(), discount.getId());
//        getAllIsActive();
        return ResponseDTO.success(discountMapper.entityToDTO(restoreVoucher));
    }

    @Override
    public ResponseDTO deleteVoucher(Long discountId) {
        adminVouchersRepository.deleteDiscountById(discountId);
        return ResponseDTO.success("Successfully deleted");
    }

    @Override
    public ResponseDTO deleteAllVoucher(List<Long> deleteAllVoucher) {
        deleteAllVoucher.stream().forEach(d ->
                adminVouchersRepository.deleteDiscountById(d));
        return ResponseDTO.success("Successfully deleted");
    }

    @SneakyThrows
    private void validatePrice(Integer minPrice, Integer maxPrice) {
        while (minPrice != null){
            if( minPrice < 0){
                throw new ValidateException(MessageCode.Discount.MINPRICE_IS_BIGGER_ZERO, MessageCode.Discount.ENTITY);
            }
            break;
        }
        while (minPrice != null && maxPrice != null) {
            if (minPrice > (maxPrice)) {
                throw new ValidateException(MessageCode.Discount.MINPRICE_IS_SMALLER_MAXPRICE, MessageCode.Discount.ENTITY);
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
    private void validateRunNowVoucher(Discount discount) {
        long currentTimeMillis = System.currentTimeMillis();

        // Chuyển đổi timestamp thành Instant
        Instant instant = Instant.ofEpochMilli(currentTimeMillis);

        // Chuyển đổi Instant thành LocalDateTime
        LocalDateTime currentDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        LocalDateTime truncatedDateTime = currentDateTime.truncatedTo(ChronoUnit.MINUTES);
        System.out.println("Có lỗi");
        if (discount.getEndDate().isBefore(truncatedDateTime)) {
            throw new ValidateException(MessageCode.Discount.END_DATE_IS_EXPIRED,MessageCode.Discount.ENTITY);
        }
    }

    @SneakyThrows
    private void validateCommonCreate(DiscountRequest createVoucherDTO) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DatePattern.DATE_TIME);
        LocalDateTime startDate = LocalDateTime.parse(createVoucherDTO.getStartDate(), formatter);
        LocalDateTime endDate = LocalDateTime.parse(createVoucherDTO.getEndDate(), formatter);
        long currentTimeMillis = System.currentTimeMillis();

        // Chuyển đổi timestamp thành Instant
        Instant instant = Instant.ofEpochMilli(currentTimeMillis);

        // Chuyển đổi Instant thành LocalDateTime
        LocalDateTime currentDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        LocalDateTime truncatedDateTime = currentDateTime.truncatedTo(ChronoUnit.MINUTES);
        if (createVoucherDTO.getQuantity() > 1000) {
            throw new ValidateException(MessageCode.Discount.IS_SMALLER_TWELVE, MessageCode.Discount.ENTITY);
        }
        if (Integer.parseInt(createVoucherDTO.getMinPrice()) < 1000000){
            throw new ValidateException(MessageCode.Discount.IS_BIGGER_MILLION, MessageCode.Discount.ENTITY);
        }
        if (createVoucherDTO.getQuantity() < 1){
            throw new ValidateException(MessageCode.Discount.IS_BIGGER_ZERO, MessageCode.Discount.ENTITY);
        }
        if (startDate.isBefore(truncatedDateTime)) {
            throw new ValidateException(MessageCode.Discount.START_DATE_ISEQUAL_OR_BIGGER, MessageCode.Discount.ENTITY);
        }
        if (startDate.isAfter(endDate)) {
            throw new ValidateException(MessageCode.Discount.START_DATE_ISAFTER_END_DATE, MessageCode.Discount.ENTITY);
        }
        if (Integer.parseInt(createVoucherDTO.getMinPrice()) < 0) {
            throw new ValidateException(MessageCode.Discount.GREATER_THAN_ZERO, MessageCode.Discount.ENTITY);
        }
    }

    @SneakyThrows
    private void validateUpdate(DiscountRequest updateVoucherDTO) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DatePattern.DATE_TIME);
        LocalDateTime startDate = LocalDateTime.parse(updateVoucherDTO.getStartDate(), formatter);
        LocalDateTime endDate = LocalDateTime.parse(updateVoucherDTO.getEndDate(), formatter);
        long currentTimeMillis = System.currentTimeMillis();

        // Chuyển đổi timestamp thành Instant
        Instant instant = Instant.ofEpochMilli(currentTimeMillis);

        // Chuyển đổi Instant thành LocalDateTime
        LocalDateTime currentDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        LocalDateTime truncatedDateTime = currentDateTime.truncatedTo(ChronoUnit.MINUTES);
        if (startDate.isBefore(truncatedDateTime)) {
            throw new ValidateException(MessageCode.Discount.START_DATE_ISEQUAL_OR_BIGGER, MessageCode.Discount.ENTITY);
        }
        if (updateVoucherDTO.getQuantity() > 1000) {
            throw new ValidateException(MessageCode.Discount.IS_SMALLER_TWELVE, MessageCode.Discount.ENTITY);
        }
        if (startDate.isAfter(endDate)) {
            throw new ValidateException(MessageCode.Discount.START_DATE_ISAFTER_END_DATE, MessageCode.Discount.ENTITY);
        }
        if (Integer.parseInt(updateVoucherDTO.getMinPrice()) < 0) {
            throw new ValidateException(MessageCode.Discount.GREATER_THAN_ZERO, MessageCode.Discount.ENTITY);
        }
    }

    @SneakyThrows
    private void validateSalePrice(DiscountRequest createVoucherDTO) {
        if (createVoucherDTO.getSalePrice().trim().isEmpty()){
            throw new ValidateException(MessageCode.Discount.SALE_PRICE_NOT_NULL, MessageCode.Discount.ENTITY);
        }
        if (Integer.parseInt(createVoucherDTO.getSalePrice()) < 0) {
            throw new ValidateException(MessageCode.Discount.GREATER_THAN_ZERO, MessageCode.Discount.ENTITY);
        }
        if (Double.parseDouble(createVoucherDTO.getSalePrice()) > Double.parseDouble(createVoucherDTO.getMinPrice())) {
            throw new ValidateException(MessageCode.Discount.IS_EQUAL, MessageCode.Discount.ENTITY);
        }
    }

    @SneakyThrows
    private void validateSalePercent(DiscountRequest createVoucherDTO) {
        if (createVoucherDTO.getSalePercent().trim().isEmpty()){
            throw new ValidateException(MessageCode.Discount.SALE_PERCENT_NOT_NULL, MessageCode.Discount.ENTITY);
        }
        if (Integer.parseInt(createVoucherDTO.getSalePercent()) <= 0) {
            throw new ValidateException(MessageCode.Discount.GREATER_THAN_ZERO, MessageCode.Discount.ENTITY);
        }
        if (Integer.parseInt(createVoucherDTO.getSalePercent()) > 100) {
            throw new ValidateException(MessageCode.Discount.GREATER_THAN_FIFTYPERCENT, MessageCode.Discount.ENTITY);
        }
    }

    @SneakyThrows
    private void validateExistedVoucherName(String voucherName) {
        if (adminVouchersRepository.existsByName(voucherName) > 0) {
            throw new ValidateException(MessageCode.Commom.EXISTED,
                    MessageCode.Discount.ENTITY);
        }
    }

    @SneakyThrows
    private void validateVoucherIsRunning(Long id) {
        if (adminVouchersRepository.existsVoucher(id) > 0) {
            throw new ValidateException(MessageCode.Discount.STOP_VOUCHER,
                    MessageCode.Discount.ENTITY);
        }
    }

    @SneakyThrows
    private void validateSaleNotNull(Boolean sale) {
        if (sale == null) {
            throw new ValidateException(MessageCode.Discount.SALE_NOT_NULL,
                    MessageCode.Discount.ENTITY);
        }
    }

    private synchronized void timerTaskStart() {
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        if (listEnd == null) {
            return;
        }
        for (Map.Entry<Long, LocalDateTime> entry : listStart.entrySet()) {
            long delay = LocalDateTime.now().until(entry.getValue(), ChronoUnit.SECONDS); // Khoảng thời gian giữa thời điểm hiện tại và thời điểm cần chạy tác vụ (tính bằng giây)
            long finalI = entry.getKey();
            executor.schedule(() -> {
                System.out.println("Bắt đầu");
                adminVouchersRepository.updateDiscount(finalI);
                listEnd.remove(finalI);
            }, delay, TimeUnit.SECONDS);
        }
    }

    private synchronized void timerTaskEnd() {
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        if (listEnd == null) {
            return;
        }
        for (Map.Entry<Long, LocalDateTime> entry : listEnd.entrySet()) {
            System.out.println(entry.getValue());
            long delay = LocalDateTime.now().until(entry.getValue(), ChronoUnit.SECONDS); // Khoảng thời gian giữa thời điểm hiện tại và thời điểm cần chạy tác vụ (tính bằng giây)
            long finalI = entry.getKey();
            executor.schedule(() -> {
                System.out.println("Đã dừng");
                adminVouchersRepository.stopVoucher(finalI);
                listEnd.remove(finalI);
            }, delay, TimeUnit.SECONDS);
        }
    }
}
