package com.example.shoestore.core.discount.invoiceproduct.promo.service.ipml;

import com.example.shoestore.core.common.BaseService;
import com.example.shoestore.core.common.dto.response.ResponseDTO;
import com.example.shoestore.core.common.mapper.DiscountMapper;
import com.example.shoestore.core.discount.invoiceproduct.discountDto.request.DiscountRequest;
import com.example.shoestore.core.discount.invoiceproduct.discountDto.request.PromoRequest;
import com.example.shoestore.core.discount.invoiceproduct.discountDto.request.UpdatePromoRequest;
import com.example.shoestore.core.discount.invoiceproduct.discountDto.response.DiscountProjection;
import com.example.shoestore.core.discount.invoiceproduct.discountDto.response.DiscountResponse;
import com.example.shoestore.core.discount.invoiceproduct.promo.service.AdminPromosService;
import com.example.shoestore.core.discount.invoiceproduct.repository.AdminDiscountRepository;
import com.example.shoestore.core.discount.invoiceproduct.repository.AdminDiscountShoesDetailRepository;
import com.example.shoestore.core.discount.invoiceproduct.repository.AdminShoesDiscountRepository;
import com.example.shoestore.entity.Discount;
import com.example.shoestore.entity.DiscountsShoesDetail;
import com.example.shoestore.entity.Shoes;
import com.example.shoestore.entity.ShoesDetail;
import com.example.shoestore.infrastructure.constants.*;
import com.example.shoestore.infrastructure.exception.ValidateException;
import com.example.shoestore.infrastructure.utils.DataBaseUtils;
import com.example.shoestore.infrastructure.utils.DataFormatUtils;
import com.example.shoestore.infrastructure.utils.DateUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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
public class AdminPromosImpl implements AdminPromosService {

    @Autowired
    AdminDiscountRepository adminPromosRepository;

    @Autowired
    AdminShoesDiscountRepository adminShoesDetailRepositoty;

    @Autowired
    AdminDiscountShoesDetailRepository adminDiscountShoesDetailRepository;

    @Autowired
    DiscountMapper discountMapper;

    private Discount discount;

    private ShoesDetail shoesDetail;
    private DiscountsShoesDetail discountsShoesDetail;

    private Map<Long, LocalDateTime> listStart = new TreeMap<>();

    private Map<Long, LocalDateTime> listEnd = new TreeMap<>();

    private Map<Long, Long> idShoes = new HashMap<>();

    private List<Long> list = new ArrayList<>();

    @Override
    public Page<DiscountResponse> search(String code, String name, Integer minPrice, Integer maxPrice, LocalDate fromDate, LocalDate toDate, Integer status, Integer type, Integer isDelete, int page, int size, String sortField, String sortOrder) {
        this.validateSearch(fromDate, toDate);
        this.validatePrice(minPrice, maxPrice);
        LocalDateTime toDateFrom;
        if (toDate != null) {
            toDateFrom = LocalDateTime.of(toDate.getYear(), toDate.getMonth(), toDate.getDayOfMonth(), 23, 59, 59);
        } else {
            toDateFrom = null;
        }
        PageRequest pageRequest = BaseService.createPageRequest(page, size, sortField, sortOrder);
        Page<Discount> result = adminPromosRepository.searchVoucher(DataBaseUtils.getLikeCondition(code), DataBaseUtils.getLikeCondition(name),
                minPrice, maxPrice, fromDate, toDateFrom, status, type, isDelete, pageRequest);
        return discountMapper.pageEntityTODTO(result);
    }

    @Override
    public ResponseDTO getAll() {
        return ResponseDTO.success(adminPromosRepository.getAllIsActivePromo());
    }

    @Override
    public ResponseDTO getAllIsActive() {
        return ResponseDTO.success(adminPromosRepository.getAllIsActivePromo());
    }

