package tho.nill.edifact.check;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

import tho.nill.edifact.EdifactException;
import tho.nill.edifact.LikeAnEnum;
import tho.nill.edifact.Segment;
import tho.nill.edifact.TagProvider;
import tho.nill.edifact.check.builder.CheckBuilder;

public class CheckVerteiler implements Function<Segment, Segment> {
	private CheckListe[] tagChecks;
	private CheckListe allgemein = new CheckListe();
	private TagProvider tags;
	private List<EdifactError> errors = new LinkedList<EdifactError>();

	private int position=0;

	public CheckVerteiler(TagProvider tags) {
		this.tags = tags;
		tagChecks = new CheckListe[tags.size()];
		for(int i=0;i< tagChecks.length;i++) {
			tagChecks[i] = new CheckListe();
		}
	}
	
	public CheckBuilder build() {
		return new CheckBuilder(this);
	}

	public Segment apply(Segment segment) {
		position++;
		LikeAnEnum e = segment.getTag();
		if (e != null) {
			List<String> segmentErrors = allgemein.check(segment);
			tagChecks[segment.getTag().ordinal()].check(segmentErrors,segment);
			if (!segmentErrors.isEmpty()) {
				errors.add(new EdifactError(segment, segmentErrors,position));
			}
		} else {
			throw new EdifactException("No tag for " + segment);
		}
		return segment;
	}

	public CheckVerteiler add(EdifactCheck c) {
		allgemein.add(c);
		return this;
	}
	
	public CheckVerteiler add(String tagName,EdifactCheck c) {
		LikeAnEnum e = tags.getEnum(tagName);
		if (e != null) {
			tagChecks[tags.getEnum(tagName).ordinal()].add(c);
		} else {
			throw new EdifactException("Can not find tag "+ tagName);
		}
		return this;
	}


	public List<EdifactError> getErrors() {
		return errors;
	}
	
	
	public void clearErrors() {
		errors.clear();
	}


}
