<%--
  ~ Copyright 2006-2011 The Kuali Foundation
  ~
  ~ Licensed under the Educational Community License, Version 2.0 (the "License");
  ~ you may not use this file except in compliance with the License.
  ~ You may obtain a copy of the License at
  ~
  ~ http://www.opensource.org/licenses/ecl2.php
  ~
  ~ Unless required by applicable law or agreed to in writing, software
  ~ distributed under the License is distributed on an "AS IS" BASIS,
  ~ WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  ~ See the License for the specific language governing permissions and
  ~ limitations under the License.
  --%>
<%@ include file="/rice-portal/jsp/sys/riceTldHeader.jsp" %>

<%
   String channelUrl = null;
   if (request.getQueryString() != null && request.getQueryString().indexOf("channelUrl") >= 0) {
      channelUrl = request.getQueryString().substring(request.getQueryString().indexOf("channelUrl")+11,request.getQueryString().length());
   }
   else if (request.getParameter("channelUrl") != null && request.getParameter("channelUrl").length() > 0) {
      channelUrl = request.getParameter("channelUrl");
   }
   request.setAttribute("channelUrl", channelUrl);
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
  <title>KS UIM Sample</title>
</head>
<body>

<h1>KS UIM Sample</h1>

<c:choose>
  <c:when test="${ empty channelUrl }">
    <ul>
      <li><portal:portalLink displayTitle="true" title="Buttons" url="/kr-krad/ks-uim?viewId=WidgetView&methodToCall=buttons" /></li>
      <li><portal:portalLink displayTitle="true" title="Widget 2" url="/kr-krad/ks-uim?viewId=ks-uim-sample-view1&methodToCall=widget2" /></li>
      <li><portal:portalLink displayTitle="true" title="Widget 3" url="/kr-krad/ks-uim?viewId=ks-uim-sample-view1&methodToCall=widget3" /></li>
    </ul>
  </c:when>
  <c:otherwise>
    <jsp:forward page="${channelUrl}" />
  </c:otherwise>
</c:choose>

</body>
</html>