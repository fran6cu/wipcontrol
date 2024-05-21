package com.backend.wipcontrol.dto.sched;

import com.backend.wipcontrol.model.Sched;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SchedDTOConverter {

    private final ModelMapper modelMapper;

    public SchedDTOConverter() { this.modelMapper = new ModelMapper(); }

    public SchedDTO convertToDTO(Sched sched){ return modelMapper.map(sched, SchedDTO.class);}
}
