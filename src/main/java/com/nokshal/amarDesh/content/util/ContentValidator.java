package com.nokshal.amarDesh.content.util;

public class ContentValidator {

    public static boolean isValidTitle(String title) {
        return title != null && !title.trim().isEmpty() && title.length() <= 100;
    }

    public static boolean isValidContent(String content) {
        return content != null && content.length() >= 50;
    }

    public static boolean isValidUrl(String url) {
        return url != null && url.startsWith("http") && url.length() <= 200;
    }
}

