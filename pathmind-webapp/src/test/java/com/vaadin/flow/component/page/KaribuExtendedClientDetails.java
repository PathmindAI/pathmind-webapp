package com.vaadin.flow.component.page;

public class KaribuExtendedClientDetails extends ExtendedClientDetails {

    public KaribuExtendedClientDetails(String screenWidth, String screenHeight, String windowInnerWidth,
                                       String windowInnerHeight, String bodyClientWidth, String bodyClientHeight,
                                       String tzOffset, String rawTzOffset, String dstShift, String dstInEffect,
                                       String tzId, String curDate, String touchDevice, String devicePixelRatio,
                                       String windowName) {
        super(screenWidth, screenHeight, windowInnerWidth, windowInnerHeight, bodyClientWidth, bodyClientHeight,
                tzOffset, rawTzOffset, dstShift, dstInEffect, tzId, curDate, touchDevice, devicePixelRatio, windowName);
    }

    public KaribuExtendedClientDetails() {
        this("1920", "1200", "1024", "640",
                "1024", "640", "420", "480", "60",
                "true", "America/Los_Angeles", "", "false", "2", "ROOT-1");
    }
}
