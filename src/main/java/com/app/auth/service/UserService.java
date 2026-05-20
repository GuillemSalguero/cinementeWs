package com.app.auth.service;

import com.app.auth.dto.UserDtos.FriendMoviesResponse;
import com.app.auth.dto.UserDtos.FriendResponse;
import com.app.auth.dto.UserDtos.UserResponse;
import com.app.auth.entity.User;
import com.app.auth.entity.UserFriend;
import com.app.auth.entity.UserFriendKey;
import com.app.auth.repository.UserFriendRepository;
import com.app.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.app.auth.repository.UserFavoriteMovieRepository;
import com.app.auth.repository.UserWatchlistRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserFriendRepository userFriendRepository;
    private final UserFavoriteMovieRepository favoriteMovieRepository;
    private final UserWatchlistRepository watchlistRepository;

    public UserResponse getById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado: " + id));
        return new UserResponse(user.getId(), user.getName(), user.getFavFilms(), user.getFriend(), user.getCreatedAt());
    }

    public List<FriendResponse> getFriends(Long userId) {
        return userFriendRepository.findByIdUserId(userId).stream()
                .map(uf -> new FriendResponse(
                        uf.getFriend().getId(),
                        uf.getFriend().getName(),
                        uf.getCreatedAt()
                ))
                .collect(Collectors.toList());
    }

    public FriendResponse addFriend(Long userId, Long friendId) {
        if (userId.equals(friendId))
            throw new IllegalArgumentException("Un usuario no puede ser amigo de sí mismo");

        UserFriendKey key = new UserFriendKey(userId, friendId);
        if (userFriendRepository.existsById(key))
            throw new IllegalArgumentException("Ya son amigos");

        User user   = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado: " + userId));
        User friend = userRepository.findById(friendId)
                .orElseThrow(() -> new IllegalArgumentException("Amigo no encontrado: " + friendId));

        UserFriend uf = UserFriend.builder()
                .id(key)
                .user(user)
                .friend(friend)
                .build();

        UserFriend saved = userFriendRepository.save(uf);
        return new FriendResponse(friendId, friend.getName(), saved.getCreatedAt());
    }

    public void removeFriend(Long userId, Long friendId) {
        UserFriendKey key = new UserFriendKey(userId, friendId);
        if (!userFriendRepository.existsById(key))
            throw new IllegalArgumentException("No son amigos");
        userFriendRepository.deleteById(key);
    }

    public List<UserResponse> getByName(String name) {
        return userRepository.findByNameContainingIgnoreCase(name)
                .stream()
                .map(user -> new UserResponse(
                    user.getId(),
                    user.getName(),
                    user.getFavFilms(),
                    user.getFriend(),
                    user.getCreatedAt()
                ))
                .toList();
    }

    public FriendMoviesResponse getFriendMovies(Long userId, Long friendId) {
        // Verificar que son amigos
        UserFriendKey key = new UserFriendKey(userId, friendId);
        if (!userFriendRepository.existsById(key)) {
            throw new IllegalArgumentException("No sois amigos");
        }

        User friend = userRepository.findById(friendId)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado: " + friendId));

        List<String> favorites = favoriteMovieRepository.findByIdUserId(friendId)
                .stream()
                .map(f -> f.getId().getMovieLink())
                .collect(Collectors.toList());

        List<String> watchlist = watchlistRepository.findByIdUserId(friendId)
                .stream()
                .map(w -> w.getId().getMovieLink())
                .collect(Collectors.toList());

        return new FriendMoviesResponse(friendId, friend.getName(), favorites, watchlist);
    }
}