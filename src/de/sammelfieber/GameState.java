package de.sammelfieber;

import de.sammelfieber.fields.AbstractFieldObject;

public class GameState {

	private AbstractFieldObject[][] foFelder;

	public AbstractFieldObject[][] getGameField() {
		return foFelder;
	}

	public void setFields(AbstractFieldObject[][] foFelder) {
		this.foFelder = foFelder;
	}

}
