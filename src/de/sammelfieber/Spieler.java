package de.sammelfieber;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

public class Spieler {
	public String id;
	public Color farbe;
	public int x, y;
	public int coins;

	private Map<Integer, KeyAction> keyActions;

	public Spieler(String id, int x, int y, Color farbe) {
		this.keyActions = new HashMap<>();
		this.coins = 0;
		this.id = id;
		this.farbe = farbe;
		this.x = x;
		this.y = y;
	}

	public void neuePosition(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public boolean keyEvent(Integer keyCode, GameState gameState) {
		return keyActions.getOrDefault(keyCode, (g) -> true).performAction(gameState);
	}

	public void addKeyAction(Integer keyCode, KeyAction keyAction) {
		this.keyActions.put(keyCode, keyAction);
	}
}