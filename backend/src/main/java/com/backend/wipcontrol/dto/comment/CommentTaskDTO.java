package com.backend.wipcontrol.dto.comment;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentTaskDTO {

    private Long id_user;

    private Long id_story;

    private String text;

}
