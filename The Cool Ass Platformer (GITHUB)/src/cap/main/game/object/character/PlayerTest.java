package cap.main.game.object.character;

import java.awt.*;

import cap.main.game.*;
import cap.main.gfx.*;
import cap.util.*;

public class PlayerTest extends Character
{
	
	private float base_walkspeed = 3f;
	private float base_movespeed = 5f;
	private float base_runspeed = 10f;
	private float current_speed = 5f;
	private float old_vx;
	
	public PlayerTest(GameWorld parent, String name)
	{
		super(parent, name);
		SetCollisionSize(64, 96);
	}

	@Override public void Move(float x, float y, float movemodifier)
	{
//		AccelerateToVelocityY(5f * y, 0.5f);
		float target_speed = base_movespeed;
		if (movemodifier >= 1.5f)
			target_speed = base_runspeed;
		else if (movemodifier <= 0.5f)
			target_speed = base_walkspeed;
		
		current_speed = MathUtil.LinearIncrease(current_speed, target_speed, 0.2f);
		
		if (char_grounded())
		{
			AccelerateToVelocityX(current_speed * x, 0.5f);
			
			if (y < -0.5f)
			{
				ApplyVelocity(0, -(7.5f + Math.abs(GetVX()) * 0.25f));
				
			}
			
			old_vx = GetVX();
		} else {
			AccelerateToVelocityX(current_speed * x, 0.08f);
			
			if (get_cinfo().IsCollidingRight())
			{
				SetVY(GetVY() * 0.8f);
				if (y < -0.5f)
				{
					SetVelocity(-old_vx * 0.85f, -(Math.abs(old_vx) * 0.7f + 7.f) );
					old_vx *= 0.707f;
				} else old_vx *= 0.98f;
			} else {
				old_vx = GetVX();
			}
		}
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
		g.drawString("OVX: " + old_vx, -32, -48);
	}
	
}
