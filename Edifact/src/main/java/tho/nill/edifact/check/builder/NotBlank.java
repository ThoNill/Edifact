package tho.nill.edifact.check.builder;

import java.util.function.Predicate;

public class NotBlank implements Predicate<String> {
	

	public NotBlank() {
		super();
	}



	@Override
	public boolean test(String t) {
		return !t.isBlank();
	}

}
