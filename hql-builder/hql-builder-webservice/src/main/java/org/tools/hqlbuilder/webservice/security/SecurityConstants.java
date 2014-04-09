package org.tools.hqlbuilder.webservice.security;

/**
 * @see http://www.codeproject.com/Articles/253901/Getting-Started-Spring-Security
 * @see http://javaworkbench.blogspot.com/p/spring-security.html
 */
public class SecurityConstants {
    /** "/j_spring_security_logout" */
    public static final String $LOGOUT$ = "/auth/logout";

    /** "/j_spring_security_check" */
    public static final String $LOGIN$ = "/auth/login";

    public static final String $WICKETURL$ = "/pages";

    public static final String $LOGINURL$ = "/login";

    public static final String $LOGOUTURL$ = "/logout";

    public static final String $LOGINWICKETURL$ = $WICKETURL$ + $LOGINURL$;

    public static final String $LOGOUTWICKETURL$ = $WICKETURL$ + $LOGOUTURL$;
}
