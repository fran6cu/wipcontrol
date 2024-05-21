package com.backend.wipcontrol.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.backend.wipcontrol.model.TaskComment;

public interface TaskCommentRepository extends JpaRepository<TaskComment, Long> {

    @Query("SELECT c FROM TaskComment  c WHERE c.task.id = :idTask")
    Page<TaskComment> findAllByTaskId(Long idTask, Pageable pageable);
}

