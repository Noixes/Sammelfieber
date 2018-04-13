package de.sammelfieber;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.JApplet;
import javax.swing.JOptionPane;

import de.sammelfieber.fields.AbstractFieldObject;
import de.sammelfieber.fields.FeldStatus;
import de.sammelfieber.fields.PlayerFieldObject;

public class Playground extends JApplet implements KeyListener, Runnable {

	private Archivements archivements;

	private static final Color BLUE = new Color(0, 0, 255);
	private static final Color RED = new Color(255, 0, 0);
	private static final long serialVersionUID = -3997705229534458132L;
	Spieler spieler1, spieler2;
	Random r;
	public AbstractFieldObject[][] foFelder = new AbstractFieldObject[32][32];
	int maxPoints = 10;

	// Fürs erste:
	private int portal1_x;
	private int portal1_y;

	private int portal2_x;
	private int portal2_y;

	long playerOneMovable = 0;
	long playerTwoMoveable = 0;

	public static long jaegerSpieler = 0;
	private long gameStartTime;

	public Playground() {
		this.setFocusable(true);
		this.addKeyListener(this);
		this.setMinimumSize(new Dimension(1280, 970));
		readArchivements();
		restart();

		Thread t = new Thread(this);
		t.start();
		new Thread(() -> {
			while (true) {

				handleKeys();
				try {
					Thread.sleep(75);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	private void readArchivements() {
		InputStream fis = null;

		try {
			fis = new FileInputStream("archive.ments");
			ObjectInputStream o = new ObjectInputStream(fis);
			archivements = (Archivements) o.readObject();
		} catch (IOException e) {
			archivements = new Archivements();
			archivements.gameTimes = new ArrayList<>();
			archivements.archivements = new ArrayList<>();
		} catch (ClassNotFoundException e) {
		} finally {
			try {
				fis.close();
			} catch (Exception e) {
			}
		}
	}

	private String getGameStats() {

		float avgGameTime = 0;
		for (Float gt : archivements.gameTimes) {
			avgGameTime += gt.doubleValue();
		}
		avgGameTime /= archivements.gameTimes.size();

		String showStats = "Gesammelte Münzen: " + archivements.globalCoins + "\n" + "Gesammelte Münzen pro Game: "
				+ ((float) archivements.globalCoins / (float) archivements.playedGames) + "\n"
				+ "Benutzte Portale pro Game: "
				+ ((float) archivements.portalEntered / (float) archivements.playedGames) + "\n" + "Played Games: "
				+ archivements.playedGames + "\n" + "Stop pro Game: "
				+ ((float) archivements.stop / (float) archivements.playedGames) + "\n" + "Jäger gewonnen: "
				+ archivements.gewonnenJaeger + "\n" + "Sammler gewonnen: " + archivements.gewonnenCoins + "\n"
				+ "Durchschnittliche Spielzeit in Sekunden: " + avgGameTime;

		showStats += "\n\n\n";
		showStats += "Archivements:\n";

		for (String archivement : archivements.archivements) {
			showStats += archivement + "\n";
		}

		return showStats;
	}

	private void restart() {
		for (int i = 0; i < keys.length; i++) {
			keys[i] = new AtomicBoolean(false);
		}
		if (gameStartTime != 0) {
			archivements.gameTimes.add(Float.valueOf((System.currentTimeMillis() - gameStartTime) / 1000));
			writeArchivement();
		}
		archivements.playedGames++;
		jaegerSpieler = 0;
		for (int x = 0; x < 32; x++) {
			for (int y = 0; y < 32; y++) {
				foFelder[x][y] = FeldStatus.NICHTS.getFieldObject();
			}
		}

		gameStartTime = System.currentTimeMillis();
		r = new Random(gameStartTime);
		PlayerFieldObject playerOne = (PlayerFieldObject) FeldStatus.SPIELER.getFieldObject();
		playerOne.setColor(RED);
		playerOne.id = 1;
		foFelder[0][0] = playerOne;
		spieler1 = new Spieler("Rot", 0, 0, RED);

		PlayerFieldObject playerTwo = (PlayerFieldObject) FeldStatus.SPIELER.getFieldObject();
		playerTwo.setColor(BLUE);
		playerTwo.id = 2;
		foFelder[31][31] = playerTwo;
		spieler2 = new Spieler("Blau", 31, 31, BLUE);

		spawnCoin();
		spawnCoin();

		for (int i = 0; i < 40; i++) {
			spawnWall();
		}

		spawnPortal(1);
		spawnPortal(2);

		spawnStop();

		// spawnJaeger();

	}

	@Override
	public void update(Graphics g) {
		paint(g);
	}

	public void paint(Graphics g) {
		this.setSize(new Dimension(1280, 980));
		for (int x = 0; x < 32; x++) {
			for (int y = 0; y < 32; y++) {
				if ((x == portal1_x && y == portal1_y) || (x == portal2_x && y == portal2_y)) {
					foFelder[x][y] = FeldStatus.PORTAL.getFieldObject();
				}
				foFelder[x][y].draw(g, x, y);
			}
		}
		g.setColor(Color.WHITE);
		g.fillRect(1000, 0, 200, 100);
		g.setColor(new Color(0, 0, 0));
		g.drawString("Zeit: " + (System.currentTimeMillis() - gameStartTime) / 1000, 1000, 20);

		g.drawString("Rot:", 1000, 40);
		g.drawString(spieler1.coins + " von " + maxPoints, 1040, 40);

		g.drawString("Blau: ", 1000, 60);
		g.drawString(spieler2.coins + " von " + maxPoints, 1040, 60);
	}

	AtomicBoolean[] keys = new AtomicBoolean[255];

	public void keyTyped(KeyEvent e) {
	}

	public void keyPressed(KeyEvent e) {
		keys[e.getKeyCode()].set(true);
	}

	private void handleKeys() {
		if (playerOneMovable < System.currentTimeMillis()) {
			if (keys[KeyEvent.VK_W].get()) {
				if (spieler1.y != 0) {
					if (performStep(spieler1, spieler1.x, spieler1.y - 1)) {
						foFelder[spieler1.x][spieler1.y] = FeldStatus.NICHTS.getFieldObject();
						spieler1.neuePosition(spieler1.x, --spieler1.y);
						foFelder[spieler1.x][spieler1.y] = FeldStatus.SPIELER.getFieldObject();

						((PlayerFieldObject) foFelder[spieler1.x][spieler1.y]).setColor(RED);
						((PlayerFieldObject) foFelder[spieler1.x][spieler1.y]).id = 1;
					}
				}
			}
			if (keys[KeyEvent.VK_A].get()) {
				if (spieler1.x != 0) {
					if (performStep(spieler1, spieler1.x - 1, spieler1.y)) {
						foFelder[spieler1.x][spieler1.y] = FeldStatus.NICHTS.getFieldObject();
						spieler1.neuePosition(--spieler1.x, spieler1.y);
						foFelder[spieler1.x][spieler1.y] = FeldStatus.SPIELER.getFieldObject();
						((PlayerFieldObject) foFelder[spieler1.x][spieler1.y]).setColor(RED);
						((PlayerFieldObject) foFelder[spieler1.x][spieler1.y]).id = 1;
					}
				}
			}
			if (keys[KeyEvent.VK_S].get()) {
				if (spieler1.y != 31) {
					if (performStep(spieler1, spieler1.x, spieler1.y + 1)) {
						foFelder[spieler1.x][spieler1.y] = FeldStatus.NICHTS.getFieldObject();
						spieler1.neuePosition(spieler1.x, spieler1.y + 1);
						foFelder[spieler1.x][spieler1.y] = FeldStatus.SPIELER.getFieldObject();
						((PlayerFieldObject) foFelder[spieler1.x][spieler1.y]).setColor(RED);
						((PlayerFieldObject) foFelder[spieler1.x][spieler1.y]).id = 1;
					}
				}
			}
			if (keys[KeyEvent.VK_D].get()) {
				if (spieler1.x != 31) {
					if (performStep(spieler1, spieler1.x + 1, spieler1.y)) {
						foFelder[spieler1.x][spieler1.y] = FeldStatus.NICHTS.getFieldObject();
						spieler1.neuePosition(++spieler1.x, spieler1.y);
						foFelder[spieler1.x][spieler1.y] = FeldStatus.SPIELER.getFieldObject();
						((PlayerFieldObject) foFelder[spieler1.x][spieler1.y]).setColor(RED);
						((PlayerFieldObject) foFelder[spieler1.x][spieler1.y]).id = 1;
					}
				}
			}
		}
		// Spieler 2
		if (playerTwoMoveable < System.currentTimeMillis()) {
			if (keys[KeyEvent.VK_UP].get()) {
				if (spieler2.y != 0) {
					if (performStep(spieler2, spieler2.x, spieler2.y - 1)) {
						foFelder[spieler2.x][spieler2.y] = FeldStatus.NICHTS.getFieldObject();
						spieler2.neuePosition(spieler2.x, --spieler2.y);
						foFelder[spieler2.x][spieler2.y] = FeldStatus.SPIELER.getFieldObject();
						((PlayerFieldObject) foFelder[spieler2.x][spieler2.y]).setColor(BLUE);
						((PlayerFieldObject) foFelder[spieler2.x][spieler2.y]).id = 2;
					}
				}
			}
			if (keys[KeyEvent.VK_LEFT].get()) {
				if (spieler2.x != 0) {
					if (performStep(spieler2, spieler2.x - 1, spieler2.y)) {
						foFelder[spieler2.x][spieler2.y] = FeldStatus.NICHTS.getFieldObject();
						spieler2.neuePosition(--spieler2.x, spieler2.y);
						foFelder[spieler2.x][spieler2.y] = FeldStatus.SPIELER.getFieldObject();
						((PlayerFieldObject) foFelder[spieler2.x][spieler2.y]).setColor(BLUE);
						((PlayerFieldObject) foFelder[spieler2.x][spieler2.y]).id = 2;
					}
				}
			}
			if (keys[KeyEvent.VK_DOWN].get()) {
				if (spieler2.y != 31) {
					if (performStep(spieler2, spieler2.x, spieler2.y + 1)) {
						foFelder[spieler2.x][spieler2.y] = FeldStatus.NICHTS.getFieldObject();
						spieler2.neuePosition(spieler2.x, ++spieler2.y);
						foFelder[spieler2.x][spieler2.y] = FeldStatus.SPIELER.getFieldObject();
						((PlayerFieldObject) foFelder[spieler2.x][spieler2.y]).setColor(BLUE);
						((PlayerFieldObject) foFelder[spieler2.x][spieler2.y]).id = 2;
					}
				}
			}
			if (keys[KeyEvent.VK_RIGHT].get()) {
				if (spieler2.x != 31) {
					if (performStep(spieler2, spieler2.x + 1, spieler2.y)) {
						foFelder[spieler2.x][spieler2.y] = FeldStatus.NICHTS.getFieldObject();
						spieler2.neuePosition(++spieler2.x, spieler2.y);
						foFelder[spieler2.x][spieler2.y] = FeldStatus.SPIELER.getFieldObject();
						((PlayerFieldObject) foFelder[spieler2.x][spieler2.y]).setColor(BLUE);
						((PlayerFieldObject) foFelder[spieler2.x][spieler2.y]).id = 2;
					}
				}
			}
		}
	}

	public void keyReleased(KeyEvent e) {
		keys[e.getKeyCode()].set(false);
		if (e.getKeyCode() == KeyEvent.VK_Z) {
			JOptionPane.showMessageDialog(null, getGameStats(), "Gamestats", JOptionPane.INFORMATION_MESSAGE);
		}
		if (e.getKeyCode() == KeyEvent.VK_I) {
			int i = JOptionPane.showConfirmDialog(null, "Wirklich löschen?", "Gamestats löschen?",
					JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
			System.out.println(i);
			if (i == 0) {
				new File("archive.ments").delete();
				restart();
			}
		}
	}

	boolean performStep(Spieler spieler, int x, int y) {
		Class<? extends AbstractFieldObject> fieldClass = foFelder[x][y].getClass();
		if (fieldClass.equals(FeldStatus.COIN.getFieldClazz())) {
			spieler.coins++;
			if (spieler.coins == 5) {
				spawnJaeger();
			}
			archivements.globalCoins++;
			writeArchivement();
			spawnCoin();
			if (spieler.coins == maxPoints) {

				JOptionPane.showMessageDialog(null, "Spieler " + spieler.id + " hat gewonnen!", "Gewonnen",
						JOptionPane.INFORMATION_MESSAGE);
				archivements.gewonnenCoins++;
				writeArchivement();
				restart();
				return false;
			}

		}
		if (archivements.globalCoins == 15 && !archivements.archivements.contains("Sammelfan! - Sammle 15 Coins")) {
			String ar = "Sammelfan! - Sammle 15 Coins";
			JOptionPane.showMessageDialog(null, ar, "Achievement!", JOptionPane.INFORMATION_MESSAGE);
			archivements.archivements.add(ar);
			writeArchivement();
		}
		if (archivements.globalCoins == 50 && !archivements.archivements.contains("Sammelirre! - Sammle 50 Coins")) {
			String ar = "Sammelirre! - Sammle 50 Coins";
			JOptionPane.showMessageDialog(null, ar, "Achievement!", JOptionPane.INFORMATION_MESSAGE);
			archivements.archivements.add(ar);
			writeArchivement();
		}
		if (archivements.globalCoins == 100
				&& !archivements.archivements.contains("Sammelfieber! - Sammle 100 Coins")) {
			String ar = "Sammelfieber! - Sammle 100 Coins";
			JOptionPane.showMessageDialog(null, ar, "Achievement!", JOptionPane.INFORMATION_MESSAGE);
			archivements.archivements.add(ar);
			writeArchivement();
		}
		if (archivements.portalEntered == 10
				&& !archivements.archivements.contains("Now you're thinking with portals!")) {
			String ar = "Now you're thinking with portals!";
			JOptionPane.showMessageDialog(null, ar, "Achievement!", JOptionPane.INFORMATION_MESSAGE);
			archivements.archivements.add(ar);
			writeArchivement();
		}
		if (fieldClass.equals(FeldStatus.JAEGER.getFieldClazz())) {
			if (spieler.equals(spieler1)) {
				jaegerSpieler = 1;
			} else {
				jaegerSpieler = 2;
			}
			// spawnJaeger();
		}
		if (fieldClass.equals(FeldStatus.STOP.getFieldClazz())) {
			archivements.stop++;
			writeArchivement();
			if (spieler.equals(spieler1)) {
				if (jaegerSpieler == 2) {
					spawnJaeger();
					jaegerSpieler = 0;
				}
				playerTwoMoveable = System.currentTimeMillis() + 500;
			} else {
				if (jaegerSpieler == 1) {
					spawnJaeger();
					jaegerSpieler = 0;
				}
				playerOneMovable = System.currentTimeMillis() + 500;
			}
			spawnStop();
		}
		if (fieldClass.equals(FeldStatus.SPIELER.getFieldClazz())) {
			if (spieler.equals(spieler1) && jaegerSpieler == 1) {
				JOptionPane.showMessageDialog(null, "Spieler " + spieler.id + " hat gewonnen!", "Gewonnen",
						JOptionPane.INFORMATION_MESSAGE);
				archivements.gewonnenJaeger++;
				restart();
				return false;
			} else if (spieler.equals(spieler2) && jaegerSpieler == 2) {
				JOptionPane.showMessageDialog(null, "Spieler " + spieler.id + " hat gewonnen!", "Gewonnen",
						JOptionPane.INFORMATION_MESSAGE);
				archivements.gewonnenJaeger++;
				restart();
				return false;
			}
		}
		if (spieler.coins == maxPoints) {

			JOptionPane.showMessageDialog(null, "Spieler " + spieler.id + " hat gewonnen!", "Gewonnen",
					JOptionPane.INFORMATION_MESSAGE);
			restart();
			return false;
		}
		if (fieldClass.equals(FeldStatus.SPIELER.getFieldClazz())
				|| fieldClass.equals(FeldStatus.WALL.getFieldClazz())) {
			return false;
		}
		if (fieldClass.equals(FeldStatus.PORTAL.getFieldClazz())) {
			archivements.portalEntered++;
			writeArchivement();
			foFelder[spieler.x][spieler.y] = FeldStatus.NICHTS.getFieldObject();
			if (x == portal1_x && y == portal1_y) {
				spieler.x = portal2_x;
				spieler.y = portal2_y;
			} else {
				spieler.x = portal1_x;
				spieler.y = portal1_y;
			}
			return true;
		}
		return true;
	}

	private void writeArchivement() {
		OutputStream fos = null;

		try {
			fos = new FileOutputStream("archive.ments", false);
			ObjectOutputStream o = new ObjectOutputStream(fos);
			o.writeObject(archivements);
			o.flush();
			o.close();
		} catch (IOException e) {
			System.err.println(e);
		} finally {
			try {
				fos.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	void spawnCoin() {
		int x = r.nextInt(32);
		int y = r.nextInt(32);
		if (foFelder[x][y].getClass().equals(FeldStatus.NICHTS.getFieldClazz())) {
			foFelder[x][y] = FeldStatus.COIN.getFieldObject();
		} else {
			spawnCoin();
		}
	}

	void spawnJaeger() {
		int x = r.nextInt(32);
		int y = r.nextInt(32);
		if (foFelder[x][y].getClass().equals(FeldStatus.NICHTS.getFieldClazz())) {
			foFelder[x][y] = FeldStatus.JAEGER.getFieldObject();
		} else {
			spawnJaeger();
		}
	}

	void spawnStop() {
		int x = r.nextInt(32);
		int y = r.nextInt(32);
		if (foFelder[x][y].getClass().equals(FeldStatus.NICHTS.getFieldClazz())) {
			foFelder[x][y] = FeldStatus.STOP.getFieldObject();
		} else {
			spawnStop();
		}
	}

	private void spawnPortal(int num) {
		int x = r.nextInt(30) + 1;
		int y = r.nextInt(30) + 1;
		if (foFelder[x][y].getClass().equals(FeldStatus.NICHTS.getFieldClazz())) {
			if (num == 1) {
				portal1_x = x;
				portal1_y = y;
				foFelder[x][y] = FeldStatus.PORTAL.getFieldObject();
			} else {
				int dist = x - portal1_x + y - portal1_y;
				portal2_x = x;
				portal2_y = y;
				foFelder[x][y] = FeldStatus.PORTAL.getFieldObject();
			}
		} else {
			spawnPortal(num);
		}
	}

	void spawnWall() {
		int x = r.nextInt(32);
		int y = r.nextInt(32);
		if (foFelder[x][y].getClass().equals(FeldStatus.NICHTS.getFieldClazz())) {
			foFelder[x][y] = FeldStatus.WALL.getFieldObject();
		} else {
			spawnWall();
		}
	}

	@Override
	public void run() {
		while (true) {
			try {
				Thread.sleep(1000 / 15);
				this.repaint();
			} catch (InterruptedException e) {
			}
		}
	}
}