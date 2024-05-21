package com.backend.wipcontrol.dto.comment;

import com.backend.wipcontrol.dto.user.UserDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentDTO {

    private Long id;

    private String text;

    private String date;

    private UserDTO user;
}
