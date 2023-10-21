package cap.main.game.object;

public class CollisionInfo
{
	float p_up, p_down, p_left, p_right;
	boolean up, down, left, right;
	
	public CollisionInfo() {}
	
	public final float GetPenetrationUp() { return p_up; }
	public final float GetPenetrationDown() { return p_down; }
	public final float GetPenetrationLeft() { return p_left; }
	public final float GetPenetrationRight() { return p_right; }
	
	public final float GetPenetrationUpWS() { return p_up; }
	public final float GetPenetrationDownWS() { return -p_down; }
	public final float GetPenetrationLeftWS() { return p_left; }
	public final float GetPenetrationRightWS() { return -p_right; }
	
	public final boolean IsCollidingUp() { return up; }
	public final boolean IsCollidingDown() { return down; }
	public final boolean IsCollidingLeft() { return left; }
	public final boolean IsCollidingRight() { return right; }
	
	final void Reset()
	{
		p_up = 0.0f;
		p_down = 0.0f;
		p_left = 0.0f;
		p_right = 0.0f;
		
		up = false;
		down = false;
		left = false;
		right = false;
	}
	
	final void pup(float value) { if (value > p_up) { p_up = value; up = true; } }
	final void pdown(float value) { if (value > p_down) { p_down = value; down = true; } }
	final void pleft(float value) { if (value > p_left) { p_left = value; left = true; } }
	final void pright(float value) { if (value > p_right) { p_right = value; right = true; } }
}
