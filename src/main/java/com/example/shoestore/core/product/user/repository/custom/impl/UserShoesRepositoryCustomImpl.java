package com.example.shoestore.core.product.user.repository.custom.impl;

import com.example.shoestore.core.product.admin.dto.request.ShoesSearchRequest;
import com.example.shoestore.core.product.user.dto.request.UserShoesSearchRequest;
import com.example.shoestore.core.product.user.dto.response.UserShoesSearchResponse;
import com.example.shoestore.core.product.user.repository.custom.UserShoesRepositoryCustom;
import com.example.shoestore.infrastructure.utils.DataBaseUtils;
import com.example.shoestore.infrastructure.utils.DataUtils;
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
public class UserShoesRepositoryCustomImpl implements UserShoesRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<UserShoesSearchResponse> search(UserShoesSearchRequest searchRequest,Pageable pageable) {
        Query queryCount = getQuerySearch(searchRequest, null);
        Long count = Long.valueOf(queryCount.getResultList().size());
        List<UserShoesSearchResponse> result = new ArrayList<>();
        if (count > 0) {
            Query query = getQuerySearch(searchRequest, pageable);
            result = query.getResultList();
        }
        return new PageImpl<>(result, pageable, count);    }

    public Query getQuerySearch(UserShoesSearchRequest searchRequest, Pageable pageable) {

        StringBuilder queryBuilder = new StringBuilder();

        queryBuilder.append(" SELECT sh.id,sh.code,sh.name,br.name as brand,o.name as origin,dsst.name as designStyle, ")
                .append(" skt.name as skinType,sl.name as sole,ln.name as lining,t.name as toe,cs.name as cushion, ")
                .append(" sh.img_name,sh.img_uri, ")
                .append(" ( SELECT MAX(price) FROM shoes_detail WHERE shoes_id = sh.id AND is_deleted = 0 AND status = 1) AS priceMax, ")
                .append(" ( SELECT MIN(price) FROM shoes_detail WHERE shoes_id = sh.id AND is_deleted = 0 AND status = 1) AS priceMin, ")
                .append(" ( SELECT MAX(discount_price) FROM shoes_detail WHERE shoes_id = sh.id AND is_deleted = 0 AND status = 1) AS discountPriceMax, ")
                .append(" ( SELECT MIN(discount_price) FROM shoes_detail WHERE shoes_id = sh.id AND is_deleted = 0 AND status = 1) AS discountPriceMin, ")
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

        appendQuery(queryBuilder, searchRequest, params);

        queryBuilder.append(" GROUP BY sh.id ");
        queryBuilder.append(" HAVING 1 = 1 AND ( priceMax IS NOT NULL OR priceMin IS NOT NULL) ");

        appendQuery2(queryBuilder, searchRequest, params);

        if (DataUtils.isNotNull(pageable)) {
            appendQuerySort(queryBuilder, ShoesSearchRequest.shoesField(), pageable.getSort());
        }

        Query query = entityManager.createNativeQuery(queryBuilder.toString(), "UserSearchShoesResultMapping");

        if (DataUtils.isNotEmpty(params)) {
            params.forEach(query::setParameter);
        }

        if (DataUtils.isNotNull(pageable)) {
            query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
            query.setMaxResults(pageable.getPageSize());
        }
        return query;
    }

    public void appendQuery(StringBuilder queryBuilder, UserShoesSearchRequest searchRequest, Map<String, Object> params) {

        if (DataUtils.isNotBlank(searchRequest.getDataInput())) {
            queryBuilder.append(" AND ( sh.code like :dataInput  OR sh.name like :dataInput ) ");
            params.put("dataInput", DataBaseUtils.getLikeCondition(searchRequest.getDataInput()));
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

    public void appendQuery2(StringBuilder queryBuilder, UserShoesSearchRequest searchRequest, Map<String, Object> params) {

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

    }


    public void appendQuerySort(StringBuilder query, Map<String, String> fieldSorts, Sort sort) {
        if (sort.isEmpty()) {
            query.append(" ORDER BY sh.name ASC ");
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
