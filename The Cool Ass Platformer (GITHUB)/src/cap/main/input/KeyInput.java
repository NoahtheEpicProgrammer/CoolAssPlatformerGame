package cap.main.input;

import java.awt.event.*;

public class KeyInput extends KeyAdapter implements InputDevice
{
	public static final byte	KEY_IDLE		= 0x00;	// not pressed
	public static final byte	KEY_PRESSED		= 0x01;	// frame that this key is pressed
	public static final byte	KEY_HELD		= 0x02;	// frames after key is pressed
	public static final byte	KEY_RELEASED	= 0x04;	//
	
	// -- //
	
	private byte[]				keys;					// Regular threads.
	private boolean[]			awtkeys, keysnap;		// AWT threads
	
	public KeyInput()
	{
		keys = new byte[256];
		awtkeys = new boolean[256];
		keysnap = new boolean[256];
	}
	
	@Override public final void Snapshot()
	{
		System.arraycopy(awtkeys, 0, keysnap, 0, 256);
	}
	
	@Override public final void Update()
	{
		for (int i = 0; i < 256; i++)
		{
			if (keysnap[i])
				switch(keys[i])
				{
					case KEY_IDLE:
					case KEY_RELEASED:	keys[i] = KEY_PRESSED; break;
					
					case KEY_PRESSED:	keys[i] = KEY_HELD; break;
					case KEY_HELD:		break;
					
					default:			System.err.println("Key state invalid.");
				}
			else
				switch(keys[i])
				{
					case KEY_IDLE:		break;
					
					case KEY_RELEASED:	keys[i] = KEY_IDLE; break;
					
					case KEY_HELD:
					case KEY_PRESSED:	keys[i] = KEY_RELEASED; break;
					
					default:			System.err.println("Key state invalid.");
				}
		}
		
	}
	
	// Basic input tests.
	public boolean IsKeyDown(int keycode) { return (keys[keycode] & 0b11) != 0; }
	public boolean IsKeyUp(int keycode) { return (keys[keycode] & 0b11) == 0; }
	
	// More advanced input tests. Targeted towards UI functions.
	public boolean WasKeyPressed(int keycode) { return keys[keycode] == KEY_PRESSED; }
	public boolean WasKeyHeld(int keycode) { return keys[keycode] == KEY_HELD; }
	public boolean WasKeyReleased(int keycode) { return keys[keycode] == KEY_RELEASED; }
	public boolean IsKeyIdle(int keycode) { return keys[keycode] == KEY_IDLE; }
	
	// For people who know what the hell they're doing.
	public int GetKeyState(int keycode) { return keys[keycode]; }
	
	// -- Key Adapter -- //
	
	@Override
	public final void keyPressed(KeyEvent e)
	{
		int keycode = e.getKeyCode();
		if (keycode < 256 && !awtkeys[keycode])
			awtkeys[keycode] = true;
	}
	
	@Override
	public final void keyReleased(KeyEvent e)
	{
		int keycode = e.getKeyCode();
		if (keycode < 256 && awtkeys[keycode])
			awtkeys[keycode] = false;
	}
}
