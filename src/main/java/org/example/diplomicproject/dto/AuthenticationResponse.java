package org.example.diplomicproject.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

// AuthenticationResponse.java
@Builder
@Getter
@Setter
public class AuthenticationResponse {
    private String token;
    private String refreshToken;
}
