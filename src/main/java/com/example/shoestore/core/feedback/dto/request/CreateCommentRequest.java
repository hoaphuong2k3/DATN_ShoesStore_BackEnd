package com.example.shoestore.core.feedback.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * DTO for {@link com.example.shoestore.entity.Comment}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateCommentRequest implements Serializable {
    private List<String> comment;
    private Integer status;
    private List<Integer> vote;
    private Long accountId;
    private List<Long> shoesDetailId;
}