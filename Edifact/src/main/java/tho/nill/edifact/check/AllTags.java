package tho.nill.edifact.check;

import java.util.function.Predicate;

import tho.nill.edifact.Segment;

public class AllTags implements Predicate<Segment> {

	public AllTags() {
		super();
	}

	@Override
	public boolean test(Segment t) {
		return true;
	}

}
