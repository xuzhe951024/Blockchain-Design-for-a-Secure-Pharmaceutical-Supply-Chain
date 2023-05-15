package com.rbpsc.ctp.common.utiles;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class WebClientUtil {
    private final WebClient webClient;

    public WebClientUtil() {
        this.webClient = WebClient.create();
    }

    public <T> Mono<T> postWithParams(String url, MultiValueMap<String, String> params, Class<T> responseType) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        return webClient.method(HttpMethod.POST)
                .uri(url)
                .headers(httpHeaders -> httpHeaders.addAll(headers))
                .body(BodyInserters.fromFormData(params))
                .retrieve()
                .bodyToMono(responseType);
    }
}

