package com.rev.api_gateway.util;

import com.rev.api_gateway.constants.SecurityConstants;

public class TokenUtils {

    public static String extractToken(String header) {

        if (header == null) {
            return null;
        }

        if (header.startsWith(SecurityConstants.TOKEN_PREFIX)) {
            return header.substring(7);
        }

        return null;
    }
}