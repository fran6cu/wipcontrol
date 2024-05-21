package com.backend.wipcontrol.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.backend.wipcontrol.model.Task;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findAllByOrderByName();

    @Query("SELECT t FROM Task t WHERE t.story.id = :idStory")
    Page<Task> findAllByStoryId(Long idStory, Pageable pageable);

    
}
