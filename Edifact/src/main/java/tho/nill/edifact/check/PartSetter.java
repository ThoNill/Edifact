package tho.nill.edifact.check;

import tho.nill.edifact.Segment;

public class PartSetter extends PartExtractor implements EdifactAction {
	private Setter setter;

	public PartSetter(Setter setter, int groupIndex, int elementIndex) {
		super(groupIndex, elementIndex);
		this.setter = setter;
	}

	@Override
	public void perform(Segment segment) {
		String text = getText(segment);
		setter.set(text);
	}

}
