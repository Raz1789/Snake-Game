package snake;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;

public class Food {

	Point p;

	public Food() {
		p = new Point();
	}

	public void generateFood(Snake snake) {

		int size = Snake.SIZE;
		do{
			p.x = (int) (Math.random() * (mainGame.WIDTH - 20)) + 10;
			p.x -= p.x % (size);
			p.x += size/2;
			p.y = (int) (Math.random() * (mainGame.HEIGHT - 30)) + 20;
			p.y -= p.y % (size);
			p.y += size/2;
			System.out.println("x: "+ p.x +" | y: "+ p.y);
		}while(snake.checkFoodOL(p));
	}

	public void remove() {
		p.x = 0;
		p.y = 0;
	}

	public void render(Graphics2D g) {
		int size = Snake.SIZE;
		if (p != null && (p.x + p.y) != 0) {
			g.setColor(Color.RED);
			g.fillOval(p.x - (size / 2), p.y - (size / 2), size, size);
			g.setColor(Color.RED.darker());
			g.setStroke(new BasicStroke(2));
			g.drawOval(p.x - (size / 2), p.y - (size / 2), size, size);
			g.setStroke(new BasicStroke(1));
		}
	}

	public Point getP() {
		return p;
	}

}
