package snake;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

public class LoadImage {

	public BufferedImage[][] getImage(URL path, BufferedImage[][] img, int row, int col, int size) {
		BufferedImage image = null;

		try {
			image = ImageIO.read(path);
			for (int i = 0; i < row; i++)
				for (int j = 0; j < col; j++) {
					img[i][j] = image.getSubimage(j * size, i * size, size, size);
				}
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}

		return img;

	}

}
