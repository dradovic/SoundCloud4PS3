package soundcloud4ps3;

import net.pms.dlna.virtual.VirtualFolder;

public class Folder extends VirtualFolder {

	public Folder() {
		super("", null);
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
}
