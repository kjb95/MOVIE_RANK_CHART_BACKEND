package movierankchart.domain.users.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import movierankchart.domain.users.dto.request.FindUsersInChatRoomRequestDto;
import movierankchart.domain.users.dto.response.FindUsersInChatRoomResponseDtos;
import movierankchart.domain.users.service.UsersService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/users")
public class UsersController {
    private final UsersService usersService;

    @Operation(summary = "채팅방에 속한 유저 조회")
    @GetMapping
    public ResponseEntity<FindUsersInChatRoomResponseDtos> findUsersInChatRoom(FindUsersInChatRoomRequestDto findUsersInChatRoomRequestDto) {
        FindUsersInChatRoomResponseDtos findUsersInChatRoomResponseDtos = usersService.findUsersInChatRoom(findUsersInChatRoomRequestDto);
        return ResponseEntity.ok(findUsersInChatRoomResponseDtos);
    }

}
