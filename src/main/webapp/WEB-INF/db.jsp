<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<h1>Работа с базами данных</h1>
<h2>JDBC</h2>

<ul class="collection">
    <li class="collection-item">
        <h3>Подключение к базе данных</h3>
        <strong>JDBC</strong> - Java Database Connectivity - интерфейс для взаимодействия с базами данных. Технология
        доступа к данным, аналогичная ADO, PDO и т.д., цель которой есть предоставление универсального интерфейса для разных
        типов СУБД и других поставщиков данных.
        Технология базируется на подключении, передавании команд к СУБД и получение ответа (или ошибки, или данных). Технически
        взаимодействие реализуют драйверы (коннекторы). Програмно желательно реализовывать в виде служб (сервисов) поскольку
        доступ к данным может понадобиться в разных частях проекта.
    </li>
    <li class="collection-item">
        <h2>Префикс</h2>
        В случаях когда приходится объединять в одной базе данных несколько проектов, или несколько баз данных в одном проекте
        (например, для разделения прав доступа) - необходимо использовать префикс для таблиц. Префикс - это строка, которая
        добавляется перед именем таблицы. Например, если префикс <code>prefix_</code>, то таблица <code>users</code> будет
        называться <code>prefix_users</code>. Префикс необходимо добавлять во все запросы.
    </li>
    <li class="collection-item">
        <h2>Первые шаги</h2>
        Создаем таблицу БД
        <button id="db-create-button" class="waves-effect waves-light deep-purple btn">
            <i class="material-icons right">format_list_bulleted_add</i>
            Query
        </button>
    </li>
</ul>

<br/>

<div class="row">
    <form class="col s12">
        <div class="row">
            <div class="input-field col s5">
                <i class="material-icons prefix">badge</i>
                <input placeholder="Input your full name" id="db-call-me-name" type="text" class="validate">
                <label for="db-call-me-name">Name</label>
            </div>
            <div class="input-field col s5">
                <i class="material-icons prefix">phone_iphone</i>
                <input placeholder="+380 XX XXX XX XX" id="db-call-me-phone" type="tel" class="validate">
                <label for="db-call-me-phone">Phone number</label>
            </div>
            <div class="input-field col s2">
                <button type="button" id="db-call-me-button" class="waves-effect waves-light deep-purple btn">
                    <i class="material-icons right">support_agent</i>
                    Order
                </button>
            </div>
        </div>
    </form>
</div>

<h2>Logging</h2>
<div class="row">
    <button type="button" id="db-get-all-button" class="waves-effect waves-light deep-purple btn">
        <i class="material-icons right">table_rows</i>
        Get all
    </button>
    <button type="button" id="db-get-all-wout-deleted-button" class="waves-effect waves-light deep-purple btn">
        <i class="material-icons right">table_rows</i>
        Get all without deleted
    </button>
</div>
<div id="db-get-all-container">
</div>