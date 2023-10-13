<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<h1>Инверсия управления в Web приложении</h1>

<ul class="collection">
    <li class="collection-item">
        Добавляем Maven зависимости в pom.xml для
        <a href="https://mvnrepository.com/artifact/com.google.inject.extensions/guice-servlet">Guice</a>
        и
        <a href="https://mvnrepository.com/artifact/com.google.inject.extensions/guice-servlet">Guice Servlet</a>
    </li>
    <li class="collection-item">
        Создаем пакет Ioc и в нем конфигурационные классы
        IocContextListener - обработчик события создания контекста ( такой себе аналог стартового метода main() )
        RouterModule - класс с настройками фильтров и сервлетов
        ServicesModule - класс с настройками сервисов
    </li>
    <li class="collection-item">
        Изменяем web.xml - оставляем фильтр от Guice и наш контекст листенер (см. web.xml)
    </li>
    <li class="collection-item">
        !!! для всех фильтров и сервлетов, заявленных в RouterModule необходимо добавить аннотацию @Singleton
    </li>
    <li class="collection-item">
        Инжекция служб(сервисов) происходит также как и в консольном проекте. Проверка: <%=request.getAttribute("hash")%>
    </li>
</ul>
