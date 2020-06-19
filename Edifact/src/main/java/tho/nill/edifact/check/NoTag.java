package tho.nill.edifact.check;

import java.util.function.Predicate;

import tho.nill.edifact.Segment;

public class NoTag implements Predicate<Segment> {

	public NoTag() {
		super();
	}

	@Override
	public boolean test(Segment t) {
		return false;
	}

}
