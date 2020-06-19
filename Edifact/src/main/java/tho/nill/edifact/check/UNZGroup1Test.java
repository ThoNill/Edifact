package tho.nill.edifact.check;

import java.util.Optional;

import tho.nill.edifact.Segment;

public class UNZGroup1Test extends IdEqualsCheck {
	private String UNBId;
	
	public UNZGroup1Test() {
		super("UNB",4,"UNZ",1);
	}

}