    @SneakyThrows
    @Override
    public ResponseDTO createPromos(PromoRequest createPromoDTO) {
        this.validateCommon(createPromoDTO);
        this.validateCreatePromo(createPromoDTO.getIdShoe());
        this.validateUpdatePromo(createPromoDTO.getIdShoe(), BigDecimal.valueOf(Long.parseLong(createPromoDTO.getMinPrice())));
        discount = new Discount();
        this.validateSaleNotNull(createPromoDTO.getSale());
        if (createPromoDTO.getSale() == true) {
            discount.setSalePrice(null);
            this.validateSalePercent(createPromoDTO);
        } else {
            discount.setSalePercent(null);
            this.validateSalePrice(createPromoDTO);
        }
        discount = discountMapper.createPromoDTOToEntity(createPromoDTO);
        System.out.println(discount);
        DataFormatUtils.trimStringFields(discount);
        adminShoesDetailRepositoty.countExistPromoByShoesIds(createPromoDTO.getIdShoe());
        this.validateIdShoeNotNull(createPromoDTO.getIdShoe());
        Discount savePromo = adminPromosRepository.save(discount);
        addDiscountShoesDetailPrice(savePromo.getId(), createPromoDTO.getIdShoe());
        listStart.put(savePromo.getId(), savePromo.getStartDate());
        listEnd.put(savePromo.getId(), savePromo.getEndDate());
        createPromoDTO.getIdShoe().forEach(x -> {
            idShoes.put(savePromo.getId(), x);
            adminShoesDetailRepositoty.saveShoe(x, savePromo.getId());
        });
        list.addAll(createPromoDTO.getIdShoe());
        timerTaskStart(list, savePromo.getId());
        timerTaskEnd(createPromoDTO.getIdShoe());
        return ResponseDTO.success(discountMapper.entityToDTO(savePromo));
    }

    @SneakyThrows
    @Override
    public synchronized ResponseDTO updatePromos(PromoRequest updatePromoDTO) {
        this.validateUpdate(updatePromoDTO);
        this.validateUpdatePromo(updatePromoDTO.getIdShoe(), BigDecimal.valueOf(Long.parseLong(updatePromoDTO.getMinPrice())));
        discount = new Discount();
        if (updatePromoDTO.getSale() == true) {
            discount.setSalePrice(null);
            this.validateSalePercent(updatePromoDTO);
        } else {
            discount.setSalePercent(null);
            this.validateSalePrice(updatePromoDTO);
        }
        discount = discountMapper.updatePromoDTOToEntity(updatePromoDTO);
        adminDiscountShoesDetailRepository.deleteDiscountShoesPrice(updatePromoDTO.getId());
        adminShoesDetailRepositoty.updateDiscountPrice(discount.getId());
        updatePromoDTO.getIdShoe().forEach(x -> {
            adminShoesDetailRepositoty.saveShoe(x, discount.getId());
        });
        updateShoesDetailPrice(updatePromoDTO.getIdShoe());
        if (updatePromoDTO.getStatus() == 1) {
            if (!list.containsAll(updatePromoDTO.getIdShoe())) {
                list.addAll(updatePromoDTO.getIdShoe());
            }
        }
        if (updatePromoDTO.getStatus() == 0) {
            updateShoesPrice(updatePromoDTO.getIdShoe(), updatePromoDTO.getId());
        }
        return ResponseDTO.success(null);
    }

    private void updateShoesDetailPrice(List<Long> idShoeDetail) {
        while (idShoeDetail.size() > 0) {
            idShoeDetail.stream().forEach(idDiscountShoes -> {
                        BigDecimal price = adminShoesDetailRepositoty.getPrice(idDiscountShoes);
                        if (discount.getSalePercent() == null) {
                            discountsShoesDetail = new DiscountsShoesDetail(discount.getId(), idDiscountShoes, price.subtract(discount.getSalePrice()));
                            adminDiscountShoesDetailRepository.save(discountsShoesDetail);
                        } else {
                            Double percent = (discount.getSalePercent() * 0.01);
                            BigDecimal salePercent = BigDecimal.valueOf(percent);
                            discountsShoesDetail = new DiscountsShoesDetail(discount.getId(), idDiscountShoes, price.subtract(price.multiply(salePercent)));
                            adminDiscountShoesDetailRepository.save(discountsShoesDetail);
                        }
                    }
            );
            break;
        }
    }


