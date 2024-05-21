package com.backend.wipcontrol.repository;

import com.backend.wipcontrol.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername (String username);

    User findByEmail (String email);

    User findByEmailAndPass (String email, String pass);

}
