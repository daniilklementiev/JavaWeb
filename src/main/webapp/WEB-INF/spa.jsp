<%@ page contentType="text/html;charset=UTF-8"%>
<h1>SPA</h1>
<p>
  <strong>SPA</strong> - Single Page Application - это приложение, которое работает в одном окне браузера, без перезагрузки страницы.
  Подход к разработке веб-приложения с минимальным количеством перезагрузок страницы. Переходы между страницами имитируются, то есть
  сама страница не обновляется, а изменяется контент определенных элементов (контейнеров). По требованиям безопасности обновляться
  страница должна при изменении статуса авторизации (как при входе, так и при выходе).
</p>
<div class="row">
  <button id="spa-btn-logout" class="btn deep-purple lighter-2">Log out</button>
  <button id="spa-btn-get-info" class="btn deep-purple lighter-2">Get info</button>
</div>
<div id="spa-container"></div>