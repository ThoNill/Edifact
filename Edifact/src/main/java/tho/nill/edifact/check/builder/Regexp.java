package tho.nill.edifact.check.builder;

import java.util.function.Predicate;
import java.util.regex.Pattern;

public class Regexp implements Predicate<String> {
	private Pattern pattern;
	
	public Regexp(String regexp) {
		super();
		this.pattern = Pattern.compile(regexp);
	}

	@Override
	public boolean test(String t) {
		return pattern.matcher(t).matches();
	}

}
