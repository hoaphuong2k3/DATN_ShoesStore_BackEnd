package com.example.shoestore.core.product.user.repository;

import com.example.shoestore.core.product.user.dto.response.UserShoesDetailResponse;
import com.example.shoestore.core.product.user.dto.response.UserShoesSearchByIdResponse;
import com.example.shoestore.core.product.user.repository.custom.UserShoesRepositoryCustom;
import com.example.shoestore.repository.ShoesRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserShoesRepository extends ShoesRepository, UserShoesRepositoryCustom {

    @Query(nativeQuery = true, value = """
            SELECT sh.id,sh.code,sh.name,br.name as brand,o.name as origin,dsst.name as designStyle,
            skt.name as skinType,sl.name as sole,ln.name as lining,t.name as toe,cs.name as cushion,
            sh.img_name,sh.img_uri,
            ( SELECT MAX(price) FROM shoes_detail WHERE shoes_id = sh.id AND is_deleted = 0 AND status = 1) AS priceMax,
            ( SELECT MIN(price) FROM shoes_detail WHERE shoes_id = sh.id AND is_deleted = 0 AND status = 1) AS priceMin,
            ( SELECT MAX(discount_price) FROM shoes_detail WHERE shoes_id = sh.id AND is_deleted = 0 AND status = 1) AS discountPriceMax,
            ( SELECT MIN(discount_price) FROM shoes_detail WHERE shoes_id = sh.id AND is_deleted = 0 AND status = 1) AS discountPriceMin,
            sh.img_uri as imgURI ,sh.img_name as imgName,
            sh.created_by,sh.created_time FROM shoes sh 
            LEFT JOIN shoes_detail shdt ON sh.id = shdt.shoes_id
            LEFT JOIN brand br ON br.id = sh.brand_id
            LEFT JOIN origin o ON o.id = sh.origin_id
            LEFT JOIN design_style dsst ON dsst.id = sh.design_style_id
            LEFT JOIN skin_type skt ON skt.id = sh.skin_type_id
            LEFT JOIN sole sl ON sl.id = sh.sole_id
            LEFT JOIN lining ln ON ln.id = sh.lining_id
            LEFT JOIN toe t ON t.id = sh.toe_id
            LEFT JOIN cushion cs ON cs.id = sh.cushion_id
            WHERE sh.is_deleted = 0 AND sh.id = :id
            GROUP BY sh.id
            HAVING 1 = 1 AND priceMax IS NOT NULL OR priceMin IS NOT NULL
             """)
    UserShoesSearchByIdResponse getOneCustom(@Param("id") Long id);

    @Query(nativeQuery = true, value = """
            SELECT shdt.id,sh.name,br.name as brand,o.name as origin,dsst.name as designStyle,
            skt.name as skinType,sl.name as sole,ln.name as lining,t.name as toe,cs.name as cushion,
            cl.name as color, sz.name as size,
            shdt.price,shdt.discount_price as discountPrice,shdt.quantity, sh.description,dc.sale_percent as salePercent\s
            FROM shoes_detail shdt
            LEFT JOIN shoes sh ON sh.id = shdt.shoes_id
            LEFT JOIN brand br ON br.id = sh.brand_id
            LEFT JOIN origin o ON o.id = sh.origin_id
            LEFT JOIN design_style dsst ON dsst.id = sh.design_style_id
            LEFT JOIN skin_type skt ON skt.id = sh.skin_type_id
            LEFT JOIN sole sl ON sl.id = sh.sole_id
            LEFT JOIN lining ln ON ln.id = sh.lining_id
            LEFT JOIN toe t ON t.id = sh.toe_id
            LEFT JOIN cushion cs ON cs.id = sh.cushion_id
            LEFT JOIN color cl on cl.id = shdt.color_id
            LEFT JOIN size sz on sz.id = shdt.size_id
            LEFT JOIN discounts dc ON shdt.promo_id = dc.id
            WHERE sh.id = :shoesId
            LIMIT 1
                    """)
    UserShoesDetailResponse getShoesDetailIndexOneByShoesId(@Param("shoesId") Long shoesId);

    @Query(nativeQuery = true, value = """
            WITH ProductSales AS (
                SELECT
                    sh.id,
                    sh.code,
                    sh.name,
                    COUNT(DISTINCT cod.client_id) AS totalClients,
                    ROW_NUMBER() OVER (ORDER BY COUNT(DISTINCT cod.client_id) DESC) AS row_num
                FROM
                    shoes sh 
                LEFT JOIN
                    shoes_detail shdt ON sh.id = shdt.shoes_id
                LEFT JOIN
                    order_detail od ON od.shoes_details_id = shdt.id
                LEFT JOIN
                    customer_order cod ON cod.id = od.order_id
                WHERE
                    sh.is_deleted = 0 and  sh.is_deleted = 0 
                GROUP BY
                    sh.id
            )
            SELECT distinct sh.id,sh.code,sh.name,br.name as brand,o.name as origin,dsst.name as designStyle,
            skt.name as skinType,sl.name as sole,ln.name as lining,t.name as toe,cs.name as cushion,
            sh.img_uri as imgURI ,sh.img_name as imgName,
            ( SELECT MAX(price) FROM shoes_detail WHERE shoes_id = sh.id AND is_deleted = 0 AND status = 1) AS priceMax,
            ( SELECT MIN(price) FROM shoes_detail WHERE shoes_id = sh.id AND is_deleted = 0 AND status = 1) AS priceMin,
            ( SELECT MAX(discount_price) FROM shoes_detail WHERE shoes_id = sh.id AND is_deleted = 0 AND status = 1) AS discountPriceMax,
            ( SELECT MIN(discount_price) FROM shoes_detail WHERE shoes_id = sh.id AND is_deleted = 0 AND status = 1) AS discountPriceMin,
            sh.created_by,
            sh.created_time, ps.totalClients FROM ProductSales ps
            JOIN shoes sh ON ps.id = sh.id
            LEFT JOIN shoes_detail shdt ON sh.id = shdt.shoes_id 
            LEFT JOIN brand br ON br.id = sh.brand_id
            LEFT JOIN origin o ON o.id = sh.origin_id
            LEFT JOIN design_style dsst ON dsst.id = sh.design_style_id
            LEFT JOIN skin_type skt ON skt.id = sh.skin_type_id
            LEFT JOIN sole sl ON sl.id = sh.sole_id
            LEFT JOIN lining ln ON ln.id = sh.lining_id
            LEFT JOIN toe t ON t.id = sh.toe_id
            LEFT JOIN cushion cs ON cs.id = sh.cushion_id
            WHERE
                ps.row_num <= 4
            ORDER BY
                ps.totalClients DESC
             """)
    List<UserShoesSearchByIdResponse> getTop4Sell();

    @Query(nativeQuery = true, value = """
            WITH ProductSales AS (
                SELECT
                    sh.id,
                    sh.code,
                    sh.name,
                    br.name as brand,
                    o.name as origin,
                    dsst.name as designStyle,
                    skt.name as skinType,
                    sl.name as sole,
                    ln.name as lining,
                    t.name as toe,
                    cs.name as cushion,
                    sh.img_uri as imgURI,
                    sh.img_name as imgName,
                    (
                        SELECT MAX(price)
                        FROM shoes_detail
                        WHERE shoes_id = sh.id AND is_deleted = 0 AND status = 1
                    ) AS priceMax,
                    (
                        SELECT MIN(price)
                        FROM shoes_detail
                        WHERE shoes_id = sh.id AND is_deleted = 0 AND status = 1
                    ) AS priceMin,
                    (
                        SELECT MAX(discount_price)
                        FROM shoes_detail
                        WHERE shoes_id = sh.id AND is_deleted = 0 AND status = 1
                    ) AS discountPriceMax,
                    (
                        SELECT MIN(discount_price)
                        FROM shoes_detail
                        WHERE shoes_id = sh.id AND is_deleted = 0 AND status = 1
                    ) AS discountPriceMin,
                    sh.created_by,
                    sh.created_time,
                    SUM(od.quantity) AS totalSold,
                    ROW_NUMBER() OVER (ORDER BY SUM(od.quantity) DESC) AS row_num
                FROM
                    shoes sh 
                LEFT JOIN
                    shoes_detail shdt ON sh.id = shdt.shoes_id
                LEFT JOIN
                    order_detail od ON od.shoes_details_id = shdt.id
                LEFT JOIN
                    brand br ON br.id = sh.brand_id
                LEFT JOIN
                    origin o ON o.id = sh.origin_id
                LEFT JOIN
                    design_style dsst ON dsst.id = sh.design_style_id
                LEFT JOIN
                    skin_type skt ON skt.id = sh.skin_type_id
                LEFT JOIN
                    sole sl ON sl.id = sh.sole_id
                LEFT JOIN
                    lining ln ON ln.id = sh.lining_id
                LEFT JOIN
                    toe t ON t.id = sh.toe_id
                LEFT JOIN
                    cushion cs ON cs.id = sh.cushion_id
                WHERE shdt.status = 1 
                and   sh.is_deleted = 0 
                GROUP BY
                    sh.id
            )
            SELECT
                id,
                code,
                name,
                brand,
                origin,
                designStyle,
                skinType,
                sole,
                lining,
                toe,
                cushion,
                imgURI,
                imgName,
                priceMax,
                priceMin,
                discountPriceMax,
                discountPriceMin,
                created_by,
                created_time,
                totalSold
            FROM
                ProductSales
            WHERE
                row_num <= 4 and priceMax is not null 
            ORDER BY
                totalSold DESC
             """)
    List<UserShoesSearchByIdResponse> getTop4SellLot();


}
