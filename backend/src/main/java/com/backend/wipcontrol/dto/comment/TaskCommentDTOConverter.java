package com.backend.wipcontrol.dto.comment;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.backend.wipcontrol.model.TaskComment;

@Component
@RequiredArgsConstructor
public class TaskCommentDTOConverter {

    private final ModelMapper modelMapper;

    public TaskCommentDTOConverter() { this.modelMapper = new ModelMapper(); }

    public CommentDTO convertToDTO(TaskComment taskComment){ return modelMapper.map(taskComment, CommentDTO.class);}
}
