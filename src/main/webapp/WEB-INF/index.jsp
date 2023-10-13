<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String context = request.getContextPath();
%>
<img src="<%= context %>/img/1.jpeg" alt="1" style="float:left; height: 500px">
<h1>Java web. Введение</h1>
<%
    String str = "Hello";
    str += " World";
    int x = 10;
%>
<p> str = <%= str%>, x+10 = <%= x + 10%> </p>
<ul>
    <% for (int i = 1; i <= 10; i++) { %>
    <li>
        item #<%= i%>
    </li>
    <% } %>
</ul>

<jsp:include page="fragment.jsp">
    <jsp:param name="str" value="<%= str %>"/>
    <jsp:param name="x" value="<%= x %>"/>
</jsp:include>
