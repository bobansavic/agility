package com.bobansavic.agility.vaadin.common.model;

import com.bobansavic.agility.vaadin.common.UiPart;
import com.vaadin.server.FontIcon;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MenuItem implements Comparable<MenuItem> {

    private String beanName;
    private String viewName;
    private FontIcon icon;
    private String name;
    private String tooltip;
    private int order;
    private List<UiPart> uiPartList = new ArrayList<>();

    public String getBeanName() {
        return beanName;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    public String getViewName() {
        return viewName;
    }

    public void setViewName(String viewName) {
        this.viewName = viewName;
    }

    public FontIcon getIcon() {
        return icon;
    }

    public void setIcon(FontIcon icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTooltip() {
        return tooltip;
    }

    public void setTooltip(String tooltip) {
        this.tooltip = tooltip;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public List<UiPart> getUiPartList() {
        return uiPartList;
    }

    public void setUiPartList(List<UiPart> uiPartList) {
        this.uiPartList = uiPartList;
    }

    public void setUiPartList(UiPart[] uiPartList) {
        if (uiPartList != null) {
            this.uiPartList = Arrays.asList(uiPartList);
        }
    }

    public boolean containsUiPart(UiPart uiPart) {
        return this.uiPartList != null && uiPart != null && this.uiPartList.contains(uiPart);
    }

    @Override
    public int compareTo(MenuItem menuItem) {
        return Integer.compare(order, menuItem.order);
    }
}
