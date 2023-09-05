package movierankchart.websocket.config;

import movierankchart.websocket.interceptor.CustomHandshakeInterceptor;
import movierankchart.websocket.interceptor.CustomChannelInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
// WebSocketMessageBrokerConfigurer: 웹 소켓 연결을 구성하기 위한 메서드 제공
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    private final CustomChannelInterceptor customChannelInterceptor;
    private final CustomHandshakeInterceptor customHandshakeInterceptor;

    // WebsocketInterceptor가 빈등록 된 이후에 WebSocketConfig를 빈등록 하기 위해 @Lazy 어노테이션 사용
    public WebSocketConfig(@Lazy CustomChannelInterceptor customChannelInterceptor, CustomHandshakeInterceptor customHandshakeInterceptor) {
        this.customChannelInterceptor = customChannelInterceptor;
        this.customHandshakeInterceptor = customHandshakeInterceptor;
    }

    @Override
    // 메시지 브로커: 클라이언트와 서버간 메시지를 주고 받을때, 중간에서 메시지를 처리하는 역할
    // MessageBrokerRegistry: 메시지 브로커를 구성하기 위한 메서드 제공
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // 메시지 브로커를 활성화 하고, 해당 주소를 구독하고 있는 클라이언트에게 메시지를 전달
        config.enableSimpleBroker("/topic");
        // 클라이언트에서 서버로 메시지를 보낼 때 사용하는 경로의 prefix 설정
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    // 클라이언트가 웹 소켓 서버에 연결하는데 사용하는 웹 소켓 엔드포인트 등록
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .addInterceptors(customHandshakeInterceptor)
                .setAllowedOriginPatterns("*");
    }

    @Override
    // 웹 소켓 채널: 웹 소켓 프로토콜을 통해 클라이언트와 서버 간에 양방향 통신을 할 수 있는 경로
    // ChannelRegistration: 웹소켓 채널을 구성하기 위한 메서드 제공
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(customChannelInterceptor);
    }
}
