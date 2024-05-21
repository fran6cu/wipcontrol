package com.backend.wipcontrol.repository;

import com.backend.wipcontrol.model.Sched;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface SchedRepository extends JpaRepository<Sched, Long> {

    /**
     * get today scheds
     *
     * @return List
     */
    List<Sched> findByDateEquals(LocalDate date);

    /**
     * get from today scheds
     *
     * @return List
     */
    List<Sched> findByDateGreaterThanEqualOrderByDate(LocalDate date);
}
