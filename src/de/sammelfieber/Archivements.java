package de.sammelfieber;

import java.io.Serializable;
import java.util.List;

public class Archivements implements Serializable {
	private static final long serialVersionUID = 4716247623522098063L;
	public int globalCoins;
	public int portalEntered;
	public int playedGames;
	public int stop;
	public int gewonnenCoins;
	public int gewonnenJaeger;
	public List<Float> gameTimes;
	public List<String> archivements;
}