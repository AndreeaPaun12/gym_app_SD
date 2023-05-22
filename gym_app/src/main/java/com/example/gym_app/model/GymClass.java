package com.example.gym_app.model;

import lombok.AllArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "gym_classes")
public class GymClass extends BaseEntity {

    @Column(nullable = false)
    private LocalDateTime startTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trainer_id", referencedColumnName = "id")
    private User trainer;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "client_gymclass",
            joinColumns = @JoinColumn(name = "gymclass_id"),
            inverseJoinColumns = @JoinColumn(name = "client_id"))
    private Set<User> clients = new HashSet<>();
}
