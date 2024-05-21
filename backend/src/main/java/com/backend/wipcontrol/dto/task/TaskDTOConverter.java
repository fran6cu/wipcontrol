package com.backend.wipcontrol.dto.task;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.backend.wipcontrol.model.Task;

@Component
@RequiredArgsConstructor
public class TaskDTOConverter {

    private final ModelMapper modelMapper;

    public TaskDTOConverter() { this.modelMapper = new ModelMapper(); }

    public TaskBacklogDTO convertToBacklogDTO(Task task){ return modelMapper.map(task, TaskBacklogDTO.class);}

    public TaskMiniDTO convertToMiniDTO(Task task){ return modelMapper.map(task, TaskMiniDTO.class);}
}
