package com.pm.apigateway.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class JwtValidationGatewayFilterFactory extends AbstractGatewayFilterFactory<Object> {

    private final WebClient webClient;

    public JwtValidationGatewayFilterFactory(WebClient.Builder webClientBuilder,
                                             @Value("${auth.service.url}")String authServiceUrl) {
//        initialize webclient using auth service url
        this.webClient = webClientBuilder.baseUrl(authServiceUrl).build();

    }

//    intercepts api requests
    @Override
    public GatewayFilter apply(Object config){
//        exchange object passed by spring gateway and holds all properties for current requests
//        chain is chain of filters in filter chain
        return (exchange, chain) -> {
            String token = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

            if(token==null||!token.startsWith("Bearer ")){
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }

            return webClient.get() // get request using web client to {baseurl}/validate uri
                    .uri("/validate")
                    .header(HttpHeaders.AUTHORIZATION,token) // taking auth header from initial request
                    .retrieve() // retieve response
                    .toBodilessEntity() // telling sprng that there are no bodies
                    .then(chain.filter(exchange)); // then continue the request (another filter or response)
        };

    }

}
