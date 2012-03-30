<%--
  Created by IntelliJ IDEA.
  User: dmulderink
  Date: 3/16/12
  Time: 9:17 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/jsp/tldHeader.jsp" %>

<html>

<style type="text/css">
    div.body {
        background-image: url("${ConfigProperties.application.url}/rice-portal/images/os-guy.gif");
        background-repeat: no-repeat;
        padding-top: 5em;
    }

    table#login {
        margin: auto;
        background-color: #dfdda9;
        border: .5em solid #fffdd8;
        /* simple rounded corners for mozilla & webkit */
        -moz-border-radius: 10px;
        -webkit-border-radius: 10px;
    }

    table#login th {
        height: 30px;
        padding-top: .8em;
        padding-bottom: .8em;
        color: #a02919;
        font-size: 2em;
    }

    #login td {
        padding: .2em;
        height: 20px;
    }

    #login .rightTd {
        padding-right: 1.2em;
    }

    #login .leftTd {
        padding-left: 1.2em;
    }

    table#login td#buttonRow {
        padding-top: 1em;
        padding-bottom: .6em;
    }

    div.build {
        float: right;
        color: #dfdda9;
        margin: .3em;
    }

</style>

<head>
    <title>Kuali Student Account Login</title>

    <%--<link href="/css/styles.css" rel="stylesheet" type="text/css">--%>
    <link href="/css/mstrstyles.css" rel="stylesheet" type="text/css">

    <c:forEach items="${fn:split(ConfigProperties.portal.css.files, ',')}" var="cssFile">
        <c:if test="${fn:length(fn:trim(cssFile)) > 0}">
            <link href="${pageContext.request.contextPath}/${fn:trim(cssFile)}" rel="stylesheet" type="text/css"/>
        </c:if>
    </c:forEach>

        <c:forEach items="${fn:split(ConfigProperties.portal.javascript.files, ',')}" var="javascriptFile">
            <c:if test="${fn:length(fn:trim(javascriptFile)) > 0}">
                <script language="JavaScript" type="text/javascript"
                        src="${ConfigProperties.application.url}/${fn:trim(javascriptFile)}"></script>
            </c:if>
        </c:forEach>

</head>

<body OnLoad ="document.loginForm.userName.focus();">
<jsp:useBean id="userLogin" scope="session" class="com.sigmasys.kuali.ksa.servlet.LoginServlet"/>
<div class="build">${ConfigProperties.version} (${ConfigProperties.datasource.ojb.platform})</div>

<form name="loginForm" action="LoginServlet" method="post" >
<div id="ksa-container">
    <div id="ksa-header">
    </div>

    <div class="body">
        <table id="login" cellspacing="0" cellpadding="0" align="center">
            <tbody>
            <tr>
                <th colspan="2">Login</th>
            </tr>
            <tr>
                <td class="leftTd" align="right" width="Infinity%">
                    <label>Username:&nbsp;</label>
                </td>
                <td class="rightTd" align="left">
                    <input type="text" name="userName" value="" size="20"/>
                </td>
            </tr>
<%--
            <c:set var="invalidAuthMsg" value="Invalid username"/>
            <c:if test="${requestScope.showPasswordField}">
                <c:set var="invalidAuthMsg" value="Invalid username or password"/>
--%>
                <tr>
                    <td class="leftTd" width="Infinity%" align="right">
                        <label>Password:&nbsp;</label>
                    </td>
                    <td class="rightTd" align="left"><input type="password" name="pswd" value="" size="20"/></td>
                </tr>
            <%--</c:if>--%>
            <%--<c:if test="${requestScope.invalidAuth}">--%>
                <tr>
                    <td align="center" colspan="2"><strong>${invalidAuthMsg}</strong></td>
                </tr>
            <%--</c:if>--%>
            <tr>
                <td id="buttonRow" height="30" colspan="2" align="center"><input type="submit" value="Login" />
                    <!-- input type="image" title="Click to login." value="login" name="imageField" src="${pageContext.request.contextPath}/rice-portal/images/tinybutton-login.gif"/ -->
                </td>
            </tr>
            </tbody>
        </table>
    </div>

    <div id="ksa-footer" align="center">
        <%@ include file="/WEB-INF/jsp/ksaFooter.jsp" %>
    </div>
</div>
</form>
</body>
</html>