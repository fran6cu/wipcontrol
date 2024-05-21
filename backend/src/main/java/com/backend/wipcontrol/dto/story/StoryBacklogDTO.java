package com.backend.wipcontrol.dto.story;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StoryBacklogDTO {

    private Long id;

    private String name;

    private String assigned;

    private Integer estimation;

    private String status;

    private String criteria;

    private String description;

    private String sprint;

    private int points;
}
