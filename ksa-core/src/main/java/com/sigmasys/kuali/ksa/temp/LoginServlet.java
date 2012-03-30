package com.sigmasys.kuali.ksa.temp;


import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LoginServlet extends HttpServlet {

   public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, java.io.IOException {

         Login user = new Login();
         user.setUserName(request.getParameter("userName"));
         user.setPswd(request.getParameter("pswd"));

         user = loginCredentials.login(user);

         if (user.isValidUser())
         {
            HttpSession session = request.getSession(true);
            session.setAttribute("currentSessionUser",user);
            //uif/alertsTransaction?methodToCall=get&viewId=AlertsTransactionView
            //uif/transactionDetails?methodToCall=get&viewId=TransactionDetails-FormView
            response.sendRedirect("uif/alertsTransaction?methodToCall=get&viewId=AlertsTransactionView"); //logged-in page
         }

         else
         {
            response.sendRedirect("login.jsp"); //error page
         }

   }

   public void doPost(HttpServletRequest request,
                      HttpServletResponse response)
                      throws ServletException, IOException {
      doGet(request, response);
   }
}

