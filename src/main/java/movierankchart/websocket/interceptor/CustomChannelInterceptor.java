package movierankchart.websocket.interceptor;

import lombok.RequiredArgsConstructor;
import movierankchart.domain.users.entity.Users;
import movierankchart.domain.users.service.UsersService;
import movierankchart.security.constants.SecurityConstants;
import movierankchart.security.constants.SecurityErrorMessage;
import movierankchart.security.service.TokenProviderService;
import movierankchart.websocket.constants.SendMessageType;
import movierankchart.websocket.constants.WebsocketConstants;
import movierankchart.websocket.domain.UserSubscription;
import movierankchart.websocket.dto.response.SendMessageResponseDto;
import movierankchart.websocket.dto.response.SubscribedUsersResponseDto;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

@RequiredArgsConstructor
@Component
// ChannelInterceptor: 메시지 브로커의 채널에 대한 인터셉터 관련 인터페이스
// 메시지를 전송하기 전에 특정 작업을 수행
public class CustomChannelInterceptor implements ChannelInterceptor {
    private final TokenProviderService tokenProviderService;
    private final UserSubscription userSubscription;
    private final UsersService usersService;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final Map<StompCommand, Consumer<StompHeaderAccessor>> stompCommandToPreSendHandler = new HashMap<StompCommand, Consumer<StompHeaderAccessor>>() {{
        put(StompCommand.CONNECT, CustomChannelInterceptor.this::handlePreSendConnect);
        put(StompCommand.DISCONNECT, CustomChannelInterceptor.this::handlePreSendDisconnect);
    }};
    private final Map<StompCommand, Consumer<StompHeaderAccessor>> stompCommandToPostSendHandler = new HashMap<StompCommand, Consumer<StompHeaderAccessor>>() {{
        put(StompCommand.SUBSCRIBE, CustomChannelInterceptor.this::handlePostSendSubscribe);
    }};

    @Override
    // 메시지가 채널로 전송되기 전, 호출되는 메소드
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        Consumer<StompHeaderAccessor> handler = stompCommandToPreSendHandler.get(accessor.getCommand());
        if (handler != null) {
            handler.accept(accessor);
        }
        return message;
    }

    // 메시지가 채널로 전송된 후, 호출되는 메소드
    @Override
    public void postSend(Message<?> message, MessageChannel channel, boolean sent) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        Consumer<StompHeaderAccessor> handler = stompCommandToPostSendHandler.get(accessor.getCommand());
        if (handler != null) {
            handler.accept(accessor);
        }
    }

    // STOMP 메시지 헤더에 유저 정보 추가
    public void handlePreSendConnect(StompHeaderAccessor accessor) {
        String authorization = accessor.getFirstNativeHeader(SecurityConstants.JWT_TOKEN_HEADER);
        String accessToken = tokenProviderService.getTokenByRequestHeader(authorization);
        Authentication authentication = tokenProviderService.createAuthentication(accessToken);
        if (authentication == null) {
            throw new AuthenticationCredentialsNotFoundException(SecurityErrorMessage.NOT_FOUND_AUTHENTICATION);
        }
        accessor.setUser(authentication);
    }

    // 해당 채팅방의 유저 삭제
    // 유저가 채팅방에 나갔다는 메시지 전송
    private void handlePreSendDisconnect(StompHeaderAccessor accessor) {
        String sessionId = (String) accessor.getSessionAttributes()
                .get(WebsocketConstants.HTTP_SESSION_ID);
        Long chatRoomId = userSubscription.findChatRoomId(sessionId);
        if (chatRoomId == null) {  // 이미 웹 소켓 연결이 끊긴 경우
            return;
        }
        Authentication authentication = (Authentication) accessor.getUser();
        Users users = usersService.findUsers(authentication);
        userSubscription.removeSubscription(sessionId);
        boolean usersInChatRoom = userSubscription.isUsersInChatRoom(users, chatRoomId);
        if (usersInChatRoom) { // 채팅방에서 나갔지만, 다른 브라우저로 같은 유저가 채팅방에 남아있는 경우
            return;
        }
        sendMessage(SendMessageType.LEAVE, chatRoomId, users.getName());
    }

    // 해당 채팅방의 유저 추가
    // 유저가 채팅방에 들어왔다는 메시지 전송
    private void handlePostSendSubscribe(StompHeaderAccessor accessor) {
        String sessionId = (String) accessor.getSessionAttributes()
                .get(WebsocketConstants.HTTP_SESSION_ID);
        Authentication authentication = (Authentication) accessor.getUser();
        Users users = usersService.findUsers(authentication);
        String destination = accessor.getDestination();
        Long chatRoomId = Long.parseLong(destination.split("/")[2]);
        boolean usersInChatRoom = userSubscription.isUsersInChatRoom(users, chatRoomId);
        userSubscription.addSubscription(chatRoomId, sessionId, users);
        // SendMessageType.NOTHING: 같은 유저가 이미 채팅방에 들어와있는 경우
        SendMessageType sendMessageType = usersInChatRoom == true ? SendMessageType.NOTHING : SendMessageType.JOIN;
        sendMessage(sendMessageType, chatRoomId, users.getName());
    }

    private void sendMessage(SendMessageType sendMessageType, long chatRoomId, String userName) {
        SubscribedUsersResponseDto subscribedUsersResponseDto = userSubscription.findSubscribedUsers(chatRoomId);
        SendMessageResponseDto sendMessageResponseDto = SendMessageResponseDto.builder()
                .sendMessageType(sendMessageType)
                .subscribedUsers(subscribedUsersResponseDto)
                .name(userName)
                .build();
        simpMessagingTemplate.convertAndSend("/topic/" + chatRoomId, sendMessageResponseDto);
    }
}
