package com.backend.wipcontrol.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.backend.wipcontrol.model.Sprint;


import java.util.List;

public interface SprintRepository extends JpaRepository<Sprint, Long> {

    Sprint findByName(String title);

    Page<Sprint> findAll(Pageable pageable);

    List<Sprint> findAllByOrderByName();

    @Query("SELECT s FROM Sprint s WHERE s.status = 'OPEN'")
    List<Sprint> findOpenSprint();

    }
