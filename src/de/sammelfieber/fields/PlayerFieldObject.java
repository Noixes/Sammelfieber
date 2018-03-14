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
//		if(this.needRepaint) {
			g.setColor(new Color(0,0,0));
            g.drawRect((x*30),(y*30),30,30);
            
            g.setColor(color);
            g.fillRect((x*30)+5,(y*30)+5,20,20);
            
			this.needRepaint = false;
//		}
	}
	
	public void setColor(Color color) {
		this.color = color;
	}

}