package com.mentoring.model.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ShipResponse {
	private List<String> connectorEquipped;
	private ShipWeaponSlot weaponSlot;
	private ShipDefenseSlot defenseSlot;
	private String shipType;
	private List<String> ability;
	private int coreHull;
	private int connectorSlot;
}
