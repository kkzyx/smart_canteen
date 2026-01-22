package com.holly.controller.store;

import com.holly.dto.CommentRepayDTO;
import com.holly.dto.CommentRepaySaveDTO;
import com.holly.result.Result;
import com.holly.service.CommentRepayService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.holly.constant.CommentConstants.COMMENT_STATUS_SELLER;

@RestController
@RequestMapping("/store/comment-repay")
@Tag(name = "评论回复相关接口")
@RequiredArgsConstructor
public class CommentReplyController {

    private final CommentRepayService commentRepayService;

    @PostMapping("/save")
    @Operation(summary = "评论回复")
    public Result commentRepay(@RequestBody CommentRepaySaveDTO dto) {
        return commentRepayService.commentRepay(dto, COMMENT_STATUS_SELLER);
    }

    /**
     * 查询所有评论回复
     *
     * @param dto
     * @return
     */
    @GetMapping("/list")
    @Operation(summary = "查询所有评论回复")
    public Result list(@RequestBody CommentRepayDTO dto) {
        return commentRepayService.list(dto);
    }

    /**
     * 根据回复评论id集合批量删除回复
     *
     * @param ids
     * @return
     */
    @DeleteMapping
    @Operation(summary = "删除回复评论")
    public Result deleteCommentRepay(@RequestParam List<String> ids) {
        return commentRepayService.deleteCommentRepay(ids);
    }

}
