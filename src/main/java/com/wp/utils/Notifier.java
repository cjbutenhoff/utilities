package com.wp.utils;

import java.io.PrintWriter;
import java.io.StringWriter;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class provides a generic notifier to record errors.
 * At the moment we record errors to New Relic.
 * We may add other destinations in the future.
 *
 * @author Julien Lancien <julien.lancien@washpost.com>
 */
public class Notifier {

    private static final Logger logger = LoggerFactory.getLogger("exceptions");
    private static final Logger rawlogger = LoggerFactory.getLogger("exceptions-raw");

    public static void noticeError(Exception e) {
        // Don't alert in the middle of the night
        // NewRelic.noticeError(e);

        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw));

        // Log to ES
        JSONObject log = new JSONObject();
        log.put("class", e.getClass().getName());
        log.put("type", "exception");
        log.put("log", sw.toString());
        logger.info(log.toString());
        
        // Log to file
        // rawlogger.error(sw.toString());
    }

}

// vim: set et ts=4 sw=4 sts=4:
