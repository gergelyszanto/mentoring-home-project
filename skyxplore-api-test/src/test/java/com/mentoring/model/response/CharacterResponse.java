package com.mentoring.model.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CharacterResponse {
    private String characterId;
    private String characterName;
}
