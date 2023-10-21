package cap.main.game;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.util.*;
import java.util.List;

import cap.main.game.controller.*;
import cap.main.game.object.*;
import cap.main.game.object.box.*;
import cap.main.game.object.character.*;
import cap.main.game.object.character.Character;
import cap.main.gfx.*;
import cap.main.input.*;

public class GameWorld implements RenderHook
{
	// -- Input -- //
	private InputProcessor											input_processor;
	private Controller												active_controller;
	private Character												current_player;
	
	// -- Objects -- //
	private ArrayList<GameObject>									objects;
	private HashMap<String, List<GameObject>>						objects_by_name;
	private HashMap<Class<? extends GameObject>, List<GameObject>>	objects_by_type;
	
	// -- Physics -- //
	private static final float										pixels_per_meter = 48f;
	private float													gravity_x;
	private float													gravity_y;
	
	// -- Camera -- //
	private int														camera_x, camera_y;
	private boolean													camera_smooth;
	
	// -- Background -- //
	private boolean													bg_enable	= true;
	private int														bg_scale	= 2;
	
	private BufferedImage											bg_cache;
	
	// -- Render Hooks -- //
	
	private void create_platform(int x, int y, int w, int h)
	{
		
		
	}
	
	public GameWorld(InputProcessor input_processor)
	{
		this.input_processor = input_processor;
		
		// Object collections
		objects = new ArrayList<GameObject>();
		objects_by_name = new HashMap<String, List<GameObject>>();
		objects_by_type = new HashMap<Class<? extends GameObject>, List<GameObject>>();
		
		// testing
		
		camera_smooth = true;
		bg_enable = true;
		bg_scale = 2;
		bg_cache = TextureHandler.LoadTexture("bg");
		
		SetGravityYinMpS(1.0f);
		
		current_player = new PlayerTest(this, "Player");
		objects.add(current_player);
		objects.add(new BasicBox(this, "", 100, 0, 50, 500, true));
		objects.add(new BasicBox(this, "", 0, 275, 500, 50, true));
		objects.add(new BasicBox(this, "", -350, 225, 100, 50, true));
		objects.add(new BasicBox(this, "", 0, 275, 500, 50, true));
		objects.add(new BasicBox(this, "", -96, -96, 32, 32, false));
	}
	
	
	/**
	 * Fills a list full of objects that occupy the sectors the object is in.
	 * @param object
	 * @param out
	 */
	public final void GetGameObjectsInSector(GameObject object, List<GameObject> out)
	{
		for (GameObject obj : objects)
			if (obj.equals(object))	continue;
			else					out.add(obj);
		
	}
	
	
	private final void update_camera()
	{
		if (current_player != null)
		{
			if (camera_smooth)
			{
				int cdx = (int) current_player.GetX() - camera_x;
				int cdy = (int) current_player.GetY() - camera_y;
				camera_x += cdx / 10;
				camera_y += cdy / 10;
			} else {
				camera_x = (int) current_player.GetX();
				camera_y = (int) current_player.GetY();
			}
		}
	}
	
	public void Update()
	{
		int dx = 0;
		int dy = 0;
		
		int movemod = 1;
		
		if (input_processor.GetKeyInput().IsKeyDown(KeyEvent.VK_W)) dy--;
		if (input_processor.GetKeyInput().IsKeyDown(KeyEvent.VK_S)) dy++;
		if (input_processor.GetKeyInput().IsKeyDown(KeyEvent.VK_A)) dx--;
		if (input_processor.GetKeyInput().IsKeyDown(KeyEvent.VK_D)) dx++;
		
		if (input_processor.GetKeyInput().IsKeyDown(KeyEvent.VK_SHIFT)) movemod++;
		if (input_processor.GetKeyInput().IsKeyDown(KeyEvent.VK_ALT)) movemod--;
		
		// Control the player
		current_player.Move(dx, dy, movemod);
		
		for (GameObject obj : objects) obj.Update();
		for (GameObject obj : objects) obj.PostUpdate();
		
		update_camera();
	}
	
	public void Render(Graphics2D g, GameRenderer renderer)
	{
		renderer.SetRenderBounds_HAspect(camera_x, camera_y, 800);
		renderer.RenderHook(g, this);
	}
	
	@Override public void draw(Graphics2D g, GameRenderer renderer)
	{
		if (bg_enable)
		{
			int bg_x = camera_x / bg_scale;
			int bg_y = camera_y / bg_scale;
			
			for (int x = renderer.GetMinX() - Math.floorMod(bg_x, bg_cache.getWidth()); x < renderer.GetMaxX(); x += bg_cache.getWidth())
			{
				g.drawImage(bg_cache, x, bg_y, null);
			}
		}
		
		for (GameObject object : objects) object.Render(g, renderer);
		
	}
	
	public final void SetGravityX(float ax) { this.gravity_x = ax; }
	public final void SetGravityY(float ay) { this.gravity_y = ay; }
	
	public final void SetGravityXinMpS(float ax_mps) { this.gravity_x = (ax_mps * pixels_per_meter) / 60f; }
	public final void SetGravityYinMpS(float ay_mps) { this.gravity_y = (ay_mps * pixels_per_meter) / 60f; }
	
	public final float GetGravityX() { return gravity_x; }
	public final float GetGravityY() { return gravity_y; }
	
	public final float ConvertMetersToPixels(float meters) { return meters * pixels_per_meter; }
	public final float ConvertMpSToPpS(float mps) { return (mps * pixels_per_meter) / 60f; }
}
