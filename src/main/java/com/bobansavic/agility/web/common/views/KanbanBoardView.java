package com.bobansavic.agility.web.common.views;

import com.bobansavic.agility.model.*;
import com.bobansavic.agility.service.ProjectService;
import com.bobansavic.agility.service.TaskService;
import com.bobansavic.agility.service.UserService;
import com.bobansavic.agility.web.common.Menu;
import com.bobansavic.agility.web.common.SecurityUtils;
import com.bobansavic.agility.web.common.UiPart;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.List;

@UIScope
@SpringView
@Menu(icon = VaadinIcons.SERVER, name = "Kanban board", order = 100, availiableFor = {UiPart.ADMIN, UiPart.USER, UiPart.PM, UiPart.PROJECT})
public class KanbanBoardView extends HorizontalLayout implements View {
    private static final Logger log = LoggerFactory.getLogger(KanbanBoardView.class);

    private static final String BACKLOG = "Backlog";
    private static final String IN_PROGRESS = "In progress";
    private static final String TESTING = "Testing";
    private static final String DONE = "Done";

    @Autowired
    private TaskService taskService;
    @Autowired
    private UserService userService;
    @Autowired
    private ProjectService projectService;

    VerticalLayout backlog;
    VerticalLayout inProgress;
    VerticalLayout testing;
    VerticalLayout done;

    VerticalLayout backlogHolderOuter;
    VerticalLayout inProgressHolderOuter;
    VerticalLayout testingHolderOuter;
    VerticalLayout doneHolderOuter;

    VerticalLayout backlogHolderInner;
    VerticalLayout inProgressHolderInner;
    VerticalLayout testingHolderInner;
    VerticalLayout doneHolderInner;

    Label backlogHeader;
    Label inProgressHeader;
    Label testingHeader;
    Label doneHeader;

    private TicketManagementView ticketManagementView = new TicketManagementView();

    @PostConstruct
    public void postConstruct() {
        setSizeFull();
        addStyleName("kanban-view");

        generateHeaders();
        generateBacklog();
        generateInProgress();
        generateTesting();
        generateDone();

        generateTickets();

        generateHolders();
    }

    private void generateBacklog() {
        backlog = new VerticalLayout();
        backlog.setHeightUndefined();
        backlog.setWidthFull();
        backlog.setStyleName("kanban-column");

        backlogHolderOuter = new VerticalLayout();
        backlogHolderOuter.setWidthFull();
        backlogHolderOuter.setHeight(90, Unit.PERCENTAGE);
        backlogHolderOuter.setStyleName("kanban-column-holder-outer");

        backlogHolderInner = new VerticalLayout();
        backlogHolderInner.setSizeFull();
        backlogHolderInner.setStyleName("kanban-column-holder-inner");
    }

    private void generateInProgress() {
        inProgress = new VerticalLayout();
        inProgress.setHeightUndefined();
        inProgress.setWidthFull();
        inProgress.setStyleName("kanban-column");

        inProgressHolderOuter = new VerticalLayout();
        inProgressHolderOuter.setWidthFull();
        inProgressHolderOuter.setHeight(90, Unit.PERCENTAGE);
        inProgressHolderOuter.setStyleName("kanban-column-holder-outer");

        inProgressHolderInner = new VerticalLayout();
        inProgressHolderInner.setSizeFull();
        inProgressHolderInner.setStyleName("kanban-column-holder-inner");
    }

    private void generateTesting() {
        testing = new VerticalLayout();
        testing.setHeightUndefined();
        testing.setWidthFull();
        testing.setStyleName("kanban-column");

        testingHolderOuter = new VerticalLayout();
        testingHolderOuter.setWidthFull();
        testingHolderOuter.setHeight(90, Unit.PERCENTAGE);
        testingHolderOuter.setStyleName("kanban-column-holder-outer");

        testingHolderInner = new VerticalLayout();
        testingHolderInner.setSizeFull();
        testingHolderInner.setStyleName("kanban-column-holder-inner");
    }

    private void generateDone() {
        done = new VerticalLayout();
        done.setHeightUndefined();
        done.setWidthFull();
        done.setStyleName("kanban-column");

        doneHolderOuter = new VerticalLayout();
        doneHolderOuter.setWidthFull();
        doneHolderOuter.setHeight(90, Unit.PERCENTAGE);
        doneHolderOuter.setStyleName("kanban-column-holder-outer");

        doneHolderInner = new VerticalLayout();
        doneHolderInner.setSizeFull();
        doneHolderInner.setStyleName("kanban-column-holder-inner");
    }

