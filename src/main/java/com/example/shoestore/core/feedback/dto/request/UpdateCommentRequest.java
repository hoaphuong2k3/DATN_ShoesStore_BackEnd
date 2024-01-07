package com.example.shoestore.core.feedback.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCommentRequest {

    private Long id;
    private String comment;
    private Integer status;
    private Integer vote;
    private Long accountId;
    private Long shoesDetailId;
}
