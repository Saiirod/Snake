package com.saiirod.snake;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;

public class Game extends Canvas implements Runnable {

	private static final long serialVersionUID = -8028401971320899226L;
	public static final int WIDTH = 320;
	public static final int HEIGHT = 320;
	public static final int SCALE = 2;
	public static final String TITLE = "Snake";

	private boolean running = false;
	private Thread thread;
	private BufferedImage screen;
	private PlayState playState;
	private InputHandler inputs;

	
	/**
	 * Initiatilisation du keyListener, de la frame, et du jeu
	 */
	private void init() {
		screen = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		playState = new PlayState(WIDTH, HEIGHT, 8);
		inputs = new InputHandler(this);
		addKeyListener(inputs);
	}

	/**
	 * Démarre le main thread
	 */
	public void start() {
		if (running) return;
		running = true;

		thread = new Thread(this, TITLE);
		thread.start();
	}

	/**
	 * Stop le main thread
	 */
	public void stop() {
		if (!running) return;
		running = false;
		try {
			thread.join();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Main loop, process la logic et le graphic 10 boucle par seconde et drop le reste.
	 */
	public void run() {
		long lastTime = System.nanoTime();
		double nanoPerTick = 1e9 / 10.0;
		double unprocessed = 0;
		long timer = System.currentTimeMillis();
		int tick = 0;
		int frame = 0;

		init();
		while (running) {
			long now = System.nanoTime();
			unprocessed += (now - lastTime) / nanoPerTick;
			lastTime = now;

			while (unprocessed >= 1) {
				tick();
				tick++;
				render();
				frame++;
				unprocessed -= 1;
			}
			while (System.currentTimeMillis() - timer > 1000) {
				System.out.printf("%d tick, %d fps\n", tick, frame);
				tick = frame = 0;
				timer += 1000;
			}
		}
	}

	/**
	 * Game logic
	 */
	public void tick() {
		playState.tick();
	}

	/**
	 * Game graphic
	 */
	public void render() {
		BufferStrategy bs = getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(2);
			return;
		}
		Graphics2D g = screen.createGraphics();
		g.setColor(Color.GRAY);
		g.fillRect(0, 0, WIDTH, HEIGHT);
		playState.render(g);
		g.dispose();

		g = (Graphics2D) bs.getDrawGraphics();

		g.drawImage(screen, 0, 0, getWidth(), getHeight(), null);
		g.dispose();
		bs.show();
	}

	/**
	 * InputLister pour gérer les pressions des touches sur le clavier
	 * @param k Touche du clavier appuyé
	 */
	public void keyPressed(int k) {
		playState.keyPressed(k);
	}

	public static void main(String[] args) {
		Game game = new Game();
		game.setSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));

		JFrame frame = new JFrame(TITLE);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.add(game);
		frame.pack();
		frame.setResizable(false);
		frame.setVisible(true);

		game.start();
		game.requestFocus();
	}
}
