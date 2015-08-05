package com.wp.utils.filters;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Enumeration;
import java.util.UUID;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import org.apache.commons.io.output.TeeOutputStream;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mock.web.DelegatingServletOutputStream;
import org.springframework.stereotype.Component;

@Component
public final class LoggingFilter extends JSONObject implements Filter {

    private JSONObject headersLog = new JSONObject();
    private static final Logger headersLogger = LoggerFactory
            .getLogger("headers");
    private static final Logger requestLogger = LoggerFactory
            .getLogger("requests");

    @Override
    public void init(FilterConfig filterConfigObj) {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain) throws IOException, ServletException {

        URI uri = null;
        String referer = "";
        HttpServletRequest req = (HttpServletRequest) request;

        // Set a unique identifier for the request
        this.put("type", "request");
        this.put("uuid", UUID.randomUUID());

        // Basic infos
        this.put("method", req.getMethod());
        this.put("path", req.getPathInfo());
        this.put("uri", req.getRequestURI());
        this.put("log", req.getMethod() + " " + req.getPathInfo());

        // All the parameters
        Enumeration<String> params = request.getParameterNames();
        for (; params.hasMoreElements();) {
            String param = params.nextElement();
            this.put("param." + param, request.getParameter(param));
        }

        // All the headers
        Enumeration<String> headers = req.getHeaderNames();
        for (; headers.hasMoreElements();) {
            String header = headers.nextElement();
            if (!"cookie".equalsIgnoreCase(header)) {
                headersLog.put("header." + header, req.getHeader(header));
            }
            if (header.equalsIgnoreCase("referer")) {
                referer = req.getHeader(header);
                this.put("referer", referer);
                if ((referer != null) && (!referer.isEmpty())) {
                    try {
                        uri = new URI(referer);
                        this.put("referer-domain", uri.getHost());
                    } catch (URISyntaxException ex) {
                        //
                    }
                }
            }
            if (header.contains("instart-geo")) {
                this.put("header." + header, req.getHeader(header));
            }
        }

        // Run the request
        OutputStream outstr = new java.io.ByteArrayOutputStream();
        HttpServletResponse responseWrapper = responseWrapper(
                (HttpServletResponse) response, outstr);
        chain.doFilter(request, responseWrapper);

        // Log everything
        headersLogger.info(headersLog.toString());
        this.put("response", outstr.toString());
        requestLogger.info(this.toString());
    }

    @Override
    public void destroy() {
    }

    private HttpServletResponse responseWrapper(HttpServletResponse response,
            final OutputStream out) {
        return new HttpServletResponseWrapper(response) {
            @Override
            public ServletOutputStream getOutputStream() throws IOException {
                return new DelegatingServletOutputStream(new TeeOutputStream(
                        super.getOutputStream(), out));
            }
        };
    }
}
