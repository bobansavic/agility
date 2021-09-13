package com.bobansavic.agility.assistant.handler;

import com.bobansavic.agility.helper.AgilityServiceHelper;
import com.bobansavic.agility.model.Project;
import com.google.actions.api.ActionResponse;
import com.google.actions.api.ForIntent;
import org.fon.master.bsavic.api.handler.IntentHandler;
import org.fon.master.bsavic.api.model.Slot;

public class CreateProjectIntentHandler extends IntentHandler {
    private static final String CREATE_PROJECT_SLOT = "create_project_title";
    public CreateProjectIntentHandler(String intentName) {
        super(intentName);
    }

    @Override
    @ForIntent
    public ActionResponse process(Object input) {
        ApiRequestResolver resolver = new ApiRequestResolver(input);
        Slot createProjectSlot = resolver.getSlot(CREATE_PROJECT_SLOT);
        String response = "";
        if (createProjectSlot != null) {
            String projectToCreate = createProjectSlot.getValue();
            Project newProject = new Project();
            newProject.setTitle(projectToCreate);
            AgilityServiceHelper.getProjectService().saveProject(newProject);
            response = "Ok, I've created the project " + projectToCreate + ". Anything else?";
        } else {
            resolver.setOutputSpeech("What will be the title of the new project?");
            resolver.setSlotToElicit(CREATE_PROJECT_SLOT);
            resolver.setEndOfConversation(false);
            return processResponse(resolver);
        }
        resolver.setOutputSpeech(response);
        return processResponse(resolver);
    }
}
