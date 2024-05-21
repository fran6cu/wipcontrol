package com.backend.wipcontrol.dto.task;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskMiniDTO {

    private Long id;

    private String name;

    private Integer estimation;
}
