package com.backend.wipcontrol.dto.story;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.backend.wipcontrol.model.Story;

@Component
@RequiredArgsConstructor
public class StoryDTOConverter {

    private final ModelMapper modelMapper;

    public StoryDTOConverter() { this.modelMapper = new ModelMapper(); }

    public StoryBacklogDTO convertToBacklogDTO(Story story){ return modelMapper.map(story, StoryBacklogDTO.class);}

    public StoryMiniDTO convertToMiniDTO(Story story){ return modelMapper.map(story, StoryMiniDTO.class);}
}
