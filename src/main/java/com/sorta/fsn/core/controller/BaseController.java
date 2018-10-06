package com.sorta.fsn.core.controller;


import com.sorta.fsn.core.security.user.Token;
import com.sorta.fsn.core.security.user.token.TokenUtil;

public class BaseController {

    private Token token;

    public Token getToken() {

        return TokenUtil.getToken();
    }

    public void setToken(Token token) {
        this.token = token;
    }
}
