package com.bobansavic.agility.helper;

import com.bobansavic.agility.config.SpringContext;
import com.bobansavic.agility.service.ProjectService;
import com.bobansavic.agility.service.TaskService;

public class AgilityServiceHelper {

    public static ProjectService getProjectService() {
        return SpringContext.getBean(ProjectService.class);
    }

    public static TaskService getTaskService() {
        return SpringContext.getBean(TaskService.class);
    }
}
