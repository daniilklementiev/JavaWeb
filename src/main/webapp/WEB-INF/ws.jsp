<%@ page contentType="text/html;charset=UTF-8"%>
<h1>Websocket</h1>
<div class="row">
  <div class="col s3" id="chat-block">
    <b id="chat-nik">Authorizing...</b>
    <br/>
    <span style="font-size: x-small; color: rgb(128,128,128);" id="chat-token"></span>
    <br/>
    <label for="chat-input">Enter your message</label>
    <input id="chat-input" disabled type="text" placeholder="type here..."/>
    <button id="chat-send" disabled type="button" class="btn deep-purple lighter-2" onclick="sendMessageClick()">Send</button>

    <ul class="collection with-header" id="chat-container">
    </ul>

  </div>
  <div class="col s9">
    <h2>Идея</h2>
    <p>
      Websocket - является протоколом, который позволяет установить постоянное дуплексное соединение (сокет) между клиентом и сервером.
      При этом клиент и сервер могут обмениваться сообщениями в любой момент времени.
      При этом сервер может отправлять сообщения клиенту, а клиент серверу. Этот протокол чаще всего работает по схеме "ws://", но может работать и по "wss://".
        Первый вариант работает по http, а второй по https, поэтому второй вариант более безопасный, но для тестирования вполне подойдет и первый вариант.
    </p>
    <h2>Реализация</h2>
    <p>
      Реализация протокола чаще всего происходит на событийной моделе (Event Model) - жизненный цикл данных сопровождается созданием программных событий
      на которые можно подписать слушателей; то есть клиент и сервер могут обрабатывать события, которые происходят во время обмена сообщениями.
      Например, клиент может обрабатывать событие открытия соединения, а сервер событие закрытия соединения.
      <a href="https://mvnrepository.com/artifact/javax.websocket/javax.websocket-api/1.1"></a>
    </p>
    <h2>Конфигурация</h2>
    <p>Во-первых, инжекция. Поскольку сервер отдельный, его запросы не проходят фильтры и, соответственно, guice инжектор</p>
  </div>
</div>

<script>
  document.addEventListener("DOMContentLoaded", initWebsocket);
  function sendMessageClick() {
    window.websocket.send(JSON.stringify({
      command: 'chat',
      data: document.getElementById("chat-input").value
    }));
  }
  // Websocket Client
  function initWebsocket() {
    const token = JSON.parse(atob(window.localStorage.getItem('token')));
    // document.getElementById("chat-token").innerText = "Token expires at: " + ;
    const exp = new Date(token.exp);
    document.getElementById("chat-token").innerText = 'Token expires at: ' + dateString(exp);
    const host = window.location.host + getAppContext();
    const ws = new WebSocket(`ws://${host}/chat`);
    ws.onopen = onWsOpen;
    ws.onclose = onWsClose;
    ws.onmessage = onWsMessage;
    ws.onerror = onWsError;

    window.websocket = ws;
  }

  function dateString(date) {
    if(date.getDate() === new Date().getDate()) {
        return `${('0' + date.getHours()).slice(-2)}:${('0' + date.getMinutes()).slice(-2)}`;
    }
    return `${('0' + date.getDate()).slice(-2)}.${('0' + (date.getMonth() + 1)).slice(-2)}.${date.getFullYear()} ${('0' + date.getHours()).slice(-2)}:${('0' + date.getMinutes()).slice(-2)}`;
  }

  function onWsOpen(e) {
    // console.log(e, "Connection established");
    window.websocket.send(JSON.stringify({
      command: 'auth',
      data: window.localStorage.getItem('token')
    }));
  }

  function onWsClose(e) {
    console.log(e, "Connection closed");
  }

  function onWsMessage(e) {
    const chatMessage = JSON.parse(e.data);
      switch (chatMessage.status){
        case 201: // broadcast
                appendMessage(chatMessage.data, chatMessage.date);
                break;
        case 202: // token accepted, .data==nik
                enableChat(chatMessage.data);
                break;
        case 403: // token rejected
                disableChat();
                break;
        case 405: // unrecognized command
                console.error(chatMessage);
                break;
      }

  }

  function enableChat(nik) {
    document.getElementById("chat-nik").innerText = nik;
    for(let child of document.getElementById("chat-block").children){
      child.disabled = false;
    }
    appendMessage(nik + " joined the chat");
  }

  function disableChat() {
    document.getElementById("chat-nik").innerText = "Not authorized";
    for(let child of document.getElementById("chat-block").children){
      child.disabled = true;
    }
    appendMessage("You are not authorized");
  }

  function appendMessage(msg, date) {
    const li = document.createElement("li");
    li.className = "collection-item";

    const div = document.createElement("div");
    div.style.display = "flex";

    const spanText = document.createElement("span");
    spanText.innerText = msg;

    const spanDate = document.createElement("span");
    // spanDate.className = "secondary-content";
    spanDate.style.color = "rgb(128,128,128);"
    spanDate.style.marginLeft = "auto";
    const msgDate = new Date(date);
    const now = new Date();
    spanDate.style.fontSize = "x-small";
    spanDate.innerText = getMsgDateStr(msgDate, now);

    div.appendChild(spanText);
    div.appendChild(spanDate);

    li.appendChild(div);
    document.getElementById("chat-container").appendChild(li);
  }

  function getMsgDateStr(msgDate, nowDate) {
    const diffTime = nowDate - msgDate;
    const diffDays = Math.floor(diffTime / (1000 * 60 * 60 * 24));

    if (diffDays === 0) {
      return `Today, ${msgDate.toLocaleString('en-US', { hour: '2-digit', minute: '2-digit' })}`;
    } else if (diffDays === 1) {
      return `Yesterday, ${msgDate.toLocaleString('en-US', { hour: '2-digit', minute: '2-digit' })}`;
    } else if (diffDays >= 2 && diffDays <= 3) {
      return `${diffDays} days ago`;
    } else {
      return msgDate.toLocaleString('en-US', {
        day: '2-digit',
        month: '2-digit',
        year: 'numeric',
        hour: '2-digit',
        minute: '2-digit',
      });
    }
  }

  function onWsError(e) {
      console.log("Error: " + e.data);
  }

  function getAppContext() {
    const isContextPresent = false;
    return isContextPresent ? "" : window.location.pathname.substring(0, window.location.pathname.indexOf('/', 2));
  }
</script>