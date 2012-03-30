package com.sigmasys.kuali.ksa.temp;

/**
 * Created by IntelliJ IDEA.
 * User: dmulderink
 * Date: 3/27/12
 * Time: 8:38 PM
 * To change this template use File | Settings | File Templates.
 */
public class loginCredentials {

    private static String defaultUserName;
    private static String defaultPswd;

    public static Login login(Login bean) {
        defaultUserName = "sigma";
        defaultPswd = "sigma";
        String username = bean.getUserName();
        String userpswd = bean.getPswd();

        if (defaultUserName.equals(username) && defaultPswd.equals(userpswd)) {
            bean.setValid(true);
        } else {
            bean.setValid(false);
        }

        return bean;

    }
}
