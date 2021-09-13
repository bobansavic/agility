package com.bobansavic.agility.vaadin.ui.utils;

import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

public class MenuLayout extends HorizontalLayout {

    private CssLayout contentArea = new CssLayout();
    private CssLayout menuArea = new CssLayout();
    private CssLayout menu = new CssLayout();
    private CssLayout menuItemsLayout = new CssLayout();

    public MenuLayout() {
        setSizeFull();

        menuArea.setPrimaryStyleName(ValoTheme.MENU_ROOT);

        menu.addStyleName(ValoTheme.MENU_PART);
        menuArea.addComponent(menu);

        menuItemsLayout.setPrimaryStyleName("valo-menuitems");
        menu.addComponent(menuItemsLayout);

        contentArea.setPrimaryStyleName("valo-content");
        contentArea.addStyleName("v-scrollable");
        contentArea.setSizeFull();

        addComponents(menuArea, contentArea);
        setExpandRatio(contentArea, 1);
    }

    public void setMenuId(String menuId) {
        menu.setId(menuId);
    }

    public CssLayout getMenu() {
        return menu;
    }

    public ComponentContainer getContentContainer() {
        return contentArea;
    }

    public void addMenuButton(Button button) {
        if (button != null) {
            button.setPrimaryStyleName(ValoTheme.MENU_ITEM);
            menuItemsLayout.addComponent(button);
        }
    }

    public void setMenu(Component menu) {
        menu.addStyleName(ValoTheme.MENU_PART);
        menuArea.addComponent(menu);
    }
}