    private void generateHeaders() {
        backlogHeader = new Label("Backlog");
        backlogHeader.setWidthFull();
//        backlogHeader.setHeight(60, Unit.PIXELS);
        backlogHeader.setHeightFull();
        backlogHeader.addStyleName("kanban-header");
        backlogHeader.addStyleName("kanban-header-backlog");

        inProgressHeader = new Label("In Progress");
        inProgressHeader.setWidthFull();
        inProgressHeader.setHeight(60, Unit.PIXELS);
        inProgressHeader.addStyleName("kanban-header");
        inProgressHeader.addStyleName("kanban-header-inprogress");

        testingHeader = new Label("Testing");
        testingHeader.setWidthFull();
        testingHeader.setHeight(60, Unit.PIXELS);
        testingHeader.addStyleName("kanban-header");
        testingHeader.addStyleName("kanban-header-testing");

        doneHeader = new Label("Done");
        doneHeader.setWidthFull();
        doneHeader.setHeight(60, Unit.PIXELS);
        doneHeader.addStyleName("kanban-header");
        doneHeader.addStyleName("kanban-header-done");
    }

    private void generateTickets() {

        List<Task> allTasks = taskService.findTasksForProject(projectService.findProjectByTitle(SecurityUtils.getCurrentProjectTitle()));

        for (Task task : allTasks) {

            Label priorityLabel = new Label();
            priorityLabel.setHeightFull();
            priorityLabel.setWidthFull();
            if (task.getPriority().equals(TaskPriority.Low.name())) {
                priorityLabel.setStyleName("priority-label-low");
            } else {
                priorityLabel.setStyleName("priority-label-high");
            }

            HorizontalLayout ticketLabelHolder = new HorizontalLayout();
            ticketLabelHolder.setWidthFull();
            ticketLabelHolder.setHeight(50, Unit.PIXELS);
            ticketLabelHolder.setStyleName("kanban-ticket-holder");

            Label ticketLabel = new Label(task.getTitle());
            ticketLabel.setWidthFull();
            ticketLabel.setHeightUndefined();

            ticketLabelHolder.addComponents(priorityLabel, ticketLabel);
            ticketLabelHolder.setExpandRatio(priorityLabel, 1);
            ticketLabelHolder.setExpandRatio(ticketLabel, 19);
            ticketLabelHolder.setComponentAlignment(ticketLabel, Alignment.MIDDLE_CENTER);
            ticketLabelHolder.addLayoutClickListener(e -> {
                TaskStore store = taskService.findTaskStoreByTitle(task.getTitle());
                ticketManagementView.edit(store, userService, taskService, projectService);
            });
            switch (task.getStatus()) {
                case BACKLOG:
                    backlog.addComponent(ticketLabelHolder);
                    break;
                case IN_PROGRESS:
                    inProgress.addComponent(ticketLabelHolder);
                    break;
                case TESTING:
                    testing.addComponent(ticketLabelHolder);
                    break;
                case DONE:
                    done.addComponent(ticketLabelHolder);
                    break;
                default:
                    log.warn("Unable to add ticket {} to Kanban Board: Unknown status.", task.getTitle());
            }
        }
    }

    private void generateHolders() {
        backlogHolderInner.addComponent(backlog);
        backlogHolderOuter.addComponent(backlogHeader);
        backlogHolderOuter.addComponent(backlogHolderInner);
        backlogHolderOuter.setExpandRatio(backlogHeader, 1);
        backlogHolderOuter.setExpandRatio(backlogHolderInner, 12);

        inProgressHolderInner.addComponent(inProgress);
        inProgressHolderOuter.addComponent(inProgressHeader);
        inProgressHolderOuter.addComponent(inProgressHolderInner);
        inProgressHolderOuter.setExpandRatio(inProgressHeader, 1);
        inProgressHolderOuter.setExpandRatio(inProgressHolderInner, 12);

        testingHolderInner.addComponent(testing);
        testingHolderOuter.addComponent(testingHeader);
        testingHolderOuter.addComponent(testingHolderInner);
        testingHolderOuter.setExpandRatio(testingHeader, 1);
        testingHolderOuter.setExpandRatio(testingHolderInner, 12);

        doneHolderInner.addComponent(done);
        doneHolderOuter.addComponent(doneHeader);
        doneHolderOuter.addComponent(doneHolderInner);
        doneHolderOuter.setExpandRatio(doneHeader, 1);
        doneHolderOuter.setExpandRatio(doneHolderInner, 12);

        addComponents(backlogHolderOuter, inProgressHolderOuter, testingHolderOuter, doneHolderOuter);
        setExpandRatio(backlogHolderOuter, 1);
        setExpandRatio(inProgressHolderOuter, 1);
        setExpandRatio(testingHolderOuter, 1);
        setExpandRatio(doneHolderOuter, 1);
        setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
    }
}
