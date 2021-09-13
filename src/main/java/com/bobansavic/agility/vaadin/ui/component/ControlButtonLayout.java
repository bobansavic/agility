package com.bobansavic.agility.vaadin.ui.component;

import com.bobansavic.agility.vaadin.ui.utils.UiUtils;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

public class ControlButtonLayout extends HorizontalLayout {

    private HorizontalLayout left = new HorizontalLayout();
    private HorizontalLayout right = new HorizontalLayout();

    private MenuBar actionMenuBar;
    private MenuBar.MenuItem actionMenuItem;
    private Button refreshButton;
    private MenuBar.MenuItem deleteMenuItem;
    private ConfirmDeleteDialog confirmDialog;

    public ControlButtonLayout() {
        setWidth(100, Unit.PERCENTAGE);

        left.setSizeUndefined();
        addComponent(left);
        setComponentAlignment(left, Alignment.BOTTOM_LEFT);

        right.setSizeUndefined();
        addComponent(right);
        setComponentAlignment(right, Alignment.BOTTOM_RIGHT);

        confirmDialog = new ConfirmDeleteDialog("Delete selected items?");
        actionMenuBar = new MenuBar();
        actionMenuItem = actionMenuBar.addItem("Action", null);
        addRight(actionMenuBar);

        deleteMenuItem = actionMenuItem.addItem("Delete", VaadinIcons.TRASH, c -> {
            if (this.confirmDialog.getParent() == null) {
                UI.getCurrent().addWindow(this.confirmDialog);
            }
        });
        deleteMenuItem.setVisible(false);
        deleteMenuItem.setEnabled(false);
        deleteMenuItem.setStyleName(ValoTheme.BUTTON_DANGER);

        refreshButton = UiUtils.createRefreshGridButton();
        refreshButton.setVisible(false);
        addRight(refreshButton);
    }

    public void setDeleteAction(Button.ClickListener listener) {
        this.deleteMenuItem.setVisible(true);
        this.confirmDialog.addYesAction(listener);
    }

    public void setRefreshAction(Button.ClickListener listener) {
        this.refreshButton.setVisible(true);
        this.refreshButton.addClickListener(listener);
    }

    public MenuBar.MenuItem getActionMenuItem() {
        return actionMenuItem;
    }

    public void setDeleteEnabled(boolean enabled) {
        this.deleteMenuItem.setEnabled(enabled);
    }

    public void addRight(Component c) {
        right.addComponent(c);
    }

    public void addLeft(Component c) {
        left.addComponent(c);
    }

    public void removeActionMenu() {
        right.removeAllComponents();
    }

    @Override
    public void attach() {
        boolean hasActionItems = this.actionMenuItem.getSize() > 0;
        this.actionMenuBar.setVisible(hasActionItems);
        super.attach();
    }
}
