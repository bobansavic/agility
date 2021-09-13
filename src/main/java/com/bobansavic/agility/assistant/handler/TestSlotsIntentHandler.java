package com.bobansavic.agility.assistant.handler;

import com.google.actions.api.ActionResponse;
import org.fon.master.bsavic.api.handler.IntentHandler;
import org.fon.master.bsavic.api.model.Slot;

public class TestSlotsIntentHandler extends IntentHandler {
    private static final String SLOT_CHOSEN_OPTIONS = "chosenOption";
    public TestSlotsIntentHandler(String intentName) {
        super(intentName);
    }

    @Override
    public ActionResponse process(Object input) {
        ApiRequestResolver resolver = new ApiRequestResolver(input);
        Slot chosenOption = resolver.getSlot("chosenOption");
        if (chosenOption != null) {
            resolver.setOutputSpeech("You have chosen: " + chosenOption.getValue());
            resolver.setEndOfConversation(true);
            return processResponse(resolver);
        } else {
            resolver.setOutputSpeech("What will be your chosenOption?");
            resolver.setEndOfConversation(false);
            resolver.setSlotToElicit("chosenOption");
            return processResponse(resolver);
        }
    }
}
