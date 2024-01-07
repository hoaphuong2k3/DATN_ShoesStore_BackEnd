package com.example.shoestore.core.product.admin.repository.custom.impl;

import com.example.shoestore.core.product.admin.dto.request.ShoesSearchRequest;
import com.example.shoestore.core.product.admin.dto.response.ShoesSearchResponse;
import com.example.shoestore.core.product.admin.repository.custom.AdminShoesRepositoryCustom;
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
public class AdminShoesRepositoryCustomImpl implements AdminShoesRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<ShoesSearchResponse> search(ShoesSearchRequest searchRequest, Pageable pageable) {
        Query queryCount = getQuerySearch(searchRequest, null);
        Long count = Long.valueOf(queryCount.getResultList().size());
        List<ShoesSearchResponse> result = new ArrayList<>();
        if (count > 0) {
            Query query = getQuerySearch(searchRequest, pageable);
            result = query.getResultList();
        }
        return new PageImpl<>(result, pageable, count);
    }

    public Query getQuerySearch(ShoesSearchRequest searchRequest, Pageable pageable) {

        StringBuilder queryBuilder = new StringBuilder();

        queryBuilder.append(" SELECT sh.id,sh.code,sh.name,br.name as brand,o.name as origin,dsst.name as designStyle, ")
                .append(" skt.name as skinType,sl.name as sole,ln.name as lining,t.name as toe,cs.name as cushion, ")
                .append(" sh.img_name,sh.img_uri, ")
                .append(" ( SELECT MAX(price) FROM shoes_detail WHERE shoes_id = sh.id AND is_deleted = 0 ) AS priceMax, ")
                .append(" ( SELECT MIN(price) FROM shoes_detail WHERE shoes_id = sh.id AND is_deleted = 0 ) AS priceMin, ")
                .append(" ( SELECT MAX(discount_price) FROM shoes_detail WHERE shoes_id = sh.id AND is_deleted = 0 AND status = 1) AS discountPriceMax, ")
                .append(" ( SELECT MIN(discount_price) FROM shoes_detail WHERE shoes_id = sh.id AND is_deleted = 0 AND status = 1) AS discountPriceMin, ")
                .append(" ( SELECT SUM(quantity) FROM shoes_detail WHERE shoes_id = sh.id AND is_deleted = 0 ) AS totalQuantity, ")
                .append(" ( SELECT COUNT(*) FROM shoes_detail WHERE shoes_id = sh.id AND is_deleted = 0 ) AS totalRecord, ")
                .append(" sh.created_by,sh.created_time FROM shoes sh  ")
                .append(" LEFT JOIN shoes_detail shdt ON sh.id = shdt.shoes_id ")
                .append(" LEFT JOIN brand br ON br.id = sh.brand_id ")
                .append(" LEFT JOIN origin o ON o.id = sh.origin_id ")
                .append(" LEFT JOIN design_style dsst ON dsst.id = sh.design_style_id ")
                .append(" LEFT JOIN skin_type skt ON skt.id = sh.skin_type_id ")
                .append(" LEFT JOIN sole sl ON sl.id = sh.sole_id ")
                .append(" LEFT JOIN lining ln ON ln.id = sh.lining_id ")
                .append(" LEFT JOIN toe t ON t.id = sh.toe_id ")
                .append(" LEFT JOIN cushion cs ON cs.id = sh.cushion_id ")
                .append(" WHERE sh.is_deleted = 0 ");

        Map<String, Object> params = new HashMap<>();

        appendFirstQuery(queryBuilder, searchRequest, params);

        queryBuilder.append(" GROUP BY sh.id ");
        queryBuilder.append(" HAVING 1 = 1 ");
        appendQuerySecond(queryBuilder, searchRequest, params);

        if (DataUtils.isNotNull(pageable)) {
            appendQuerySort(queryBuilder, ShoesSearchRequest.shoesField(), pageable.getSort());
        }

        Query query = entityManager.createNativeQuery(queryBuilder.toString(), "AdminSearchShoesResultMapping");

        if (DataUtils.isNotEmpty(params)) {
            params.forEach(query::setParameter);
        }

        if (DataUtils.isNotNull(pageable)) {
            query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
            query.setMaxResults(pageable.getPageSize());
        }
        return query;
    }

    public void appendFirstQuery(StringBuilder queryBuilder, ShoesSearchRequest searchRequest, Map<String, Object> params) {

        if (DataUtils.isNotBlank(searchRequest.getCodeOrName())) {
            queryBuilder.append(" AND (sh.code LIKE :codeOrName OR sh.name LIKE :codeOrName) ");
            params.put("codeOrName", DataBaseUtils.getLikeCondition(searchRequest.getCodeOrName()));
        }

        if (DataUtils.isNotNull(searchRequest.getFromDate()) && DataUtils.isNotNull(searchRequest.getToDate())) {
            queryBuilder.append(" AND sh.created_time BETWEEN :fromDate AND :toDate ");
            params.put("fromDate", searchRequest.getFromDate());
            params.put("toDate", searchRequest.getToDate());
        }
        if (DataUtils.isNotNull(searchRequest.getFromDate()) && DataUtils.isNull(searchRequest.getToDate())) {
            queryBuilder.append(" AND sh.created_time >= :fromDate ");
            params.put("fromDate", searchRequest.getFromDate());
        }
        if (DataUtils.isNotNull(searchRequest.getToDate()) && DataUtils.isNull(searchRequest.getFromDate())) {
            queryBuilder.append(" AND sh.created_time <= :toDate ");
            params.put("toDate", searchRequest.getToDate());
        }
        if (StringUtils.isNotBlank(searchRequest.getCreatedBy())) {
            queryBuilder.append(" AND sh.created_by LIKE :createdBy ");
            params.put("createdBy", DataBaseUtils.getLikeCondition(searchRequest.getCreatedBy()));
        }
        if (DataUtils.isNotNull(searchRequest.getDesignStyleId())) {
            queryBuilder.append(" AND sh.design_style_id = :designStyleId ");
            params.put("designStyleId", searchRequest.getDesignStyleId());
        }
        if (DataUtils.isNotNull(searchRequest.getBrandId())) {
            queryBuilder.append(" AND sh.brand_id = :brandId ");
            params.put("brandId", searchRequest.getBrandId());
        }
        if (DataUtils.isNotNull(searchRequest.getSkinTypeId())) {
            queryBuilder.append(" AND sh.skin_type_id = :skinTypeId ");
            params.put("skinTypeId", searchRequest.getSkinTypeId());
        }
        if (DataUtils.isNotNull(searchRequest.getSoleId())) {
            queryBuilder.append(" AND sh.sole_id = :soleId ");
            params.put("soleId", searchRequest.getSoleId());
        }
        if (DataUtils.isNotNull(searchRequest.getLiningId())) {
            queryBuilder.append(" AND sh.lining_id = :liningId ");
            params.put("liningId", searchRequest.getLiningId());
        }
        if (DataUtils.isNotNull(searchRequest.getOriginId())) {
            queryBuilder.append(" AND sh.origin_id = :originId ");
            params.put("originId", searchRequest.getOriginId());
        }
        if (DataUtils.isNotNull(searchRequest.getToeId())) {
            queryBuilder.append(" AND sh.toe_id = :toeId ");
            params.put("toeId", searchRequest.getToeId());
        }
        if (DataUtils.isNotNull(searchRequest.getCushionId())) {
            queryBuilder.append(" AND sh.cushion_id = :cushionId ");
            params.put("cushionId", searchRequest.getCushionId());
        }
    }

    public void appendQuerySecond(StringBuilder queryBuilder, ShoesSearchRequest searchRequest, Map<String, Object> params) {

        if (DataUtils.isNotNull(searchRequest.getFromPrice()) && DataUtils.isNotNull(searchRequest.getToPrice())) {
            queryBuilder.append(" AND discountPriceMin >= :fromPrice AND discountPriceMin <= :toPrice ");
            params.put("fromPrice", searchRequest.getFromPrice());
            params.put("toPrice", searchRequest.getToPrice());
        }

        if (DataUtils.isNotNull(searchRequest.getFromPrice()) && DataUtils.isNull(searchRequest.getToPrice())) {
            queryBuilder.append(" AND discountPriceMin >= :fromPrice ");
            params.put("fromPrice", searchRequest.getFromPrice());
        }

        if (DataUtils.isNotNull(searchRequest.getToPrice()) && DataUtils.isNull(searchRequest.getFromPrice())) {
            queryBuilder.append(" AND discountPriceMin <= :toPrice ");
            params.put("toPrice", searchRequest.getToPrice());
        }

        if (DataUtils.isNotNull(searchRequest.getFromQuantity()) && DataUtils.isNotNull(searchRequest.getToQuantity())) {
            queryBuilder.append(" AND totalQuantity BETWEEN :fromQuantity AND :toQuantity ");
            params.put("fromQuantity", searchRequest.getFromQuantity());
            params.put("toQuantity", searchRequest.getToQuantity());
        }

        if (DataUtils.isNotNull(searchRequest.getFromQuantity()) && DataUtils.isNull(searchRequest.getToQuantity())) {
            queryBuilder.append(" AND totalQuantity >= :fromQuantity ");
            params.put("fromQuantity", searchRequest.getFromQuantity());
        }

        if (DataUtils.isNotNull(searchRequest.getToQuantity()) && DataUtils.isNull(searchRequest.getFromQuantity())) {
            queryBuilder.append(" AND totalQuantity <= :toQuantity ");
            params.put("toQuantity", searchRequest.getToQuantity());
        }
    }


    public void appendQuerySort(StringBuilder query, Map<String, String> fieldSorts, Sort sort) {
        if (sort.isEmpty()) {
            query.append(" ORDER BY sh.updated_time DESC ");
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
