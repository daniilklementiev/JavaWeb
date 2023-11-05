package step.learning.ws;

import com.google.inject.Inject;
import com.google.inject.Injector;

import javax.servlet.http.HttpServletRequest;
import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;
import java.lang.reflect.Field;

public class WebsocketConfigurator extends ServerEndpointConfig.Configurator {
    @Inject
    private static Injector injector;

    @Override
    public <T> T getEndpointInstance(Class<T> endpointClass) throws InstantiationException {
        return injector.getInstance(endpointClass);
    }

    @Override
    public void modifyHandshake(ServerEndpointConfig sec, HandshakeRequest request, HandshakeResponse response) {
        super.modifyHandshake(sec, request, response);

        HttpServletRequest httpRequest = null;
        for(Field field : request.getClass().getDeclaredFields()) {
            if (HttpServletRequest.class.isAssignableFrom(field.getType())) {
                field.setAccessible(true);
                try {
                    httpRequest = (HttpServletRequest) field.get(request);
                    break;
                } catch (IllegalAccessException ignored) {
                }
            }
        }
        if(httpRequest!= null) {
            String culture = httpRequest.getAttribute("culture").toString();
            sec.getUserProperties().put("culture", culture);
        }
    }
}

/*
* Конфигуратор - класс, который отвечает за создание экземпляров классов, аннотированных @ServerEndpoint. По умолчанию, для каждого подключения создается новый экземпляр
* класса. Если необходимо, чтобы все подключения использовали один экземпляр, то необходимо переопределить метод getEndpointInstance. В данном случае, он возвращает
* существующий экземпляр класса WebsocketController
*
* Авторизация и Вебсокет
* В зависимости от способа поддержания авторизации на сервере, решения будут отличаться
*  - в случае серверной поддержки (HTTP-сессии) необходимо получить доступ к запросу, через который создается вебсокет и проверить, что сессия действительна
* - в случае клиентской поддержки (токен) необходимо получить доступ к заголовкам запроса и проверить, что токен действителен
*
* */