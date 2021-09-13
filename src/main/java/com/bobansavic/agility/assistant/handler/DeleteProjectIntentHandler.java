package com.bobansavic.agility.assistant.handler;

import com.bobansavic.agility.helper.AgilityServiceHelper;
import com.bobansavic.agility.model.Project;
import com.google.actions.api.ActionResponse;
import org.fon.master.bsavic.api.handler.IntentHandler;
import org.fon.master.bsavic.api.model.Slot;

public class DeleteProjectIntentHandler extends IntentHandler {
    private static final String SLOT_DELETE_PROJECT_TITLE = "delete_project_title";
    private static final String SLOT_CONTINUE_CONFIRM = "continue_confirm";
    private static final String SESSION_PROJECT_DELETED_FLAG = "project_deleted_flag";

    public DeleteProjectIntentHandler(String intentName) {
        super(intentName);
    }

    @Override
    public ActionResponse process(Object input) {
        ApiRequestResolver resolver = new ApiRequestResolver(input);
        Slot deleteProjectSlot = resolver.getSlot(SLOT_DELETE_PROJECT_TITLE);
        Slot continueConfirm = resolver.getSlot(SLOT_CONTINUE_CONFIRM);
        Object sessionProjectDeletedFlag = resolver.getSessionStorage().get(SESSION_PROJECT_DELETED_FLAG);
        String response = "";
        if (sessionProjectDeletedFlag != null && Boolean.parseBoolean(sessionProjectDeletedFlag.toString())) {
            if (continueConfirm != null && continueConfirm.getValue().equalsIgnoreCase("yes")) {
                resolver.setOutputSpeech("Alright, goodbye.");
                resolver.setEndOfConversation(true);
                return processResponse(resolver);
            } else if (continueConfirm != null && continueConfirm.getValue().equalsIgnoreCase("no")) {
                response = "Is there something else I can help you with?";
                resolver.setOutputSpeech(response);
                resolver.setEndOfConversation(false);
                resolver.setNextScene("StartProper");
                return processResponse(resolver);
            } else {
                resolver.setEndOfConversation(true);
                return processResponse(resolver);
            }
        }
        if (deleteProjectSlot != null) {
            String projectToDelete = deleteProjectSlot.getValue();
            Project project = AgilityServiceHelper.getProjectService().findProjectByTitle(projectToDelete);
            if (project != null) {
                AgilityServiceHelper.getProjectService().deleteProject(project.getTitle());
                response = "Ok, I've deleted the project " + project.getTitle() + ". Would that be all?";
                resolver.getSessionStorage().put(SESSION_PROJECT_DELETED_FLAG, true);
                resolver.setOutputSpeech(response);
                resolver.setSlotToElicit(SLOT_CONTINUE_CONFIRM);
            } else {
                response = "I'm sorry, there are no projects with that title.";
                resolver.setEndOfConversation(true);
            }
        } else {
            resolver.setOutputSpeech("Which project would you like to delete?");
            resolver.setSlotToElicit(SLOT_DELETE_PROJECT_TITLE);
            resolver.setEndOfConversation(false);
            return processResponse(resolver);
        }
        resolver.setOutputSpeech(response);
        return processResponse(resolver);
    }
}
