package snake;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Insets;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class mainGame extends JPanel implements Runnable {

	// *** FIELDS ***//

	private static final long serialVersionUID = -2170434274000730026L;

	private static GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
	
	private static int w = gd.getDisplayMode().getWidth();
	private static int h = gd.getDisplayMode().getHeight()-40;
	
	public static int scale = 3;
	public static final int WIDTH = (w/3) - ((w/3)%10); 
	public static final int HEIGHT = (h/3) - ((h/3)%10); 

	public static int UPS = 10;

	private boolean running = false;
	private int ups = 0, fps = 0;
	private Point[] grass;

	private BufferedImage dbImage;
	private BufferedImage[][] texture;
	private Graphics2D dbG;
	private Thread thread;
	private JFrame frame;
	private Snake snake;
	private MKListen mkListen;
	private Insets insets;
	private LoadImage loadImage;
	private Food f;

	// *** CONSTRUCTOR ***//

	public mainGame() {
		dbImage = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		dbG = (Graphics2D) dbImage.getGraphics();
		thread = new Thread(this, "Game Runner");
		mkListen = new MKListen();
		loadImage = new LoadImage();
		texture = new BufferedImage[2][4];
		for (int i = 1; i < 2; i++)
			for (int j = 1; j < 4; j++)
				texture[i][j] = new BufferedImage(32, 32, BufferedImage.TYPE_INT_RGB);
		texture = loadImage.getImage(getClass().getResource("/res/Textures.png"),texture, 2, 4, 32);
		grass = new Point[20];
		System.out.println(WIDTH + " | " + HEIGHT);
	}

	// *** GETTERS ***//

	public int getUPS() {
		return ups;
	}

	public int getFPS() {
		return fps;
	}

	// *** METHODS ***//

	public void start() {
		running = true;

		f = new Food();
		// dbG.setRenderingHints(new
		// RenderingHints(RenderingHints.KEY_ANTIALIASING,
		// RenderingHints.VALUE_ANTIALIAS_ON));
		for (int i = 0; i < grass.length; i++) {
			grass[i] = new Point((int) (Math.random() * WIDTH) - 16, (int) (Math.random() * HEIGHT) - 16);
		}

		if (insets == null)
			insets = frame.getInsets();

		insets.top /= scale;
		insets.bottom /= scale;
		insets.right /= scale;
		insets.left /= scale;

		System.out.println(
				"top:" + insets.top + " right:" + insets.right + " bottom:" + insets.bottom + " left:" + insets.left);
		snake = new Snake();
		thread.start();
	}

	public void run() {

		long lastTime = System.nanoTime();
		long startTime = System.currentTimeMillis();
		double delta = 0.0d;
		double ns = 1000000000 / UPS;

		while (running) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = System.nanoTime();

			if (delta > 1) {
				ups++;
				update();
				render();
				delta--;
			}

			drawToScreen();
			fps++;

			if ((System.currentTimeMillis() - startTime) > 1000) {
				startTime += 1000;
				/*
				 * if (!snake.isDead()) snake.grow();
				 */
				frame.setTitle("Snake 1.0 || UPS:" + ups + " | FPS:" + fps);
				ups = 0;
				fps = 0;
			}
		}

		close();
	}

	public void update() {
		if ((f.getP().x + f.getP().y) == 0)
			f.generateFood(snake);
		if (mkListen.isQuit())
			running = false;

		if (snake.isDead()) {
			if (mkListen.isProceed()) {
				snake.restart();
				f.remove();
			}
		} else {
			snake.update(mkListen, f);
		}

	}

	public void render() {
		dbG.setColor(new Color(100, 168, 45));
		dbG.fillRect(0, 0, WIDTH, HEIGHT);
		for (Point p : grass)
			dbG.drawImage(texture[0][0], p.x, p.y, null);
		snake.render(dbG);
		f.render(dbG);
		if (snake.isDead())
			dead();
	}

	public void dead() {
		dbG.setColor(Color.RED);
		dbG.setFont(new Font("Chiller", Font.BOLD, 45));
		int len = (int) dbG.getFontMetrics().getStringBounds("YOU ARE DEAD!!!", dbG).getWidth();
		dbG.drawString("YOU ARE DEAD!!!", (WIDTH - len) / 2, HEIGHT / 2 - 20);
		dbG.setFont(new Font("Century Gothic", Font.BOLD, 12));
		len = (int) dbG.getFontMetrics().getStringBounds("Press 'SPACE to continue OR 'Q' for closing", dbG).getWidth();
		dbG.drawString("Press 'SPACE to continue OR 'Q' for closing", (WIDTH - len) / 2, (3 * HEIGHT) / 4 - 20);
		drawToScreen();
	}

	public void drawToScreen() {
		BufferedImage image = new BufferedImage(frame.getWidth(), frame.getHeight(), BufferedImage.TYPE_INT_RGB);
		Graphics2D g = ((Graphics2D) frame.getGraphics());
		int[] dbPixels = ((DataBufferInt) dbImage.getRaster().getDataBuffer()).getData();
		int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();

		for (int i = 0; i < HEIGHT * scale; i++)
			for (int j = 0; j < WIDTH * scale; j++)
				pixels[i * WIDTH * scale + j] = dbPixels[(i / scale) * WIDTH + (j / scale)];

		g.drawImage(image, 0, 0, null);
		g.dispose();
	}

	public void close() {
		running = false;
		System.out.println("Game Stopping...");
		try {
			thread.join(100);
			dbG.dispose();
			System.out.println("Game Stopped");
		} catch (InterruptedException e) {
			System.out.println(e.getMessage());
		}
		System.exit(0);
	}

	public static void main(String[] args) {
		mainGame game = new mainGame();
		game.frame = new JFrame("Snake 1.0");

		// game.frame.setUndecorated(true);
		game.frame.setPreferredSize(new Dimension(WIDTH * scale, HEIGHT * scale));
		game.frame.pack();
		
		//game.frame.setLocationRelativeTo(null);
		game.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		game.frame.setVisible(true);
		game.frame.add(game);
		game.frame.addKeyListener(game.mkListen);

		game.start();
	}

}
