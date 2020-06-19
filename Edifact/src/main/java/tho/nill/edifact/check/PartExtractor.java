package tho.nill.edifact.check;

import tho.nill.edifact.ElementGroup;
import tho.nill.edifact.Segment;

public class PartExtractor {

	private int groupIndex;
	private int elementIndex;



	public PartExtractor(int groupIndex, int elementIndex) {
		super();
		this.groupIndex = groupIndex;
		this.elementIndex = elementIndex;
	}



	protected String getText(Segment segment) {
		String text = "";
		if ( groupIndex < segment.getGroupCount()) {
			ElementGroup group = segment.getGroup(groupIndex);
			if (elementIndex < group.getElementCount()) {
				text = group.getElement(elementIndex);
			}
		}
		return text;
	}

}