package com.news.newsworld.model;

import org.springframework.context.annotation.Scope;

@Scope("singleton")
public class AdminAccess {

    private static AdminAccess adminAccess = new AdminAccess();
    private static boolean flag = false;

    private AdminAccess() {}

    public static AdminAccess getInstance() {
        if (!flag) {
            flag = true;
            return adminAccess;
        } else {
            return null;
        }
    }
}
