package com.saiirod.snake;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class Snake {

	private int width, height;
	private int mapWidth, mapHeight;
	private int x, y;
	private List<Point> cells;
	private PlayState map;
	private boolean up, down, left, right;

	/**
	 * Constructeur de classe
	 * @param map Le jeu
	 * @param tileSize taille des cellules
	 */
	public Snake(PlayState map, int tileSize) {
		width = height = tileSize;
		mapWidth = map.getWidth();
		mapHeight = map.getHeight();
		this.map = map;
	}

	/**
	 * Initialisation des variables pour une nouvelle partie
	 */
	public void init() {
		cells = new ArrayList<Point>();
		cells.add(new Point(x, y));
		up = down = left = right = false;
	}

	/**
	 * Change l'emplacement de la tete du snake à X,Y et vérifie que ces coordonnées ne sont pas outOfBounds
	 * @param x
	 * @param y
	 */
	public void setPosition(int x, int y) {
		if (x < 1) x = 1;
		if (x > mapWidth - 1) x = mapWidth - 1;
		if (y < 1) y = 1;
		if (y > mapHeight - 1) y = mapHeight - 1;
		this.x = x;
		this.y = y;
	}

	/**
	 * Déplace l'intégralité du snake aux coordonnées x,y. 
	 * @param x
	 * @param y
	 */
	public void moveTo(int x, int y) {
		if (x == 0 || x == mapWidth - 1 || y == 0 || y == mapHeight - 1 || collide(x, y)) {
			map.setLost();
		}
		else {
			int xTemp = x;
			int yTemp = y;
			for (int i = 0; i < cells.size(); i++) {
				Point p = cells.get(i);
				xTemp = xTemp ^ p.x;
				p.x = xTemp ^ p.x;
				xTemp = xTemp ^ p.x;

				yTemp = yTemp ^ p.y;
				p.y = yTemp ^ p.y;
				yTemp = yTemp ^ p.y;
			}
			if (hasToken(x, y)) {
				cells.add(new Point(xTemp, yTemp));
				map.nextToken();
				map.setScore();
			}
			setPosition(x, y);
		}
	}

	/**
	 * 
	 * @param x
	 * @param y
	 * @return True si un token se trouve aux coordonnées x,y
	 */
	private boolean hasToken(int x, int y) {
		return (map.getTokenX() == x && map.getTokenY() == y);
	}

	/**
	 * 
	 * @param x
	 * @param y
	 * @return True si le snake se percute sur lui même
	 */
	private boolean collide(int x, int y) {
		boolean result = false;
		for (Point p : cells) {
			if (p.getX() == x && p.getY() == y) {
				result = true;
				break;
			}
		}
		return result;
	}

	/**
	 * 
	 * @param x
	 * @param y
	 * @return true si le snake ne se trouve pas aux coordonnés x,y
	 */
	public boolean notAt(int x, int y) {
		return !collide(x, y);
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public void tick() {
		if (up) {
			moveTo(x, y - 1);
		}
		else if (down) {
			moveTo(x, y + 1);
		}
		else if (left) {
			moveTo(x - 1, y);
		}
		else if (right) {
			moveTo(x + 1, y);
		}
	}

	/**
	 * Crée le snake sur l'objet Graphics
	 * @param g
	 */
	public void render(Graphics2D g) {
		g.setColor(Color.WHITE);
		g.fillRect(x * width, y * height, width, height);
		for (Point p : cells) {
			g.fillRect(p.x * width, p.y * height, width, height);
		}

	}

	public void keyPressed(int k) {

		switch (k) {
			case KeyEvent.VK_Z:
			case KeyEvent.VK_UP:
				if (down) break;
				up = true;
				down = left = right = false;
				break;
			case KeyEvent.VK_S:
			case KeyEvent.VK_DOWN:
				if (up) break;
				down = true;
				up = left = right = false;
				break;
			case KeyEvent.VK_Q:
			case KeyEvent.VK_LEFT:
				if (right) break;
				left = true;
				up = down = right = false;
				break;
			case KeyEvent.VK_D:
			case KeyEvent.VK_RIGHT:
				if (left) break;
				right = true;
				up = left = down = false;
				break;
		}
	}
}
