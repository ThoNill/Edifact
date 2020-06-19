package tho.nill.edifact;

public interface Segment {
	LikeAnEnum getTag();
	ElementGroup[] getGroups();
	ElementGroup getGroup(int index);
	int getGroupCount();
	String getGroupAsText(int index);
}
