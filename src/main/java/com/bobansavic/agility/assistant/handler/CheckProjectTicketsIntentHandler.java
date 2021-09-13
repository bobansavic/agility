package com.bobansavic.agility.assistant.handler;

import com.bobansavic.agility.helper.AgilityServiceHelper;
import com.bobansavic.agility.model.Project;
import com.bobansavic.agility.model.Task;
import com.google.actions.api.ActionResponse;
import com.google.common.base.Strings;
import org.fon.master.bsavic.api.handler.IntentHandler;
import org.fon.master.bsavic.api.model.Slot;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CheckProjectTicketsIntentHandler extends IntentHandler {
    private static final String SESSION_PROJECT = "session_project_title";
    private static final String SLOT_MANAGED_PROJECT = "managed_project_title";
    private static final String SLOT_TICKET_COLUMN = "ticket_column";

    public CheckProjectTicketsIntentHandler(String intentName) {
        super(intentName);
    }

    @Override
    public ActionResponse process(Object input) {
        ApiRequestResolver resolver = new ApiRequestResolver(input);
        Object sessionProject = resolver.getSessionStorage().get(SESSION_PROJECT);
        Slot managedProjectSlot = resolver.getSlot(SLOT_MANAGED_PROJECT);
        Slot ticketColumnSlot = resolver.getSlot(SLOT_TICKET_COLUMN);
        String response = "";
        Project project = null;
        if (sessionProject != null
                || (managedProjectSlot != null && !Strings.isNullOrEmpty(managedProjectSlot.getValue()))) {
            if (sessionProject != null) {
                project = AgilityServiceHelper.getProjectService().findProjectByTitle(sessionProject.toString());
            } else if (managedProjectSlot != null && !Strings.isNullOrEmpty(managedProjectSlot.getValue())) {
                project = AgilityServiceHelper.getProjectService().findProjectByTitle(managedProjectSlot.getValue());
            }
            if (project == null) {
                response = "That project doesn't exist.";
                resolver.setOutputSpeech(response);
                resolver.setEndOfConversation(true);
                return processResponse(resolver);
            }

            if (ticketColumnSlot != null && !Strings.isNullOrEmpty(ticketColumnSlot.getValue())) {
                String ticketColumn = resolver.getSlot(SLOT_TICKET_COLUMN).getValue();
                Set<Task> tasks = project.getTasks();
                List<Task> targetedTasks = new ArrayList<>();
                for (Task t : tasks) {
                    if (t.getStatus().equalsIgnoreCase(ticketColumn)) {
                        targetedTasks.add(t);
                    }
                }
                if (!targetedTasks.isEmpty()) {
                    response = "There's " + targetedTasks.size() + " tickets in the " + ticketColumn + " column.";
                } else {
                    response = "There are no tickets in the " + ticketColumn + " column";
                }
            } else {
                response = "Which ticket column are you interested in?";
                resolver.setOutputSpeech(response);
                resolver.setEndOfConversation(false);
                resolver.setSlotToElicit(SLOT_TICKET_COLUMN);
                return processResponse(resolver);
            }

        } else {
            response = "For which project?";
            resolver.setOutputSpeech(response);
            resolver.setEndOfConversation(false);
            resolver.setSlotToElicit(SLOT_MANAGED_PROJECT);
            return processResponse(resolver);
        }
        resolver.setOutputSpeech(response);
        resolver.setEndOfConversation(true);
        return processResponse(resolver);
    }
}