    @Override
    public ResponseDTO detailPromos(Long id) {
        return ResponseDTO.success(adminShoesDetailRepositoty.getOneCustom(id));
    }

    @Override
    public ResponseDTO setPromoRunNow(Long id) {
        Optional<Discount> optionalDiscount = adminPromosRepository.findById(id);
        List<DiscountsShoesDetail> listShoeDetail = adminDiscountShoesDetailRepository.getByPromoId(id);
        List<Long> listShoeDetailId = adminDiscountShoesDetailRepository.getByShoesDetailId(id);
        validateRunNowVoucher(optionalDiscount);
        adminPromosRepository.setDiscountById(id);
        listShoeDetail.forEach(discount -> adminShoesDetailRepositoty.updatePromoIdByShoeId(discount.getPromoId(), discount.getShoesDetailId()));
        updateShoesPriceBy(listShoeDetailId, optionalDiscount.get().getId(), optionalDiscount.get().getSalePercent(), optionalDiscount.get().getSalePrice());
        return ResponseDTO.success("Set successfully");
    }

    @SneakyThrows
    void updateShoesPriceBy(List<Long> idShoes, Long idPromo, Integer salePercentDiscount, BigDecimal salePriceDiscount) {
        idShoes.forEach(x -> {
                    BigDecimal price = adminShoesDetailRepositoty.getPrice(x);
                    if (salePercentDiscount == null) {
                        adminShoesDetailRepositoty.addDiscountPrice(price.subtract(salePriceDiscount), x);
                    } else {
                        Double percent = (salePercentDiscount * 0.01);
                        BigDecimal salePercent = BigDecimal.valueOf(percent);
                        adminShoesDetailRepositoty.addDiscountPrice(price.subtract(price.multiply(salePercent)), x);
                    }
                }
        );
    }

    @Override
    public ResponseDTO stopPromos(Long id) {
        adminPromosRepository.stopDiscountById(id);
        adminShoesDetailRepositoty.updateDiscountPrice(id);
        adminShoesDetailRepositoty.updatePromoId(id);
        return ResponseDTO.success("Success");
    }

    @Override
    public ResponseDTO restorePromos(DiscountRequest restorePromoDTO) {
        discount = new Discount();
        discount = discountMapper.updateDTOToEntity(restorePromoDTO);
        discount.setStatus(Status.ACTIVATE.getValue());
        Discount restorePromo = adminPromosRepository.save(discount);
        getAllIsActive();
        return ResponseDTO.success(discountMapper.entityToDTO(restorePromo));
    }

    @Override
    public ResponseDTO deletePromos(Long promoId) {
        adminShoesDetailRepositoty.updateDiscountPrice(promoId);
        adminShoesDetailRepositoty.updatePromoId(promoId);
        adminPromosRepository.deleteDiscountById(promoId);
        return ResponseDTO.success("Delete Successfully");
    }

