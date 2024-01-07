package com.example.shoestore.entity.base;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class AuditEntity extends PrimaryEntity{

    @Column(updatable = false)
    @CreatedDate
    private LocalDateTime createdTime;

    @Column
    @LastModifiedDate
    private LocalDateTime updatedTime;

    @Column(updatable = false)
    @CreatedBy
    private String createdBy;

    @Column
    @LastModifiedBy
    private String updatedBy;
}
