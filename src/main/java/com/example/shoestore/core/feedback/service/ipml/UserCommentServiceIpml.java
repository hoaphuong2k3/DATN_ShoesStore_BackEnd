package com.example.shoestore.core.feedback.service.ipml;

import com.example.shoestore.core.account.client.repository.UserClientRepository;
import com.example.shoestore.core.common.dto.response.ResponseDTO;
import com.example.shoestore.core.common.mapper.CommentMapper;
import com.example.shoestore.core.feedback.dto.request.CreateCommentRequest;
import com.example.shoestore.core.feedback.dto.request.UpdateCommentRequest;
import com.example.shoestore.core.feedback.repository.UserCommentRepository;
import com.example.shoestore.core.feedback.repository.UserOrderCommentRepository;
import com.example.shoestore.core.feedback.service.UserCommentService;
import com.example.shoestore.entity.Comment;
import com.example.shoestore.infrastructure.constants.MessageCode;
import com.example.shoestore.infrastructure.exception.ValidateException;
import com.example.shoestore.infrastructure.utils.MessageUtils;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserCommentServiceIpml implements UserCommentService {

    @Autowired
    UserCommentRepository userCommentRepository;

//    @Autowired
//    UserClientRepository userClientRepository;

    @Autowired
    UserOrderCommentRepository userOrderCommentRepository;

    @Autowired
    CommentMapper commentMapper;

    private Comment comment;

    @Override
    public ResponseDTO getAllComment(Long id) {
        return ResponseDTO.success(userCommentRepository.getAllComment(id));
    }

    @Override
    public ResponseDTO getAllCommentByAccount(Long id) {
        return ResponseDTO.success(userCommentRepository.getAllCommentByIdAccount(id));
    }

    @Override
    public ResponseDTO getAllProductByAccount(Long id) {
        return ResponseDTO.success(userOrderCommentRepository.getDetailShoe(id));
    }

    @Override
    public ResponseDTO createComment(CreateCommentRequest createCommentDTO) {
        this.validateIdMustSignIn(createCommentDTO.getAccountId());
        List<Comment> commentList = commentMapper.createDTOToEntity(createCommentDTO);
        userCommentRepository.saveAll(commentList);
        return ResponseDTO.success("successfully created");
    }

    @Override
    public ResponseDTO updateComment(UpdateCommentRequest updateCommentDTO) {
        this.validateIdMustSignIn(updateCommentDTO.getAccountId());
        comment = new Comment();
        comment = commentMapper.updateDTOToEntity(updateCommentDTO);
        comment.setCreatedDate(LocalDateTime.now());
        Comment updateComment = userCommentRepository.save(comment);
        return ResponseDTO.success(commentMapper.entityToDTO(updateComment));
    }

    @Override
    public ResponseDTO deleteComment(Long id) {
//        this.validateIdMustSignIn(deleteCommentDTO.getAccountId());
//        comment = new Comment();
//        comment = commentMapper.createDTOToEntity(deleteCommentDTO);
        userCommentRepository.deleteById(id);
        return ResponseDTO.success("Delete successfully !,");
    }

    @SneakyThrows
    private void validateIdMustSignIn(Long id) {
        if (id == null) {
            throw new ValidateException(MessageUtils.getMessage(MessageCode.Comment.MUST_SIGN_IN,
                    MessageUtils.getMessage(MessageCode.Comment.ENTITY)));
        }
    }
}
