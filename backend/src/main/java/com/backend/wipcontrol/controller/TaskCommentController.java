package com.backend.wipcontrol.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import com.backend.wipcontrol.dto.comment.CommentTaskDTO;
import com.backend.wipcontrol.model.TaskComment;
import com.backend.wipcontrol.services.TaskCommentService;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class TaskCommentController {

    private final TaskCommentService taskCommentService;


    /**
     * get comments of a task
     */
    @GetMapping("/taskComments/task/{idTask}/page/{nPage}")
    public Map<String, Object> getCommentsTask(@PathVariable Long idTask, @PathVariable int nPage) {
        return taskCommentService.getCommentsTask(idTask, nPage);
    }

    /**
     * delete comment by id
     *
     * @param idComment 
     * @return delete comment 
     */
    @DeleteMapping("/taskComments/{idComment}")
    public TaskComment DeleteTaskComment(@PathVariable Long idComment) {
        return taskCommentService.deleteTaskComment(idComment);
    }

    /**
     * Add new comment
     *
     * @param newComment 
     * @return comment added
     */
    @PostMapping("/taskComments")
    public TaskComment addTaskComment(@RequestBody CommentTaskDTO newComment) {
        return taskCommentService.addTaskComment(newComment);
    }
}
