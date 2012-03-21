<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <title>Persona</title>
    <link href="/css/styles.css" rel="stylesheet" type="text/css">
    <link href="/css/portal.css" rel="stylesheet" type="text/css">
</head>

<body>
<div class="body">
    <table id="login" cellspacing="0" cellpadding="0" align="center">
        <tbody>
        <tr>
            <td class="rightTd" align="left">
                <table id="login2" cellspacing="0" cellpadding="0" align="center">
                    <tbody>
                    <tr>
                        <th colspan="2">Persona</th>
                    </tr>
                    <tr>
                        <td class="leftTd" align="right" width="Infinity%">
                            <a href="/login.jsp?persona=student">
                                <img src="images/persona-student.png" alt="persona student" width="140" height="140"
                                     border="0">
                            </a></td>
                        <td align="left" nowrap class="rightTd" id="u700i"><p><strong>Wanda Lyrn</strong><br>
                            <span id="u500">Student</span></p>
                            <hr/>
                            <p id="u300">&bull; <a href="/login.jsp?persona=student">Select for UX
                                test</a><br>
                                &bull; <a href="/media/persona-student.pdf">View detailed persona</a></p></td>
                    </tr>
                    <tr>
                        <td colspan="2" align="right" class="leftTd">
                            <hr/>
                        </td>
                    </tr>
                    <tr>
                        <td class="leftTd" align="right" width="Infinity%">
                            <a href="/login.jsp?persona=cashier">
                                <img src="images/persona-cashier.png" alt="persona cashier" width="140" height="140"
                                     border="0">
                            </a></td>
                        <td align="left" nowrap class="rightTd" id="u700i"><strong>Missy Munni</strong><br>
                            <span id="u500">Cashier</span>
                            <hr/>
                            <p id="u300">&bull; <a href="/login.jsp?persona=cashier">Select for UX
                                test</a><br>
                                &bull; <a href="media/persona-cashier.pdf">View detailed persona</a></p></td>
                    </tr>
                    <tr>
                        <td id="buttonRow" height="30" colspan="2" align="center">&nbsp;</td>
                    </tr>
                    </tbody>
                </table>
            </td>
        </tr>
        </tbody>
    </table>
</div>

<div id="footer" align="center">
       <%@ include file="/WEB-INF/jsp/ksaFooter.jsp" %>
</div>

</body>
</html>