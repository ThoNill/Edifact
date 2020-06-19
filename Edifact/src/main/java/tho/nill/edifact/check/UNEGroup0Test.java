package tho.nill.edifact.check;

import java.util.function.Predicate;

import tho.nill.edifact.Segment;

public class UNEGroup0Test extends CountCheck{

	public UNEGroup0Test() {
		super(0,"UNE", new AllTags(), new HasTag("UNG"));
	}



}
