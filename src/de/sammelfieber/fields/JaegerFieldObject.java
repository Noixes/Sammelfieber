package de.sammelfieber.fields;

import java.awt.Color;
import java.awt.Graphics;

public class JaegerFieldObject extends AbstractFieldObject {

	public JaegerFieldObject() {
		super();
	}

	@Override
	public void draw(Graphics g, int x, int y) {
		g.setColor(new Color(144, 0, 144));
		g.fillRect((x * 30) + 5, (y * 30) + 5, 20, 20);

		this.needRepaint = false;
	}
}