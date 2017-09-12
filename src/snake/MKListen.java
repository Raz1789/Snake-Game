package snake;

import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;

public class MKListen implements KeyListener {

	private boolean left, right, up, down, quit, proceed;

	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_UP)
			up = true;
		if (e.getKeyCode() == KeyEvent.VK_DOWN)
			down = true;
		if (e.getKeyCode() == KeyEvent.VK_LEFT)
			left = true;
		if (e.getKeyCode() == KeyEvent.VK_RIGHT)
			right = true;
		if (e.getKeyCode() == KeyEvent.VK_Q || e.getKeyCode() == KeyEvent.VK_ESCAPE)
			quit = true;
		if (e.getKeyCode() == KeyEvent.VK_SPACE)
			proceed = true;
	}

	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_UP)
			up = false;
		if (e.getKeyCode() == KeyEvent.VK_DOWN)
			down = false;
		if (e.getKeyCode() == KeyEvent.VK_LEFT)
			left = false;
		if (e.getKeyCode() == KeyEvent.VK_RIGHT)
			right = false;
		if (e.getKeyCode() == KeyEvent.VK_Q || e.getKeyCode() == KeyEvent.VK_ESCAPE)
			quit = false;
		if (e.getKeyCode() == KeyEvent.VK_SPACE)
			proceed = false;
	}

	public void keyTyped(KeyEvent e) {
	}

	public boolean isLeft() {
		return left;
	}

	public boolean isRight() {
		return right;
	}

	public boolean isUp() {
		return up;
	}

	public boolean isDown() {
		return down;
	}

	public boolean isQuit() {
		return quit;
	}
	
	public boolean isProceed() {
		return proceed;
	}
}
