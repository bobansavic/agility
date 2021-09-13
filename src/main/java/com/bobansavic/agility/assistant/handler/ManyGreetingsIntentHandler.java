package com.bobansavic.agility.assistant.handler;

import com.google.actions.api.ActionResponse;
import com.google.actions.api.ForIntent;
import org.fon.master.bsavic.api.handler.IntentHandler;
import org.fon.master.bsavic.api.model.Slot;

public class ManyGreetingsIntentHandler extends IntentHandler {
    private static final String SLOT_NAME = "greeting_times";
    public ManyGreetingsIntentHandler(String intentName) {
        super(intentName);
    }

    @ForIntent
    @Override
    public ActionResponse process(Object input) {
        ApiRequestResolver resolver = new ApiRequestResolver(input);
        Slot slot = resolver.getSlot(SLOT_NAME);
        if (slot == null) {
            resolver.setOutputSpeech("How many times would you like me to greet you?");
            resolver.setEndOfConversation(false);
        } else {
            int slotValue = slot.getValueAsInteger();
            StringBuilder response = new StringBuilder();
            if (slotValue < 0) {
                response = new StringBuilder("That is not a valid input.");
            } else if (slotValue == 0) {
                response = new StringBuilder("Ok, I won't greet you.");
            } else {
                for (int i = slotValue; i > 0; i--) {
                    if (slotValue == 1) {
                        response.append("Hello!");
                    } else if (i > 1 && i == slotValue) {
                        response.append("Hello");
                    } else if (i == 1) {
                        response.append(", hello!");
                    } else {
                        response.append(", hello");
                    }
                }
            }
            resolver.setOutputSpeech(response.toString());
            resolver.setEndOfConversation(true);
        }
        return processResponse(resolver);
    }
}
