<%@ page import="step.learning.dto.entities.Cart" %>
<%@ page import="java.util.List" %>
<%@ page import="step.learning.dto.entities.Product" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<h1>Your cart</h1>

<%
    Cart cart = (Cart) request.getAttribute("cart");

    int totalAmount = 0;
%>

<p>Total Amount: <%= request.getAttribute("total") %></p>

<form action="" method="post">
    <input type="submit" value="Place Order">
</form>