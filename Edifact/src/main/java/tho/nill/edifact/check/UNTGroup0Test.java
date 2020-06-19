package tho.nill.edifact.check;

import java.util.function.Predicate;

import tho.nill.edifact.Segment;

public class UNTGroup0Test extends CountCheck{

	public UNTGroup0Test() {
		super(0,"UNT", new AllTags(), new HasTag("UNH"));
	}



}
