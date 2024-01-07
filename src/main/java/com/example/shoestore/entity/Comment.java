package com.example.shoestore.entity;

import com.example.shoestore.entity.base.PrimaryEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "comment")
public class Comment extends PrimaryEntity {

    @Column(name = "comment")
    private String comment;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @Column(name = "status")
    private Integer status;

    @Column(name = "vote")
    private Integer vote;

    @Column(name = "client_id")
    private Long accountId;

    @Column(name = "shoes_detail_id")
    private Long shoesDetailId;

}