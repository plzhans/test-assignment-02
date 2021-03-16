package com.plzhans.assignment.api.auth;

import lombok.Getter;

@Getter
public class AuthRoomRequester {
    int userId;
    String roomId;

    public AuthRoomRequester(int userId, String roomId){
        this.userId = userId;
        this.roomId = roomId;
    }
}
