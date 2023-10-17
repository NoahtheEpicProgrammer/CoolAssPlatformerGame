package cap.main.input;

import java.awt.*;
import java.awt.event.*;

public class InputProcessor
{
	private Canvas			current_canvas;
	
	private KeyInput		keyboard_input;
	private MouseInput		mouse_input;
	
	private ActionStateMap	current_map;
	
	public InputProcessor()
	{
		keyboard_input = new KeyInput();
		mouse_input = new MouseInput();
	}
	
	public final void Update()
	{
		keyboard_input.Snapshot();
		mouse_input.Snapshot();
		
		keyboard_input.Update();
		mouse_input.Update();
		
		
	}
	
	public final KeyInput GetKeyInput() { return keyboard_input; }
	public final MouseInput GetMouseInput() { return mouse_input; }
	
	
	/**
	 * Attaches the necessary key and mouse listeners to the canvas.
	 * 
	 * @param canvas Canvas to attach to.
	 */
	public final void Attach(Canvas canvas)
	{
		if (current_canvas != null) Detach(); // TODO throw error instead?
		this.current_canvas = canvas;
		
		canvas.addKeyListener(keyboard_input);
		
		canvas.addMouseListener(mouse_input);
		canvas.addMouseMotionListener(mouse_input);
		canvas.addMouseWheelListener(mouse_input);
	}
	
	public final void Detach()
	{
		if (current_canvas == null) throw new IllegalStateException("Tried to detach a canvas before one was attached.");
		current_canvas.removeKeyListener(keyboard_input);
		
		current_canvas.removeMouseListener(mouse_input);
		current_canvas.removeMouseMotionListener(mouse_input);
		current_canvas.removeMouseWheelListener(mouse_input);
		
		current_canvas = null;
	}
	
	/**
	 * Full-mode creates an invisible window above all other applications that will
	 * redirect all events back to the game, should focus be lost for whatever
	 * reason. It also applies in fullscreen (maybe?), so that multi-monitor setups
	 * don't cause issues when trying to flick shot an enemy on the other side of
	 * the screen.
	 * 
	 * This invisible window can also be bypassed to interact with the desktop and
	 * other windows by holding down a special key, which is configurable.
	 */
	public final void SetFullMode(boolean state)
	{
		
	}
	
}
