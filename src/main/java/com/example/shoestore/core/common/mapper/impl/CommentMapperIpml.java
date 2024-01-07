package com.example.shoestore.core.common.mapper.impl;

import com.example.shoestore.core.common.mapper.CommentMapper;
import com.example.shoestore.core.feedback.dto.request.CreateCommentRequest;
import com.example.shoestore.core.feedback.dto.request.UpdateCommentRequest;
import com.example.shoestore.core.feedback.dto.response.CommentResponse;
import com.example.shoestore.entity.Comment;
import com.example.shoestore.infrastructure.constants.Status;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class CommentMapperIpml implements CommentMapper {
    @Override
    public List<Comment> createDTOToEntity(CreateCommentRequest commentDTO) {
        if (commentDTO == null) {
            return null;
        }

        List<Comment> comments = new ArrayList<>();

        List<Long> shoesDetailIds = commentDTO.getShoesDetailId();
        List<String> commentList = commentDTO.getComment();
        List<Integer> votes = commentDTO.getVote();
        Long accountId = commentDTO.getAccountId();
        LocalDateTime createdDate = LocalDateTime.now();
        int status = Status.ACTIVATE.getValue();

        int size = Math.max(shoesDetailIds.size(), Math.max(commentList.size(), votes.size()));

        for (int i = 0; i < size; i++) {
            Comment comment = new Comment();

            if (i < shoesDetailIds.size()) {
                comment.setShoesDetailId(shoesDetailIds.get(i));
            }

            if (i < commentList.size()) {
                comment.setComment(commentList.get(i));
            }

            if (i < votes.size()) {
                comment.setVote(votes.get(i));
            }

            comment.setCreatedDate(createdDate);
            comment.setAccountId(accountId);
            comment.setStatus(status);

            comments.add(comment);
        }

        return comments;
    }

    @Override
    public Comment updateDTOToEntity(UpdateCommentRequest commentDTO) {
        if (commentDTO == null) {
            return null;
        }

        Comment comment = new Comment();
        comment.setId(commentDTO.getId());
        comment.setComment(commentDTO.getComment());
        comment.setVote(commentDTO.getVote());
        comment.setAccountId(commentDTO.getAccountId());
        comment.setStatus(commentDTO.getStatus());
        comment.setShoesDetailId(commentDTO.getShoesDetailId());

        return comment;
    }

    @Override
    public List<CommentResponse> entityListToDTOList(List<Comment> entityList) {
        if (CollectionUtils.isEmpty(entityList)) {
            return null;
        }

        List<CommentResponse> dtoList = new ArrayList<>();

        entityList.forEach(entity->{
            CommentResponse dto = entityToDTO(entity);
            if (dto != null) {
                dtoList.add(dto);
            }
        });

        return dtoList;
    }

    @Override
    public Comment DTOToEntity(CommentResponse dto) {
        return null;
    }

    @Override
    public CommentResponse entityToDTO(Comment entity) {
        if (entity == null) {
            return null;
        }

        CommentResponse commentResponse = new CommentResponse();
        commentResponse.setId(entity.getId());
        commentResponse.setComment(entity.getComment());
        commentResponse.setVote(entity.getVote());
        commentResponse.setCreateDate(entity.getCreatedDate());
        commentResponse.setAccountId(entity.getAccountId());
        commentResponse.setShoesDetailId(entity.getShoesDetailId());

        return commentResponse;
    }
}
