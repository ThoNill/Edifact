package tho.nill.edifact.check.builder;

import java.util.function.Predicate;

public class Between implements Predicate<String> {
	private int min;
	private int max;
	
	

	public Between(int min, int max) {
		super();
		this.min = min;
		this.max = max;
	}



	@Override
	public boolean test(String t) {
		int length = t.strip().length();
		return min <= length && length <= max;
	}

}
