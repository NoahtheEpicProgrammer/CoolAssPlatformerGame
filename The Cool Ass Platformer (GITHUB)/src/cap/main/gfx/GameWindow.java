package cap.main.gfx;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;

import javax.swing.*;

import cap.main.*;

public class GameWindow
{
	private JFrame			game_window;
	private Canvas			game_canvas;
	
	private BufferStrategy	bs;
	private Graphics2D		current_graphics;
	
	private boolean			resized;
	
	public GameWindow(Config config, String window_name)
	{
		game_window = new JFrame(window_name);
		game_canvas = new Canvas();
		
		game_window.add(game_canvas);
		
		game_window.setResizable(true);
		game_window.setAutoRequestFocus(true);
		game_window.setDefaultCloseOperation(0x03);
		
		game_window.setBackground(Color.black);
		
		int res_w = (int) config.GetValueOrDefault("resolution.w", 800);
		int res_h = (int) config.GetValueOrDefault("resolution.h", 600);
		
		// Size the JFrame using pack() so the content of the game
		// is exactly the resolution we provided.
		Dimension gdim = new Dimension(res_w, res_h);
		game_canvas.setPreferredSize(gdim);
		game_window.pack();
		game_canvas.setPreferredSize(null);
		
		game_window.addComponentListener(new ComponentListener()
		{
			@Override public void componentResized(ComponentEvent e)
			{
				resized = true;
				System.out.println("Resized");
			}
			
			@Override public void componentShown(ComponentEvent e) { }
			@Override public void componentMoved(ComponentEvent e) { }
			@Override public void componentHidden(ComponentEvent e) { }
		});
		
		game_window.setVisible(true);
		game_canvas.requestFocus();
	}
	
	public final void SetSVideoMode(int width, int height, boolean fullscreen, boolean borderless)
	{
		
		
	}
	
	public final void SetHVideoMode(DisplayMode dmode, boolean fullscreen, boolean borderless)
	{
		
		
		
	}
	
	public final void SetResolution(int width, int height)
	{
		
		
	}
	
	private final void new_graphics()
	{
		if (current_graphics != null) current_graphics.dispose();
		current_graphics = (Graphics2D) bs.getDrawGraphics();
	}
	
	public final Graphics2D StartFrame()
	{
		if (resized && bs != null)
		{
			bs.dispose();
			bs = null;
			
			resized = false;
		}
		
		if (bs == null)
		{
			game_canvas.createBufferStrategy(3);
			bs = game_canvas.getBufferStrategy();
			
			new_graphics();
		}
		
		// if the user moves the window in between monitors, we will lose the contents!
		if (bs.contentsLost()) new_graphics();
		
		return current_graphics;
	}
	
	public final void EndFrame()
	{
		bs.show();
	}
	
	public final Canvas GetGameCanvas() { return game_canvas; }
	public final JFrame GetGameJFrame() { return game_window; }
	
}
