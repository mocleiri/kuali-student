package com.sigmasys.kuali.ksa.util;

/**
 * Created by IntelliJ IDEA.
 * User: dmulderink
 * Date: 3/27/12
 * Time: 7:36 PM
 * To change this template use File | Settings | File Templates.
 *
 * Login bean
 */
public class Login  {
   
   private String userName;
   private String pswd;
   boolean isValidUser;

   public String getUserName() {
      return this.userName;
   }
   
   public void setUserName(String userName) {
      this.userName = userName;
   }
   
   public String getPswd() {
      return this.pswd;
   }
   
   public void setPswd(String pswd) {
      this.pswd = pswd;
   }

   public boolean isValidUser() {
      return isValidUser;
   }

   public void setValid(boolean newValidUser) {
      isValidUser = newValidUser;
   }
}
