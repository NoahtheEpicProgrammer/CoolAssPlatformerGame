package cap.main.game.object.character;

import java.awt.*;

import cap.main.game.*;
import cap.main.gfx.*;

public class PlayerTest extends Character
{

	public PlayerTest(GameWorld parent, String name)
	{
		super(parent, name);
		// TODO Auto-generated constructor stub
	}

	@Override public void Move(float x, float y)
	{
		AccelerateToVelocityX(5f * x, 0.5f);
		AccelerateToVelocityY(5f * y, 0.5f);
	}

	@Override public void Aim(float x, float y)
	{
		
	}

	@Override public void FireWeapon(int index, boolean state)
	{
		
	}

	@Override public void TakeDamage(float damage)
	{
		
	}

	@Override public void RecvImpulse(float x, float y)
	{
		// TODO Auto-generated method stub
		
	}

	@Override public void RecvImpulse_Lazy(float x, float y, float magn)
	{
		// TODO Auto-generated method stub
		
	}

	@Override protected void char_update()
	{
		// TODO Auto-generated method stub
		
	}

	@Override public void draw(Graphics2D g, GameRenderer renderer)
	{
		g.setColor(Color.white);
		g.fillRect(-32, -48, 64, 96);
	}
	
}
