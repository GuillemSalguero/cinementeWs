package com.app.auth.repository;

import com.app.auth.entity.UserFriend;
import com.app.auth.entity.UserFriendKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserFriendRepository extends JpaRepository<UserFriend, UserFriendKey> {
    List<UserFriend> findByIdUserId(Long userId);
}