    @Override
    public ResponseDTO deleteAllPromo(List<Long> deleteAllPromo) {
        deleteAllPromo.stream().forEach(d -> {
            adminShoesDetailRepositoty.updateDiscountPrice(d);
            adminShoesDetailRepositoty.updatePromoId(d);
            adminPromosRepository.deleteDiscountById(d);
        });
        return ResponseDTO.success("Delete Successfully");
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
    private void validatePrice(Integer minPrice, Integer maxPrice) {
        while (minPrice != null) {
            if (minPrice < 0) {
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
    private void validateCommon(PromoRequest createPromoDTO) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DatePattern.DATE_TIME);
        LocalDateTime startDate = LocalDateTime.parse(createPromoDTO.getStartDate(), formatter);
        LocalDateTime endDate = LocalDateTime.parse(createPromoDTO.getEndDate(), formatter);
        long currentTimeMillis = System.currentTimeMillis();

        // Chuyển đổi timestamp thành Instant
        Instant instant = Instant.ofEpochMilli(currentTimeMillis);

        // Chuyển đổi Instant thành LocalDateTime
        LocalDateTime currentDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        if (Integer.parseInt(createPromoDTO.getMinPrice()) < 1000000 ){
            throw new ValidateException(MessageCode.Discount.IS_MILLION, MessageCode.Discount.ENTITY);
        }
        if (startDate.isAfter(endDate)) {
            throw new ValidateException(MessageCode.Discount.START_DATE_ISAFTER_END_DATE, MessageCode.Discount.ENTITY);
        }

        LocalDateTime truncatedDateTime = currentDateTime.truncatedTo(ChronoUnit.MINUTES);
        if (startDate.isBefore(truncatedDateTime)) {
            throw new ValidateException(MessageCode.Discount.START_DATE_ISEQUAL_OR_BIGGER, MessageCode.Discount.ENTITY);
        }
        if (Integer.parseInt(createPromoDTO.getMinPrice()) < 0) {
            throw new ValidateException(MessageCode.Discount.GREATER_THAN_ZERO, MessageCode.Discount.ENTITY);
        }
    }

    @SneakyThrows
    private void validateRunNowVoucher(Optional<Discount> optionalDiscount) {
        long currentTimeMillis = System.currentTimeMillis();

        // Chuyển đổi timestamp thành Instant
        Instant instant = Instant.ofEpochMilli(currentTimeMillis);

        // Chuyển đổi Instant thành LocalDateTime
        LocalDateTime currentDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        LocalDateTime truncatedDateTime = currentDateTime.truncatedTo(ChronoUnit.MINUTES);
        if (optionalDiscount.get().getEndDate().isBefore(truncatedDateTime)) {
            throw new ValidateException(MessageCode.Discount.END_DATE_IS_EXPIRED, MessageCode.Discount.ENTITY);
        }
//        if (optionalDiscount.get().getStartDate().isBefore(LocalDateTime.now()) || optionalDiscount.get().getStartDate().isEqual(LocalDateTime.now())) {
//            throw new ValidateException(MessageCode.Discount.START_DATE_IS_RUNNING, MessageCode.Discount.ENTITY);
//        }
    }

    @SneakyThrows
    private void validateUpdate(PromoRequest createPromoDTO) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DatePattern.DATE_TIME);
        LocalDateTime startDate = LocalDateTime.parse(createPromoDTO.getStartDate(), formatter);
        LocalDateTime endDate = LocalDateTime.parse(createPromoDTO.getEndDate(), formatter);
        if (Integer.parseInt(createPromoDTO.getMinPrice()) < 1000000 ){
            throw new ValidateException(MessageCode.Discount.IS_MILLION, MessageCode.Discount.ENTITY);
        }
        if (startDate.isAfter(endDate)) {
            throw new ValidateException(MessageCode.Discount.START_DATE_ISAFTER_END_DATE, MessageCode.Discount.ENTITY);
        }
        if (Integer.parseInt(createPromoDTO.getMinPrice()) < 0) {
            throw new ValidateException(MessageCode.Discount.GREATER_THAN_ZERO, MessageCode.Discount.ENTITY);
        }
    }

    @SneakyThrows
    private void validateSalePriceUp(UpdatePromoRequest createPromoDTO) {
        if (!DateUtils.isNotNumber(createPromoDTO.getSalePrice())) {
            throw new ValidateException(MessageCode.Discount.IS_NUMBER, MessageCode.Discount.ENTITY);
        }
        if (Integer.parseInt(createPromoDTO.getSalePrice()) <= 0) {
            throw new ValidateException(MessageCode.Discount.GREATER_THAN_ZERO, MessageCode.Discount.ENTITY);
        }
        if (Integer.parseInt(createPromoDTO.getSalePrice()) * 100 / Integer.parseInt(createPromoDTO.getMinPrice()) > 100) {
            throw new ValidateException(MessageCode.Discount.GREATER_THAN_FIFTYPERCENT, MessageCode.Discount.ENTITY);
        }
    }

