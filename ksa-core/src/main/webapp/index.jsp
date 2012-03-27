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
            <td class="rightTd" align="left"><table id="login2" cellspacing="0" cellpadding="6" align="center">
                <tbody>
                <tr>
                    <th colspan="6">Persona</th>
                </tr>
                <tr>
                    <td class="leftTd" align="right"><a href="login.jsp?persona=bursar"> <img src="images/persona-bursar.png" alt="persona: bursar" width="140" height="140"
                                                                                               border="0"></a></td>
                    <td align="left" nowrap class="rightTd" ><p><strong>Ida Bozwumon</strong><br>
                        <span >Bursar</span></p>
                        <hr/>
                        <p >&bull; <a href="login.jsp?persona=bursar">Select for UX
                            test</a><br>
                            &bull; <a href="media/persona-bursar.pdf">View detailed persona</a></p></td>
                    <td class="leftTd" align="right"><a href="login.jsp?persona=student"> <img src="images/persona-student.png" alt="persona: student" width="140" height="140"
                                                                                                border="0"></a></td>
                    <td align="left" nowrap class="rightTd" ><p><strong>Wanda Lyrn</strong><br>
                        <span >Student</span></p>
                        <hr/>
                        <p >&bull;  UX
                            Work in Progress<br>
                            &bull; <a href="media/persona-student.pdf">View detailed persona</a></p></td>
                    <td class="leftTd" align="right"><a href="login.jsp?persona=parent"> <img src="images/persona-parent.png" alt="persona: parent" width="140" height="140"
                                                                                               border="0"></a></td>
                    <td align="left" nowrap class="rightTd" ><p><strong>Helen Kopter</strong><br>
                        <span >Parent</span></p>
                        <hr/>
                        <p >&bull; UX
                            Work in Progress<br>
                            &bull; <a href="media/persona-parent.pdf">View detailed persona</a></p></td>
                </tr>
                <tr>
                    <td class="leftTd" align="right"><a href="login.jsp?persona=sponsor"> <img src="images/persona-sponsor.png" alt="persona: sponsor" width="140" height="140"
                                                                                                border="0"></a></td>
                    <td align="left" nowrap class="rightTd" ><p><strong>Ben E. Fakter</strong><br>
                        <span >Sponsor</span></p>
                        <hr/>
                        <p >&bull; UX
                            Work in Progress<br>
                            &bull; <a href="media/persona-sponsor.pdf">View detailed persona</a></p></td>
                    <td class="leftTd" align="right"><a href="login.jsp?persona=faculty_advisor"> <img src="images/persona-faculty_advisor.png" alt="persona: faculty advisor" width="140" height="140"
                                                                                                        border="0"></a></td>
                    <td align="left" nowrap class="rightTd" ><p><strong>Will N. Lyten</strong><br>
                        <span >Faculty Advisor/Department Liason</span></p>
                        <hr/>
                        <p >&bull; UX
                            Work in Progress<br>
                            &bull; <a href="media/persona-faculty_advisor.pdf">View detailed persona</a></p></td>
                    <td class="leftTd" align="right" width="Infinity%"><a href="login.jsp?persona=customer_service_representative"> <img src="images/persona-customer_service_representative.png" alt="persona customer service representative" width="140" height="140"
                                                                                                                                          border="0"> </a></td>
                    <td align="left" nowrap class="rightTd" ><p><strong>Hal Kanni-Helfew</strong><br>
                        <span >Customer Service Representative</span></p>
                        <hr/>
                        <p >&bull; <a href="login.jsp?persona=student">Select for UX
                            test</a><br>
                            &bull; <a href="media/persona-customer_service_representative.pdf">View detailed persona</a></p></td>
                </tr>
                <tr>
                    <td class="leftTd" align="right"><a href="login.jsp?persona=cashier"> <img src="images/persona-cashier.png" alt="persona: cashier" width="140" height="140"
                                                                                                border="0"></a></td>
                    <td align="left" nowrap class="rightTd" ><strong>Missy Munni</strong><br>
                        <span >Cashier</span>
                        <hr/>
                        <p >&bull; <a href="login.jsp?persona=cashier">Select for UX
                            test</a><br>
                            &bull; <a href="media/persona-cashier.pdf">View detailed persona</a></p></td>
                    <td class="leftTd" align="right"><a href="login.jsp?persona=supervising_cashier"> <img src="images/persona-supervising_cashier.png" alt="persona: supervising cashier" width="140" height="140"
                                                                                                            border="0"></a></td>
                    <td align="left" nowrap class="rightTd" ><p><strong>Bill Peymaster</strong><br>
                        <span >Supervising Cashier</span></p>
                        <hr/>
                        <p >&bull; UX
                            Work in Progress<br>
                            &bull; <a href="media/persona-supervising_cashier.pdf">View detailed persona</a></p></td>
                    <td class="leftTd" align="right"><a href="login.jsp?persona=administrative_liaison"> <img src="images/persona-administrative_liaison.png" alt="persona: administrative liaison" width="140" height="140"
                                                                                                               border="0"></a></td>
                    <td align="left" nowrap class="rightTd" ><p><strong>Dana N. Tyree</strong><br>
                        <span >Administrative Liaison</span></p>
                        <hr/>
                        <p >&bull; <a href="login.jsp?persona=administrative_liaison">Select for UX
                            test</a><br>
                            &bull; <a href="media/persona-administrative_liaison.pdf">View detailed persona</a></p></td>
                </tr>
                <tr>
                    <td id="buttonRow" height="30" colspan="6" align="center">&nbsp;</td>
                </tr>
                </tbody>
            </table></td>
        </tr>
        </tbody>
    </table>
</div>
<div id="footer" align="center">
    <%@ include file="/WEB-INF/jsp/ksaFooter.jsp" %>
</div>
</body>
</html>