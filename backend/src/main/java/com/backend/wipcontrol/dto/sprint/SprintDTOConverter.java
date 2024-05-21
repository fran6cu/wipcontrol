package com.backend.wipcontrol.dto.sprint;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.backend.wipcontrol.model.Sprint;

@Component
@RequiredArgsConstructor
public class SprintDTOConverter {

    private final ModelMapper modelMapper;

    public SprintDTOConverter() { this.modelMapper = new ModelMapper(); }

    public SprintBacklogDTO convertToBacklogDTO(Sprint sprint){ return modelMapper.map(sprint, SprintBacklogDTO.class);}

    public SprintMiniDTO convertToMiniDTO(Sprint sprint){ return modelMapper.map(sprint, SprintMiniDTO.class);}
}
