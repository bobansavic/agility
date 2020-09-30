package com.bobansavic.agility.web.common.views;

import com.bobansavic.agility.web.common.SecurityUtils;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.stereotype.Component;

@Component
@UIScope
public class AccessDeniedView extends VerticalLayout implements View {

    public AccessDeniedView() {
        setMargin(true);
        Label lbl = new Label("Access denied!");
        lbl.addStyleName(ValoTheme.LABEL_FAILURE);
        lbl.setSizeUndefined();
        addComponent(lbl);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        if (!SecurityUtils.isLoggedIn()) {
            UI.getCurrent().getPage().setLocation("");
        }
    }
}
