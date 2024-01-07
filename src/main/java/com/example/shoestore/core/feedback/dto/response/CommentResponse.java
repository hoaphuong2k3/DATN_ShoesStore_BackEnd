package com.example.shoestore.core.feedback.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link com.example.shoestore.entity.Comment}
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentResponse implements Serializable {
    private Long id;
    private String comment;
    private LocalDateTime createDate;
    private Integer status;
    private Integer vote;
    private Long accountId;
    private Long shoesDetailId;
}