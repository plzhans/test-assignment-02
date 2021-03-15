package com.plzhans.assignment.api.auth;

import lombok.Getter;

@Getter
public class AuthRoomRequester {
    int userId;
    int roomId;

    public AuthRoomRequester(int userId, int roomId){
        this.userId = userId;
        this.roomId = roomId;
    }
}
