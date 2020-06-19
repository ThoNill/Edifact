package tho.nill.test.edifact;

import tho.nill.grundgestein.zelt.EntityZelt;

public class TestZelt implements EntityZelt {
	private String lastCommand;
	private String x;
	private String y;
	
	public TestZelt() {
	}

	public String getLastCommand() {
		return lastCommand;
	}

	public String getX() {
		return x;
	}

	public String getY() {
		return y;
	}


	
	public void create() {
		lastCommand = "create";
	}

	
	public void save() {
		lastCommand = "safe";
		
	}

	public void setX(String text) {
		x = text;
	}
	
	public void setY(String text) {
		y = text;
	}
}
