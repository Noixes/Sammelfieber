package de.sammelfieber.fields;

import java.awt.Color;
import java.awt.Graphics;

public class NothingFieldObject extends AbstractFieldObject {

	public NothingFieldObject() {
		super();
	}

	@Override
	public void draw(Graphics g, int x, int y) {
		if(this.needRepaint) {
			g.setColor(new Color(0,0,0));
            g.drawRect((x*30),(y*30),30,30);
			this.needRepaint = false;
		}
	}

}