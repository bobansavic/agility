package com.bobansavic.agility.assistant.handler;

import com.bobansavic.agility.helper.AgilityServiceHelper;
import com.bobansavic.agility.model.Project;
import com.bobansavic.agility.service.ProjectService;
import com.google.actions.api.ActionResponse;
import org.fon.master.bsavic.api.handler.IntentHandler;

import java.util.Set;

public class GetProjectsIntentHandler extends IntentHandler {
    public GetProjectsIntentHandler(String intentName) {
        super(intentName);
    }

    @Override
    public ActionResponse process(Object input) {
        ProjectService projectService = AgilityServiceHelper.getProjectService();
        Set<Project> projectSet = projectService.getAllProjects();
        int projectCount = projectSet.size();
        Project[] allProjects = new Project[projectCount];
        System.arraycopy(projectSet.toArray(), 0, allProjects, 0, projectCount);

        StringBuilder stringBuilder = new StringBuilder();
        if (projectCount == 0) {
            stringBuilder.append("I didn't find any projects.");
        } else if (projectCount == 1) {
            stringBuilder.append("There's only one project: ");
            stringBuilder.append(allProjects[0].getTitle());
            stringBuilder.append(".");
        } else {
            stringBuilder.append(" I found ");
            stringBuilder.append(projectCount);
            stringBuilder.append(" projects: \n");
            for (int i = 0; i < projectCount; i++) {
                if (i == projectCount - 1) {
                    stringBuilder.append("and ");
                    stringBuilder.append(allProjects[i].getTitle());
                    stringBuilder.append(".");
                } else if (i == projectCount - 2){
                    stringBuilder.append(allProjects[i].getTitle());
                    stringBuilder.append(" \n");
                } else {
                    stringBuilder.append(allProjects[i].getTitle());
                    stringBuilder.append(", \n");
                }
            }
        }
        stringBuilder.append("\nWhat would you like to do?");
        ApiRequestResolver resolver = new ApiRequestResolver(input);
        resolver.setOutputSpeech(stringBuilder.toString());
        resolver.setEndOfConversation(false);
        resolver.setNextScene("StartProper");
        return processResponse(resolver);
    }
}
