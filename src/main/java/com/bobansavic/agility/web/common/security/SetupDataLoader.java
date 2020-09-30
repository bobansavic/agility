package com.bobansavic.agility.web.common.security;

import com.bobansavic.agility.model.Project;
import com.bobansavic.agility.model.User;
import com.bobansavic.agility.model.UserRole;
import com.bobansavic.agility.model.UserRoleType;
import com.bobansavic.agility.service.ProjectService;
import com.bobansavic.agility.service.UserRoleService;
import com.bobansavic.agility.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {

    private boolean alreadySetup = false;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRoleService roleService;

    @Autowired
    private ProjectService projectService;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        if (alreadySetup) {
            return;
        }

        createRoleIfNotFound(UserRoleType.ADMIN.getValue());
        createRoleIfNotFound(UserRoleType.USER.getValue());
        createRoleIfNotFound(UserRoleType.PM.getValue());
        createUsers();
        if (projectService.findProjectByTitle("Test project") == null) {
            Project testProject = new Project();
            testProject.setTitle("Test project");
            projectService.saveProject(testProject);
        }

        alreadySetup = true;
    }

    private void createUsers() {
        UserRole adminRole = roleService.findByName(UserRoleType.ADMIN.getValue());
        UserRole projectManagerRole = roleService.findByName(UserRoleType.PM.getValue());
        UserRole userRole = roleService.findByName(UserRoleType.USER.getValue());

        if (userService.findByUsername("admin") == null) {
            User user = new User();
            user.setFirstName("Admin");
            user.setLastName("Admin");
            user.setEmail("admin@agility.com");
            user.setUsername("admin");
            user.setPassword("admin");
            user.setRole(adminRole);
            userService.save(user);
        }
        if (userService.findByUsername("pm") == null) {
            User user = new User();
            user.setFirstName("Project");
            user.setLastName("Manager");
            user.setEmail("pm@agility.com");
            user.setUsername("pm");
            user.setPassword("pm");
            user.setRole(projectManagerRole);
            userService.save(user);
        }
        if (userService.findByUsername("testuser") == null) {
            User user = new User();
            user.setFirstName("Test");
            user.setLastName("User");
            user.setEmail("test@test.com");
            user.setUsername("testuser");
            user.setPassword("test");
            user.setRole(userRole);
            userService.save(user);
        }
        if (userService.findByUsername("bsavic") == null) {
            User user = new User();
            user.setFirstName("Boban");
            user.setLastName("Savic");
            user.setEmail("bobanzsavic@gmail.com");
            user.setUsername("bsavic");
            user.setPassword("test");
            user.setRole(userRole);
            userService.save(user);
        }
    }

    @Transactional
    UserRole createRoleIfNotFound(String name) {
        UserRole role = roleService.findByName(name);
        if (role == null) {
            role = new UserRole();
            role.setName(name);
            roleService.save(role);
        }
        return role;
    }
}
