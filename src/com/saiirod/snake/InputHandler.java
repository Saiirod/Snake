package com.saiirod.snake;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


public class InputHandler implements KeyListener {

	private Game game;

	public InputHandler(Game game ){
		this.game = game;
	}
	
	public void keyPressed(KeyEvent e) {
		game.keyPressed(e.getKeyCode());
	}
	
	public void keyReleased(KeyEvent e) {}
	public void keyTyped(KeyEvent e) {}
}
