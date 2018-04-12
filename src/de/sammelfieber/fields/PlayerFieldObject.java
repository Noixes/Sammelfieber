package de.sammelfieber.fields;

import java.awt.Color;
import java.awt.Graphics;

public class PlayerFieldObject extends AbstractFieldObject {

	private Color color;

	public PlayerFieldObject() {
		super();
	}

	@Override
	public void draw(Graphics g, int x, int y) {
		g.setColor(color);
		g.fillRect((x * 30) + 5, (y * 30) + 5, 20, 20);

		this.needRepaint = false;
	}

	public void setColor(Color color) {
		this.color = color;
	}

}