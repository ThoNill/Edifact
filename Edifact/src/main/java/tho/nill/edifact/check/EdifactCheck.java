package tho.nill.edifact.check;

import java.util.Optional;

import tho.nill.edifact.Segment;

public interface EdifactCheck {
	Optional<String> check(Segment segment);
}
