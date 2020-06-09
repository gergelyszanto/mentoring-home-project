package com.mentoring.model.requestbody;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateUserRequest {
    private String username;
    private String password;
    private String email;
}
