package tho.nill.edifact.check;

import java.util.Optional;
import java.util.function.Predicate;

import tho.nill.edifact.Segment;

public class CountCheck implements EdifactCheck {
	private int group;
	private int currentCounter = 0;
	private String tag;
	private Predicate<Segment> countSegment;
	private Predicate<Segment> initSegment;

	
	
	public CountCheck(int group, String tag, Predicate<Segment> countSegment, Predicate<Segment> initSegment) {
		super();
		this.group = group;
		this.tag = tag;
		this.countSegment = countSegment;
		this.initSegment = initSegment;
		init();
	}


	

	@Override
	public Optional<String> check(Segment segment) {
		if (segment.getTag() != null) {
			if (initSegment.test(segment)) {
				init();
			}

			if (countSegment.test(segment)) {
				currentCounter++;
			}
			if (tag.equals(segment.getTag().name())) {
				String counterString = segment.getGroupAsText(group);
				int counter = Integer.parseInt(counterString.replaceAll("^0+",""));
				if (counter != currentCounter) {
					return Optional.of("counter does not match " + counter + " in file my count is "+currentCounter);
				}
			}
		}
		return Optional.empty();
		
	}

	protected void init() {
		currentCounter=0;
	}
}
