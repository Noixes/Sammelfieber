package de.sammelfieber;


import java.awt.*;
import javax.swing.*;

import de.sammelfieber.fields.FeldStatus;
import de.sammelfieber.fields.PlayerFieldObject;
import de.sammelfieber.fields.AbstractFieldObject;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

public class Playground extends JApplet implements KeyListener, Runnable {

	private int globalCoins;
	private boolean showed = false;
	
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
	
	long jaegerSpieler = 0;
	private long gameStartTime;

	public Playground() {
		this.setFocusable(true);
		this.addKeyListener(this);
		this.setMinimumSize(new Dimension(1280, 970));
		restart();
		Thread t = new Thread(this);
		t.start();
	}

	private void restart() {
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
		foFelder[0][0] = playerOne;
		spieler1 = new Spieler("Rot", 0, 0, RED);

		PlayerFieldObject playerTwo = (PlayerFieldObject) FeldStatus.SPIELER.getFieldObject();
		playerTwo.setColor(BLUE);
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
		
		spawnJaeger();

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
		g.fillRect(1000, 0, 200, 500);
		g.setColor(new Color(0, 0, 0));
		g.drawString("Zeit: " + (System.currentTimeMillis() - gameStartTime) / 1000, 1000, 20);
		
		g.drawString("Rot:", 1000, 40);
		g.drawString(spieler1.coins + " von " + maxPoints, 1040, 40);
		
		g.drawString("Blau: ", 1000, 60);
		g.drawString(spieler2.coins + " von " + maxPoints, 1040, 60);
	}

	public void keyTyped(KeyEvent e) {

	}

	public void keyPressed(KeyEvent e) {

	}

