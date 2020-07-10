package com.zennaki.toubibrdv.security;

/**
 * Constants for Spring Security authorities.
 */
public final class AuthoritiesConstants {

    public static final String ADMIN = "ROLE_ADMIN";

    public static final String USER = "ROLE_USER";

    public static final String ANONYMOUS = "ROLE_ANONYMOUS";

    public static final String PATIENT = "ROLE_PATIENT";

    public static final String DOCTEUR = "ROLE_DOCTEUR";


    private AuthoritiesConstants() {
    }
}
