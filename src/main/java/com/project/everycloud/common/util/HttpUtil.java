package com.project.everycloud.common.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.project.everycloud.common.exception.CertErrorException;
import com.project.everycloud.common.exception.CertSiteErrorException;
import com.project.everycloud.common.type.ResponseType;
import org.springframework.http.*;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

public class HttpUtil {

    private static final String DEFAULT_SITE = "https://everycert.sung-a.duckdns.org/everycloud";

    /**
     * http get request
     *
     * @param header HttpHeaders header = new HttpHeaders
     * @param body MultiValueMap(String, String) body = new LinkedMultiValueMap
     * @param site if not exist value, use default site
     * @param path (ex. /test/to/path)
     */
    public static JsonNode get(HttpHeaders header, MultiValueMap body, String site, String path) {
        return request(header, body, "get", site, path);
    }

    /**
     * http post request
     *
     * @param header HttpHeaders header = new HttpHeaders
     * @param body MultiValueMap(String, String) body = new LinkedMultiValueMap
     * @param site if not exist value, use default site
     * @param path (ex. /test/to/path)
     */
    public static JsonNode post(HttpHeaders header, MultiValueMap body, String site, String path) {
        return request(header, body, "post", site, path);
    }

    /**
     * http request
     *
     * @param header HttpHeaders header = new HttpHeaders
     * @param body MultiValueMap(String, String) body = new LinkedMultiValueMap
     * @param method post or get
     * @param site if not exist value, use default site
     * @param path (ex. /test/to/path)
     */
    public static JsonNode request(HttpHeaders header, MultiValueMap body, String method, String site, String path) {
        header.setAccept(Arrays.asList(new MediaType[] { MediaType.APPLICATION_JSON }));
        header.setContentType(MediaType.APPLICATION_JSON);

        if(!StringUtils.hasText(site)) site = DEFAULT_SITE;

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(body, header);

        RestTemplate rt = new RestTemplate();

        ResponseEntity<JsonNode> resEntity = null;
        try {
            resEntity = rt.exchange(
                            site + path,
                            method.equals("post") ? HttpMethod.POST : HttpMethod.GET,
                            entity,
                            JsonNode.class
                        );
        } catch (ResourceAccessException e) {
            throw new CertSiteErrorException();
        }
        if(!resEntity.getStatusCode().is2xxSuccessful()) throw new CertSiteErrorException();

        try {
            JsonNode jsonNode = resEntity.getBody();

            if (!ResponseType.SUCCESS.code().equals(jsonNode.get("code").asText())) {
                throw new CertErrorException();
            }
            return jsonNode.get("data");

        } catch (NullPointerException e) {
            throw new CertErrorException();
        }
    }
}
