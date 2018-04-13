package de.sammelfieber.fields;

import java.awt.Color;
import java.awt.Graphics;

import de.sammelfieber.Playground;

public class PlayerFieldObject extends AbstractFieldObject {

	private Color color;
	public int id;

	public PlayerFieldObject() {
		super();
	}

	@Override
	public void draw(Graphics g, int x, int y) {
		if (Playground.jaegerSpieler == id) {
			g.setColor(Color.GREEN);
			g.fillRect((x * 30) + 1, (y * 30) + 1, 20 + 8, 20 + 8);
		}
		g.setColor(color);
		g.fillRect((x * 30) + 5, (y * 30) + 5, 20, 20);

	}

	public void setColor(Color color) {
		this.color = color;
	}

}