package com.backend.wipcontrol.services;

import com.backend.wipcontrol.controller.FilesController;
import com.backend.wipcontrol.dto.user.UserModDTO;
import com.backend.wipcontrol.fileupload.StorageService;
import com.backend.wipcontrol.model.User;
import com.backend.wipcontrol.model.UserRol;
import com.backend.wipcontrol.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import javax.persistence.EntityExistsException;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StorageService storageService;

    /**
     * get all users
     *
     * @return users list
     */
    public List<User> getAllUsers() {
        List<User> users = userRepository.findAll();
        if(users.isEmpty()) {
            throw new ResourceNotFoundException("Users not found");
        } else {
            return users;
        }
    }

    /**
     * get users by id
     *
     * @param id user
     * @return 404 if not found
     */
    public User getUserById(Long id) {
        
        return userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Not found user with id: " + id));
    }

    /**
     * get user by username
     *
     * @param usernameSearched Username
     * @return true if exist, false if not
     */
    public Boolean getUserByUsername(String usernameSearched) {
        User foundUser = userRepository.findByUsername(usernameSearched);
        return foundUser != null;
    }

    /**
     * get user by email
     *
     * @param emailSearched Email 
     * @return true if exist, false if not
     */
    public Boolean getUserByEmail(String emailSearched) {
        User foundUser = userRepository.findByEmail(emailSearched);
        return foundUser != null;
    }

    /**
     * check login
     *
     * @param email Email 
     * @param pass Pass word
     * @return 404 if not exist
     */
    public User checkLogin(String email, String pass) {
        User foundUser = userRepository.findByEmail(email);
        if(foundUser == null) {
            throw new ResourceNotFoundException("Not found user with email: " + email);
        } else {
            if(!foundUser.getPass().equals(pass)) {
                throw new ResourceNotFoundException("Incorrect password");
            } else {
                return foundUser;
            }
        }
    }

    /**
     * Create new user
     *
     * @param newUser User 
     * @return new user
     */
    public User createUser(UserModDTO newUser) {
        Boolean userExist = getUserByUsername(newUser.getUsername());
        Boolean emailExist = getUserByEmail(newUser.getEmail());
        if(userExist || emailExist) {
            throw new EntityExistsException("User already exist");
        }
        User userCreated = new User();
        userCreated.setUsername(newUser.getUsername());
        userCreated.setEmail(newUser.getEmail());
        userCreated.setPass(newUser.getPass());
        userCreated.setRol(UserRol.USER.toString());
        return userRepository.save(userCreated);
    }

    /**
     * update user
     *
     * @param idUser 
     * @param updatedUser
     * @param newAvatar 
     */
    public User updateUser(Long idUser, UserModDTO updatedUser, MultipartFile newAvatar){
        User userToUpdate = userRepository
                                            .findById(idUser)
                                            .orElseThrow(() -> new ResourceNotFoundException("User not found: " + idUser));

        User usernameExist = null;
        User emailExist = null;
        if(!updatedUser.getUsername().isEmpty()){
            usernameExist = userRepository.findByUsername(updatedUser.getUsername());
        }
        if(!updatedUser.getEmail().isEmpty()){
            emailExist = userRepository.findByEmail(updatedUser.getEmail());
        }
        if(usernameExist != null && !usernameExist.getId().equals(idUser)) {
            throw new EntityExistsException("Username already exist");
        }
        if(emailExist != null && !emailExist.getId().equals(idUser)) {
            throw new EntityExistsException("Email already exist");
        }

        String urlAvatar = null;

        if(newAvatar != null && !newAvatar.isEmpty()) {
            String avatarUploaded = storageService.store(newAvatar);
            urlAvatar = MvcUriComponentsBuilder
                            .fromMethodName(FilesController.class, "serveFile", avatarUploaded, null)
                            .build().toUriString();
        }

        if(!updatedUser.getUsername().isEmpty() && !updatedUser.getUsername().equals(userToUpdate.getUsername())) {
            userToUpdate.setUsername(updatedUser.getUsername());
        }
        if(!updatedUser.getEmail().isEmpty() && !updatedUser.getEmail().equals(userToUpdate.getEmail())) {
            userToUpdate.setEmail(updatedUser.getEmail());
        }
        if(!updatedUser.getPass().isEmpty() && !updatedUser.getPass().equals(userToUpdate.getPass())) {
            userToUpdate.setPass(updatedUser.getPass());
        }
        if(urlAvatar != null){
            userToUpdate.setAvatar(urlAvatar);
        }
        System.out.println(userToUpdate);
        return userRepository.save(userToUpdate);
    }

}
