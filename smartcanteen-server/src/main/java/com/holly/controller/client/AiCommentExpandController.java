package com.holly.controller.client;

import com.holly.dto.CommentSaveDTO;
import com.holly.result.Result;
import com.holly.service.AiCommentExpandService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/client/ai-comment")
@RequiredArgsConstructor
@Tag(name = "Ai评论接口")
public class AiCommentExpandController {
    private final AiCommentExpandService aiCommentExpandService;

    /**
     * ai评论帮写、续写、润色、精简
     */
    @PostMapping("/expand")
    @Operation(summary = "ai评论帮写、续写、润色、精简")
    public Result<Map<String, String>> expand(@RequestBody CommentSaveDTO dto) {
        return Result.success(aiCommentExpandService.expand(dto));
    }
}
