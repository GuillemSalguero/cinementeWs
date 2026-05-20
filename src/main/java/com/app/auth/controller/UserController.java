package com.app.auth.controller;

import com.app.auth.dto.UserDtos.FriendResponse;
import com.app.auth.dto.UserDtos.UserResponse;
import com.app.auth.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.app.auth.dto.UserDtos.FriendMoviesResponse;


import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/find/user")
    public ResponseEntity<List<UserResponse>> getUserByName(@RequestParam String name) {
        return ResponseEntity.ok(userService.getByName(name));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUser(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getById(id));
    }


    @GetMapping("/{id}/friends")
    public ResponseEntity<List<FriendResponse>> getFriends(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getFriends(id));
    }

    @PostMapping("/{id}/friends")
    public ResponseEntity<FriendResponse> addFriend(
            @PathVariable Long id,
            @RequestBody Map<String, Long> body) {
        Long friendId = body.get("friendId");
        return ResponseEntity.ok(userService.addFriend(id, friendId));
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public ResponseEntity<Void> removeFriend(
            @PathVariable Long id,
            @PathVariable Long friendId) {
        userService.removeFriend(id, friendId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/friends/{friendId}/movies")
    public ResponseEntity<FriendMoviesResponse> getFriendMovies(
            @PathVariable Long id,
            @PathVariable Long friendId) {
        return ResponseEntity.ok(userService.getFriendMovies(id, friendId));
    }
}