package com.bobansavic.agility.service;

import com.bobansavic.agility.model.Project;
import com.bobansavic.agility.model.Task;
import com.bobansavic.agility.model.TaskStore;

import java.util.List;

public interface TaskService {
    public Task saveTask(TaskStore task, boolean edit);
    public Task findTaskByTitle(String title);
    public TaskStore findTaskStoreByTitle(String title);
    public List<Task> findAllTasks();
    public List<Task> findTasksForProject(Project project);
    public void deleteTask(TaskStore dto);
    public boolean isUnique(String title);
}
