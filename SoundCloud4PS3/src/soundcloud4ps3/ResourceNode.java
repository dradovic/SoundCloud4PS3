package soundcloud4ps3;

import java.util.ArrayList;

public class ResourceNode {
	private final String resource;
	private final ArrayList<ResourceNode> children = new ArrayList<ResourceNode>();
	
	public ResourceNode(String resource) {
		this.resource = resource;
	}
	
	public void addChild(ResourceNode child) {
		children.add(child);
	}
	
	public String getResource() {
		return resource;
	}

	public ArrayList<ResourceNode> getChildren() {
		return children;
	}
}
