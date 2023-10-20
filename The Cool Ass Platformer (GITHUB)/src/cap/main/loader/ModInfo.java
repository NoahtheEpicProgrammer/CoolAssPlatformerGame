package cap.main.loader;

/**
 * Represents basic info about a mod.
 */
public final class ModInfo
{
	private final String name;
	private final String desc;
	
	private final String author;
	private final String version;
	
	private final String path;

	ModInfo(String name, String desc, String author, String version, String path)
	{
		this.name = name;
		this.desc = desc;
		
		this.author = author;
		this.version = version;
		
		this.path = path;
	}

	public final String getName() { return name; }
	public final String getDesc() { return desc; }

	public final String getAuthor() { return author; }
	public final String getVersion() { return version; }

	public final String getPath() { return path; }
}
