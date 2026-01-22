package com.holly.service;

import com.holly.dto.CommentSaveDTO;

import java.util.Map;

public interface AiCommentExpandService {

    /**
     * ai评论帮写、续写、润色、精简
     * @param dto
     * @return
     */
    Map<String, String> expand(CommentSaveDTO dto);
}
