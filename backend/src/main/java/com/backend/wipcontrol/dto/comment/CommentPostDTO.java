package com.backend.wipcontrol.dto.comment;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentPostDTO {

    private Long id_user;

    private Long id_story;

    private String text;

}
