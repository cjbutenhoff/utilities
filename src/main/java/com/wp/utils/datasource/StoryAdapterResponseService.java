package com.wp.utils.datasource;

import java.net.URI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.retry.policy.TimeoutRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

public class StoryAdapterResponseService {

    private static final Logger log = LoggerFactory
            .getLogger(StoryAdapterResponseService.class);

    private final RestOperations restOperations;
    private final RetryTemplate retryTemplate;
    private final String host;

    public StoryAdapterResponseService(String host, int readTimeout, int connectTimeout) {
        this.host = host;
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setReadTimeout(readTimeout);
        factory.setConnectTimeout(connectTimeout);
        restOperations = new RestTemplate(factory);
        retryTemplate = new RetryTemplate();
        TimeoutRetryPolicy timeoutRetryPolicy = new TimeoutRetryPolicy();
        timeoutRetryPolicy.setTimeout(30000L);
        retryTemplate.setRetryPolicy(timeoutRetryPolicy);
    }

    public StoryAdapterResponse findByUrl(String url) {
        URI queryUrl = UriComponentsBuilder.newInstance().scheme("http")
                .host(host).path("/api/url/")
                .queryParam("url", url).build().toUri();
        log.debug("StoryAdapter query: " + queryUrl);
        StoryAdapterResponse res = null;
        try {
            res = restOperations.getForObject(queryUrl,
                    StoryAdapterResponse.class);
        } catch (HttpClientErrorException e) {
            log.warn("Storyadapter 4xx for: " + url);
        } catch (HttpServerErrorException e) {
            log.warn("Storyadapter 5xx for: " + url);
        } catch (ResourceAccessException e) {
            log.warn("Storyadapter timeout for: " + url);
        } catch (org.springframework.http.converter.HttpMessageNotReadableException e) {
            log.warn("Storyadapter can't read doc for: " + url);
        }
        return res;
    }

}
