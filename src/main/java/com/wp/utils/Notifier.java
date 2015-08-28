package com.wp.utils;

import com.newrelic.api.agent.NewRelic;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
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

    public static void noticeError(Exception e) {
        noticeError(e, null);
    }

    public static void noticeError(Exception e, HashMap<String, Object> meta) {
        // Don't alert in the middle of the night
        NewRelic.noticeError(e);

        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw));

        // Log to ES
        JSONObject log = new JSONObject();
        log.put("class", e.getClass().getName());
        log.put("type", "exception");
        log.put("log", sw.toString());

        // Add meta information
        if (meta != null) {
            for (Map.Entry<String, Object> entry : meta.entrySet()) {
                String key = entry.getKey();
                Object val = entry.getValue();
                log.put(key, val);
            }
        }
        logger.info(log.toString());

    }

}