    @SneakyThrows
    private void validateSalePercentUp(UpdatePromoRequest createPromoDTO) {
        if (!DateUtils.isNotNumber(createPromoDTO.getSalePercent())) {
            throw new ValidateException(MessageCode.Discount.IS_NUMBER, MessageCode.Discount.ENTITY);
        }
        if (Integer.parseInt(createPromoDTO.getSalePercent()) <= 0) {
            throw new ValidateException(MessageCode.Discount.GREATER_THAN_ZERO, MessageCode.Discount.ENTITY);
        }
        if (Integer.parseInt(createPromoDTO.getSalePercent()) > 100) {
            throw new ValidateException(MessageCode.Discount.GREATER_THAN_FIFTYPERCENT, MessageCode.Discount.ENTITY);
        }
    }

    private void validateCreatePromo(List<Long> idShoesDetails) {
        if (idShoesDetails.isEmpty()) {
            return;
        }
        idShoesDetails.forEach(priceShoes -> {
            if (adminShoesDetailRepositoty.findPriceIsUsing(priceShoes).getPromoId() != null) {
                try {
                    throw new ValidateException("Sản phẩm mã " + adminShoesDetailRepositoty.findPriceIsUsing(priceShoes).getShoeCode() +
                            " có mã chi tiết là " + adminShoesDetailRepositoty.findPriceIsUsing(priceShoes).getShoeDetailCode() +
                            " đang được sử dụng cho giảm giá sản phẩm khác," +
                            "vui lòng chọn mã chi tiết khác", MessageCode.Discount.ENTITY);
                } catch (ValidateException e) {
                    throw new RuntimeException(e);
                }
            }
        });

    }


