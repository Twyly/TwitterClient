package com.codepath.apps.restclienttemplate.TimeUnits;

import org.ocpsoft.prettytime.Duration;
import org.ocpsoft.prettytime.TimeFormat;
import org.ocpsoft.prettytime.TimeUnit;

/**
 * Created by teddywyly on 5/24/15.
 */
public class Year implements TimeUnit, TimeFormat {

    @Override
    public long getMaxQuantity() {
        return 0;
    }

    @Override
    public long getMillisPerUnit() {
        return 1000L * 60L * 60L * 24L * 7L * 52L;
    }

    public String format(final Duration duration) {
        return duration.getQuantity() * -1 + "y";
    }

    public String formatUnrounded(Duration duration) {
        return format(duration);
    }

    public String decorate(Duration duration, String time) {
        if (duration.isInPast())
            return time;
        else
            return time + " from now";
    }

    public String decorateUnrounded(Duration duration, String time) {
        return decorate(duration, time);
    }
}
