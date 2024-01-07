package com.example.shoestore.entity;

import com.example.shoestore.entity.base.AuditEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "image")
@Builder
public class Image extends AuditEntity {

    @Column(name = "shoes_detail_id")
    private Long shoesDetailId;

    @Column(name = "image_name")
    private String imgName;

    @Column(name = "image_uri")
    private String imgURI;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

}
