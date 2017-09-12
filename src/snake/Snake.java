package snake;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Snake {

	private final int INITSIZE = 10;
	private static int growLimit;
	private static int currGrow;

	private ArrayList<Point> body;
	private ArrayList<Integer> orient;
	public static final int SIZE = 10;
	private int dir;
	private boolean left, right, up, down, dead;

	private LoadImage load;
	private BufferedImage[][] snakeImg;

	public Snake() {
		body = new ArrayList<Point>(2);
		orient = new ArrayList<Integer>(2);
		load = new LoadImage();
		snakeImg = new BufferedImage[4][3];
		snakeImg = load.getImage(getClass().getResource("/res/Snake.png"), snakeImg, 4, 3, SIZE);
		restart();

	}

	public void restart() {
		body.removeAll(body);
		orient.removeAll(orient);
		dir = 0;
		currGrow = 0;
		growLimit = 0;
		for (int i = 0; i < SIZE * INITSIZE; i += SIZE) {
			body.add(new Point((mainGame.WIDTH + SIZE) / 2 - (SIZE * INITSIZE) + i, (mainGame.HEIGHT + SIZE) / 2));
			orient.add(new Integer(dir));
		}
		left = false;
		right = true;
		up = false;
		down = false;
		dead = false;
	}

	public void update(MKListen mkListen, Food f) {

		if (mkListen.isUp()) {
			if (!down) {
				up = true;
				down = false;
				left = false;
				right = false;
				dir = 2;
			}
		}
		if (mkListen.isDown()) {
			if (!up) {
				up = false;
				down = true;
				left = false;
				right = false;
				dir = 3;
			}
		}
		if (mkListen.isRight()) {
			if (!left) {
				up = false;
				down = false;
				left = false;
				right = true;
				dir = 0;
			}
		}
		if (mkListen.isLeft()) {
			if (!right) {
				up = false;
				down = false;
				left = true;
				right = false;
				dir = 1;
			}
		}
		Point next = new Point();
		next.x = body.get(body.size() - 1).x;
		next.y = body.get(body.size() - 1).y;

		calcNext(next);

		// *** COLLISION DETECTION ***//

		if (isBound(next)) {
			checkDead(next);
			if (!dead) {
				body.add(next);
				orient.add(dir);
				if (growLimit == 0) {
					orient.remove(0);
					body.remove(0);
				}
			}
		}

		Point food = new Point();
		Point head = new Point();
		food = f.getP();
		head = body.get(body.size() - 1);

		if (Math.pow(head.x - food.x, 2) + (Math.pow(head.y - food.y, 2)) < Math.pow(SIZE, 2)) {
			f.remove();
			growLimit += 5;
		}
		if (currGrow < growLimit) {
			currGrow++;
		} else {
			growLimit = 0;
			currGrow = 0;
		}

	}

	private boolean isBound(Point p) {

		if ((p.x - SIZE / 2) < mainGame.WIDTH - 10 && (p.x + SIZE / 2) > 10 && (p.y - SIZE / 2) < mainGame.HEIGHT - 10
				&& (p.y + SIZE / 2) > 20)
			return true;
		else {
			dead = true;
			return false;
		}

	}

	private void calcNext(Point next) {
			if (up) {
				next.y -= SIZE;
			}
			if (down) {
				next.y += SIZE;
			}
			if (left) {
				next.x -= SIZE;
			}
			if (right) {
				next.x += SIZE;
			}
	}

	private void checkDead(Point p) {
		for (Point self : body) {
			if (p.x == self.x && p.y == self.y)
				dead = true;
		}
	}

	public boolean checkFoodOL(Point p) {
		for (Point self : body) {
			if (p.x == self.x && p.y == self.y)
				return true;
		}
		return false;
	}

	public boolean isDead() {
		return dead;
	}

	public void render(Graphics2D g) {
		g.setColor(Color.BLACK);
		g.setFont(new Font("Century Gothic", Font.PLAIN, 8));
		g.drawString("Score: " + (body.size() - INITSIZE), 15, 30);

		g.fillRect(0, 0, (mainGame.WIDTH), 20);
		g.fillRect(0, 0, 10, (mainGame.HEIGHT));
		g.fillRect((mainGame.WIDTH) - 10, 0, 10, (mainGame.HEIGHT));
		g.fillRect(0, (mainGame.HEIGHT) - 10, (mainGame.WIDTH), 10);

		for (int i = 0; i < body.size(); i++) {
			Point p = body.get(i);
			if (p == body.get(0))
				g.drawImage(snakeImg[orient.get(1)][1], p.x - SIZE / 2, p.y - SIZE / 2, null);
			else if (p == body.get(body.size() - 1))
				g.drawImage(snakeImg[orient.get(i)][0], p.x - SIZE / 2, p.y - SIZE / 2, null);
			else
				g.drawImage(snakeImg[orient.get(i)][2], p.x - SIZE / 2, p.y - SIZE / 2, null);

		}
	}

}