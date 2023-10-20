package cap.main.game.object;

import java.awt.*;
import java.awt.geom.*;

import cap.main.game.*;
import cap.main.gfx.*;

public abstract class GameObject implements RenderHook
{
	private static boolean	debug_render;
	
	private GameWorld		parent;			// TODO maybe make static? would reduce a lot of unnecessary code.
	private String			name;
	
	private float			x, y;
	private float			vx, vy;
	private float			w, h;
	
	private boolean			phys_static;	// Object does not move.
	
	public GameObject(GameWorld parent, String name)
	{
		this.parent = parent;
		this.name = name;
	}
	
	public GameObject(GameWorld parent, String name, float x, float y)
	{
		this(parent, name);
		
		this.x = x;
		this.y = y;
	}
	
	public final void Update()
	{
		// blah blah blah
		obj_update();
	}
	
	public final void PostUpdate()
	{
		if (!phys_static)
		{
			// Physics update!
			x += vx;
			y += vy;
		}
		
		// TODO update sector.
	}
	
	protected abstract void obj_update();
	
	public final void Render(Graphics2D g, GameRenderer renderer)
	{
		AffineTransform old_tx = g.getTransform();
		
		g.translate(x, y);
		draw(g, renderer);
		
		if (debug_render)
		{
			g.setColor(Color.red);
			g.drawRect((int) (-w / 2), (int) (-h / 2), (int) w, (int) h);
		}
		
		g.setTransform(old_tx);
	}
	
	
	protected final void SetCollisionSize(float w, float h)
	{
		this.w = w;
		this.h = h;
	}
	
	public final boolean IsColliding(GameObject other_object)
	{
		// simple box check.
		if (Math.abs(this.x - other_object.x) > this.w + other_object.w) return false;
		if (Math.abs(this.y - other_object.y) > this.h + other_object.h) return false;
		
		return true;
	}
	
	// -- Position Functions -- //
	public final void Teleport(float x, float y)
	{
		// see what sectors we'll stay in and which ones we will not.
		
		// update position.
		this.x = x;
		this.y = y;
	}
	public final void Displace(float dx, float dy) { this.x += dx; this.y += dy; }
	
	public final float GetX() { return x; }
	public final float GetY() { return y; }
	
	// -- Velocity Functions -- //
	public final void SetVelocity(float vx, float vy) { this.vx = vx; this.vy = vy; }
	public final void ApplyVelocity(float dvx, float dvy) { this.vx += dvx; this.vy += dvy; }
	
	public final void AccelerateToVelocityX(float vx, float ax)
	{
		float diff = vx - this.vx;
		// Instead of having to do an ABS function, we could instead do
		// a bitwise AND on the float to get the absolute value.
		// That's one of my biggest gripes with Java: the security.
		
		if (Math.abs(diff) < ax)	this.vx = vx;
		else						this.vx += Math.copySign(ax, diff);
	}
	
	public final void AccelerateToVelocityY(float vy, float ay)
	{
		float diff = vy - this.vy;
		
		if (Math.abs(diff) < ay)	this.vy = vy;
		else						this.vy += Math.copySign(ay, diff);
	}
	
	public final float GetVX() { return vx; }
	public final float GetVY() { return vy; }
	
}
