package cap.main.game.object;

import java.awt.*;
import java.awt.geom.*;
import java.util.*;

import cap.main.game.*;
import cap.main.gfx.*;

public abstract class GameObject implements RenderHook
{
	private static boolean	debug_render = true;
	private static int		obj_index_ctr = 0;
	
	private GameWorld		parent;				// TODO maybe make static? would reduce a lot of unnecessary code.
	private String			name;
	
	private float			x, y;
	private float			vx, vy;
	private float			w, h, hw, hh;		// also store half sizes.
	
	private boolean			circle;				// Object is a sphere.
	private boolean			phys_static;		// Object does not move.
	
	private int				obj_index;
	
	private CollisionInfo	cinfo;
	
	public GameObject(GameWorld parent, String name)
	{
		this.parent = parent;
		this.name = name;
		
		this.obj_index = obj_index_ctr++;
		
		cinfo = new CollisionInfo();
	}
	
	public GameObject(GameWorld parent, String name, float x, float y)
	{
		this(parent, name);
		
		this.x = x;
		this.y = y;
	}
	
	public GameObject(GameWorld parent, String name, float x, float y, float w, float h)
	{
		this(parent, name, x, y);
		SetCollisionSize(w, h);
	}
	
	public final void Update()
	{
		// blah blah blah
		obj_update();
	}
	
	private final void phys_update()
	{
		ArrayList<GameObject> objects = new ArrayList<>();
		
		parent.GetGameObjectsInSector(this, objects);
		
		cinfo.Reset();
		for (GameObject obj : objects)
		{
			if (this.obj_index < obj.obj_index || obj.phys_static)	// Static objects will not looped through yet.
				if (obj.Intersecting(this))
				{
					// find which axis hits first (these are boxes after all)
					// this is done by calculating the relative velocity between the two
					// objects, and then choosing the shorter intercept.
					
					float edgex = (this.vx < obj.vx) ? -1 : 1;
					float rx_m = (this.vx - obj.vx);
					float rx_b = (this.x + edgex * this.hw) - (obj.x - edgex * obj.hw) - rx_m;
					
					if (rx_m == 0) edgex = 0;
					
					float edgey = (this.vy < obj.vy) ? -1 : 1;
					float ry_m = this.vy - obj.vy;
					float ry_b = (this.y + edgey * this.hh) - (obj.y - edgey * obj.hh) - ry_m;
					
					if (ry_m == 0) edgey = 0;
					
					float rx_intercept = Math.abs(-rx_b / rx_m);
					float ry_intercept = Math.abs(-ry_b / ry_m);
					
					if (Math.signum(rx_m) == Math.signum(rx_b)) rx_intercept = Float.POSITIVE_INFINITY;// else if (Float.isInfinite(rx_m)) rx_m = 0;
					if (Math.signum(ry_m) == Math.signum(ry_b)) ry_intercept = Float.POSITIVE_INFINITY;// else if (Float.isInfinite(ry_m)) ry_m = 0;
					
//					System.out.println("E: " + edgex + " " + edgey);
//					System.out.println("M: " + rx_m + " " + ry_m);
//					System.out.println("B: " + rx_b + " " + ry_b);
//					System.out.println("I: " + rx_intercept + " " + ry_intercept);
					
					if (rx_m == ry_m && rx_m == Float.POSITIVE_INFINITY) continue; // no correlation on velocity.
					
					// prefer X axis for no reason.
					if (rx_intercept <= ry_intercept && rx_intercept != Float.POSITIVE_INFINITY)
					{
						float pen = Math.abs((this.x + edgex * this.hw) - (obj.x - edgex * obj.hw));
						if (obj.phys_static)
						{
							this.vx = 0;
						} else {
							float new_vx = this.vx + obj.vx;
							
							this.vx = new_vx;
							obj.vx = new_vx;
						}
						
						if (edgex == 1)
						{
							this.cinfo.pright(pen);
							this.cinfo.right = true;
							
							obj.cinfo.pleft(pen);
							obj.cinfo.left = true;
						} else if (edgex == -1) {
							this.cinfo.pleft(pen);
							this.cinfo.left = true;
							
							obj.cinfo.pright(pen);
							obj.cinfo.right = true;
						}
						
						
					} else if (ry_intercept <  rx_intercept && ry_intercept != Float.POSITIVE_INFINITY) {
						
						float pen = Math.abs((this.y + edgey * this.hh) - (obj.y - edgey * obj.hh));
						if (obj.phys_static)
						{
							this.vy = 0;
						} else {
							float new_vy = this.vy + obj.vy;
							
							this.vy = new_vy;
							obj.vy = new_vy;
						}
						
						if (edgey == 1)
						{
							this.cinfo.pdown(pen);
							this.cinfo.down = true;
							
							obj.cinfo.pup(pen);
							obj.cinfo.up = true;
						} else if (edgey == -1) {
							this.cinfo.pup(pen);
							this.cinfo.up = true;
							
							obj.cinfo.pdown(pen);
							obj.cinfo.down = true;
						}
						
						
					}
					
				}
		}
	}
	
	public final void PostUpdate()
	{
		if (!phys_static)
		{
			// Physics update!
			phys_update();
			
			x += cinfo.GetPenetrationLeftWS() + cinfo.GetPenetrationRightWS();
			y += cinfo.GetPenetrationUpWS() + cinfo.GetPenetrationDownWS();
			
			x += vx;
			y += vy;
			
			vx += parent.GetGravityX();
			vy += parent.GetGravityY();
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
		
		this.hw = w / 2;
		this.hh = h / 2;
	}
	
	protected final void SetCollisionWidth(float w) { this.w = w; this.hw = w / 2; }
	protected final void SetCollisionHeight(float h) { this.h = h; this.hh = h / 2; }
	
	protected final void SetStatic(boolean state) { this.phys_static = state; }
	
	public final boolean Intersecting(GameObject other_object)
	{
		// simple box check.
		if (Math.abs(this.x - other_object.x) > this.hw + other_object.hw) return false;
		if (Math.abs(this.y - other_object.y) > this.hh + other_object.hh) return false;
		
		return true;
	}
	
	protected final CollisionInfo get_cinfo() { return cinfo; }
	
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
	
	public final void SetVX(float vx) { this.vx = vx; }
	public final void SetVY(float vy) { this.vy = vy; }
	
	public final float GetVX() { return vx; }
	public final float GetVY() { return vy; }
	
	// Rendering helper functions

	protected final void DrawRect(Graphics2D g) { g.drawRect((int) (-w / 2), (int) (-h / 2), (int) w, (int) h); }
	protected final void FillRect(Graphics2D g) { g.fillRect((int) (-w / 2), (int) (-h / 2), (int) w, (int) h); }
	
	protected final void DrawCenteredRect(Graphics2D g, float x, float y, float w, float h) { g.drawRect((int) (x - w / 2), (int) (y - h / 2), (int) w, (int) h); }
	protected final void FillCenteredRect(Graphics2D g, float x, float y, float w, float h) { g.fillRect((int) (x - w / 2), (int) (y - h / 2), (int) w, (int) h); }
	
}
