package com.bobansavic.agility.assistant.servlet;

import com.bobansavic.agility.assistant.handler.*;
import org.fon.master.bsavic.api.servlet.SkillAndActionServlet;

    public class AgilitySkillAndActionServlet extends SkillAndActionServlet {

        public AgilitySkillAndActionServlet() {
            super("AgilityManager",
                    new WelcomeIntentHandler("WelcomeIntent"),
                    new GetProjectsIntentHandler("GetProjectsIntent"),
                    new ManageProjectIntentHandler("ManageProjectIntent"),
                    new CheckProjectTicketsIntentHandler("CheckProjectTicketsIntent"),
                    new CreateProjectIntentHandler("CreateProjectIntent"),
                    new DeleteProjectIntentHandler("DeleteProjectIntent"),
                    new CreateTicketIntentHandler("CreateTicketIntent"),
                    new DeleteTicketIntentHandler("DeleteTicketIntent"));
        }
    }
