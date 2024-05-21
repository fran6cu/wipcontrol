package com.backend.wipcontrol.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import com.backend.wipcontrol.dto.comment.CommentDTO;
import com.backend.wipcontrol.dto.comment.TaskCommentDTOConverter;
import com.backend.wipcontrol.dto.comment.CommentTaskDTO;
import com.backend.wipcontrol.model.TaskComment;
import com.backend.wipcontrol.model.Task;
import com.backend.wipcontrol.model.User;
import com.backend.wipcontrol.repository.TaskCommentRepository;
import com.backend.wipcontrol.repository.TaskRepository;
import com.backend.wipcontrol.repository.UserRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TaskCommentService {

    @Autowired
    private TaskCommentRepository taskCommentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskRepository taskRepository;

    /**
     * get comments from task in order
     * @param idStory 
     * @param nPage 
     * @return 
     */
    public Map<String, Object> getCommentsTask(Long idTask, int nPage) {
        Pageable datosPage = PageRequest.of(nPage,  200, Sort.by("date").descending());
        Page<TaskComment> listCommentsPage = taskCommentRepository.findAllByTaskId(idTask, datosPage);
        // Convert list to DTO
        TaskCommentDTOConverter taskCommentDTOConverter = new TaskCommentDTOConverter();
        List<CommentDTO> listCommentsDTO = listCommentsPage.getContent()
                                                                        .stream()
                                                                        .map(taskCommentDTOConverter::convertToDTO)
                                                                        .collect(Collectors.toList());
        Map<String, Object> paginatedResponse = new HashMap<>();
        paginatedResponse.put("comments", listCommentsDTO);
        paginatedResponse.put("currentPage", listCommentsPage.getNumber());
        paginatedResponse.put("totalBacklog", listCommentsPage.getTotalElements());
        paginatedResponse.put("totalPages", listCommentsPage.getTotalPages());

        return paginatedResponse;
    }

    /**
     * Add new comment to a task
     * @param newComment 
     * @return comment added
     */
    public TaskComment addTaskComment(CommentTaskDTO newComment) {
        System.out.println(newComment.getId_user());
        Optional<User> usersExists = userRepository.findById(newComment.getId_user());
        Optional<Task> taskExists = taskRepository.findById(newComment.getId_story());
        if(usersExists.isEmpty() || taskExists.isEmpty()) {
            throw new ResourceNotFoundException("user does not exist");
        }

        TaskComment taskComment = new TaskComment();
        taskComment.setUser(usersExists.get());
        taskComment.setTask(taskExists.get());
        taskComment.setText(newComment.getText());
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedNow = now.format(formatter);
        taskComment.setDate(formattedNow);
        return taskCommentRepository.save(taskComment);
    }

    /**
     * delete comment
     * @param idComment 
     * @return 
     */
    public TaskComment deleteTaskComment(Long idComment) {
        Optional<TaskComment> commentExists = taskCommentRepository.findById(idComment);
        if(commentExists.isEmpty()) {
            throw new ResourceNotFoundException("Comment does not exist");
        }
        taskCommentRepository.deleteById(idComment);
        return commentExists.get();
    }
}
