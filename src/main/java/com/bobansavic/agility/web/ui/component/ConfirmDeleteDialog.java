package com.bobansavic.agility.web.ui.component;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

public class ConfirmDeleteDialog extends Window {
    private Label infoLabel;
    private Button yesButton;
    private Button noButton;

    public ConfirmDeleteDialog(String text) {

        infoLabel = new Label(text, ContentMode.HTML);
        infoLabel.setSizeFull();

        yesButton = new Button("Yes");
        yesButton.addStyleName(ValoTheme.BUTTON_DANGER);
        yesButton.addClickListener(e -> close());

        noButton = new Button("No");
        noButton.focus();
        noButton.addClickListener(e -> close());

        HorizontalLayout buttons = new HorizontalLayout(yesButton, noButton);
        VerticalLayout main = new VerticalLayout(infoLabel, buttons);
        main.setComponentAlignment(buttons, Alignment.TOP_CENTER);

        setWidth(20, Unit.PERCENTAGE);
        setHeightUndefined();
        setModal(true);
        center();
        setContent(main);
        setIcon(VaadinIcons.WARNING);
    }

    public void addYesAction(Button.ClickListener yesListener) {
        if (yesListener != null) {
            yesButton.addClickListener(yesListener);
        }
    }
}
