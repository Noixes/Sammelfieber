package de.sammelfieber.fields;


public enum FeldStatus{
    NICHTS(NothingFieldObject.class),
    SPIELER(PlayerFieldObject.class),
    COIN(CoinFieldObject.class),
    WALL(WallFieldObject.class),
    PORTAL(PortalFieldObject.class),
    STOP(StopFieldObject.class);
	
	private Class<? extends AbstractFieldObject> fieldObjectClazz;

	private FeldStatus(Class<? extends AbstractFieldObject> fieldObjectClazz) {
		this.fieldObjectClazz = fieldObjectClazz;
	}
	
	public AbstractFieldObject getFieldObject() {
		try {
			return fieldObjectClazz.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
			throw new RuntimeException("Fehler beim Initialisieren des Feldes " + fieldObjectClazz.getName());
		}
	}

	public Class<? extends AbstractFieldObject> getFieldClazz() {
		return this.fieldObjectClazz;
	}
}