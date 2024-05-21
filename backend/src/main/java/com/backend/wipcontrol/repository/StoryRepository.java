package com.backend.wipcontrol.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.backend.wipcontrol.model.Story;

import java.util.List;

public interface StoryRepository extends JpaRepository<Story, Long> {

    Story findByName(String title);

    Page<Story> findAll(Pageable pageable);

    List<Story> findAllByOrderByName();

    @Query("SELECT p FROM Story p JOIN Sprint sp ON p.sprint = sp.name WHERE p.points >= 0 and sp.status = 'OPEN' ORDER BY p.points DESC")
    List<Story> findNext10ByOrderByPointsDesc();

    @Query("SELECT p FROM Story p JOIN Sprint sp ON p.sprint = sp.name WHERE sp.status = 'OPEN' ")
    List<Story> findStoriesCurrentSprint();
}
