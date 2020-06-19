package tho.nill.edifact.check;

import java.util.function.Predicate;

import tho.nill.edifact.LikeAnEnum;
import tho.nill.edifact.Segment;

public class TagAction implements EdifactAction {
	private Predicate<LikeAnEnum> actionSignal;
	private Execute execute;

	public TagAction(Predicate<LikeAnEnum> actionSignal, Execute execute) {
		super();
		this.actionSignal = actionSignal;
		this.execute = execute;
	}


	@Override
	public void perform(Segment segment) {
		if(actionSignal.test(segment.getTag())) {
			execute.perform();
		}
		
	}

}
