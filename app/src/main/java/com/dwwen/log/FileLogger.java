package com.dwwen.log;

import javax.inject.Inject;

public class FileLogger {

    @Inject FileLogger() {
    }

    public void add(Log log) {
        // Fake logging.
        System.out.println(log.getMessage());
    }
}
