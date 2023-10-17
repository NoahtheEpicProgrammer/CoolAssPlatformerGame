package cap.main.game.object;

import cap.main.game.*;

public abstract class GameObject
{
	private GameWorld parent; // TODO maybe make static?
	private String name;
	
	private float x, y;
	private float w, h;
	
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
	
	
	
}
