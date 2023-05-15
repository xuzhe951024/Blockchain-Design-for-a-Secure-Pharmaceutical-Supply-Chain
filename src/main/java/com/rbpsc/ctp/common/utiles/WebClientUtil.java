package com.rbpsc.ctp.common.utiles;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class WebClientUtil {
    private final WebClient webClient;

    public WebClientUtil() {
        this.webClient = WebClient.create();
    }

    public <T, E> Mono<T> postWithParams(String url, E param, Class<E> paramType, Class<T> responseType) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return webClient.method(HttpMethod.POST)
                .uri(url)
                .headers(httpHeaders -> httpHeaders.addAll(headers))
                .body(Mono.just(param), paramType)
                .retrieve()
                .bodyToMono(responseType);
    }

    public <T> Mono<T> getWithoutParams(String url, Class<T> responseType) {
        return webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(responseType);
    }
}

