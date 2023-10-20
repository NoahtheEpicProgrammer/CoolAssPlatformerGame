package cap.main.game.object.box;

import java.awt.*;

import cap.main.game.*;
import cap.main.game.object.*;
import cap.main.gfx.*;

public class BasicBox extends GameObject
{
	private Color color;
	
	
	public BasicBox(GameWorld world, String name, float x, float y, float w, float h)
	{
		super(world, name);
		SetCollisionSize(w, h);
		
		color = Color.gray;
	}

	@Override public void draw(Graphics2D g, GameRenderer renderer)
	{
		// TODO Auto-generated method stub
		
	}

	@Override protected void obj_update()
	{
		// TODO Auto-generated method stub
		
	}
	
}
