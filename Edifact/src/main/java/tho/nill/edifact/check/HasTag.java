package tho.nill.edifact.check;

import java.util.function.Predicate;

import tho.nill.edifact.Segment;

public class HasTag implements Predicate<Segment> {
	private String tagName;

	public HasTag(String tagName) {
		super();
		this.tagName = tagName;
	}

	public boolean test(Segment segment) {
		return tagName.equals(segment.getTag().name());
	}

}
