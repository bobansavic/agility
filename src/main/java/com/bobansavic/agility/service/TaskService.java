package com.bobansavic.agility.service;

import com.bobansavic.agility.model.Project;
import com.bobansavic.agility.model.Task;
import com.bobansavic.agility.model.TaskStore;

import java.util.List;

public interface TaskService {
    public Task saveTask(TaskStore task, boolean edit);
    public Task saveTask(Task task);
    public Task findTaskByTitle(String title);
    public TaskStore findTaskStoreByTitle(String title);
    public List<Task> findAllTasks();
    public List<Task> findTasksForProject(Long id);
    public void deleteTask(Long id);
    public boolean isUnique(String title);
}
