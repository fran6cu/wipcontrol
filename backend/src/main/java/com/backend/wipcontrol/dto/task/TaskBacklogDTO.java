package com.backend.wipcontrol.dto.task;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskBacklogDTO {

    private Long id;

    private String name;

    private String status;

    private String criteria;

    private String description;

   }
