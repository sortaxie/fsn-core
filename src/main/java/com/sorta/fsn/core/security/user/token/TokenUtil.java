package com.sorta.fsn.core.security.user.token;

import com.sorta.fsn.core.security.user.Token;

public class TokenUtil {
    private static ThreadLocal<Token> localToken = new ThreadLocal<>();

    public static void setToken(Token token) {
        localToken.set(token);
    }

    public static Token getToken() {
        return localToken.get();
    }

    public static void removeToken() {
        localToken.remove();
    }

    public static String getAid() {
        Token token = localToken.get();
        if (token == null) {
            throw new RuntimeException("token is not exist");
        }

        return token.getAid();
    }

    public static String getUid() {
        Token token = localToken.get();
        if (token == null) {
            throw new RuntimeException("token is not exist");
        }

        return token.getUid();
    }
}
