package tho.nill.edifact.check.builder;

import java.util.function.Predicate;

public class Min implements Predicate<String> {
	private int min;
	
	

	public Min(int min) {
		super();
		this.min = min;
	}



	@Override
	public boolean test(String t) {
		int length = t.strip().length();
		return min <= length ;
	}

}
