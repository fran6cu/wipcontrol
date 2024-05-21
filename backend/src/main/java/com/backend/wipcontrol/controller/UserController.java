package com.backend.wipcontrol.controller;

import com.backend.wipcontrol.dto.user.UserDTO;
import com.backend.wipcontrol.dto.user.UserDTOConverter;
import com.backend.wipcontrol.dto.user.UserModDTO;
import com.backend.wipcontrol.dto.user.UserRequestDTO;
import com.backend.wipcontrol.model.User;
import com.backend.wipcontrol.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * get all users
     */
    @GetMapping("/users")
    public List<UserDTO> getAllUsers() {
        List<User> users = userService.getAllUsers();
        UserDTOConverter converter = new UserDTOConverter();
        List<UserDTO> usersDTO = users.stream().map(converter::convertToDTO).collect(Collectors.toList());
        return usersDTO;
    }

    /**
     * get user by id
     */
    @GetMapping("/user")
    public UserDTO getUserById(@RequestParam Long idUser) {
        System.out.println(idUser);
        User user = userService.getUserById(idUser);
        System.out.println(user);
        UserDTOConverter converter = new UserDTOConverter();
        return converter.convertToDTO(user);
    }

    /**
     * get user by username
     *
     * @param usernameSearched
     * @return true if exist, false if not
     */
    @GetMapping("/user/username")
    public Boolean getUserByUsername(@RequestParam String usernameSearched) {
        Boolean userExist = userService.getUserByUsername(usernameSearched);
        return userExist;
    }

    /**
     * get user by email
     *
     * @param emailSearched
     * @return true if exist, false if not
     */
    @GetMapping("/user/email")
    public Boolean getUserByEmail(@RequestParam String emailSearched) {
        Boolean userExist = userService.getUserByEmail(emailSearched);
        return userExist;
    }

    @PostMapping("/user/login")
    public UserDTO loginUser(@RequestBody UserRequestDTO userRequestDTO) {
        String email = userRequestDTO.getEmail();
        String pass = userRequestDTO.getPass();
    	User user = userService.checkLogin(email, pass);
    	UserDTOConverter converter = new UserDTOConverter();
    	return converter.convertToDTO(user);
    }

    /**
     * Create new user
     *
     * @param newUser
     * @return UserDTO
     */
    @PostMapping("/user")
    public UserDTO createUser(@RequestBody UserModDTO newUser) {
        User user = userService.createUser(newUser);
        UserDTOConverter converter = new UserDTOConverter();
        return converter.convertToDTO(user);
    }

    /**
     * update user
     *
     * @param idUser 
     * @param userToUpdate 
     * @param newAvatar 
     * @return 
     */
    @PutMapping(value = "/user", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public UserDTO updateUser(@RequestParam Long idUser,
                                    @RequestPart("userToUpdate") UserModDTO userToUpdate,
                                    @RequestPart(value ="newAvatar", required = false) MultipartFile newAvatar) {
        User user = userService.updateUser(idUser, userToUpdate, newAvatar);
        UserDTOConverter converter = new UserDTOConverter();
        return converter.convertToDTO(user);
    }
}
