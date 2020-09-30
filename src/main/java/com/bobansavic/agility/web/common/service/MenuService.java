package com.bobansavic.agility.web.common.service;

import com.bobansavic.agility.web.common.Menu;
import com.bobansavic.agility.web.common.SecurityUtils;
import com.bobansavic.agility.web.common.UiPart;
import com.bobansavic.agility.web.common.model.MenuItem;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.spring.access.ViewAccessControl;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.internal.Conventions;
import com.vaadin.ui.UI;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Service
public class MenuService {


    private final ApplicationContext applicationContext;
    private final Collection<ViewAccessControl> viewAccessControls;

    private List<MenuItem> allMenuItems = new ArrayList<>();

    public MenuService(ApplicationContext applicationContext, Collection<ViewAccessControl> viewAccessControls) {
        this.applicationContext = applicationContext;
        this.viewAccessControls = viewAccessControls;
    }


    @PostConstruct
    public void postConstruct() {
        String[] beanNames = applicationContext.getBeanNamesForAnnotation(Menu.class);
        for (String beanName : beanNames) {
            final Class<?> type = applicationContext.getType(beanName);
            if (View.class.isAssignableFrom(type) && type.isAnnotationPresent(SpringView.class)) {
                final SpringView viewAnnotation = AnnotatedElementUtils.findMergedAnnotation(type, SpringView.class);
                String viewName = getViewNameFromAnnotation(type, viewAnnotation);
                final Menu menuAnnotation = AnnotatedElementUtils.findMergedAnnotation(type, Menu.class);

                MenuItem mi = new MenuItem();
                mi.setBeanName(beanName);
                mi.setViewName(viewName);
                mi.setIcon(menuAnnotation.icon());
                mi.setName(menuAnnotation.name());
                mi.setTooltip(menuAnnotation.tooltip());
                mi.setOrder(menuAnnotation.order());
                mi.setUiPartList(menuAnnotation.availiableFor());

                allMenuItems.add(mi);
            }
        }
    }


    public List<MenuItem> getAllowedMenuItems(UI currentUI) {
        List<MenuItem> menuItems = new ArrayList<>();

        boolean admin = SecurityUtils.isAdmin();
        boolean projectManager = SecurityUtils.isProjectManager();

        for (MenuItem menuItem : this.allMenuItems) {
            UiPart requiredUiPart = UiPart.USER;
            if (admin) {
                requiredUiPart = UiPart.ADMIN;
            } else if (projectManager) {
                requiredUiPart = UiPart.PM;
            }

            if (!SecurityUtils.isProjectView()) {
                if (menuItem.containsUiPart(requiredUiPart) && !menuItem.containsUiPart(UiPart.PROJECT)) {
                    if (viewAccessControls != null && !viewAccessControls.isEmpty()) {
                        for (ViewAccessControl viewAccessControl : viewAccessControls) {
                            if (viewAccessControl.isAccessGranted(currentUI, menuItem.getBeanName())) {
                                menuItems.add(menuItem);
                            }
                        }
                    } else {
                        menuItems.add(menuItem);
                    }
                }
            } else {
                if (menuItem.containsUiPart(requiredUiPart) && menuItem.containsUiPart(UiPart.PROJECT)) {
                    if (viewAccessControls != null && !viewAccessControls.isEmpty()) {
                        for (ViewAccessControl viewAccessControl : viewAccessControls) {
                            if (viewAccessControl.isAccessGranted(currentUI, menuItem.getBeanName())) {
                                menuItems.add(menuItem);
                            }
                        }
                    } else {
                        menuItems.add(menuItem);
                    }
                }
            }
        }
        Collections.sort(menuItems);
        return menuItems;
    }

    private String getViewNameFromAnnotation(Class<?> beanClass, SpringView annotation) {
        String viewName = Conventions.deriveMappingForView(beanClass, annotation);
        return applicationContext.getEnvironment().resolvePlaceholders(viewName);
    }
}
