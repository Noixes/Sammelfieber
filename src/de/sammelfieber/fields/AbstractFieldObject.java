package de.sammelfieber.fields;

import java.awt.Graphics;

public abstract class AbstractFieldObject {
	public boolean needRepaint;

	public AbstractFieldObject() {
		needRepaint = true;
	}

	public abstract void draw(Graphics g, int x, int y);
}