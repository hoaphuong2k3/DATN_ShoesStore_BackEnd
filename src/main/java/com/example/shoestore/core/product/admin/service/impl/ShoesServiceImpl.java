package com.example.shoestore.core.product.admin.service.impl;

import com.amazonaws.util.IOUtils;
import com.example.shoestore.core.common.dto.response.FileResponseDTO;
import com.example.shoestore.core.common.dto.response.ResponseDTO;
import com.example.shoestore.core.common.mapper.ShoesMapper;
import com.example.shoestore.core.product.admin.dto.ShoesDTO;
import com.example.shoestore.core.product.admin.dto.request.*;
import com.example.shoestore.core.product.admin.dto.response.ShoesDetailImportResponse;
import com.example.shoestore.core.product.admin.dto.response.ShoesFindOneResponse;
import com.example.shoestore.core.product.admin.dto.response.ShoesImportResponse;
import com.example.shoestore.core.product.admin.dto.response.ShoesSearchResponse;
import com.example.shoestore.core.product.admin.repository.*;
import com.example.shoestore.core.product.admin.service.ShoesService;
import com.example.shoestore.entity.*;
import com.example.shoestore.entity.base.PrimaryEntity;
import com.example.shoestore.infrastructure.constants.HeaderTable;
import com.example.shoestore.infrastructure.constants.IsDeleted;
import com.example.shoestore.infrastructure.constants.MessageCode;
import com.example.shoestore.infrastructure.exception.ResourceNotFoundException;
import com.example.shoestore.infrastructure.exception.ValidateException;
import com.example.shoestore.infrastructure.exception.ValidationError;
import com.example.shoestore.infrastructure.utils.*;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShoesServiceImpl implements ShoesService {

    private final ShoesMapper shoesMapper;

    private final AdminShoesRepository shoesRepository;

    private final AdminBrandsRepository brandsRepository;

    private final AdminOriginRepository originRepository;

    private final AdminDesignStyleRepository designStyleRepository;

    private final AdminSkinTypeRepository skinTypeRepository;

    private final AdminSoleRepository soleRepository;

    private final AdminLiningRepository liningRepository;

    private final AdminToeRepository toeRepository;

    private final AdminCushionRepository cushionRepository;

    private final AdminShoesDetailRepositoty shoesDetailRepositoty;

    private final FileValidateUtils fileValidateUtils;

    @SneakyThrows
    @Override
    public ResponseDTO create(ShoesCreateRequest createRequest, MultipartFile file) {

        fileValidateUtils.validateImageFile(file);

        Long brandId = createRequest.getBrandId();
        Long originId = createRequest.getOriginId();
        Long designStyleId = createRequest.getDesignStyleId();
        Long skinTypeId = createRequest.getSkinTypeId();
        Long soleId = createRequest.getSoleId();
        Long liningId = createRequest.getLiningId();
        Long toeId = createRequest.getToeId();
        Long cushionId = createRequest.getCushionId();
        String name = createRequest.getName();

        this.checkIdsNotExist(brandId, originId, designStyleId, skinTypeId, soleId, liningId, toeId, cushionId);

        this.validateShoesDuplicate(null, name, brandId, originId, designStyleId, skinTypeId, soleId, liningId, toeId, cushionId);

        DataFormatUtils.trimStringFields(createRequest);

        Shoes shoes = shoesMapper.createToEntity(createRequest, file);

        shoes = shoesRepository.save(shoes);

        ShoesDTO shoesDTO = shoesMapper.entityToDTO(shoes);

        return ResponseDTO.success(shoesDTO);
    }

    @SneakyThrows
    @Override
    public ResponseDTO update(Long id, ShoesUpdateRequest updateRequest, MultipartFile file, Boolean isChange) {

        Shoes shoes = this.getExistById(id);

        if (DataUtils.isNotNull(isChange)) {
            fileValidateUtils.validateImageFile(file);
        }

        Long brandId = updateRequest.getBrandId();
        Long originId = updateRequest.getOriginId();
        Long designStyleId = updateRequest.getDesignStyleId();
        Long skinTypeId = updateRequest.getSkinTypeId();
        Long soleId = updateRequest.getSoleId();
        Long liningId = updateRequest.getLiningId();
        Long toeId = updateRequest.getToeId();
        Long cushionId = updateRequest.getCushionId();
        String name = updateRequest.getName();

        this.checkIdsNotExist(brandId, originId, designStyleId, skinTypeId, soleId, liningId, toeId, cushionId);

        this.validateShoesDuplicate(id, name, brandId, originId, designStyleId, skinTypeId, soleId, liningId, toeId, cushionId);

        DataFormatUtils.trimStringFields(updateRequest);

        shoesMapper.updateToEntity(updateRequest, file, shoes, isChange);

        shoes = shoesRepository.save(shoes);

        ShoesDTO shoesDTO = shoesMapper.entityToDTO(shoes);

        return ResponseDTO.success(shoesDTO);

    }

    @SneakyThrows
    @Override
    public ResponseDTO search(ShoesSearchRequest searchRequest, Pageable pageable) {

        DataFormatUtils.trimStringFields(searchRequest);

        DataUtils.validateStringDate(searchRequest.getFromDateStr(), searchRequest.getToDateStr());

        DataUtils.validateFromDateAfterToDate(searchRequest.getFromDate(), searchRequest.getToDate());

        Page<ShoesSearchResponse> result = shoesRepository.search(searchRequest, pageable);

        if (result.isEmpty()) {
            throw new ValidateException(MessageCode.Commom.RESULT_EMPTY);
        }
        return ResponseDTO.success(result);
    }

    @Override
    public ResponseDTO findOne(Long id) {

        ShoesFindOneResponse shoesFindOneResponse = shoesRepository.findOne(id);

        return ResponseDTO.success(shoesFindOneResponse);
    }

    @SneakyThrows
    @Override
    public ResponseDTO deleteMultipart(List<Long> ids) {

        if (CollectionUtils.isEmpty(ids)) {
            throw new ValidateException(MessageCode.Commom.NOT_NULL, MessageCode.Shoes.ENTITY);
        }

        List<Shoes> shoes = shoesRepository.getExistByIds(ids);

        if (shoes.size() != ids.size()) {
            throw new ResourceNotFoundException(MessageCode.Commom.NOT_EXIST, MessageCode.Shoes.ENTITY);
        }

        List<String> nameByIds = shoesDetailRepositoty.getNameByShoesIds(ids);

        if (CollectionUtils.isNotEmpty(nameByIds)) {
            String nameError = "";
            if (nameByIds.size() > 1) {
                nameError = String.join(", ", nameByIds);
            }
            throw new ValidateException(MessageCode.Shoes.EXIST_DB_OTHER, nameError);
        }

        shoesRepository.setIsDelete(ids, IsDeleted.DELETED.getValue());

        return ResponseDTO.success();
    }

    @SneakyThrows
    @Override
    public FileResponseDTO exportExel(List<ShoesExportExcelRequest> shoesExport) {

        int stt = 1;
        for (ShoesExportExcelRequest shoesDetail : shoesExport) {
            shoesDetail.setStt(stt++);
        }

        FileResponseDTO fileResponseDTO = ExelExportUtils.exportToExcel(Arrays.asList(HeaderTable.SHOES), shoesExport, "Shoes_Export.xlsx", "Sheet1");

        return fileResponseDTO;
    }

    @SneakyThrows
    @Override
    public FileResponseDTO exportPDF(List<ShoesExportExcelRequest> shoesExport) {

        int stt = 1;
        for (ShoesExportExcelRequest shoesDetail : shoesExport) {
            shoesDetail.setStt(stt++);
        }

        FileResponseDTO fileResponseDTO = PDFUtils.exportToPDF(Arrays.asList(HeaderTable.SHOES), shoesExport, "Shoes_Export.pdf");

        return fileResponseDTO;
    }

    @SneakyThrows
    @Override
    public FileResponseDTO exportPattern() {

        InputStream inputStream = getClass().getResourceAsStream("/templates/xlsx/Shoes_Pattern.xlsx");

        byte[] fileBytes = IOUtils.toByteArray(inputStream);

        ByteArrayResource byteArrayResource = new ByteArrayResource(fileBytes);

        FileResponseDTO fileResponseDTO = new FileResponseDTO();
        fileResponseDTO.setByteArrayResource(byteArrayResource);
        fileResponseDTO.setFileName("Shoes_Pattern.xlsx");

        return fileResponseDTO;
    }

    private <T> ValidationError<T> findErrorByObject(List<ValidationError<T>> errorList, T objectToFind) {
        for (ValidationError<T> error : errorList) {
            if (Objects.equals(error.getObject(), objectToFind)) {
                return error;
            }
        }
        return null;
    }

    private <T> void validateAndAddError(List<ShoesImportRequest> shoesFromExcels,
                                         Set<Long> idSetInDB,
                                         Function<ShoesImportRequest, Long> idExtractor,
                                         List<ValidationError<ShoesImportRequest>> errorList,
                                         String errorMessage) {
        for (ShoesImportRequest shoesDetail : shoesFromExcels) {
            Long id = idExtractor.apply(shoesDetail);

            if (!idSetInDB.contains(id)) {
                ValidationError<ShoesImportRequest> existingError = findErrorByObject(errorList, shoesDetail);

                if (existingError == null) {
                    ValidationError<ShoesImportRequest> newError = new ValidationError<>();
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

        Pair<List<ShoesImportRequest>, List<ValidationError<ShoesImportRequest>>> resultPair = ExcelImportUtils.importFromExcel(fileExcel, ShoesImportRequest.class);
        // listAll
        List<ShoesImportRequest> shoesFromExcel = resultPair.getLeft();
        List<ValidationError<ShoesImportRequest>> errorList = resultPair.getRight();

        // validate Id exist in DB
        String errorBrandMess = MessageUtils.getMessage(MessageCode.Commom.NOT_EXIST, MessageUtils.getMessage(MessageCode.Brand.ENTITY));
        String errorOriginMess = MessageUtils.getMessage(MessageCode.Commom.NOT_EXIST, MessageUtils.getMessage(MessageCode.Origin.ENTITY));
        String errorDesignStyleMess = MessageUtils.getMessage(MessageCode.Commom.NOT_EXIST, MessageUtils.getMessage(MessageCode.DesignStyle.ENTITY));
        String errorSkinTypeMess = MessageUtils.getMessage(MessageCode.Commom.NOT_EXIST, MessageUtils.getMessage(MessageCode.SkinType.ENTITY));
        String errorSoleMess = MessageUtils.getMessage(MessageCode.Commom.NOT_EXIST, MessageUtils.getMessage(MessageCode.Sole.ENTITY));
        String errorLiningMess = MessageUtils.getMessage(MessageCode.Commom.NOT_EXIST, MessageUtils.getMessage(MessageCode.Lining.ENTITY));
        String errorToeMess = MessageUtils.getMessage(MessageCode.Commom.NOT_EXIST, MessageUtils.getMessage(MessageCode.Toe.ENTITY));
        String errorCushionMess = MessageUtils.getMessage(MessageCode.Commom.NOT_EXIST, MessageUtils.getMessage(MessageCode.Cushion.ENTITY));

        Set<Long> brandIdInRQ = shoesFromExcel.stream().map(ShoesImportRequest::getBrandId).collect(Collectors.toSet());
        Set<Long> originIdInRQ = shoesFromExcel.stream().map(ShoesImportRequest::getOriginId).collect(Collectors.toSet());
        Set<Long> designStyleIdInRQ = shoesFromExcel.stream().map(ShoesImportRequest::getDesignStyleId).collect(Collectors.toSet());
        Set<Long> skinTypeIdInRQ = shoesFromExcel.stream().map(ShoesImportRequest::getSkinTypeId).collect(Collectors.toSet());
        Set<Long> soleIdInRQ = shoesFromExcel.stream().map(ShoesImportRequest::getSoleId).collect(Collectors.toSet());
        Set<Long> liningIdInRQ = shoesFromExcel.stream().map(ShoesImportRequest::getLiningId).collect(Collectors.toSet());
        Set<Long> toeIdInRQ = shoesFromExcel.stream().map(ShoesImportRequest::getToeId).collect(Collectors.toSet());
        Set<Long> cushionIdInRQ = shoesFromExcel.stream().map(ShoesImportRequest::getCushionId).collect(Collectors.toSet());

        Set<Long> brandIdInDB = brandsRepository.getExistByIds(new ArrayList<>(brandIdInRQ)).stream().map(PrimaryEntity::getId).collect(Collectors.toSet());
        Set<Long> originIdInDB = originRepository.getExistByIds(new ArrayList<>(originIdInRQ)).stream().map(PrimaryEntity::getId).collect(Collectors.toSet());
        Set<Long> designStyleIdInDB = designStyleRepository.getExistByIds(new ArrayList<>(designStyleIdInRQ)).stream().map(PrimaryEntity::getId).collect(Collectors.toSet());
        Set<Long> skinTypeIdInDB = skinTypeRepository.getExistByIds(new ArrayList<>(skinTypeIdInRQ)).stream().map(PrimaryEntity::getId).collect(Collectors.toSet());
        Set<Long> soleIdInInDB = soleRepository.getExistByIds(new ArrayList<>(soleIdInRQ)).stream().map(PrimaryEntity::getId).collect(Collectors.toSet());
        Set<Long> liningIdIdInDB = liningRepository.getExistByIds(new ArrayList<>(liningIdInRQ)).stream().map(PrimaryEntity::getId).collect(Collectors.toSet());
        Set<Long> toeIdIdInDB = toeRepository.getExistByIds(new ArrayList<>(toeIdInRQ)).stream().map(PrimaryEntity::getId).collect(Collectors.toSet());
        Set<Long> cushionIdInDB = cushionRepository.getExistByIds(new ArrayList<>(cushionIdInRQ)).stream().map(PrimaryEntity::getId).collect(Collectors.toSet());

        validateAndAddError(shoesFromExcel, brandIdInDB, ShoesImportRequest::getBrandId, errorList, errorBrandMess);
        validateAndAddError(shoesFromExcel, originIdInDB, ShoesImportRequest::getOriginId, errorList, errorOriginMess);
        validateAndAddError(shoesFromExcel, designStyleIdInDB, ShoesImportRequest::getDesignStyleId, errorList, errorDesignStyleMess);
        validateAndAddError(shoesFromExcel, skinTypeIdInDB, ShoesImportRequest::getSkinTypeId, errorList, errorSkinTypeMess);
        validateAndAddError(shoesFromExcel, soleIdInInDB, ShoesImportRequest::getSoleId, errorList, errorSoleMess);
        validateAndAddError(shoesFromExcel, liningIdIdInDB, ShoesImportRequest::getLiningId, errorList, errorLiningMess);
        validateAndAddError(shoesFromExcel, toeIdIdInDB, ShoesImportRequest::getToeId, errorList, errorToeMess);
        validateAndAddError(shoesFromExcel, cushionIdInDB, ShoesImportRequest::getCushionId, errorList, errorCushionMess);

        // Lấy ra tất cả stt trong ShoesDetailImportRequest
        Set<Integer> sttSetInRequest = shoesFromExcel.stream()
                .map(ShoesImportRequest::getStt)
                .collect(Collectors.toSet());

        // Lấy ra tất cả stt trong errorList
        Set<Integer> sttSetInErrorList = errorList.stream()
                .map(error -> error.getObject().getStt())
                .collect(Collectors.toSet());

        // Tìm những stt có trong cả sttSetInRequest và sttSetInErrorList
        Set<Integer> commonSttSet = new HashSet<>(sttSetInRequest);
        commonSttSet.retainAll(sttSetInErrorList);

        // Loại bỏ các đối tượng có stt thuộc commonSttSet khỏi shoesDetailFromExcel
        shoesFromExcel.removeIf(detail -> commonSttSet.contains(detail.getStt()));

        Iterator<ShoesImportRequest> iterator = shoesFromExcel.iterator();

        while (iterator.hasNext()) {
            ShoesImportRequest shoesExcel = iterator.next();

            Long brandId = shoesExcel.getBrandId();
            Long originId = shoesExcel.getOriginId();
            Long designStyleId = shoesExcel.getDesignStyleId();
            Long skinTypeId = shoesExcel.getSkinTypeId();
            Long soleId = shoesExcel.getSoleId();
            Long liningId = shoesExcel.getLiningId();
            Long toeId = shoesExcel.getToeId();
            Long cushionId = shoesExcel.getCushionId();
            String name = shoesExcel.getName();

            Shoes shoes = shoesRepository.getExistByListId(null, name, brandId, originId, designStyleId, skinTypeId, soleId, liningId, toeId, cushionId);

            if (DataUtils.isNotNull(shoes)) {
                String errorMessage = MessageUtils.getMessage(MessageCode.Commom.EXISTED, MessageUtils.getMessage(MessageCode.Shoes.ENTITY));
                ValidationError<ShoesImportRequest> existingError = findErrorByObject(errorList, shoesExcel);

                if (existingError == null) {
                    ValidationError<ShoesImportRequest> newError = new ValidationError<>();
                    newError.setObject(shoesExcel);
                    newError.setErrors(new ArrayList<>());
                    newError.getErrors().add(errorMessage);
                    errorList.add(newError);
                } else {
                    existingError.getErrors().add(errorMessage);
                }

                // Xoá đối tượng trùng khỏi shoesFromExcel
                iterator.remove();
            }
        }

        shoesFromExcel.removeIf(detail -> commonSttSet.contains(detail.getStt()));

        List<Shoes> shoes = shoesMapper.importExcelsToEntities(shoesFromExcel);

        shoes = shoesRepository.saveAll(shoes);

        List<ShoesImportResponse> responseList = new ArrayList<>();

        // Duyệt qua từng ShoesDetailImportRequest trong errorList
        for (ValidationError<ShoesImportRequest> validationError : errorList) {
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

            ShoesImportResponse response = shoesMapper.toShoesDetailImportExcel(validationError.getObject(),validationError.getErrors().toString());
            responseList.add(response);

        }

        int stt = 1;
        for (ShoesImportResponse response : responseList) {
            response.setStt(stt++);
        }

        FileResponseDTO fileResponseDTO = ExelExportUtils.exportToExcel(Arrays.asList(HeaderTable.SHOES_IMPORT_RESPONSE), responseList, "Shoes_Import_Response.xlsx", "Sheet1");

        return fileResponseDTO;

    }

    @SneakyThrows
    private Shoes getExistById(Long id) {
        return shoesRepository.getExistById(id).orElseThrow(
                () -> new ResourceNotFoundException(MessageCode.Commom.NOT_EXIST, MessageCode.Shoes.ENTITY));

    }

    @SneakyThrows
    private void checkIdsNotExist(Long brandId, Long originId, Long designStyleId, Long skinTypeId, Long soleId, Long liningId, Long toeId, Long cushionId) {

        Brand brand = brandsRepository.getExistById(brandId).orElseThrow(
                () -> new ResourceNotFoundException(MessageCode.Commom.NOT_EXIST, MessageCode.Brand.ENTITY)
        );

        Origin origin = originRepository.getExistById(originId).orElseThrow(
                () -> new ResourceNotFoundException(MessageCode.Commom.NOT_EXIST, MessageCode.Origin.ENTITY)
        );

        DesignStyle designStyle = designStyleRepository.getExistById(designStyleId).orElseThrow(
                () -> new ResourceNotFoundException(MessageCode.Commom.NOT_EXIST, MessageCode.DesignStyle.ENTITY)
        );

        SkinType skinType = skinTypeRepository.getExistById(skinTypeId).orElseThrow(
                () -> new ResourceNotFoundException(MessageCode.Commom.NOT_EXIST, MessageCode.SkinType.ENTITY)
        );

        Sole sole = soleRepository.getExistById(soleId).orElseThrow(
                () -> new ResourceNotFoundException(MessageCode.Commom.NOT_EXIST, MessageCode.Sole.ENTITY)
        );

        Lining lining = liningRepository.getExistById(liningId).orElseThrow(
                () -> new ResourceNotFoundException(MessageCode.Commom.NOT_EXIST, MessageCode.Lining.ENTITY)
        );

        Toe toe = toeRepository.getExistById(toeId).orElseThrow(
                () -> new ResourceNotFoundException(MessageCode.Commom.NOT_EXIST, MessageCode.Toe.ENTITY)
        );

        Cushion cushion = cushionRepository.getExistById(cushionId).orElseThrow(
                () -> new ResourceNotFoundException(MessageCode.Commom.NOT_EXIST, MessageCode.Cushion.ENTITY)
        );
    }

    @SneakyThrows
    private void validateShoesDuplicate(Long id, String name, Long brandId, Long originId, Long designStyleId, Long skinTypeId, Long soleId, Long liningId, Long toeId, Long cushionId) {
        Shoes shoes = shoesRepository.getExistByListId(id, name, brandId, originId, designStyleId, skinTypeId, soleId, liningId, toeId, cushionId);
        if (DataUtils.isNotNull(shoes)) {
            throw new ValidateException(MessageCode.Commom.EXISTED, MessageCode.Shoes.ENTITY);
        }
    }


}
