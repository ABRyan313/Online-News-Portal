package com.nokshal.amarDesh.content.util;

public class ContentFormatter {

    public static String formatTitle(String title) {
        if (title == null || title.isEmpty()) {
            return "";
        }
        return title.trim().substring(0, 1).toUpperCase() + title.trim().substring(1);
    }

    public static String shortenContent(String content, int length) {
        if (content == null || content.length() <= length) {
            return content;
        }
        return content.substring(0, length) + "...";
    }
}

