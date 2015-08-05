package com.wp.utils.datasource;

import java.util.List;

import lombok.Data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class StoryAdapterResponse {
	private List<Creator> creator = null;
	private Tracking tracking = null;
	private String webHeadline = null;
	private String mobileHeadline = null;
	private String primarySlotHtml = null;
	private Thumbnail thumbnail = null;
	private String canonicalUrl = null;
	private String redirectUrl = null;
	private String displayDate = null;
	private String siteNode = null;
	private String type = null;
	private DisplaySummary displaySummary = null;
	private SiteService siteService = null;
	private Long displayDateNum = null;

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
		private String blogName = null;
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
		private Feature featured = null;

		@Data
		@JsonIgnoreProperties(ignoreUnknown = true)
		public static class Feature {
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
