package movierankchart.domain.users.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import movierankchart.domain.users.dto.request.FindUsersInChatRoomRequestDto;
import movierankchart.domain.users.dto.request.UpdateUserChatRoomRequestDto;
import movierankchart.domain.users.dto.response.CreateUserRequestDto;
import movierankchart.domain.users.dto.response.FindUsersInChatRoomResponseDtos;
import movierankchart.domain.users.service.UsersService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/users")
public class UsersController {
    private final UsersService usersService;

    @Operation(summary = "채팅방에 속한 유저 조회")
    @GetMapping
    public ResponseEntity<FindUsersInChatRoomResponseDtos> findUsersInChatRoom(@Valid FindUsersInChatRoomRequestDto findUsersInChatRoomRequestDto) {
        FindUsersInChatRoomResponseDtos findUsersInChatRoomResponseDtos = usersService.findUsersInChatRoom(findUsersInChatRoomRequestDto);
        return ResponseEntity.ok(findUsersInChatRoomResponseDtos);
    }

    @Operation(summary = "회원가입")
    @PostMapping
    public ResponseEntity<Void> createUser(@Valid @RequestBody CreateUserRequestDto createUserRequestDto) {
        usersService.createUser(createUserRequestDto);
        return ResponseEntity.ok()
                .build();
    }

    @Operation(summary = "채팅방에 유저 입장 or 나가기")
    @PatchMapping("/{usersId}")
    public ResponseEntity<Void> updateUserChatRoom(@PathVariable Long usersId, @Valid @RequestBody UpdateUserChatRoomRequestDto updateUserChatRoomRequestDto) {
        usersService.updateUserChatRoom(usersId, updateUserChatRoomRequestDto);
        return ResponseEntity.ok()
                .build();
    }

}
