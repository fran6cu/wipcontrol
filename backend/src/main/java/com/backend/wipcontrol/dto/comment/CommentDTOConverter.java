package com.backend.wipcontrol.dto.comment;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.backend.wipcontrol.model.Comment;

@Component
@RequiredArgsConstructor
public class CommentDTOConverter {

    private final ModelMapper modelMapper;

    public CommentDTOConverter() { this.modelMapper = new ModelMapper(); }

    public CommentDTO convertToDTO(Comment comment){ return modelMapper.map(comment, CommentDTO.class);}
}
