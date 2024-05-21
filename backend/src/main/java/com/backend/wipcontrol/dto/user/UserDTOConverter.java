package com.backend.wipcontrol.dto.user;

import com.backend.wipcontrol.model.User;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserDTOConverter {

    private final ModelMapper modelMapper;

    public UserDTOConverter() { this.modelMapper = new ModelMapper(); }

    public UserDTO convertToDTO(User user){ return modelMapper.map(user, UserDTO.class);}
}
