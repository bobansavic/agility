package com.bobansavic.agility.assistant.handler;

import com.google.actions.api.ActionResponse;
import org.fon.master.bsavic.api.handler.IntentHandler;

public class WelcomeIntentHandler extends IntentHandler {
    public WelcomeIntentHandler(String intentName) {
        super(intentName);
    }

    @Override
    public ActionResponse process(Object input) {
        ApiRequestResolver resolver = new ApiRequestResolver(input);
        resolver.setOutputSpeech("Welcome to Agility Manager. How can I help you?");
        resolver.setEndOfConversation(false);
        return processResponse(resolver);
    }
}