	public void keyReleased(KeyEvent e) {
		// Spieler 1
		if (playerOneMovable < System.currentTimeMillis()) {
			if (e.getKeyCode() == KeyEvent.VK_W) {
				if (spieler1.y != 0) {
					if (performStep(spieler1, spieler1.x, spieler1.y - 1)) {
						foFelder[spieler1.x][spieler1.y] = FeldStatus.NICHTS.getFieldObject();
						spieler1.neuePosition(spieler1.x, --spieler1.y);
						foFelder[spieler1.x][spieler1.y] = FeldStatus.SPIELER.getFieldObject();
						((PlayerFieldObject) foFelder[spieler1.x][spieler1.y]).setColor(RED);
					}
				}
			}
			if (e.getKeyCode() == KeyEvent.VK_A) {
				if (spieler1.x != 0) {
					if (performStep(spieler1, spieler1.x - 1, spieler1.y)) {
						foFelder[spieler1.x][spieler1.y] = FeldStatus.NICHTS.getFieldObject();
						spieler1.neuePosition(--spieler1.x, spieler1.y);
						foFelder[spieler1.x][spieler1.y] = FeldStatus.SPIELER.getFieldObject();
						((PlayerFieldObject) foFelder[spieler1.x][spieler1.y]).setColor(RED);
					}
				}
			}
			if (e.getKeyCode() == KeyEvent.VK_S) {
				if (spieler1.y != 31) {
					if (performStep(spieler1, spieler1.x, spieler1.y + 1)) {
						foFelder[spieler1.x][spieler1.y] = FeldStatus.NICHTS.getFieldObject();
						spieler1.neuePosition(spieler1.x, spieler1.y + 1);
						foFelder[spieler1.x][spieler1.y] = FeldStatus.SPIELER.getFieldObject();
						((PlayerFieldObject) foFelder[spieler1.x][spieler1.y]).setColor(RED);
					}
				}
			}
			if (e.getKeyCode() == KeyEvent.VK_D) {
				if (spieler1.x != 31) {
					if (performStep(spieler1, spieler1.x + 1, spieler1.y)) {
						foFelder[spieler1.x][spieler1.y] = FeldStatus.NICHTS.getFieldObject();
						spieler1.neuePosition(++spieler1.x, spieler1.y);
						foFelder[spieler1.x][spieler1.y] = FeldStatus.SPIELER.getFieldObject();
						((PlayerFieldObject) foFelder[spieler1.x][spieler1.y]).setColor(RED);
					}
				}
			}
		}

		// Spieler 2
		if (playerTwoMoveable < System.currentTimeMillis()) {
			if (e.getKeyCode() == KeyEvent.VK_UP) {
				if (spieler2.y != 0) {
					if (performStep(spieler2, spieler2.x, spieler2.y - 1)) {
						foFelder[spieler2.x][spieler2.y] = FeldStatus.NICHTS.getFieldObject();
						spieler2.neuePosition(spieler2.x, --spieler2.y);
						foFelder[spieler2.x][spieler2.y] = FeldStatus.SPIELER.getFieldObject();
						((PlayerFieldObject) foFelder[spieler2.x][spieler2.y]).setColor(BLUE);
					}
				}
			}
			if (e.getKeyCode() == KeyEvent.VK_LEFT) {
				if (spieler2.x != 0) {
					if (performStep(spieler2, spieler2.x - 1, spieler2.y)) {
						foFelder[spieler2.x][spieler2.y] = FeldStatus.NICHTS.getFieldObject();
						spieler2.neuePosition(--spieler2.x, spieler2.y);
						foFelder[spieler2.x][spieler2.y] = FeldStatus.SPIELER.getFieldObject();
						((PlayerFieldObject) foFelder[spieler2.x][spieler2.y]).setColor(BLUE);
					}
				}
			}
			if (e.getKeyCode() == KeyEvent.VK_DOWN) {
				if (spieler2.y != 31) {
					if (performStep(spieler2, spieler2.x, spieler2.y + 1)) {
						foFelder[spieler2.x][spieler2.y] = FeldStatus.NICHTS.getFieldObject();
						spieler2.neuePosition(spieler2.x, ++spieler2.y);
						foFelder[spieler2.x][spieler2.y] = FeldStatus.SPIELER.getFieldObject();
						((PlayerFieldObject) foFelder[spieler2.x][spieler2.y]).setColor(BLUE);
					}
				}
			}
			if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
				if (spieler2.x != 31) {
					if (performStep(spieler2, spieler2.x + 1, spieler2.y)) {
						foFelder[spieler2.x][spieler2.y] = FeldStatus.NICHTS.getFieldObject();
						spieler2.neuePosition(++spieler2.x, spieler2.y);
						foFelder[spieler2.x][spieler2.y] = FeldStatus.SPIELER.getFieldObject();
						((PlayerFieldObject) foFelder[spieler2.x][spieler2.y]).setColor(BLUE);
					}
				}
			}
		}
		//repaint();
	}

	boolean performStep(Spieler spieler, int x, int y) {
		Class<? extends AbstractFieldObject> fieldClass = foFelder[x][y].getClass();
		if (fieldClass.equals(FeldStatus.COIN.getFieldClazz())) {
			spieler.coins++;
			globalCoins++;
			showed = false;
			spawnCoin();
			if (spieler.coins == maxPoints) {

				JOptionPane.showMessageDialog(null, "Spieler " + spieler.id + " hat gewonnen!", "Gewonnen",
						JOptionPane.INFORMATION_MESSAGE);

				restart();
				return false;
			}

		}
		if(globalCoins == 15 && !showed) {
			showed = true;
			JOptionPane.showMessageDialog(null, "Sammelfan! - Sammle 15 Coins", "Achievement!",
					JOptionPane.INFORMATION_MESSAGE);
		}
		if(globalCoins == 50 && !showed) {
			showed = true;
			JOptionPane.showMessageDialog(null, "Sammelirre! - Sammle 50 Coins", "Achievement!",
					JOptionPane.INFORMATION_MESSAGE);
		}
		if(globalCoins == 100 && !showed) {
			showed = true;
			JOptionPane.showMessageDialog(null, "Sammelfieber! - Sammle 100 Coins", "Achievement!",
					JOptionPane.INFORMATION_MESSAGE);
		}
		if(fieldClass.equals(FeldStatus.JAEGER.getFieldClazz())) {
			if (spieler.equals(spieler1)) {
				jaegerSpieler = 1;
			} else {
				jaegerSpieler = 2;
			}
			spawnJaeger();
		}
		if (fieldClass.equals(FeldStatus.STOP.getFieldClazz())) {
			if (spieler.equals(spieler1)) {
				playerTwoMoveable = System.currentTimeMillis() + 500;
			} else {
				playerOneMovable = System.currentTimeMillis() + 500;
			}
			spawnStop();
		}
		if(fieldClass.equals(FeldStatus.SPIELER.getFieldClazz())){
			if (spieler.equals(spieler1) && jaegerSpieler == 1) {
				spieler.coins = maxPoints;
			} else if (spieler.equals(spieler2) && jaegerSpieler == 2) {
				spieler.coins = maxPoints;
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
		int x = r.nextInt(30)+1;
		int y = r.nextInt(30)+1;
		if (foFelder[x][y].getClass().equals(FeldStatus.NICHTS.getFieldClazz())) {
			if (num == 1) {
				portal1_x = x;
				portal1_y = y;
				foFelder[x][y] = FeldStatus.PORTAL.getFieldObject();
			} else {
				int dist = x - portal1_x + y - portal1_y;
				portal2_x = x;
				portal2_y = y;
				if(dist > 10) {
					foFelder[x][y] = FeldStatus.PORTAL.getFieldObject();
				}else {
					spawnPortal(num);
				}
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
		while(true) {
			try {
				Thread.sleep(1000/20);
				this.repaint();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}