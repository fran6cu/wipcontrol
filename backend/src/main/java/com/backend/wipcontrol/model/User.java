package com.backend.wipcontrol.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@NoArgsConstructor
@AllArgsConstructor
@Entity
@Data
@Table(name = "User")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "username", length = 30, nullable = false, unique = true)
    private String username;

    @Column(name = "email", length = 85, nullable = false, unique = true)
    private String email;

    @Column(name = "pass", length = 255, nullable = false)
    private String pass;

    @Column(name = "avatar", length = 255)
    private String avatar;

    @Column(name = "rol", length = 45, nullable = false)
    private String rol;

   
}
