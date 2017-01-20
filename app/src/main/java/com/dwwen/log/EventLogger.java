package com.dwwen.log;

import javax.inject.Inject;

public class EventLogger {
    @Inject FileLogger fileLogger;
/**
    @Subscribe public void logSwipe(SwipeEvent event) {
        fileLogger.add(new SwipeLog(event.successfulSwipe));
    }
    **/
}
