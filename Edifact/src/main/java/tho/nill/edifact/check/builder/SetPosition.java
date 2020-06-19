package tho.nill.edifact.check.builder;

public interface SetPosition {
		SetPredicate atGroupElement(int group, int element);

		default SetPredicate atGroup(int group) {
			return atGroupElement(group,0);
		}
}
