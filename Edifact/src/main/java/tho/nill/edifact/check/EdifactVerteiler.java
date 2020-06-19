package tho.nill.edifact.check;

import tho.nill.edifact.TagProvider;

public class EdifactVerteiler extends CheckVerteiler{

	public EdifactVerteiler(TagProvider tags) {
		super(tags);
		add(new UNEGroup0Test());
		add(new UNEGroup1Test());

		add(new UNTGroup0Test());
		add(new UNTGroup1Test());
		add(new UNZGroup0Test());
		add(new UNZGroup1Test());
	}

	

}
