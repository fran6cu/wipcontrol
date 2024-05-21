package com.backend.wipcontrol.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import com.backend.wipcontrol.dto.comment.CommentPostDTO;
import com.backend.wipcontrol.model.Comment;
import com.backend.wipcontrol.services.CommentService;

import java.util.Map;


@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;


    /**
     * get comments of a story
     */
    @GetMapping("/comments/story/{idStory}/page/{nPage}")
    public Map<String, Object> getCommentsStory(@PathVariable Long idStory, @PathVariable int nPage) {
         return commentService.getCommentsStory(idStory, nPage);
    }

    /**
     * delete comment by id
     *
     * @param idComment 
     * @return delete comment 
     */
    @DeleteMapping("/comments/{idComment}")
    public Comment deleteComment(@PathVariable Long idComment) {
        return commentService.deleteComment(idComment);
    }

    /**
     * Add new comment
     *
     * @param newComment 
     * @return comment added
     */
    @PostMapping("/comments")
    public Comment addComment(@RequestBody CommentPostDTO newComment) {
        return commentService.addComment(newComment);
    }
}
