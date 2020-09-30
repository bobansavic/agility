package com.bobansavic.agility.service;

import com.bobansavic.agility.model.*;
import com.bobansavic.agility.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class TaskServiceImplementation implements TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Override
    public Task saveTask(TaskStore dto, boolean edit) {
        Task entity;
        if (edit) {
            entity = taskRepository.getOne(dto.getId());
        } else {
            entity = new Task();
        }
        entity.setTitle(dto.getTitle());
        entity.setStatus(dto.getStatus().getValue());
        entity.setPriority(dto.getPriority().name());
        entity.setAssignee(dto.getAssignee());
        entity.setDescription(dto.getDescription());
        entity.setProject(dto.getProject());
        return taskRepository.save(entity);
    }

    @Override
    public Task findTaskByTitle(String title) {
        return taskRepository.findByTitle(title);
    }

    @Override
    public TaskStore findTaskStoreByTitle(String title) {
        Task entity = findTaskByTitle(title);
        if (entity != null) {
            TaskStore store = new TaskStore();
            store.setId(entity.getId());
            store.setTitle(entity.getTitle());
            store.setStatus(TicketStatus.forValue(entity.getStatus()));
            store.setPriority(TaskPriority.valueOf(entity.getPriority()));
            store.setAssignee(entity.getAssignee());
            store.setDescription(entity.getDescription());
            return store;
        }
        return null;
    }

    @Override
    public List<Task> findAllTasks() {
        return taskRepository.findAll();
    }

    @Override
    public List<Task> findTasksForProject(Project project) {
        return taskRepository.findByProject_Id(project.getId());
    }

    @Override
    public void deleteTask(TaskStore dto) {
        taskRepository.deleteById(dto.getId());
    }

    @Override
    public boolean isUnique(String title) {
        return findTaskByTitle(title) == null;
    }
}
