package tho.nill.edifact.check.builder;

public class Numeric extends Regexp {
	

	public Numeric() {
		super("^[0-9]*([\\.\\,][0-9]+){0,1}$");
	}





}
