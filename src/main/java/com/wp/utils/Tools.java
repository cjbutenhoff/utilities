package com.wp.utils;

public class Tools {
   
    public static String normalizeURL(String url) {
        String normalizedURL = url;
        if (normalizedURL.startsWith("//")) {
            normalizedURL = "http:" + normalizedURL;
        }
        normalizedURL = normalizedURL.replace("//washingtonpost", "//www.washingtonpost");
        return normalizedURL;
    }
    
}
