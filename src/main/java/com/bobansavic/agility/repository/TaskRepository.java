package com.bobansavic.agility.repository;

import com.bobansavic.agility.model.Project;
import com.bobansavic.agility.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    Task findByTitle(String title);
    List<Task> findByProject_Id(Long id);
}
