package cap.main.gfx;

import java.awt.*;
import java.awt.geom.*;
import java.util.*;
import java.util.Map.*;

/**
 * Specializes in rendering the game with customizing the rendering bounds.
 */
public class GameRenderer
{
	private Canvas	game_canvas;
	
	private HashMap<String, RenderHook>	hook_lookup;
	private ArrayList<RenderHook>		render_hooks;
	
	private int		irect_x1, irect_y1, irect_x2, irect_y2;
	private int		rect_x1, rect_y1, rect_x2, rect_y2;
	
	private	double	current_aspect;
	
	public GameRenderer(Canvas game_canvas)
	{
		this.game_canvas = game_canvas;
		
		hook_lookup = new HashMap<String, RenderHook>();
		render_hooks = new ArrayList<RenderHook>();
	}
	
	// -- Hooks and (un)hooking operations -- //
	
	public final void Hook(String id, RenderHook hook)
	{
		// if we already have a hook with that id, remove it first.
		if (hook_lookup.containsKey(id))
			Unhook(id);
		
		hook_lookup.put(id, hook);
		render_hooks.add(hook);
	}
	
	public final void Unhook(String id)
	{
		RenderHook hook = hook_lookup.get(id);
		if (hook == null)
			throw new NoSuchElementException("No hook named " + id);
		
		render_hooks.remove(hook);
	}
	
	// -- Render Operations -- //
	
	/**
	 * Should be called at the beginning of every frame.
	 */
	public final void InitializeRender()
	{
		irect_x1 = 0;
		irect_y1 = 0;
		irect_x2 = game_canvas.getWidth();
		irect_y2 = game_canvas.getHeight();
		
		rect_x1 = irect_x1;
		rect_y1 = irect_y1;
		rect_x2 = irect_x2;
		rect_y2 = irect_y2;
		
		current_aspect = (double) (irect_x2 - irect_x1) / (double) (irect_y2 - irect_y1);
	}
	
	public final void InitializeRender(int rwidth, int rheight)
	{
		irect_x1 = 0;
		irect_y1 = 0;
		irect_x2 = rwidth;
		irect_y2 = rheight;
		
		rect_x1 = irect_x1;
		rect_y1 = irect_y1;
		rect_x2 = irect_x2;
		rect_y2 = irect_y2;
		
		current_aspect = (double) rwidth / (double) rheight;
	}
	
	public final void RenderAll(Graphics2D g)
	{
		for (RenderHook hook : render_hooks)
			RenderHook(g, hook);
	}
	
	public final void Render(Graphics2D g)
	{
		RenderHook hook = hook_lookup.get("main");
		if (hook == null)
			throw new NoSuchElementException("No main hook");
		
		// Main does not get any transform applied to it.
		// Only the next hook draw calls are.
		hook.draw(g);
	}
	
	public final void RenderHook(Graphics2D g, String id)
	{
		RenderHook hook = hook_lookup.get(id);
		if (hook == null)
			throw new NoSuchElementException("No hook named " + id);
		
		RenderHook(g, hook);
	}
	
	public final void RenderHooks_StartsWith(Graphics2D g, String id_prefix)
	{
		for (Entry<String, RenderHook> entry : hook_lookup.entrySet())
			if (entry.getKey().startsWith(id_prefix))
				RenderHook(g, entry.getValue());
	}
	
	public final void RenderHooks_Regex(Graphics2D g, String id_regex)
	{
		for (Entry<String, RenderHook> entry : hook_lookup.entrySet())
			if (entry.getKey().matches(id_regex))
				RenderHook(g, entry.getValue());
	}
	
	public final void RenderHook(Graphics2D g, RenderHook hook)
	{
		AffineTransform old_tx = g.getTransform();
		int old_irectx1 = irect_x1;
		int old_irecty1 = irect_y1;
		int old_irectx2 = irect_x2;
		int old_irecty2 = irect_y2;
		
		transform(g);
		hook.draw(g);
		
		// restore previous transform and state
		g.setTransform(old_tx);
		irect_x1 = old_irectx1;
		irect_y1 = old_irecty1;
		irect_x2 = old_irectx2;
		irect_y2 = old_irecty2;
	}
	
	//
	
	private final void transform(Graphics2D g)
	{
		//System.out.printf("(%d, %d) => (%d, %d)\n", irect_x1, irect_y1, irect_x2, irect_y2);
		// Note that all the transforms are inverse.
		double scale_x = (double) (irect_x2 - irect_x1) / (double) (rect_x2 - rect_x1);
		double scale_y = (double) (irect_y2 - irect_y1) / (double) (rect_y2 - rect_y1);
		
		g.scale(scale_x, scale_y);
		g.translate(irect_x1 - rect_x1, irect_y1 - rect_y1);
		
		// Update irect variables.
		irect_x1 = rect_x1;
		irect_y1 = rect_y1;
		irect_x2 = rect_x2;
		irect_y2 = rect_y2;
	}
	
	// -- Render Controls -- //
	
	/**
	 * Sets the rendering bounds for the next render hook.
	 * 
	 * @param x1 Top left corner X-component.
	 * @param y1 Top left corner Y-component.
	 * @param x2 Bottom right corner X-component.
	 * @param y2 Bottom right corner Y-component.
	 */
	public final void SetRenderBounds(int x1, int y1, int x2, int y2)
	{
		rect_x1 = x1;
		rect_y1 = y1;
		rect_x2 = x2;
		rect_y2 = y2;
	}
	
	/**
	 * Sets the rendering bounds for the next render hook.
	 * 
	 * @param cx     The X-component of the center point.
	 * @param cy     The Y-component of the center point.
	 * @param width  The width of the view.
	 * @param height The height of the view.
	 */
	public final void SetRenderBounds_Rect(int cx, int cy, int width, int height)
	{
		int hw = width / 2;
		int hh = height / 2;
		
		SetRenderBounds(cx - hw, cy - hh, cx + hw, cy + hh);
	}
	
	/**
	 * Sets the rendering bounds for the next render hook, making sure the bounds respect the 
	 * 
	 * 
	 * @param cx                The X-component of the center point.
	 * @param cy                The Y-component of the center point.
	 * 
	 * @param horizontal_length The width of the rendering bounds.
	 */
	public final void SetRenderBounds_HAspect(int cx, int cy, int horizontal_length)
	{
		int hw = horizontal_length;
		int hh = (int) (horizontal_length / current_aspect);
		
		SetRenderBounds(cx - hw, cy - hh, cx + hw, cy + hh);
	}
	
	public final int GetMinX() { return irect_x1; }
	public final int GetMinY() { return irect_y1; }
	public final int GetMaxX() { return irect_x2; }
	public final int GetMaxY() { return irect_y2; }
	
	public final int GetBoundsWidth() { return irect_x2 - irect_x1; }
	public final int GetBoundsHeight() { return irect_y2 - irect_y1; }
	
}
