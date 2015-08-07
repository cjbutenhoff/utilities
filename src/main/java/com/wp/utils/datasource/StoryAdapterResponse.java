package com.wp.utils.datasource;

import java.util.List;

import lombok.Data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class StoryAdapterResponse {
    private List<Creator> creator = null;
    private Tracking tracking = null;
    @JsonProperty("web_headline")
    private String webHeadline = null;
    @JsonProperty("mobile_headline")
    private String mobileHeadline = null;
    @JsonProperty("primary_slot_html")
    private String primarySlotHtml = null;
    private Thumbnail thumbnail = null;
    @JsonProperty("canonical_url")
    private String canonicalUrl = null;
    @JsonProperty("redirect_url")
    private String redirectUrl = null;
    @JsonProperty("display_date")
    private String displayDate = null;
    @JsonProperty("site_node")
    private String siteNode = null;
    private String type = null;
    @JsonProperty("display_summary")
    private DisplaySummary displaySummary = null;
    @JsonProperty("site_service")
    private SiteService siteService = null;
    @JsonProperty("display_date_num")
    private Long displayDateNum = null;
    private String uuid = null;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class SiteService {
        private String id = null;
        private String name = null;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Creator {
        private String byline = null;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Tracking {
        @JsonProperty("blog_name")
        private String blogName = null;
        @JsonProperty("content_category")
        private String contentCategory = null;
        private Section section = null;

        @Data
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Section {
            String section = null;
        }
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Thumbnail {
        private String url = null;
        private Featured featured = null;

        @Data
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Featured {
            private String url = null;
        }
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class DisplaySummary {
        private String headline = null;
        private String blurb = null;
    }

}
