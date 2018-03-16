package de.sammelfieber.fields;

import java.awt.Color;
import java.awt.Graphics;

public class StopFieldObject extends AbstractFieldObject {

	public StopFieldObject() {
		super();
	}

	@Override
	public void draw(Graphics g, int x, int y) {
		g.setColor(new Color(144, 76, 0));
		g.fillRect((x * 30) + 5, (y * 30) + 5, 20, 20);

		this.needRepaint = false;
	}
}