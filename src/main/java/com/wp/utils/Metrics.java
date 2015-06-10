/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wp.utils;

import com.newrelic.api.agent.NewRelic;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class provides a generic abstraction to record metrics. At the moment we
 * record metrics to New Relic. We may add other destinations in the future.
 *
 * @author Julien Lancien <julien.lancien@washpost.com>
 */
public class Metrics {

    private static final Logger logger = LoggerFactory.getLogger("exceptions");

    public static void recordMetric(String name, float value) {
        JSONObject json = new JSONObject();
        json.put("type", "metric");
        json.put("Metric/" + name, value);
        json.put("log", "Metric/" + name + " : " + value);
        logger.info(json.toString());

        NewRelic.recordMetric("Custom/" + name, value);
    }

    public static void recordMetric(String prefix, String name) {
        JSONObject json = new JSONObject();
        json.put("type", "metric");
        json.put("Metric/" + prefix, name);
        json.put("log", "Metric/" + prefix + " : " + name);
        logger.info(json.toString());

        NewRelic.recordMetric("Custom/" + prefix + "/" + name, 1);
    }

    public static void recordMetric(String prefix, String name, float value) {
        JSONObject json = new JSONObject();
        json.put("type", "metric");
        json.put("value", value);
        json.put("Metric/" + prefix, name);
        json.put("log", "Metric/" + prefix + " : " + name);
        logger.info(json.toString());

        NewRelic.recordMetric("Custom/" + prefix + "/" + name, value);
    }
}
