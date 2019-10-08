package com.saiirod.snake;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.util.Random;

public class PlayState {

	private Snake snake;
	private int tileSize;
	private int height, width;
	private boolean lost;
	private int xToken, yToken;
	private Random random;
	private int score;
	private Font lostFont = new Font("Monospaced", Font.PLAIN, 12);
	private Font scoreFont = new Font("Monospaced", Font.PLAIN, 10);

	public PlayState(int width, int height, int tileSize) {
		this.tileSize = tileSize;
		this.height = height / tileSize;
		this.width = width / tileSize;
		random = new Random();
		init();
	}

	/**
	 * Initialise une nouvelle partie
	 */
	private void init() {
		score = 0;
		snake = new Snake(this, tileSize);
		snake.setPosition(1 + random.nextInt(height - 2), 1 + random.nextInt(height - 2));
		snake.init();

		lost = false;

		nextToken();
	}

	public void tick() {
		if (!hasLost()) {
			snake.tick();
		}
	}

	/**
	 * Crée chaque pixel de la fenetre et passe l'objet Graphics à l'objet snake
	 * @param g
	 */
	public void render(Graphics2D g) {
		g.setColor(Color.BLACK);
		g.setFont(scoreFont);
		g.drawString("Score: " + getScore(), 10, 7);
		if (!hasLost()) {
			g.setColor(Color.BLACK);
			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					if (y == 0 || y == height - 1 || x == 0 || x == width - 1) continue;
					g.fillRect(x * tileSize, y * tileSize, tileSize, tileSize);
				}
			}
			g.setColor(Color.GRAY.brighter());
			g.fillRect(xToken * tileSize, yToken * tileSize, tileSize, tileSize);
			snake.render(g);
		}
		else {
			g.setColor(Color.WHITE);
			int x = Game.WIDTH / 4;
			int y = Game.HEIGHT / 4;
			int w = 170;
			int h = 70;
			g.drawRect(x, y, w, h);
			g.setColor(Color.BLACK);
			g.fillRect(x + 2, y + 2, w - 2, h - 2);
			g.setFont(lostFont);
			g.setColor(Color.WHITE);
			g.drawString("You lost !", x + x / 2, y + y / 3);
			g.drawString("Score : " + getScore(), x + x / 2, y + y / 2);
			g.drawString("Escape: quit | R: retry", x + 4, y + y * 3 / 4);
		}
	}

	public void keyPressed(int k) {
		if (!hasLost()) {
			snake.keyPressed(k);
		}
		else {
			if (KeyEvent.VK_ESCAPE == k) {
				System.exit(0);
			}
			else if (KeyEvent.VK_R == k) {
				init();
			}
		}
	}

	public void nextToken() {
		int xR;
		int yR;
		do {
			xR = 1 + random.nextInt(width - 2);
			yR = 1 + random.nextInt(height - 2);
		} while (!snake.notAt(xR, yR));
		xToken = xR;
		yToken = yR;
	}

	public void setScore() {
		this.score++;
	}

	public int getScore() {
		return score * 3;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public int getTileSize() {
		return tileSize;
	}

	public void setLost() {
		lost = true;
	}

	public boolean hasLost() {
		return lost;
	}

	public int getTokenX() {
		return xToken;
	}

	public int getTokenY() {
		return yToken;
	}
}
