package cap.main.game.object.character;

import cap.main.game.*;
import cap.main.game.object.*;

// TODO name conflicts with boxed primitive type. I'm not to hardpressed about it, but might change it later.
public abstract class Character extends GameObject
{
	
	private boolean grounded;
	
	public Character(GameWorld parent, String name)
	{
		super(parent, name);
		
	}
	
	@Override protected final void obj_update()
	{
		// Update the character's state. Are we on the ground?
		
		char_update();
	}
	protected abstract void char_update();
	
	/**
	 * Instructs the character to move towards the point (x, y).
	 * @param x
	 * @param y
	 */
	public abstract void Move(float x, float y);
	/**
	 * Instructs the character to aim at the point (x, y).
	 * @param x
	 * @param y
	 */
	public abstract void Aim(float x, float y);
	/**
	 * Instructs the character to either start or stop firing their weapon
	 * @param index
	 * @param state
	 */
	public abstract void FireWeapon(int index, boolean state);
	
	/**
	 * 
	 * @param damage
	 */
	public abstract void TakeDamage(float damage);
	public abstract void RecvImpulse(float x, float y);
	public abstract void RecvImpulse_Lazy(float x, float y, float magn);
	
}
