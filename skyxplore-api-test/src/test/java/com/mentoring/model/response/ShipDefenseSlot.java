package com.mentoring.model.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ShipDefenseSlot {
	private List<String> leftEquipped;
	private int rightSlot;
	private List<String> frontEquipped;
	private List<String> backEquipped;
	private int leftSlot;
	private int frontSlot;
	private List<String> rightEquipped;
	private int backSlot;
}