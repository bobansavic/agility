package com.bobansavic.agility.assistant.handler;

import com.google.actions.api.ActionResponse;
import com.google.actions.api.ForIntent;
import org.fon.master.bsavic.api.handler.IntentHandler;

public class HelloIntentHandler extends IntentHandler {
    public HelloIntentHandler(String intentName) {
        super(intentName);
    }

    @ForIntent
    @Override
    public ActionResponse process(Object input) {
        ApiRequestResolver resolver = new ApiRequestResolver(input);
        resolver.setOutputSpeech("This is your HelloIntent handler and it told me to say hello. Try an action.");
        resolver.setEndOfConversation(false);
        return processResponse(resolver);
    }
}
