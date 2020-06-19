package tho.nill.edifact;

public interface ElementGroup {
	String getGroup();
	String getElement(int index);
	int getElementCount();
	boolean isSimple(); // Only a String, no subgroups
}
