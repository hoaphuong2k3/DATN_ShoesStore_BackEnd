package com.example.shoestore.core.feedback.repository;

import com.example.shoestore.core.feedback.dto.response.CommentProjection;
import com.example.shoestore.entity.Comment;
import com.example.shoestore.repository.CommentRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserCommentRepository extends CommentRepository {

    @Query(nativeQuery = true,value = """
            select cmt.id as id, cmt.comment as comment, cmt.vote as vote, cmt.created_date as createdDate,
            c.username as username, c.avatar as avatar from comment cmt
            left join shoes_detail shdt on cmt.shoes_detail_id = shdt.id
            left join client c on c.id = cmt.client_id
            where shdt.id = :shoeId
            """)
    List<CommentProjection> getAllComment(@Param("shoeId") Long id);

    @Query(nativeQuery = true,value = """
            select cmt.id as id, cmt.comment as comment, cmt.vote as vote, cmt.created_date as createdDate,
            c.username as username, c.avatar as avatar from comment cmt
            left join shoes_detail shdt on cmt.shoes_detail_id = shdt.id
            left join client c on c.id = cmt.client_id
            where c.id = :accountId
            """)
    List<CommentProjection> getAllCommentByIdAccount(@Param("accountId") Long id);
}
