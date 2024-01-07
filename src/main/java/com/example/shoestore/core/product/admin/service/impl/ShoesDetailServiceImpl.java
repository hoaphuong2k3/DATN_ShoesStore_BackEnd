package com.example.shoestore.core.product.admin.service.impl;

import com.amazonaws.util.IOUtils;
import com.example.shoestore.core.common.BaseService;
import com.example.shoestore.core.common.dto.response.FileResponseDTO;
import com.example.shoestore.core.common.dto.response.ResponseDTO;
import com.example.shoestore.core.common.mapper.ImageMapper;
import com.example.shoestore.core.common.mapper.ShoesDetailMapper;
import com.example.shoestore.core.product.admin.dto.request.*;
import com.example.shoestore.core.product.admin.dto.response.*;
import com.example.shoestore.core.product.admin.repository.*;
import com.example.shoestore.core.product.admin.service.ShoesDetailService;
import com.example.shoestore.core.product.admin.dto.request.ShoesDetailReportRequest;
import com.example.shoestore.entity.*;
import com.example.shoestore.entity.base.PrimaryEntity;
import com.example.shoestore.infrastructure.constants.HeaderTable;
import com.example.shoestore.infrastructure.constants.IsDeleted;
import com.example.shoestore.infrastructure.constants.MessageCode;
import com.example.shoestore.infrastructure.constants.ShoesStatus;
import com.example.shoestore.infrastructure.exception.ResourceNotFoundException;
import com.example.shoestore.infrastructure.exception.ValidateException;
import com.example.shoestore.infrastructure.exception.ValidationError;
import com.example.shoestore.infrastructure.utils.*;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
public class ShoesDetailServiceImpl implements ShoesDetailService {

    private final AdminShoesDetailRepositoty shoesDetailRepositoty;

    private final AdminShoesRepository shoesRepository;

    private final ShoesDetailMapper shoesDetailMapper;

    private final AdminColorRepository colorRepository;

    private final AdminSizeRepository sizeRepository;

    private final AdminImageRepository imageRepository;

    private final FileValidateUtils fileValidateUtils;

    private final ImageUtils imageUtils;

    private final ImageMapper imageMapper;

    private final AdminMemberAccountRepository accountRepository;
    private final GmailUtils gmailUtils;
    public static final String subject = "BÁO CÁO SẢN PHẨM HẾT HÀNG";
    public static final String text = "GỬI ĐẾN CÁC SẾP TỔNG NHÀ MÌNH";

