package de.sammelfieber.fields;

import java.awt.Color;
import java.awt.Graphics;

public class WallFieldObject extends AbstractFieldObject {

	public WallFieldObject() {
		super();
	}

	@Override
	public void draw(Graphics g, int x, int y) {
		g.setColor(new Color(0, 0, 0));
		g.fillRect(x * 30, y * 30, 30, 30);
	}

}