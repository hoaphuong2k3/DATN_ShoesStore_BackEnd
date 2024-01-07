package com.example.shoestore.core.product.admin.repository.custom.impl;

import com.example.shoestore.core.product.admin.dto.request.ShoesDetailSearchRequest;
import com.example.shoestore.core.product.admin.dto.response.ShoesDetailReportExcelResponse;
import com.example.shoestore.core.product.admin.dto.response.ShoesDetailSearchResponse;
import com.example.shoestore.core.product.admin.repository.custom.AdminShoesDetailRepositotyCustom;
import com.example.shoestore.infrastructure.utils.DataBaseUtils;
import com.example.shoestore.infrastructure.utils.DataUtils;
import io.micrometer.common.util.StringUtils;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class AdminShoesDetailRepositotyCustomImpl implements AdminShoesDetailRepositotyCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<ShoesDetailSearchResponse> search(Long idShoes, ShoesDetailSearchRequest searchRequest, Pageable pageable) {
        Query queryCount = getQuerySearch(idShoes, searchRequest, null);
        Long count = Long.valueOf(queryCount.getResultList().size());
        List<ShoesDetailSearchResponse> result = new ArrayList<>();
        if (count > 0) {
            Query query = getQuerySearch(idShoes, searchRequest, pageable);
            result = query.getResultList();
        }
        return new PageImpl<>(result, pageable, count);
    }

    @Override
    public List<ShoesDetailReportExcelResponse> getAllShoesDetailReport(Integer status) {
        String query = "SELECT " +
                "    ROW_NUMBER() OVER (ORDER BY sz.name) AS stt, " +
                "    sh.code AS shoesCode, " +
                "    shdt.code , " +
                "    sz.name AS size, " +
                "    cl.name AS color, " +
                "    shdt.price , " +
                "    shdt.created_by , " +
                "    shdt.created_time , " +
                "    shdt.updated_by , " +
                "    shdt.updated_time  " +
                "FROM " +
                "    shoes_detail shdt " +
                "INNER JOIN " +
                "    shoes sh ON shdt.shoes_id = sh.id " +
                "INNER JOIN " +
                "    color cl ON shdt.color_id = cl.id AND cl.is_deleted = 0 " +
                "INNER JOIN " +
                "    size sz ON shdt.size_id = sz.id AND sz.is_deleted = 0 " +
                "WHERE shdt.status = :status AND shdt.quantity = 0 AND shdt.is_deleted = 0";

        return entityManager.createNativeQuery(query, "StaffShoesDetailReportMapping")
                .setParameter("status", status)
                .getResultList();
    }

    public Query getQuerySearch(Long id, ShoesDetailSearchRequest searchRequest, Pageable pageable) {

        StringBuilder queryBuilder = new StringBuilder();

        queryBuilder.append(" SELECT shdt.id,sh.name,shdt.code, cl.name as color, sz.name as size, ")
                .append(" shdt.price,shdt.discount_price,shdt.quantity,shdt.status,shdt.created_by, shdt.created_time,qr_code_uri ")
                .append(" FROM shoes_detail shdt")
                .append(" LEFT JOIN shoes sh on sh.id = shdt.shoes_id ")
                .append(" LEFT JOIN color cl on cl.id = shdt.color_id ")
                .append(" LEFT JOIN size sz on sz.id = shdt.size_id ")
                .append(" WHERE shdt.is_deleted = 0 ");

        Map<String, Object> params = new HashMap<>();

        appendQuery(queryBuilder, id, searchRequest, params);

        if (DataUtils.isNotNull(pageable)) {
            appendQuerySort(queryBuilder, ShoesDetailSearchRequest.shoesDetailField(), pageable.getSort());
        }

        Query query = entityManager.createNativeQuery(queryBuilder.toString(), "AdminSearchShoesDetailResultMapping");

        if (DataUtils.isNotEmpty(params)) {
            params.forEach(query::setParameter);
        }

        if (DataUtils.isNotNull(pageable)) {
            query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
            query.setMaxResults(pageable.getPageSize());
        }
        return query;
    }

    public void appendQuery(StringBuilder queryBuilder, Long idShoes, ShoesDetailSearchRequest searchRequest, Map<String, Object> params) {

        if (DataUtils.isNotNull(idShoes)) {
            queryBuilder.append(" AND shdt.shoes_id = :id ");
            params.put("id", idShoes);
        }

        if (DataUtils.isNotBlank(searchRequest.getCode())) {
            queryBuilder.append(" AND shdt.code LIKE :code ");
            params.put("code", DataBaseUtils.getLikeCondition(searchRequest.getCode()));
        }

        if (DataUtils.isNotNull(searchRequest.getSizeId())) {
            queryBuilder.append(" AND shdt.size_id = :sizeId ");
            params.put("sizeId", searchRequest.getSizeId());
        }

        if (DataUtils.isNotNull(searchRequest.getColorId())) {
            queryBuilder.append(" AND shdt.color_id = :colorId ");
            params.put("colorId", searchRequest.getColorId());
        }

        if (DataUtils.isNotNull(searchRequest.getFromPrice()) && DataUtils.isNotNull(searchRequest.getToPrice())) {
            queryBuilder.append(" AND shdt.price  BETWEEN :fromPrice AND :toPrice ");
            params.put("fromPrice", searchRequest.getFromPrice());
            params.put("toPrice", searchRequest.getToPrice());
        }

        if (DataUtils.isNotNull(searchRequest.getFromPrice()) && DataUtils.isNull(searchRequest.getToPrice())) {
            queryBuilder.append(" AND shdt.price >= :fromPrice ");
            params.put("fromPrice", searchRequest.getFromPrice());
        }

        if (DataUtils.isNotNull(searchRequest.getToPrice()) && DataUtils.isNull(searchRequest.getFromPrice())) {
            queryBuilder.append(" AND shdt.price <= :toPrice ");
            params.put("toPrice", searchRequest.getToPrice());
        }

        if (DataUtils.isNotNull(searchRequest.getFromQuantity()) && DataUtils.isNotNull(searchRequest.getToQuantity())) {
            queryBuilder.append(" AND shdt.quantity BETWEEN :fromQuantity AND :toQuantity ");
            params.put("fromQuantity", searchRequest.getFromQuantity());
            params.put("toQuantity", searchRequest.getToQuantity());
        }

        if (DataUtils.isNotNull(searchRequest.getFromQuantity()) && DataUtils.isNull(searchRequest.getToQuantity())) {
            queryBuilder.append(" AND shdt.quantity >= :fromQuantity ");
            params.put("fromQuantity", searchRequest.getFromQuantity());
        }

        if (DataUtils.isNotNull(searchRequest.getToQuantity()) && DataUtils.isNull(searchRequest.getFromQuantity())) {
            queryBuilder.append(" AND shdt.quantity <= :toQuantity ");
            params.put("toQuantity", searchRequest.getToQuantity());
        }

        if (DataUtils.isNotNull(searchRequest.getStatus())) {
            queryBuilder.append(" AND shdt.status = :status ");
            params.put("status", searchRequest.getStatus());
        }

        if (DataUtils.isNotNull(searchRequest.getFromDate()) && DataUtils.isNotNull(searchRequest.getToDate())) {
            queryBuilder.append(" AND shdt.created_time BETWEEN :fromDate AND :toDate ");
            params.put("fromDate", searchRequest.getFromDate());
            params.put("toDate", searchRequest.getToDate());
        }
        if (DataUtils.isNotNull(searchRequest.getFromDate()) && DataUtils.isNull(searchRequest.getToDate())) {
            queryBuilder.append(" AND shdt.created_time >= :fromDate ");
            params.put("fromDate", searchRequest.getFromDate());
        }
        if (DataUtils.isNotNull(searchRequest.getToDate()) && DataUtils.isNull(searchRequest.getFromDate())) {
            queryBuilder.append(" AND shdt.created_time <= :toDate ");
            params.put("toDate", searchRequest.getToDate());
        }
        if (StringUtils.isNotBlank(searchRequest.getCreatedBy())) {
            queryBuilder.append(" AND shdt.created_by LIKE :createdBy ");
            params.put("createdBy", DataBaseUtils.getLikeCondition(searchRequest.getCreatedBy()));
        }
        if(StringUtils.isNotBlank(searchRequest.getQrCode())){
            queryBuilder.append(" AND shdt.qr_code_uri LIKE :qrCode ");
            params.put("qrCode", DataBaseUtils.getLikeCondition(searchRequest.getQrCode()));
        }
    }

    public void appendQuerySort(StringBuilder query, Map<String, String> fieldSorts, Sort sort) {
        if (sort.isEmpty()) {
            query.append(" ORDER BY shdt.code ASC ");
        } else {
            query.append(" ORDER BY ");
            sort.forEach(value -> {
                String sortProperty = value.getProperty();
                if (fieldSorts.containsKey(sortProperty)) {
                    query.append(fieldSorts.get(sortProperty)).append(" ").append(value.getDirection().name()).append(",");
                }
            });
            query.deleteCharAt(query.length() - 1);
        }
    }
}
