package cap.main.input;

import java.awt.*;
import java.awt.event.*;

public final class MouseInput extends MouseAdapter implements InputDevice
{
	// State values < 0 are released (in double click time window).
	// State values > 1 are held (in double click time window).
	public static final int	MOUSE_IDLE		= 0x00;
	public static final int	MOUSE_PRESSED	= 0x01;
	public static final int	MOUSE_RELEASED	= -0x01;
	
	// Double-click configuration, provided to keep UI programming consistent.
	private int				dc_timeout		= 15;	// Number of frames after a press that any new presses are
													// considered a "double-click".
	private int				dc_release		= 17;	// Number of frames after a release that any new presses are
													// considered a "double-click".
	
	private int				mx, my, awt_mx, awt_my;
	private int				cx, cy, awt_cx, awt_cy;
	
	private int[]			buttons;
	private boolean[]		awt_buttons;
	private boolean[]		buttonsnap;
	
	public MouseInput()
	{
		int num_buttons = MouseInfo.getNumberOfButtons();
		
		buttons = new int[num_buttons];
		awt_buttons = new boolean[num_buttons];
		buttonsnap = new boolean[num_buttons];
	}
	
	@Override public void Snapshot()
	{
		mx = awt_mx;
		my = awt_my;
		
		cx = awt_cx;
		cy = awt_cy;
		
		System.arraycopy(awt_buttons, 0, buttonsnap, 0, awt_buttons.length);
	}
	
	@Override public void Update()
	{
		for (int i = 0; i < buttons.length; i++)
		{
			if (buttonsnap[i])
				switch (buttons[i])
				{
					case MOUSE_IDLE:	buttons[i] = MOUSE_PRESSED; break;
					
					default:
						if (buttons[i] < 0)
							buttons[i] = MOUSE_PRESSED; // released
						else
							buttons[i]++; // held/pressed
					
				}
			else
				switch (buttons[i])
				{
					case MOUSE_IDLE:	buttons[i] = -dc_timeout; break;
					
					default:
						if (buttons[i] < 0)
							buttons[i]++;
				}
			
		}
	}
	
	/** @return The current X position of the mouse.*/
	public final int GetX() { return mx; }
	/** @return The current Y position of the mouse.*/
	public final int GetY() { return my; }
	
	/** @return The last place the mouse was clicked (on the X axis). */
	public final int GetClickX() { return cx; }
	/** @return The last place the mouse was clicked (on the Y axis). */
	public final int GetClickY() { return cy; }
	
	/** @return Is the mouse button pressed? */
	public final boolean IsButtonDown(int button)		{ return buttons[button] >  0; }
	/** @return Is the mouse button unpressed?*/
	public final boolean IsButtonUp(int button)			{ return buttons[button] <= 0; }
	
	/** @return Was the mouse button clicked on this frame? */
	public final boolean IsButtonPressed (int button)	{ return buttons[button] == MOUSE_PRESSED; }
	/** @return Was the mouse button released on this frame? */
	public final boolean IsButtonReleased(int button)	{ return buttons[button] == -dc_release; }
	
	
	// -- Mouse Adapter -- //
	
	@Override public void mousePressed(MouseEvent e)
	{
		awt_cx = e.getX();
		awt_cy = e.getY();
		
		awt_buttons[e.getButton()] = true;
	}
	
	@Override public void mouseReleased(MouseEvent e)
	{
		awt_buttons[e.getButton()] = false;
	}
	
	
	@Override public void mouseMoved(MouseEvent e)
	{
		awt_mx = e.getX();
		awt_my = e.getY();
	}
	
	@Override public void mouseDragged(MouseEvent e)
	{
		awt_mx = e.getX();
		awt_my = e.getY();
	}
	
	
	@Override public void mouseExited(MouseEvent e)
	{
		// wrap the mouse around?
	}
	
	
	@Override public void mouseWheelMoved(MouseWheelEvent e)
	{
		
	}
}
