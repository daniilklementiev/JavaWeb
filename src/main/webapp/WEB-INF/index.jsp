<%@ page import="step.learning.dto.entities.Product" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String context = request.getContextPath();
%>

<h3>Продукты</h3>

<style>
    .card {
        height: 100%;
        display: flex;
        flex-direction: column;
    }

    .card-image {
        flex: 1;
        overflow: hidden;
    }

    .card-image img {
        width: 100%;
        height: auto;
    }

    .card-content {
        flex: 1;
    }

    .card-title {
        color: black;
    }

    .addcart {
        width: 100%;
    }
</style>

<div class="row">
    <% List<Product> products = (List<Product>) request.getAttribute("products");
        for (Product product : products) {
    %>
    <div class="col s12 m6 l4">
        <div class="card">
            <div class="card-image">
                <img src="<%= product.getImage() %>" alt="<%= product.getName() %>">
                <span class="card-title" style="color:black;"><%= product.getName() %></span>
            </div>
            <div class="card-content">
                <p><%= product.getDescription() %></p>
                <p>Цена: <%= product.getPrice() %></p>
            </div>
            <!-- Добавлен вызов addToCart с параметрами при клике на кнопке -->
            <button onclick="addToCart('<%= product.getId() %>', '<%= product.getName() %>', <%= product.getPrice() %>)" class="waves-effect waves-light btn addcart">
                Add to cart
            </button>
        </div>
    </div>
    <% } %>
</div>

<script src="https://code.jquery.com/jquery-3.6.4.min.js"></script>

<script>
    function addToCart(productId, productName, productPrice) {
        const userToken = JSON.parse(atob(window.localStorage.getItem('token')));

        const data = {
            productId: productId,
            productName: productName,
            productPrice: productPrice,
            userToken: userToken.sub,
        };
        console.log(data);
        fetch(window.location.href, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json;charset=utf-8'
            },
            body: JSON.stringify(data) // Use JSON.stringify for the body
        }).then(r => r.json()).then(response => {
            // Handle the response if needed
            console.log(response);
        });
    }
</script>



<%--<%@ page import="step.learning.dto.entities.Product" %>--%>
<%--<%@ page import="java.util.List" %>--%>
<%--<%@ page contentType="text/html;charset=UTF-8" language="java" %>--%>
<%--<%--%>
<%--    String context = request.getContextPath();--%>
<%--%>--%>

<%--<h3>Продукты</h3>--%>

<%--<div class="row">--%>
<%--    <% List<Product> products = (List<Product>) request.getAttribute("products");--%>
<%--        for (Product product : products) {--%>
<%--    %>--%>
<%--    <div class="col s12 m6 l4">--%>
<%--        <div class="card">--%>
<%--            <div class="card-image">--%>
<%--                <img src="<%= product.getImage() %>" alt="<%= product.getName() %>">--%>
<%--                <span class="card-title" style="color:black;"><%= product.getName() %></span>--%>
<%--            </div>--%>
<%--            <div class="card-content">--%>
<%--                <p><%= product.getDescription() %></p>--%>
<%--                <p>Цена: <%= product.getPrice() %></p>--%>
<%--            </div>--%>
<%--            <button class="waves-effect waves-light btn addcart">--%>
<%--                Add to cart--%>
<%--            </button>--%>
<%--        </div>--%>
<%--    </div>--%>
<%--    <% } %>--%>
<%--</div>--%>

<%--<script src="https://code.jquery.com/jquery-3.6.4.min.js"></script>--%>


<%--<script>--%>
<%--    document.addEventListener('DOMContentLoaded', function() {--%>
<%--       const addToCartButton = document.querySelector('.addcart');--%>
<%--       if(addToCartButton) {--%>
<%--           addToCartButton.addEventListener('click', addToCart);--%>
<%--       }--%>
<%--    });--%>

<%--    function addToCart(productId, productName, productPrice) {--%>
<%--        // Получение токена пользователя--%>
<%--        const userToken = window.localStorage.getItem('token');--%>

<%--        // Формирование данных для отправки--%>
<%--        const data = {--%>
<%--            productId: productId,--%>
<%--            productName: productName,--%>
<%--            productPrice: productPrice,--%>
<%--            userToken: userToken--%>
<%--        };--%>
<%--        console.log(data);--%>

<%--        fetch(window.location.href, {--%>
<%--            method: 'POST',--%>
<%--            headers: {--%>
<%--                'Content-Type': 'application/json;charset=utf-8'--%>
<%--            },--%>
<%--            body: JSON.stringify({--%>
<%--                id: productId,--%>
<%--                name: productId,--%>
<%--                price: productPrice,--%>
<%--                token: userToken--%>
<%--            })--%>
<%--        }).then(r => r.text()).then(console.log);--%>
<%--    }--%>
<%--</script>--%>