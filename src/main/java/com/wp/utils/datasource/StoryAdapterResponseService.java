package com.wp.utils.datasource;

import java.net.URI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class StoryAdapterResponseService {

    private static final Logger log = LoggerFactory
            .getLogger(StoryAdapterResponseService.class);

    @Value("${story-adapter.host}")
    private String storyAdapterApiHost;

    @Value("${story-adapter.read-timeout}")
    private int storyAdapterReadTimeout;

    @Value("${story-adapter.connect-timeout}")
    private int storyAdapterConnectTimeout;

    private final RestOperations restOperations;

    public StoryAdapterResponseService() {
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setReadTimeout(storyAdapterReadTimeout);
        factory.setConnectTimeout(storyAdapterConnectTimeout);
        restOperations = new RestTemplate(factory);
    }

    public StoryAdapterResponse findByUrl(String url) {
        URI queryUrl = UriComponentsBuilder.newInstance().scheme("http")
                .host(storyAdapterApiHost).path("/api/url/")
                .queryParam("url", url).build().toUri();
        log.debug("StoryAdapter query: " + queryUrl);
        StoryAdapterResponse res = null;
        try {
            res = restOperations.getForObject(queryUrl,
                    StoryAdapterResponse.class);
        } catch (HttpClientErrorException e) {
            log.warn("Storyadapter 404 for: " + url);
        }
        return res;
    }

}
