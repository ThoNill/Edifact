package tho.nill.edifact.check;

import java.util.Optional;

import tho.nill.edifact.Segment;

public class IdEqualsCheck implements EdifactCheck {
	private String firstTag;
	private int firstGroup;
	private String nextTag;
	private int nextGroup;
	private String firstId;
	


	public IdEqualsCheck(String firstTag, int firstGroup, String nextTag, int nextGroup) {
		super();
		this.firstTag = firstTag;
		this.firstGroup = firstGroup;
		this.nextTag = nextTag;
		this.nextGroup = nextGroup;
		this.firstId=null;
	}



	@Override
	public Optional<String> check(Segment segment) {
		if (segment.getTag()!=null) {
			if (firstTag.equals(segment.getTag().name())) {
				firstId = segment.getGroup(firstGroup).getGroup();
			}
			if (nextTag.equals(segment.getTag().name())) {
				String nextId = segment.getGroup(nextGroup).getGroup();
				if (firstId == null || (!firstId.equals(nextId))) {
					return Optional.of("Id in " + firstTag + " " + firstId + " is different from " + nextTag + " " + nextId);
				}
			}

		}
		return Optional.empty();
	}

}
