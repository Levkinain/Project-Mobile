package com.netcracker.myapplication.Security;
import android.util.Base64;

public class TokenService {

    public static final String LOGIN_URL = "/login";
    public static final String LOGOUT_URL = "/logout";

    public static final String BASE = "Base ";
    public static final String AUTH = "Auth";

    public static final String SESSION = "Session ";
    public static final String APP_PREFERENCES = "AuthData";

    public static final String ID_DRIVER = "IdDriver";
    public static final String DRIVER_ON_SHIFT = "DriverData";

    public static String getToken(String login, String password) {
        String originalInput = login + ":" + password;
        String token =  BASE +  Base64.encodeToString(originalInput.getBytes(), Base64.DEFAULT);
        return token.replaceAll("\n","");
    }
}
