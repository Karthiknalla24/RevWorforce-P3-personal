package com.rev.api_gateway.filter;

import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.concurrent.ConcurrentHashMap;

@Component
public class RateLimitFilter implements GlobalFilter, Ordered {

    private final ConcurrentHashMap<String, TokenBucket> buckets = new ConcurrentHashMap<>();
    private static final int REFILL_RATE = 50; // max tokens allowed per second
    private static final int MAX_BUCKET_SIZE = 100; // max burst size

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String clientIp = exchange.getRequest().getRemoteAddress() != null ? 
                exchange.getRequest().getRemoteAddress().getAddress().getHostAddress() : "UNKNOWN";

        TokenBucket bucket = buckets.computeIfAbsent(clientIp, k -> new TokenBucket(MAX_BUCKET_SIZE, REFILL_RATE));

        if (!bucket.tryConsume()) {
            exchange.getResponse().setStatusCode(HttpStatus.TOO_MANY_REQUESTS);
            return exchange.getResponse().setComplete();
        }

        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return -100; // Ensures rate limit applies before routing & circuit breaking
    }

    private static class TokenBucket {
        private final int maxTokens;
        private final int refillRate;
        private double currentTokens;
        private long lastRefillTimestamp;

        public TokenBucket(int maxTokens, int refillRate) {
            this.maxTokens = maxTokens;
            this.refillRate = refillRate;
            this.currentTokens = maxTokens;
            this.lastRefillTimestamp = System.currentTimeMillis();
        }

        public synchronized boolean tryConsume() {
            refill();
            if (currentTokens >= 1) {
                currentTokens -= 1;
                return true;
            }
            return false;
        }

        private void refill() {
            long now = System.currentTimeMillis();
            double tokensToAdd = ((double)(now - lastRefillTimestamp) / 1000.0) * refillRate;
            if (tokensToAdd > 0) {
                currentTokens = Math.min(currentTokens + tokensToAdd, maxTokens);
                lastRefillTimestamp = now;
            }
        }
    }
}