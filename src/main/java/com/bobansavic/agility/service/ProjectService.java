package com.bobansavic.agility.service;

import com.bobansavic.agility.model.Project;

import java.util.List;
import java.util.Set;

public interface ProjectService {

    Project saveProject(Project project);
    Set<Project> getAllProjects();
    Project findProjectByTitle(String title);
    void deleteProject(String projectTitle);
}
