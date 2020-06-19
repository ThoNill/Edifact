package tho.nill.edifact.check;

import java.util.Optional;
import java.util.function.Predicate;

import tho.nill.edifact.Segment;

public class PartCheck extends PartExtractor implements EdifactCheck {
	private PartCheckData check ;

	public PartCheck(Predicate<String> check,int groupIndex, int elementIndex,String errormessage) {
		super(groupIndex,elementIndex);
		this.check = new PartCheckData(check,errormessage);
	}

	@Override
	public Optional<String> check(Segment segment) {
			String text = getText(segment);
			return check.check(text);
	}

}
