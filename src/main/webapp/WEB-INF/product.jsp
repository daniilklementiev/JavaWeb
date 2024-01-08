<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<body class="cyan lighten-3">
<div class="container">
    <h2 class="mt-4">Добавление продукта</h2>
    <form method="post">
        <div class="form-group">
            <label for="name" style="color: black">Name:</label>
            <input type="text" class="form-control" id="name" name="name" required><br>
        </div>

        <div class="form-group">
            <label for="description" style="color: black">Description:</label>
            <textarea id="description" class="form-control" name="description" required></textarea><br>
        </div>

        <div class="form-group">
            <label for="price" style="color: black">Price:</label>
            <input type="number" id="price" name="price" required><br>
        </div>

        <div class="form-group">
            <label for="image" style="color: black">Image URL:</label>
            <input type="text" id="image" name="image" required><br>
        </div>
        <input type="submit" class="btn btn-primary">
    </form>
<%--        <div class="form-group">--%>
<%--            <label for="name">Название продукта:</label>--%>
<%--            <input type="text" class="form-control" id="name" name="name" required>--%>
<%--        </div>--%>

<%--        <div class="form-group">--%>
<%--            <label for="description">Описание продукта:</label>--%>
<%--            <textarea class="form-control" id="description" name="description" required></textarea>--%>
<%--        </div>--%>

<%--        <div class="form-group">--%>
<%--            <label for="price">Цена продукта:</label>--%>
<%--            <input type="number" class="form-control" id="price" name="price" step="0.01" required>--%>
<%--        </div>--%>

<%--        <div class="form-group">--%>
<%--            <label for="image">URL на изображение:</label>--%>
<%--            <input type="text" class="form-control" id="image" name="image" required>--%>
<%--        </div>--%>

<%--        <div class="form-group">--%>
<%--            <label for="file">Или загрузите изображение (если используете файл):</label>--%>
<%--            <input type="file" class="form-control-file" id="file" name="file">--%>
<%--        </div>--%>

<%--        <button type="submit" class="btn btn-primary">Добавить продукт</button>--%>
<%--    </form>--%>
</div>
<!-- Подключение Materialize JS -->
<script src="https://cdnjs.cloudflare.com/ajax/libs/materialize/1.0.0/js/materialize.min.js"></script>
</body>