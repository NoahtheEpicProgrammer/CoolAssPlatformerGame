package cap.main.loader;

import java.io.*;
import java.security.*;
import java.util.*;

public abstract class ModLoaderBase extends ClassLoader
{
	private static ProtectionDomain pdom;
	
	static
	{
		Permissions perms = new Permissions();
		
		perms.add(new FilePermission("<<ALL FILES>>", null));
		
	}
	
	
	public ModLoaderBase()
	{
		
	}
	

	@Override protected Class<?> findClass(String name) throws ClassNotFoundException
	{
		// TODO Auto-generated method stub
		return super.findClass(name);
	}
	
}
