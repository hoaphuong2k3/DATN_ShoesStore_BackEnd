package com.example.shoestore.core.common.mapper;

import com.example.shoestore.core.common.mapper.base.BaseMapper;
import com.example.shoestore.core.feedback.dto.request.CreateCommentRequest;
import com.example.shoestore.core.feedback.dto.request.UpdateCommentRequest;
import com.example.shoestore.core.feedback.dto.response.CommentResponse;
import com.example.shoestore.entity.Comment;

import java.util.List;

public interface CommentMapper extends BaseMapper<Comment, CommentResponse> {

    List<Comment> createDTOToEntity(CreateCommentRequest commentDTO);

    Comment updateDTOToEntity(UpdateCommentRequest commentDTO);

    List<CommentResponse> entityListToDTOList(List<Comment> entityList);
}
