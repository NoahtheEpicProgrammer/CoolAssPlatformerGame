package cap.main;

import java.awt.*;

import cap.main.game.*;
import cap.main.gfx.*;
import cap.main.input.*;

public class Main
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
		RenderHook test = (g) -> {
			g.setColor(Color.white);
			g.fillRect(-32, -48, 64, 96);
		};
		
		game_renderer.Hook("main", (g) -> {
			// clear
			Canvas game_canvas = game_window.GetGameCanvas();
			
			g.setColor(Color.black);
			g.fillRect(0, 0, game_canvas.getWidth(), game_canvas.getHeight());
			
			// Render
			game_renderer.SetRenderBounds_Rect(0, 0, 800, 600);
			game_renderer.RenderHook(g, test);
		});
		
		input_processor = new InputProcessor();
		input_processor.Attach(game_window.GetGameCanvas());
		
		game_world = new GameWorld();
		
		while (true)
		{
			tick();
			render();
			
			try { Thread.sleep(16); } catch (InterruptedException e) { e.printStackTrace(); }
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
	}
	
	// Java Entry Point
	public static void main(String[] args)
	{
		new Main();
	}
	
}
