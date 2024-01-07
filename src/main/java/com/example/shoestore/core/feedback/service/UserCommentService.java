package com.example.shoestore.core.feedback.service;

import com.example.shoestore.core.common.dto.response.ResponseDTO;
import com.example.shoestore.core.feedback.dto.request.CreateCommentRequest;
import com.example.shoestore.core.feedback.dto.request.UpdateCommentRequest;

public interface UserCommentService {

    ResponseDTO getAllComment(Long id);

    ResponseDTO getAllCommentByAccount(Long id);

    ResponseDTO getAllProductByAccount(Long id);

    ResponseDTO createComment(CreateCommentRequest createCommentDTO);

    ResponseDTO updateComment(UpdateCommentRequest updateCommentDTO);

    ResponseDTO deleteComment(Long id);
}
