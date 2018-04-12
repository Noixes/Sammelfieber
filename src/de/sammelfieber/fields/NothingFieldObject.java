package de.sammelfieber.fields;

import java.awt.Color;
import java.awt.Graphics;

public class NothingFieldObject extends AbstractFieldObject {

	public NothingFieldObject() {
		super();
	}

	@Override
	public void draw(Graphics g, int x, int y) {
		g.setColor(new Color(255, 255, 255));
		g.fillRect((x * 30), (y * 30), 30, 30);
		this.needRepaint = false;
	}

}