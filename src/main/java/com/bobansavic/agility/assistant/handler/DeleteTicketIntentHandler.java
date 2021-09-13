package com.bobansavic.agility.assistant.handler;

import com.google.actions.api.ActionResponse;
import org.fon.master.bsavic.api.handler.IntentHandler;

public class DeleteTicketIntentHandler extends IntentHandler {
    public DeleteTicketIntentHandler(String intentName) {
        super(intentName);
    }

    @Override
    public ActionResponse process(Object input) {
        return null;
    }
}
