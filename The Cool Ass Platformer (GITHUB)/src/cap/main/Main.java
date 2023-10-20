package cap.main;

import java.awt.*;

import cap.main.game.*;
import cap.main.gfx.*;
import cap.main.input.*;

public class Main implements Runnable
{
	private Config			config;
	
	private GameWindow		game_window;
	private GameRenderer	game_renderer;
	
	private	InputProcessor	input_processor;
	
	private GameWorld		game_world;
	
	public Main()
	{
		config = new Config();
		game_window = new GameWindow(config, "Lolol");
		
		game_renderer = new GameRenderer(game_window.GetGameCanvas());
		
		game_renderer.Hook("main", (g, renderer) -> {
			// clear
			Canvas game_canvas = game_window.GetGameCanvas();
			
			g.setColor(Color.black);
			g.fillRect(0, 0, game_canvas.getWidth(), game_canvas.getHeight());
			
			// Render
			game_renderer.SetRenderBounds_Rect(0, 0, 800, 600);
			game_world.Render(g, renderer);
		});
		
		input_processor = new InputProcessor();
		input_processor.Attach(game_window.GetGameCanvas());
		
		game_world = new GameWorld(input_processor);
		
		Thread thread = new Thread(this);
		thread.start();
	}
	
	@Override public void run()
	{
		long frame_time = 1000000000 / 60;
		long last_time = System.nanoTime();
		
		System.out.println("Frame Time (ms): " + frame_time / 1000000);
		
		
		long timer = System.currentTimeMillis();
		int frames = 0;
		
		while (true)
		{
			long now = System.nanoTime();
//			System.out.println(now + " / " + (last_time + frame_time));
			if (last_time + frame_time > now) continue;
			
			tick();
			render();
			
			frames++;
			
			if (timer + 1000 < System.currentTimeMillis())
			{
				System.out.println("FPS: " + frames);
				frames = 0;
				timer += 1000;
			}
			
			last_time = now;
		}
	}
	
	private final void tick()
	{
		// Update input.
		input_processor.Update();
		
		// Update game.
		game_world.Update();
	}
	
	private final void render()
	{
		Graphics2D g = game_window.StartFrame();
		if (g == null) return; // if no graphics, don't render
		
		game_renderer.InitializeRender();
		game_renderer.Render(g);
		
		game_window.EndFrame();
		Toolkit.getDefaultToolkit().sync();
	}
	
	// Java Entry Point
	public static void main(String[] args)
	{
		new Main();
	}
	
}