    private void validateUpdatePromo(List<Long> idShoesDetails, BigDecimal salePrice) {
        if (idShoesDetails.size() == 0) {
            return;
        } else {
            List<ShoesDetail> listPrice = adminShoesDetailRepositoty.findPrice(idShoesDetails);
            listPrice.forEach(priceShoes -> {
                if (salePrice.intValue() > priceShoes.getPrice().intValue() ) {
                    try {
                        throw new ValidateException("Sản phẩm có mã chi tiết là " + priceShoes.getCode() +
                                " có giá là " + priceShoes.getPrice() + " đang nhỏ hơn giá trị sản phẩm từ " + salePrice.intValue() +
                                " vui lòng chọn sản phẩm khác có giá lớn hơn giá trị sản phẩm !",
                                MessageCode.Discount.ENTITY);
                    } catch (ValidateException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        }
    }


    @SneakyThrows
    private void validateSalePrice(PromoRequest createPromoDTO) {
        if (Integer.parseInt(createPromoDTO.getSalePrice()) < 1000) {
            throw new ValidateException(MessageCode.Discount.SALE_IS_BIGGER_THOUSAND, MessageCode.Discount.ENTITY);
        }
        if (createPromoDTO.getSalePrice().trim().isEmpty()) {
            throw new ValidateException(MessageCode.DiscountPeriod.SALE_NOT_NULL, MessageCode.Discount.ENTITY);
        }
        if (Integer.parseInt(createPromoDTO.getSalePrice()) <= 0) {
            throw new ValidateException(MessageCode.Discount.GREATER_THAN_ZERO, MessageCode.Discount.ENTITY);
        }
        if ((Integer.parseInt(createPromoDTO.getSalePrice()) > Integer.parseInt(createPromoDTO.getMinPrice()))) {
            throw new ValidateException(MessageCode.Discount.SALE_PRICE_MIN_PRICE, MessageCode.Discount.ENTITY);
        }
    }

    @SneakyThrows
    private void validateSalePercent(PromoRequest createPromoDTO) {
        if (createPromoDTO.getSalePercent().trim().isEmpty()) {
            throw new ValidateException(MessageCode.DiscountPeriod.SALE_PERCENT_NOT_NULL, MessageCode.Discount.ENTITY);
        }
        if (Integer.parseInt(createPromoDTO.getSalePercent()) <= 0) {
            throw new ValidateException(MessageCode.Discount.GREATER_THAN_ZERO, MessageCode.Discount.ENTITY);
        }
        if (Integer.parseInt(createPromoDTO.getSalePercent()) > 100) {
            throw new ValidateException(MessageCode.Discount.GREATER_THAN_FIFTYPERCENT, MessageCode.Discount.ENTITY);
        }
    }

    @SneakyThrows
    private void validatePromoIsRunning(List<Long> id) {
        if (adminShoesDetailRepositoty.countExistPromoByShoesIds(id) > 0) {
            throw new ValidateException(MessageCode.Discount.PROMOS_IS_RUNNING,
                    MessageCode.Discount.ENTITY);
        }
    }


    @SneakyThrows
    private void validateIdShoeNotNull(List<Long> id) {
        if (id.size() == 0) {
            throw new ValidateException(MessageCode.Discount.ID_NOT_NULL,
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

    @SneakyThrows
    void addDiscountShoesDetailPrice(Long idPromo, List<Long> idShoes) {
        idShoes.forEach(x -> {
                    BigDecimal price = adminShoesDetailRepositoty.getPrice(x);
                    if (discount.getSalePercent() == null) {
                        discountsShoesDetail = new DiscountsShoesDetail(idPromo, x, price.subtract(discount.getSalePrice()));
                        adminDiscountShoesDetailRepository.save(discountsShoesDetail);
                    } else {
                        Double percent = (discount.getSalePercent() * 0.01);
                        BigDecimal salePercent = BigDecimal.valueOf(percent);
                        discountsShoesDetail = new DiscountsShoesDetail(idPromo, x, price.subtract(price.multiply(salePercent)));
                        adminDiscountShoesDetailRepository.save(discountsShoesDetail);
                    }
                }
        );
    }

    @SneakyThrows
    void updateShoesPrice(List<Long> idShoes, Long idPromo) {
        idShoes.forEach(x -> {
//                    adminShoesDetailRepositoty.saveShoe(x, idPromo);
                    BigDecimal price = adminShoesDetailRepositoty.getPrice(x);
                    if (discount.getSalePercent() == null) {
                        adminShoesDetailRepositoty.addDiscountPrice(price.subtract(discount.getSalePrice()), x);
                    } else {
                        Double percent = (discount.getSalePercent() * 0.01);
                        BigDecimal salePercent = BigDecimal.valueOf(percent);
                        adminShoesDetailRepositoty.addDiscountPrice(price.subtract(price.multiply(salePercent)), x);
                    }
                }
        );
    }

    private synchronized void timerTaskStart(List<Long> idShoes, Long idPromo) {
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        if (listEnd == null) {
            return;
        }
        for (Map.Entry<Long, LocalDateTime> entry : listStart.entrySet()) {
            long delay = LocalDateTime.now().until(entry.getValue(), ChronoUnit.SECONDS); // Khoảng thời gian giữa thời điểm hiện tại và thời điểm cần chạy tác vụ (tính bằng giây)
            long finalI = entry.getKey();
            executor.schedule(() -> {
                updateShoesPrice(idShoes, idPromo);
                System.out.println("Bắt đầu");
                adminPromosRepository.updateDiscount(finalI);
                getAllIsActive();
                listEnd.remove(finalI);
            }, delay, TimeUnit.SECONDS);
        }
    }


    private synchronized void timerTaskEnd(List<Long> id) {
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        if (listEnd == null || listEnd.isEmpty()) {
            return;
        }

        Iterator<Map.Entry<Long, LocalDateTime>> iterator = listEnd.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Long, LocalDateTime> entry = iterator.next();
            long delay = LocalDateTime.now().until(entry.getValue(), ChronoUnit.SECONDS);

            executor.schedule(() -> {
                System.out.println("Đã dừng");
                // Thực hiện các tác vụ cần thiết
                id.forEach(x -> adminShoesDetailRepositoty.stopShoe(x));
                adminPromosRepository.stopVoucher(entry.getKey());
                getAllIsActive();
                iterator.remove(); // Loại bỏ phần tử đã được xử lý
            }, delay, TimeUnit.SECONDS);
        }
    }
}
