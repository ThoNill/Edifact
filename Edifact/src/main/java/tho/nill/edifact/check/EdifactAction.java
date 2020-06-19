package tho.nill.edifact.check;

import tho.nill.edifact.Segment;

public interface EdifactAction {
	void perform(Segment segment);
}
