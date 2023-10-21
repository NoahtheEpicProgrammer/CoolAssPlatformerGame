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
	
	// KEY MAPPING: (TODO)
	//		  0      : The first key is an unmapped key.
	//		  1      : Escape key.
	//		  2 -  13: Function keys (F1 - F12).
	//		 14	-  16: Printscreen, Scroll Lock, Pause/Break
	//		 17 -  30: ~, 1, 2, 3, 4, 5, 6, 7, 8, 9, 0, -, =, Backspace
	//		 31 -  44: Tab, Q, W, E, R, T, Y, U, I, O, P, [, ], \
	//		 45 -  57: Caps Lock, A, S, D, F, G, H, J, K, L, ;, ', Enter
	//		 58 -  70: LShift, Z, X, C, V, B, N, M, ,, ., /, RShift           Yes, that is a comma entered in a list delimited by commas.
	//		 73 -  80: LCtrl, LSuper, LAlt, Space, RAlt, RSuper, Context Menu Key?, RCtrl
	//		 81 -  86: Insert, Home, Page Up, Delete, End, Page Down
	//		 87 -  90: Up, Left, Down, Right
	//		 91 - 107: Numpad keys: Lock, /, *, -, 7, 8, 9, +, 4, 5, 6, 1, 2, 3, Enter, 0, .
	
	
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
	
	// why the fuck is there no differentiation between different locations of a key.
	// I guess live by the sword, die by the sword. I'm the one using GUI for a game.
	public static final int translatequick(int keycode, int where)
	{
		switch (where)
		{
			case KeyEvent.KEY_LOCATION_RIGHT:
				if (keycode == KeyEvent.VK_SHIFT)
				{
					return 1;
				} else if (keycode == KeyEvent.VK_CONTROL)
				{
					return 2;
					
				} else if (keycode == KeyEvent.VK_ALT)
				{
					
					return 4;
				}
		
			default:
				return keycode;
		}
	}
	
	public static final int Translate(int keycode, int where)
	{
		switch(keycode)
		{
			
			
			default:
				
				
				
				return keycode;
		}
		
	}
	
}
