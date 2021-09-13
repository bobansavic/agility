package com.bobansavic.agility.assistant.handler;

import com.bobansavic.agility.helper.AgilityServiceHelper;
import com.bobansavic.agility.model.Project;
import com.bobansavic.agility.model.Task;
import com.bobansavic.agility.model.TaskPriority;
import com.bobansavic.agility.model.TicketStatus;
import com.google.actions.api.ActionResponse;
import org.fon.master.bsavic.api.handler.IntentHandler;
import org.fon.master.bsavic.api.model.Slot;

public class CreateTicketIntentHandler extends IntentHandler {
    private static final String SESSION_PROJECT = "session_project_title";

    public CreateTicketIntentHandler(String intentName) {
        super(intentName);
    }

    @Override
    public ActionResponse process(Object input) {
        ApiRequestResolver resolver = new ApiRequestResolver(input);
        Object sessionProjectTitle = resolver.getSessionStorage().get(SESSION_PROJECT);
        Slot projectSlot = resolver.getSlot("managed_project_title");
        Slot ticketSlot = resolver.getSlot("new_ticket_title");
        String response = "";
        if (projectSlot != null || sessionProjectTitle != null) {
            Project project;
            if (sessionProjectTitle != null) {
                project = AgilityServiceHelper.getProjectService().findProjectByTitle(sessionProjectTitle.toString());
            } else {
                project = AgilityServiceHelper.getProjectService().findProjectByTitle(projectSlot.getValue());
            }
            if (project != null) {
                if (ticketSlot != null) {
                    Task newTask = createNewTask(ticketSlot.getValue(), project);
                    response = "Ok, I've created the " + newTask.getTitle() + " ticket for the " + project.getTitle() + " project.";
                } else {
                    response = "What will be the ticket title?";
                    resolver.setOutputSpeech(response);
                    resolver.setSlotToElicit("new_ticket_title");
                    resolver.setEndOfConversation(false);
                    return processResponse(resolver);
                }
            } else {
                response = "That project doesn't exist.";
                resolver.setOutputSpeech(response);
                resolver.setEndOfConversation(true);
                return processResponse(resolver);
            }
        } else {
            response = "For which project?";
            resolver.setOutputSpeech(response);
            resolver.setSlotToElicit("managed_project_title");
            resolver.setEndOfConversation(false);
            return processResponse(resolver);
        }
        resolver.setOutputSpeech(response);
        resolver.setEndOfConversation(true);
        return processResponse(resolver);
    }

    private Task createNewTask(String taskTitle, Project project) {
        Task newTask = new Task();
        newTask.setTitle(taskTitle);
        newTask.setProject(project);
        newTask.setStatus(TicketStatus.BACKLOG.getValue());
        newTask.setPriority(TaskPriority.High.name());
        return AgilityServiceHelper.getTaskService().saveTask(newTask);
    }
}
