package com.bobansavic.agility.assistant.handler;

import com.bobansavic.agility.helper.AgilityServiceHelper;
import com.bobansavic.agility.model.Project;
import com.google.actions.api.ActionResponse;
import org.fon.master.bsavic.api.handler.IntentHandler;
import org.fon.master.bsavic.api.model.Slot;

public class ManageProjectIntentHandler extends IntentHandler {
    private static final String MANAGE_PROJECT_SLOT = "manage_project_title";
    private static final String SESSION_PROJECT = "session_project_title";

    public ManageProjectIntentHandler(String intentName) {
        super(intentName);
    }

    @Override
    public ActionResponse process(Object input) {
        ApiRequestResolver resolver = new ApiRequestResolver(input);
        Slot manageProjectSlot = resolver.getSlot(MANAGE_PROJECT_SLOT);
        String response = "";
        if (manageProjectSlot != null) {
            String projectToManage = manageProjectSlot.getValue();
            Project project = AgilityServiceHelper.getProjectService().findProjectByTitle(projectToManage);
            if (project != null) {
                response = "Ok, what would you like to do?";
                resolver.getSessionStorage().put(SESSION_PROJECT, projectToManage);
                resolver.setEndOfConversation(false);
            } else {
                response = "I'm sorry, there are no projects with that title.";
                resolver.setEndOfConversation(true);
            }
        } else {
            resolver.setOutputSpeech("Which project would you like to manage?");
            resolver.setSlotToElicit(MANAGE_PROJECT_SLOT);
            resolver.setEndOfConversation(false);
            return processResponse(resolver);
        }
        resolver.setOutputSpeech(response);
        return processResponse(resolver);
    }
}
