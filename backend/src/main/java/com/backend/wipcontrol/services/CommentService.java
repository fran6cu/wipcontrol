package com.backend.wipcontrol.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import com.backend.wipcontrol.dto.comment.CommentDTO;
import com.backend.wipcontrol.dto.comment.CommentDTOConverter;
import com.backend.wipcontrol.dto.comment.CommentPostDTO;
import com.backend.wipcontrol.model.Comment;
import com.backend.wipcontrol.model.Story;
import com.backend.wipcontrol.model.User;
import com.backend.wipcontrol.repository.CommentRepository;
import com.backend.wipcontrol.repository.StoryRepository;
import com.backend.wipcontrol.repository.UserRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StoryRepository storyRepository;

    /**
     * get all comments of a us
     * @param idStory 
     * @param nPage 
     * @return 
     */
    public Map<String, Object> getCommentsStory(Long idStory, int nPage) {
        Pageable datosPage = PageRequest.of(nPage,  200, Sort.by("date").descending());
        Page<Comment> listCommentsPage = commentRepository.findAllByStoryId(idStory, datosPage);
        CommentDTOConverter commentDTOConverter = new CommentDTOConverter();
        List<CommentDTO> listCommentsDTO = listCommentsPage.getContent()
                                                                        .stream()
                                                                        .map(commentDTOConverter::convertToDTO)
                                                                        .collect(Collectors.toList());
        Map<String, Object> paginatedResponse = new HashMap<>();
        paginatedResponse.put("comments", listCommentsDTO);
        paginatedResponse.put("currentPage", listCommentsPage.getNumber());
        paginatedResponse.put("totalBacklog", listCommentsPage.getTotalElements());
        paginatedResponse.put("totalPages", listCommentsPage.getTotalPages());

        return paginatedResponse;
    }

    /**
     * 
     * @param newComment 
     * @return 
     */
    public Comment addComment(CommentPostDTO newComment) {
        System.out.println(newComment.getId_user());
        Optional<User> usersExists = userRepository.findById(newComment.getId_user());
        Optional<Story> storyExists = storyRepository.findById(newComment.getId_story());
        if(usersExists.isEmpty() || storyExists.isEmpty()) {
            throw new ResourceNotFoundException("User or user story does not exist");
        }

        Comment comment = new Comment();
        comment.setUser(usersExists.get());
        comment.setStory(storyExists.get());
        comment.setText(newComment.getText());
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedNow = now.format(formatter);
        comment.setDate(formattedNow);
        return commentRepository.save(comment);
    }

    /**
     * delete comment
     * @param idComment id
     * @return comment deleted
     */
    public Comment deleteComment(Long idComment) {
        Optional<Comment> commentExists = commentRepository.findById(idComment);
        if(commentExists.isEmpty()) {
            throw new ResourceNotFoundException("Comment does not exist");
        }
        commentRepository.deleteById(idComment);
        return commentExists.get();
    }
}
