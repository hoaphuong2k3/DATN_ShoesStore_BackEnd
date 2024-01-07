package com.example.shoestore.core.feedback.dto.response;

import java.time.LocalDateTime;

public interface CommentProjection {

    String getUserName();

    Long getId();

    Byte[] getAvatar();

    String getComment();

    Integer getVote();

    LocalDateTime getCreatedDate();
}
