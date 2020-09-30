package com.bobansavic.agility.repository;

import com.bobansavic.agility.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Long> {

    Project findByTitle(String title);
}
