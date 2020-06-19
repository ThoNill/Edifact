package tho.nill.edifact.check;

import java.util.Optional;
import java.util.function.Predicate;

public class PartCheckData {
	private Predicate<String> check;
	private String errormessage;

	public PartCheckData(Predicate<String> check, String errormessage) {
		super();
		this.check = check;
		this.errormessage = errormessage;
	}


	public Optional<String> check(String text) {
			if (!check.test(text)) {
				return Optional.of(errormessage);
			}
			return Optional.empty();
	}
}