<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
  String str = request.getParameter("str");
  if (str == null) {
    str = "пусто";
  }
  String x = request.getParameter("x");

%>
<h2>
  Привет из фрагмента
</h2>
<p>
    Строка: <%= str %>
</p>
