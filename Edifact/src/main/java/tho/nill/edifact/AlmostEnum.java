package tho.nill.edifact;

public class AlmostEnum implements LikeAnEnum {
	private int ordinal;
	private String name;
	

	public AlmostEnum(int ordinal, String name) {
		super();
		this.ordinal = ordinal;
		this.name = name;
	}

	@Override
	public int ordinal() {
		return ordinal;
	}

	@Override
	public String name() {
		return name;
	}

}
