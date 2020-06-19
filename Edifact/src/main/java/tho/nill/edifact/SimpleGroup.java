package tho.nill.edifact;

public class SimpleGroup implements ElementGroup {
	public static final String EMPTY = "";
	private String group;
	
	
	public SimpleGroup(String text) {
		this.group = text;
	}


	@Override
	public String getGroup() {
		return group;
	}

	@Override
	public String getElement(int index) {
		if (index == 0) {
			return group;
		}
		return EMPTY;
	}

	@Override
	public boolean isSimple() {
		return true;
	}

	@Override
	public int getElementCount() {
		return 1;
	}

	@Override
	public String toString() {
		return "SimpleGroup [group=" + group + "]";
	}
}
