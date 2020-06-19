package tho.nill.edifact;

public class GroupWithElements implements ElementGroup {
	private String group;
	private String[] elements;
	
	public GroupWithElements(String group,String[] elements) {
		this.elements = elements;
		this.group = group;
	}

	@Override
	public String getGroup() {
		return group;
	}

	@Override
	public String getElement(int index) {
		return elements[index];
	}

	@Override
	public boolean isSimple() {
		return false;
	}

	@Override
	public int getElementCount() {
		return elements.length;
	}

}
