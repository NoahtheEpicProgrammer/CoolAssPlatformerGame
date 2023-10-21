package cap.main.game.object.box;

import java.awt.*;

import cap.main.game.*;
import cap.main.game.object.*;
import cap.main.gfx.*;

public class BasicBox extends GameObject
{
	private Color color;
	
	
	public BasicBox(GameWorld world, String name, float x, float y, float w, float h, boolean is_static)
	{
		super(world, name, x, y);
		SetCollisionSize(w, h);
		SetStatic(is_static);
		
		color = Color.gray;
	}

	@Override public void draw(Graphics2D g, GameRenderer renderer)
	{
		g.setColor(color);
		FillRect(g);
	}

	@Override protected void obj_update()
	{
		if (get_cinfo().IsCollidingDown()) AccelerateToVelocityX(0, 0.15f);
		
	}
	
}
