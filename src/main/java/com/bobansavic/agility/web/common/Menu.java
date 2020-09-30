package com.bobansavic.agility.web.common;

import com.vaadin.icons.VaadinIcons;

import java.lang.annotation.*;

@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Menu {

    public VaadinIcons icon() default VaadinIcons.MENU;

    public String name() default "";

    public String tooltip() default "";

    public UiPart[] availiableFor() default { UiPart.USER };

    public int order() default 0;
}
