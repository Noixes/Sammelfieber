package de.sammelfieber.fields;

import java.awt.Color;
import java.awt.Graphics;

public class CoinFieldObject extends AbstractFieldObject {

	public CoinFieldObject() {
		super();
	}

	@Override
	public void draw(Graphics g, int x, int y) {
//		if(this.needRepaint) {
			g.setColor(new Color(255, 215, 0));
        	g.fillOval(x*30+7, y*30+2, 16, 27);
//        	this.needRepaint = false;
//		}
	}
}