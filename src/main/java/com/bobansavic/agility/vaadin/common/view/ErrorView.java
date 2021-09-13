package com.bobansavic.agility.vaadin.common.view;

import com.bobansavic.agility.vaadin.common.SecurityUtils;
import com.bobansavic.agility.vaadin.ui.utils.UiUtils;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import org.springframework.stereotype.Component;

@Component
@UIScope
public class ErrorView extends VerticalLayout implements View {

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        if (!SecurityUtils.isLoggedIn()) {
            UI.getCurrent().getNavigator().navigateTo(UiUtils.getLoginViewName());
        }
    }
}
