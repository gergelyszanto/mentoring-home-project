package com.mentoring.model.requestbody;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CharacterRequest {
    private String characterName;
}
