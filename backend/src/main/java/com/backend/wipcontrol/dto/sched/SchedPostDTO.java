package com.backend.wipcontrol.dto.sched;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter @Setter
public class SchedPostDTO {

    private LocalDate date;

    private String hours;

    private Long id_story;

    }
