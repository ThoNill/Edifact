package tho.nill.edifact.check;

import java.util.function.Predicate;

import tho.nill.edifact.Segment;

public class UNZGroup0Test extends CountCheck{

	public UNZGroup0Test() {
		super(0,"UNZ", new HasTag("UNH"),new NoTag());
	}



}
