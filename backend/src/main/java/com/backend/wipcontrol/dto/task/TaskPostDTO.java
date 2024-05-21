package com.backend.wipcontrol.dto.task;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskPostDTO {

    private String name;

    private String assigned;

    private Integer estimation;

    private String status;

    private String description;

    private String criteria;

    private Long id_story;

    

   
}