    @SneakyThrows
    @Override
    public ResponseDTO createMultipart(Long shoesId, List<ShoesDetailCreateRequest> createRequest, List<MultipartFile> files) {

        this.getExistByShoesId(shoesId);

        fileValidateUtils.validateImageFiles(files);

        List<Long> colorIds = createRequest.stream().map(ShoesDetailCreateRequest::getColorId).collect(Collectors.toList());

        List<Long> sizeIds = createRequest.stream().map(ShoesDetailCreateRequest::getSizeId).collect(Collectors.toList());

        List<Long> colorIdsUnique = colorIds.stream()
                .distinct()
                .collect(Collectors.toList());

        List<Long> sizerIdsUnique = sizeIds.stream()
                .distinct()
                .collect(Collectors.toList());

        this.checkIdsNotExist(colorIdsUnique, sizerIdsUnique);

        // validate ShoesDetail Duplicate
        createRequest.forEach(shdt -> {
            ShoesDetail shoesDetail = shoesDetailRepositoty.getExistByListIdForCreate(shoesId, shdt.getColorId(), shdt.getSizeId());
            if (DataUtils.isNotNull(shoesDetail)) {
                try {
                    throw new ResourceNotFoundException(MessageCode.Commom.EXISTED, MessageCode.ShoesDetail.ENTITY);
                } catch (ResourceNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        this.validateImageDuplicate(files);

        List<MultipartFile> multipartFilesUpload = new ArrayList<>();

        Map<Long, List<MultipartFile>> map = new HashMap<>();

        final int MIN_FILES_PER_GROUP = 3;
        final int MAX_FILES_PER_GROUP = 5;

        for (MultipartFile file : files) {

            String originalFileName = file.getOriginalFilename();
            Long id = extractIdFromFileName(originalFileName);

            if (map.containsKey(id)) {
                map.get(id).add(file);
            } else {
                List<MultipartFile> fileList = new ArrayList<>();
                fileList.add(file);
                map.put(id, fileList);
            }
            multipartFilesUpload.add(file);
        }

        for (Map.Entry<Long, List<MultipartFile>> entry : map.entrySet()) {
            List<MultipartFile> fileList = entry.getValue();
            int numberOfFiles = fileList.size();
            if (numberOfFiles < MIN_FILES_PER_GROUP || numberOfFiles > MAX_FILES_PER_GROUP) {
                throw new ValidateException(MessageCode.Image.ALLOWED_IMAGES_SIZE);
            }
        }

        List<ShoesDetail> shoesDetails = shoesDetailMapper.createsToEntities(shoesId, createRequest);

        shoesDetailRepositoty.saveAll(shoesDetails);

        List<Image> imageList = new ArrayList<>();

        Map<String, Map<Long, String>> imgURIsByColorAndImgName = new HashMap<>();

        shoesDetails.forEach(shdt -> {
            Long colorId = shdt.getColorId();

            List<MultipartFile> imageFiles = map.get(colorId);

            for (MultipartFile imageFile : imageFiles) {
                Image.ImageBuilder imageBuilder = Image.builder();
                imageBuilder.shoesDetailId(shdt.getId());
                String imgName = imageFile.getOriginalFilename();

                imgURIsByColorAndImgName.computeIfAbsent(imgName, k -> new HashMap<>());

                Map<Long, String> imgURIsByColor = imgURIsByColorAndImgName.get(imgName);

                if (!imgURIsByColor.containsKey(colorId)) {
                    String imgURI = ImageUtils.generateUniqueFileName(imgName);
                    imgURIsByColor.put(colorId, imgURI);
                }

                String imgURI = imgURIsByColor.get(colorId);
                imageBuilder.imgName(imgName);
                imageBuilder.imgURI(imgURI);
                imageBuilder.isDeleted(Boolean.FALSE);
                Image image = imageBuilder.build();
                imageList.add(image);
            }
        });

        imageRepository.saveAll(imageList);

        Map<String, String> mapURI = new HashMap<>();

        imageList.forEach(image -> mapURI.put(image.getImgName(), image.getImgURI()));

        // Cùng màu khác size thì upload 1 ảnh, thêm cùng 1 URI
        imageUtils.uploadImagesCustom(multipartFilesUpload, mapURI);

        return ResponseDTO.success(shoesDetailMapper.entitiesToDTOs(shoesDetails, imageList));
    }

    @SneakyThrows
    @Override
    public ResponseDTO update(Long id, ShoesDetailUpdateRequest updateRequest) {

        ShoesDetail shoesDetail = this.getExistById(id);

        Long colorId = updateRequest.getColorId();
        Long sizeId = updateRequest.getSizeId();

        this.checkIdNotExist(colorId, sizeId);

        // validate ShoesDetail Duplicate
        ShoesDetail shoesDetailExist = shoesDetailRepositoty.getExistByListIdForUpdate(id, shoesDetail.getShoesId(), colorId, sizeId);

        if (DataUtils.isNotNull(shoesDetailExist)) {
            throw new ResourceNotFoundException(MessageCode.Commom.EXISTED, MessageCode.ShoesDetail.ENTITY);
        }

        shoesDetailMapper.updateToEntity(updateRequest, shoesDetail);
        shoesDetailRepositoty.save(shoesDetail);

        return ResponseDTO.success(shoesDetailMapper.entityToDTO(shoesDetail));
    }

    @SneakyThrows
    @Override
    public ResponseDTO search(Long idShoes, ShoesDetailSearchRequest searchRequest, Pageable pageable) {

        this.getExistByShoesId(idShoes);

        DataFormatUtils.trimStringFields(searchRequest);

        DataUtils.validateStringDate(searchRequest.getFromDateStr(), searchRequest.getToDateStr());

        DataUtils.validateFromDateAfterToDate(searchRequest.getFromDate(), searchRequest.getToDate());

        Page<ShoesDetailSearchResponse> result = shoesDetailRepositoty.search(idShoes, searchRequest, pageable);

        if (result.isEmpty()) {
            throw new ValidateException(MessageCode.Commom.RESULT_EMPTY);
        }

        List<Long> shoesDetailIds = result.map(ShoesDetailSearchResponse::getId).stream().toList();

        List<Image> images = imageRepository.getAllByIds(shoesDetailIds);

        Map<Long, List<Image>> map = new HashMap<>();

        images.forEach(image -> {
            if (map.containsKey(image.getShoesDetailId())) {
                map.get(image.getShoesDetailId()).add(image);
            } else {
                List<Image> imageList = new ArrayList<>();
                imageList.add(image);
                map.put(image.getShoesDetailId(), imageList);
            }
        });

        List<ShoesDetailImageSearchResponse> response = new ArrayList<>();

        result.getContent().forEach(shoesDetailSearchResponse -> {
            ShoesDetailImageSearchResponse searchResponse = new ShoesDetailImageSearchResponse();
            Long shoesDetailId = shoesDetailSearchResponse.getId();

            if (map.containsKey(shoesDetailId)) {
                searchResponse.setImageDTOS(imageMapper.entityListToDTOList(map.get(shoesDetailId)));
            }
            searchResponse.setShoesDetailSearchResponse(shoesDetailSearchResponse);
            response.add(searchResponse);
        });

        Page<ShoesDetailImageSearchResponse> searchResponses = new PageImpl<>(response, pageable, result.getTotalElements());

        return ResponseDTO.success(searchResponses);
    }

    @Override
    public ResponseDTO findOne(Long id) {
        return ResponseDTO.success(shoesDetailMapper.entityToDTO(this.getExistById(id)));
    }

    @Override
    public ResponseDTO findOneByQRCode(String qrCode) {
        return ResponseDTO.success(shoesDetailMapper.entityToDTO(this.getExistByQRCode(qrCode)));
    }

    @SneakyThrows
    @Override
    public ResponseDTO deleteMultipart(List<Long> ids) {

        if (CollectionUtils.isEmpty(ids)) {
            throw new ValidateException(MessageCode.Commom.NOT_NULL, MessageCode.ShoesDetail.ENTITY);
        }

        if (shoesDetailRepositoty.getNotOnBusinessByIs(ids).size() != ids.size()) {
            throw new ResourceNotFoundException(MessageCode.ShoesDetail.DELETE_ON_BUSINESS);
        }

        shoesDetailRepositoty.setIsDelete(ids, IsDeleted.DELETED.getValue());

        return ResponseDTO.success();
    }

    @Override
    public ResponseDTO onBusiness(Long id) {

        shoesDetailRepositoty.setStatus(id, ShoesStatus.ON_BUSINESS.getValue());
        return ResponseDTO.success();
    }

    @Override
    public ResponseDTO stopBusiness(Long id) {

        shoesDetailRepositoty.setStatus(id, ShoesStatus.STOP_BUSINESS.getValue());
        return ResponseDTO.success();
    }

    private <T> ValidationError<T> findErrorByObject(List<ValidationError<T>> errorList, T objectToFind) {
        for (ValidationError<T> error : errorList) {
            if (Objects.equals(error.getObject(), objectToFind)) {
                return error;
            }
        }
        return null;
    }

    private <T> void validateAndAddError(List<ShoesDetailImportRequest> shoesDetailFromExcel,
                                                Set<Long> idSetInDB,
                                                Function<ShoesDetailImportRequest, Long> idExtractor,
                                                List<ValidationError<ShoesDetailImportRequest>> errorList,
                                                String errorMessage) {
        for (ShoesDetailImportRequest shoesDetail : shoesDetailFromExcel) {
            Long id = idExtractor.apply(shoesDetail);

            if (!idSetInDB.contains(id)) {
                ValidationError<ShoesDetailImportRequest> existingError = findErrorByObject(errorList, shoesDetail);

                if (existingError == null) {
                    ValidationError<ShoesDetailImportRequest> newError = new ValidationError<>();
                    newError.setObject(shoesDetail);
                    newError.setErrors(new ArrayList<>());
                    newError.getErrors().add(errorMessage);
                    errorList.add(newError);
                } else {
                    existingError.getErrors().add(errorMessage);
                }
            }
        }
    }

    @SneakyThrows
    @Override
    public FileResponseDTO importExcel(MultipartFile fileExcel) {

        fileValidateUtils.validateExcelFile(fileExcel);

        Pair<List<ShoesDetailImportRequest>, List<ValidationError<ShoesDetailImportRequest>>> resultPair = ExcelImportUtils.importFromExcel(fileExcel, ShoesDetailImportRequest.class);

        // list All
        List<ShoesDetailImportRequest> shoesDetailFromExcel = resultPair.getLeft();
        List<ValidationError<ShoesDetailImportRequest>> errorList = resultPair.getRight();

        // validate Id exist in DB
        String errorShoesMess = MessageUtils.getMessage(MessageCode.Commom.NOT_EXIST, MessageUtils.getMessage(MessageCode.Shoes.ENTITY));
        String errorColorMess = MessageUtils.getMessage(MessageCode.Commom.NOT_EXIST, MessageUtils.getMessage(MessageCode.Color.ENTITY));
        String errorSizeMess = MessageUtils.getMessage(MessageCode.Commom.NOT_EXIST, MessageUtils.getMessage(MessageCode.Size.ENTITY));

        Set<Long> shoesIdInRQ = shoesDetailFromExcel.stream().map(ShoesDetailImportRequest::getShoesId).collect(Collectors.toSet());
        Set<Long> colorIdInRQ = shoesDetailFromExcel.stream().map(ShoesDetailImportRequest::getColorId).collect(Collectors.toSet());
        Set<Long> sizeIdInRQ = shoesDetailFromExcel.stream().map(ShoesDetailImportRequest::getSizeId).collect(Collectors.toSet());


        Set<Long> shoesIdInDB = shoesRepository.getExistByIds(new ArrayList<>(shoesIdInRQ)).stream().map(PrimaryEntity::getId).collect(Collectors.toSet());
        Set<Long> colorIdInDB = colorRepository.getExistByIds(new ArrayList<>(colorIdInRQ)).stream().map(PrimaryEntity::getId).collect(Collectors.toSet());
        Set<Long> sizeIdInDB = sizeRepository.getExistByIds(new ArrayList<>(sizeIdInRQ)).stream().map(PrimaryEntity::getId).collect(Collectors.toSet());

        validateAndAddError(shoesDetailFromExcel, shoesIdInDB, ShoesDetailImportRequest::getShoesId, errorList, errorShoesMess);
        validateAndAddError(shoesDetailFromExcel, colorIdInDB, ShoesDetailImportRequest::getColorId, errorList, errorColorMess);
        validateAndAddError(shoesDetailFromExcel, sizeIdInDB, ShoesDetailImportRequest::getSizeId, errorList, errorSizeMess);

        // Lấy ra tất cả stt trong ShoesDetailImportRequest
        Set<Integer> sttSetInRequest = shoesDetailFromExcel.stream()
                .map(ShoesDetailImportRequest::getStt)
                .collect(Collectors.toSet());

        // Lấy ra tất cả stt trong errorList
        Set<Integer> sttSetInErrorList = errorList.stream()
                .map(error -> error.getObject().getStt())
                .collect(Collectors.toSet());

        // Tìm những stt có trong cả sttSetInRequest và sttSetInErrorList
        Set<Integer> commonSttSet = new HashSet<>(sttSetInRequest);
        commonSttSet.retainAll(sttSetInErrorList);

        // Loại bỏ các đối tượng có stt thuộc commonSttSet khỏi shoesDetailFromExcel
        shoesDetailFromExcel.removeIf(detail -> commonSttSet.contains(detail.getStt()));

        // validate ShoesDetail Duplicate
        shoesDetailFromExcel.forEach(shdt -> {
            // Kiểm tra xem ShoesDetail có tồn tại trong cơ sở dữ liệu không
            ShoesDetail shoesDetail = shoesDetailRepositoty.getExistByListIdForCreate(shdt.getShoesId(), shdt.getColorId(), shdt.getSizeId());

            if (DataUtils.isNotNull(shoesDetail)) {
                // Nếu đã tồn tại, cộng dồn số lượng từ dữ liệu từ Excel
                shoesDetail.setQuantity(shoesDetail.getQuantity() + shdt.getQuantity());
                shoesDetailRepositoty.save(shoesDetail); // Giả sử có một phương thức save trong repository để lưu thay đổi vào cơ sở dữ liệu
            }
        });

        List<ShoesDetail> shoesDetails = shoesDetailMapper.importExcelsToEntities(shoesDetailFromExcel);

        shoesDetails = shoesDetailRepositoty.saveAll(shoesDetails);

        List<ShoesDetailImportResponse> responseList = new ArrayList<>();

        // Duyệt qua từng ShoesDetailImportRequest trong errorList
        for (ValidationError<ShoesDetailImportRequest> validationError : errorList) {
            // Duyệt qua danh sách errors của ShoesDetailImportRequest
            List<String> errors = validationError.getErrors();

            // Gộp tất cả các lỗi thành một chuỗi duy nhất, ngăn cách bởi ", "
            StringBuilder mergedErrors = new StringBuilder();
            for (String error : errors) {
                mergedErrors.append(error).append(", ");
            }

            // Kiểm tra xem có errors nào để gộp không
            if (mergedErrors.length() > 0) {
                // Xóa ký tự ", " cuối cùng
                mergedErrors.delete(mergedErrors.length() - 2, mergedErrors.length());

                // Thay thế danh sách errors trong ShoesDetailImportRequest bằng một list chứa chuỗi errors gộp
                errors.clear();
                errors.add(mergedErrors.toString());
            }

            ShoesDetailImportResponse response = shoesDetailMapper.toShoesDetailImportExcel(validationError.getObject(),validationError.getErrors().toString());
            responseList.add(response);

        }

        int stt = 1;
        for (ShoesDetailImportResponse shoesDetail : responseList) {
            shoesDetail.setStt(stt++);
        }

        FileResponseDTO fileResponseDTO = ExelExportUtils.exportToExcel(Arrays.asList(HeaderTable.SHOES_DETAILS_IMPORT_RESPONSE), responseList, "Shoes_Detail_Import_Response.xlsx", "Sheet1");


        return fileResponseDTO;
    }

    @SneakyThrows
    @Override
    public FileResponseDTO exportExel(List<ShoesDetailExportExcelRequest> exportResponses) {

        int stt = 1;
        for (ShoesDetailExportExcelRequest shoesDetail : exportResponses) {
            shoesDetail.setStt(stt++);
        }

        FileResponseDTO fileResponseDTO = ExelExportUtils.exportToExcel(Arrays.asList(HeaderTable.SHOES_DETAIL), exportResponses, "ShoesDetail_Export.xlsx", "Sheet1");

        return fileResponseDTO;
    }


    @SneakyThrows
    @Override
    public FileResponseDTO exportPDF(Long shoesId, List<ShoesDetailExportPDFRequest> exportResponses) {

        int stt = 1;
        for (ShoesDetailExportPDFRequest shoesDetail : exportResponses) {
            shoesDetail.setStt(stt++);
        }

        ShoesFindOneResponse shoes = shoesRepository.findOne(shoesId);

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        InputStream inputStream = getClass().getResourceAsStream("/templates/pdf/ShoesDetail_Export.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("code", shoes.getCode());
        parameters.put("name", shoes.getName());
        parameters.put("brand", shoes.getBrandName());
        parameters.put("origin", shoes.getOriginName());
        parameters.put("designStyle", shoes.getDesignStyleName());
        parameters.put("skinType", shoes.getSkinTypeName());
        parameters.put("toe", shoes.getSkinTypeName());
        parameters.put("sole", shoes.getSoleName());
        parameters.put("lining", shoes.getLiningName());
        parameters.put("cushion", shoes.getCushionName());
        parameters.put("imgURI", shoes.getImgURI());
        parameters.put("dataTable", new JRBeanCollectionDataSource(exportResponses));

        FileResponseDTO fileResponseDTO = new FileResponseDTO(JasperUtils.exportJasperReportToPdf(jasperReport, parameters, bos), "ShoesDetail_Export.pdf");

        return fileResponseDTO;
    }

    @SneakyThrows
    @Override
    public FileResponseDTO exportPattern() {

        InputStream inputStream = getClass().getResourceAsStream("/templates/xlsx/ShoesDetail_Pattern.xlsx");

        byte[] fileBytes = IOUtils.toByteArray(inputStream);

        ByteArrayResource byteArrayResource = new ByteArrayResource(fileBytes);

        FileResponseDTO fileResponseDTO = new FileResponseDTO();
        fileResponseDTO.setByteArrayResource(byteArrayResource);
        fileResponseDTO.setFileName("ShoesDetail_Pattern.xlsx");

        return fileResponseDTO;
    }

    @SneakyThrows
    @Override
    public ResponseDTO reportByShoesIdToDoc(Long shoesId, ShoesDetailReportRequest request) {

        String nameUser = BaseService.getUserLogin().getFullname();
        String email = BaseService.getUserLogin().getEmail();
        Timestamp timeNow = new Timestamp(System.currentTimeMillis());

        List<ShoesDetailReportDocResponse> shoesDetailReportDocResponses = request.getShoesDetailReportDocResponses();

        if (DataUtils.isEmpty(shoesDetailReportDocResponses)) {
            throw new ResourceNotFoundException(MessageCode.ShoesDetail.ENTITY_NOT_FOUND);
        }

        ShoesFindOneResponse shoes = shoesRepository.findOne(shoesId);

        if (DataUtils.isNull(shoes)) {
            throw new ResourceNotFoundException(MessageCode.ShoesDetail.ENTITY_NOT_FOUND);
        }

        int stt = 1;
        for (ShoesDetailReportDocResponse shoesDetail : shoesDetailReportDocResponses) {
            shoesDetail.setStt(stt++);
        }

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        InputStream inputStream = getClass().getResourceAsStream("/templates/doc/ShoesDetail_Report.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);

        Map<String, Object> parameters = new HashMap<>();

        parameters.put("code", shoes.getCode());
        parameters.put("name", shoes.getName());
        parameters.put("brand", shoes.getBrandName());
        parameters.put("origin", shoes.getOriginName());
        parameters.put("designStyle", shoes.getDesignStyleName());
        parameters.put("skinType", shoes.getSkinTypeName());
        parameters.put("toe", shoes.getSkinTypeName());
        parameters.put("sole", shoes.getSoleName());
        parameters.put("lining", shoes.getLiningName());
        parameters.put("cushion", shoes.getCushionName());
        parameters.put("imgURI", shoes.getImgURI());

        parameters.put("logoPath", getClass().getResourceAsStream("/templates/image/logo.png"));
        parameters.put("nameUser", nameUser);
        parameters.put("email", email);
        parameters.put("timeNow", timeNow);
        parameters.put("dataTable", new JRBeanCollectionDataSource(request.getShoesDetailReportDocResponses()));

        FileResponseDTO fileResponseDTO = new FileResponseDTO(JasperUtils.exportJasperReportToDoc(jasperReport, parameters, bos), "reportByShoesIdToDoc.docx");

        List<String> emails = accountRepository.getEmailAdmin();

        if (DataUtils.isEmpty(emails)) {
            throw new ValidateException(MessageCode.Accounts.ADMIN_NOT_EXIST);
        }

        gmailUtils.sendEmail(subject, text, fileResponseDTO, emails);

        return ResponseDTO.success();

    }

    @SneakyThrows
    @Override
    public ResponseDTO reportAllToExcel() {

        List<ShoesDetailReportExcelResponse> reportResponses = shoesDetailRepositoty.getAllShoesDetailReport(ShoesStatus.OUT_OF_STOCK.getValue());

        if (DataUtils.isEmpty(reportResponses)) {
            throw new ResourceNotFoundException(MessageCode.ShoesDetail.ENTITY_NOT_FOUND);
        }

        FileResponseDTO fileResponseDTO = ExelExportUtils.exportToExcel(Arrays.asList(HeaderTable.SHOES_DETAILS_REPORT), reportResponses, "Shoes_Report.xlsx", "Sheet1");

        List<String> emails = accountRepository.getEmailAdmin();

        if(DataUtils.isEmpty(emails)){
            throw new ResourceNotFoundException(MessageCode.Accounts.ADMIN_NOT_EXIST);
        }

        gmailUtils.sendEmail(subject, text, fileResponseDTO, emails);

        return ResponseDTO.success();
    }

    @SneakyThrows
    private void checkIdsNotExist(List<Long> colorIds, List<Long> sizeIds) {

        List<Color> colors = colorRepository.getExistByIds(colorIds);

        if (colors.size() != colorIds.size()) {
            throw new ResourceNotFoundException(MessageCode.Commom.NOT_EXIST, MessageCode.Color.ENTITY);
        }

        List<Size> sizes = sizeRepository.getExistByIds(sizeIds);

        if (sizes.size() != sizeIds.size()) {
            throw new ResourceNotFoundException(MessageCode.Commom.NOT_EXIST, MessageCode.Size.ENTITY);
        }

    }

    @SneakyThrows
    private void checkIdNotExist(Long colorId, Long sizeId) {

        Color color = colorRepository.getExistById(colorId).orElseThrow(
                () -> new ResourceNotFoundException(MessageCode.Commom.NOT_EXIST, MessageCode.Color.ENTITY)
        );

        Size size = sizeRepository.getExistById(sizeId).orElseThrow(
                () -> new ResourceNotFoundException(MessageCode.Commom.NOT_EXIST, MessageCode.Size.ENTITY)
        );

    }

    @SneakyThrows
    private ShoesDetail getExistById(Long id) {
        return shoesDetailRepositoty.getExistById(id).orElseThrow(
                () -> new ResourceNotFoundException(MessageCode.Commom.NOT_EXIST, MessageCode.ShoesDetail.ENTITY)
        );
    }

    @SneakyThrows
    private ShoesDetail getExistByQRCode(String qrCode) {
        return shoesDetailRepositoty.getExistByQRCode(qrCode).orElseThrow(
                () -> new ResourceNotFoundException(MessageCode.Commom.NOT_EXIST, MessageCode.ShoesDetail.ENTITY)
        );
    }

    @SneakyThrows
    private Shoes getExistByShoesId(Long shoesId) {
        return shoesRepository.getExistById(shoesId).orElseThrow(
                () -> new ResourceNotFoundException(MessageCode.Commom.NOT_EXIST, MessageCode.Shoes.ENTITY)
        );
    }

    private Long extractIdFromFileName(String fileName) {
        int lastIndexOfDot = fileName.lastIndexOf('.');

        int lastIndexOfUnderscore = fileName.lastIndexOf('_', lastIndexOfDot);

        if (lastIndexOfUnderscore != -1) {
            try {
                return Long.parseLong(fileName.substring(lastIndexOfUnderscore + 1, lastIndexOfDot));
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        return -1L;
    }

    private String extractName(String fileName) {
        // Tìm vị trí của dấu chấm (.)
        int dotIndex = fileName.lastIndexOf('.');

        // Tìm vị trí của dấu gạch dưới (_), bắt đầu từ vị trí cuối cùng của chuỗi
        int underscoreIndex = fileName.lastIndexOf('_', dotIndex);

        // Kiểm tra xem có dấu gạch dưới trong tên file hay không
        if (underscoreIndex != -1) {
            // Cắt bỏ phần mở rộng và ID và trả về kết quả
            return fileName.substring(0, underscoreIndex);
        } else {
            // Nếu không có dấu gạch dưới, chỉ cắt bỏ phần mở rộng
            return fileName.substring(0, dotIndex);
        }
    }

    @SneakyThrows
    private void validateImageDuplicate(List<MultipartFile> files) {
        List<String> originFilenames = files.stream()
                .map(file -> this.extractName(file.getOriginalFilename()))
                .collect(Collectors.toList());

        Set<String> fileDuplicate = new HashSet<>();
        Set<String> set = new HashSet<>();
        for (String element : originFilenames) {
            if (set.contains(element)) {
                fileDuplicate.add(element);
            }
            set.add(element);
        }

        if (CollectionUtils.isNotEmpty(fileDuplicate)) {
            throw new ValidateException(MessageCode.Image.IMAGE_DUPLICATE, String.join(", ", fileDuplicate));
        }
    }

}
