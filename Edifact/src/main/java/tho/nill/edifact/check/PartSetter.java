package tho.nill.edifact.check;

import java.util.function.Consumer;

import tho.nill.edifact.Segment;

public class PartSetter extends PartExtractor implements Consumer<Segment> {
	private Setter setter;

	public PartSetter(Setter setter, int groupIndex, int elementIndex) {
		super(groupIndex, elementIndex);
		this.setter = setter;
	}

	@Override
	public void accept(Segment segment) {
		String text = getText(segment);
		setter.accept(text);
	}


}
