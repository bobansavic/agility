package com.bobansavic.agility.service;

import com.bobansavic.agility.model.Project;
import com.bobansavic.agility.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class ProjectServiceImplementation implements ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Override
    public Project saveProject(Project project) {
        return projectRepository.save(project);
    }

    @Override
    public Set<Project> getAllProjects() {
        return new HashSet<>(projectRepository.findAll());
    }

    @Override
    public Project findProjectByTitle(String title) {
        return projectRepository.findByTitle(title);
    }

    @Override
    public void deleteProject(String projectTitle) {
        projectRepository.delete(findProjectByTitle(projectTitle));
    }
}
