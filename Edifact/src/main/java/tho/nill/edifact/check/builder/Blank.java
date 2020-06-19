package tho.nill.edifact.check.builder;

import java.util.function.Predicate;

public class Blank implements Predicate<String> {
	

	public Blank() {
		super();
	}



	@Override
	public boolean test(String t) {
		return t.isBlank();
	}

}
