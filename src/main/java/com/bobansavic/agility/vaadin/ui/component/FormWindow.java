package com.bobansavic.agility.vaadin.ui.component;

import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

public class FormWindow extends Window {

    private Panel main;
    private HorizontalLayout footerButtons;

    public FormWindow(String title) {
        center();
        setWidth(60, Unit.PERCENTAGE);
        setHeight(60, Unit.PERCENTAGE);
        setCaption(title);
        setCaptionAsHtml(true);
        setDraggable(true);
        setResponsive(true);

        VerticalLayout holder = new VerticalLayout();
        holder.setMargin(false);
        holder.setSizeFull();


        main = new Panel();
        main.addStyleName("form-window-main");
        main.setSizeFull();
        main.addStyleName(ValoTheme.PANEL_BORDERLESS);
        holder.addComponentsAndExpand(main);

        HorizontalLayout footerLayout = new HorizontalLayout();
        footerLayout.setWidth(100, Unit.PERCENTAGE);
        footerLayout.addStyleName("form-window-footer");

        footerButtons = new HorizontalLayout();
        footerButtons.setSizeUndefined();
        footerLayout.addComponent(footerButtons);
        footerLayout.setComponentAlignment(footerButtons, Alignment.MIDDLE_RIGHT);
        holder.addComponent(footerLayout);
        setContent(holder);
    }

    public FormWindow(String title, Button saveBtn, Button deleteBtn) {
        center();
        setWidth(60, Unit.PERCENTAGE);
        setHeight(60, Unit.PERCENTAGE);
        setCaption(title);
        setCaptionAsHtml(true);
        setDraggable(true);
        setResponsive(true);

        VerticalLayout holder = new VerticalLayout();
        holder.setMargin(false);
        holder.setSizeFull();


        main = new Panel();
        main.addStyleName("form-window-main");
        main.setSizeFull();
        main.addStyleName(ValoTheme.PANEL_BORDERLESS);
        holder.addComponentsAndExpand(main);

        HorizontalLayout footerLayout = new HorizontalLayout();
        footerLayout.setWidth(100, Unit.PERCENTAGE);
        footerLayout.addStyleName("form-window-footer");

        footerButtons = new HorizontalLayout();
        footerButtons.setSizeUndefined();
        footerButtons.addComponent(saveBtn);
        footerButtons.addComponent(deleteBtn);
        footerLayout.addComponent(footerButtons);
        footerLayout.setComponentAlignment(footerButtons, Alignment.MIDDLE_RIGHT);
        holder.addComponent(footerLayout);
        setContent(holder);
    }

    public void setFormView(Component c) {
        if (c != null) {
            this.main.setContent(c);
        }
    }

    public void addFooterBtn(Button btn) {
        addFooterBtn(btn, false);
    }

    public void addFooterBtn(Button btn, boolean closeWindowOnClick) {
        if (closeWindowOnClick) {
            btn.addClickListener(e -> close());
        }
        footerButtons.addComponent(btn);
    }

    public HorizontalLayout getFooter() {
        return footerButtons;
    }
}
