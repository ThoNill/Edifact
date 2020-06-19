package tho.nill.edifact;

import java.util.Arrays;

import lombok.Data;

@Data
public class DefaultSegment implements Segment {
	
	private final LikeAnEnum tag;
	private final ElementGroup[] groups;
	
	public DefaultSegment(LikeAnEnum tag, ElementGroup[] groups) {
		super();
		this.tag = tag;
		this.groups = groups;
	}
	@Override
	public ElementGroup[] getGroups() {
		return Arrays.copyOf(groups,groups.length);
	}

	@Override
	public ElementGroup getGroup(int index) {
		return groups[index];
	}

	@Override
	public int getGroupCount() {
		return groups.length;
	}

	@Override
	public String getGroupAsText(int index) {
		return groups[index].getGroup();
	}




}
