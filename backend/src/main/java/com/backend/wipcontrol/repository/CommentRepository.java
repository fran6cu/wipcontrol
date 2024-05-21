package com.backend.wipcontrol.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.backend.wipcontrol.model.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("SELECT c FROM Comment c WHERE c.story.id = :idStory")
    Page<Comment> findAllByStoryId(Long idStory, Pageable pageable);
}
