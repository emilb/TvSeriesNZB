package com.greyzone.util;

import org.apache.commons.lang.SystemUtils;
import org.apache.commons.lang.builder.StandardToStringStyle;

@SuppressWarnings("serial")
public class ToStringStyleNewLine extends StandardToStringStyle {

    public ToStringStyleNewLine() {
        super();
        this.setUseShortClassName(true);
        this.setUseIdentityHashCode(false);
        this.setContentStart("[");
        this.setFieldSeparator(SystemUtils.LINE_SEPARATOR + "  ");
        this.setFieldSeparatorAtStart(true);
        this.setContentEnd(SystemUtils.LINE_SEPARATOR + "]");
    }
}
