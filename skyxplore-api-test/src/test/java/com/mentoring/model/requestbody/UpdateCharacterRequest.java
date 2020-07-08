package com.mentoring.model.requestbody;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdateCharacterRequest {
    private String newCharacterName;
    private String characterId;
}
