package tho.nill.edifact.zelt;

import tho.nill.edifact.Segment;
import tho.nill.edifact.check.PartSetter;
import tho.nill.edifact.check.Setter;
import tho.nill.grundgestein.consumer.ConsumerList;

public class SetterList extends ConsumerList<Segment> {

	public SetterList(Setter... setters) {
		createList(setters);
	}
	
	private void createList(Setter[] setters) {
		for (int groupIndex = 0; groupIndex < setters.length; groupIndex++) {
			addSetter(setters[groupIndex], groupIndex, 0);
		}
	}

	public void addSetter(Setter s, int groupIndex, int elementIndex) {
		addConsumer(new PartSetter(s, groupIndex, elementIndex));
	}

}