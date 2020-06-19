package tho.nill.edifact.check;

import java.util.Optional;
import java.util.function.Predicate;

import tho.nill.edifact.LikeAnEnum;
import tho.nill.edifact.Segment;

public class TagCheck implements EdifactCheck {
	private Predicate<LikeAnEnum> checkSignal;
	private EdifactCheck check;

	public TagCheck(Predicate<LikeAnEnum> checkSignal, EdifactCheck check) {
		super();
		this.checkSignal = checkSignal;
		this.check = check;
	}

	@Override
	public Optional<String> check(Segment segment) {
		if (checkSignal.test(segment.getTag())) {
			return check.check(segment);
		}
		return Optional.empty();
	}

}
