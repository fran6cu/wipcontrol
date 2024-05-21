package com.backend.wipcontrol.dto.story;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StoryPostDTO {

    private String name;

    private String assigned;

    private Integer estimation;

    private String status;

    private String description;

    private String criteria;

    private String sprint;

    private Integer points;

}
