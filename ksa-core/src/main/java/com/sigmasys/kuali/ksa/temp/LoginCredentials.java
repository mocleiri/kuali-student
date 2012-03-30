package com.sigmasys.kuali.ksa.temp;

/**
 * Created by IntelliJ IDEA.
 * User: dmulderink
 * Date: 3/27/12
 * Time: 8:38 PM
 * To change this template use File | Settings | File Templates.
 */
public class LoginCredentials {

    private static final String defaultUserName = "sigma";
    private static final String defaultPswd = "sigma";

    public static Login login(Login bean) {

        String username = bean.getUserName();
        String userpswd = bean.getPswd();

        bean.setValid(defaultUserName.equals(username) && defaultPswd.equals(userpswd));

        return bean;

    }
}
