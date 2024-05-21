package com.backend.wipcontrol.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Data
@Table(name = "Story")
public class Story {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", length = 200, nullable = false, unique = true)
    private String name;

    @Column(name = "assigned", length = 100, nullable = false)
    private String assigned;

    @Column(name = "estimation", nullable = false)
    private Integer estimation;

    @Column(name = "status", length = 20, nullable = false)
    private String status;

    @Column(name = "description", length = 750, nullable = false)
    private String description;

    @Column(name = "criteria", length = 750, nullable = false)
    private String criteria;

    @Column(name = "sprint", length = 20, nullable = false)
    private String sprint;

    @Column(name="points")
    private int points = 0;
      
    @JsonManagedReference
    @OneToMany(mappedBy = "story", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Task> tasks;

    @JsonManagedReference
    @OneToMany(mappedBy = "story", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;

}
