package movierankchart.websocket.domain;

import movierankchart.domain.users.entity.Users;
import movierankchart.websocket.dto.response.SubscribedUserResponseDto;
import movierankchart.websocket.dto.response.SubscribedUsersResponseDto;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class UserSubscription {
    // Long: chatRoomId, String: httpSessionId, Users: user
    private final Map<Long, Map<String, Users>> subscribedUsers = new HashMap<>();

    public void addSubscription(Long chatRoomId, String sessionId, Users user) {
        subscribedUsers.putIfAbsent(chatRoomId, new HashMap<>());
        subscribedUsers.get(chatRoomId)
                .put(sessionId, user);
    }

    public void removeSubscription(String sessionId) {
        subscribedUsers.values()
                .stream()
                .filter(sessionIdToUsers -> sessionIdToUsers.containsKey(sessionId))
                .forEach(sessionIdToUsers -> sessionIdToUsers.remove(sessionId));
    }

    public SubscribedUsersResponseDto findSubscribedUsers(Long chatRoomId) {
        List<SubscribedUserResponseDto> subscribedUsers = this.subscribedUsers.get(chatRoomId)
                .values()
                .stream()
                .distinct()
                .map(Users::toSubscribedUserResponseDto)
                .collect(Collectors.toList());
        return new SubscribedUsersResponseDto(subscribedUsers);
    }

    public Long findChatRoomId(String sessionId) {
        return subscribedUsers.entrySet()
                .stream()
                .filter(entry -> entry.getValue()
                        .containsKey(sessionId))
                .findAny()
                .map(Map.Entry::getKey)
                .orElse(null);
    }

    public boolean isUsersInChatRoom(Users users, long chatRoomId) {
        Map<String, Users> sessionIdToUsers = subscribedUsers.get(chatRoomId);
        if (sessionIdToUsers == null) {
            return false;
        }
        return sessionIdToUsers.containsValue(users);
    }

}
