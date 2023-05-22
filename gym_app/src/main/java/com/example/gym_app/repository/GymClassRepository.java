package com.example.gym_app.repository;

import com.example.gym_app.model.GymClass;
import com.example.gym_app.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GymClassRepository extends JpaRepository<GymClass, Long> {
}