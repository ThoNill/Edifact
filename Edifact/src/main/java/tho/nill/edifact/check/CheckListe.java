package tho.nill.edifact.check;

import java.util.LinkedList;
import java.util.List;

import tho.nill.edifact.Segment;

public class CheckListe {
	private List<EdifactCheck> liste;

	public CheckListe() {
		super();
		liste = new LinkedList<>();
	}

	public void add(EdifactCheck check) {
		liste.add(check);
	}
	
	public List<String> check(Segment segment) {
		List<String> errors = new LinkedList<String>();
		check(errors,segment);
		return errors;
	}

	void check(List<String> errors,Segment segment) {
		for(EdifactCheck c : liste) {
			c.check(segment).ifPresent(errors::add);
		}
	}
}
