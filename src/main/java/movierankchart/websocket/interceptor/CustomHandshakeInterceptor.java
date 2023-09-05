package movierankchart.websocket.interceptor;

import movierankchart.websocket.constants.WebsocketConstants;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import javax.servlet.http.HttpSession;
import java.util.Map;

@Component
// HandshakeInterceptor: 웹소켓 연결 핸드셰이크 과정에서 요청과 응답을 가로채는 역할
public class CustomHandshakeInterceptor implements HandshakeInterceptor {

    @Override
    // Map<String, Object> attribute: 웹소켓 세션에 저장할 속성
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;
        HttpSession session = servletRequest.getServletRequest()
                .getSession();
        attributes.put(WebsocketConstants.HTTP_SESSION_ID, session.getId());
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {

    }
}
