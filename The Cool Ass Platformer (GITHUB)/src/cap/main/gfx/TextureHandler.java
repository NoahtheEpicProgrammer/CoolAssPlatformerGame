package cap.main.gfx;

import java.awt.image.*;
import java.io.*;

import javax.imageio.*;

public class TextureHandler
{
	
	private static final BufferedImage error_texture;
	
	static
	{
		error_texture = new BufferedImage(64, 64, BufferedImage.TYPE_INT_RGB);
		
		for (int x = 0; x < error_texture.getWidth(); x++)
			for (int y = 0; y < error_texture.getHeight(); y++)
				error_texture.setRGB(x, y, ((x + y) % 2 == 1) ? 0xFFFF00FF : 0xFF000000);
		
	}
	
	public static final BufferedImage LoadTexture(final String texture_name)
	{
		File f = new File("resources/texture/" + texture_name.replaceAll("\\.", "/") + ".png");
		System.out.println(f.getPath());
		if (!f.exists()) return error_texture;
		
		try
		{
			BufferedImage image = ImageIO.read(f);
			return image;
		} catch (IOException e)
		{
			e.printStackTrace();
			return error_texture;
		}
		
	}
	
}
