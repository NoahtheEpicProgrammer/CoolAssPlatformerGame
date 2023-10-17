package cap.main;

import java.io.*;
import java.util.*;

public final class Config
{
	private HashMap<String, Object> config_map;
	
	public Config()
	{
		config_map = new HashMap<String, Object>();
	}
	
	public Object GetValue(String key) { return config_map.get(key); }
	public Object GetValueOrDefault(String key, Object def) { return config_map.getOrDefault(key, def); }
	public void SetValue(String key, Object value) { config_map.put(key, value); }
	
	public final void SaveConfig(String path)
	{
		if (path == null) path = "config.cfg";
		File cfile = new File(path);
		try
		{
			ObjectOutputStream obj_out = new ObjectOutputStream(new FileOutputStream(cfile));
			obj_out.writeObject(config_map);
			obj_out.close();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Loads the config file at path.
	 * 
	 * @param path Path to the config file.
	 */
	public final void LoadConfig(String path)
	{
		File cfile = new File(path);
		try
		{
			ObjectInputStream obj_in = new ObjectInputStream(new FileInputStream(cfile));
			Object input = (HashMap<String, Object>) obj_in.readObject();
			if (input instanceof HashMap<?,?>) config_map = (HashMap<String, Object>) input;
			obj_in.close();
		} catch (FileNotFoundException fnotfound)
		{
			System.out.println("Config file does not exist. Not loading.");
		} catch (IOException | ClassNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
