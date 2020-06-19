package tho.nill.edifact.check.builder;

import java.util.function.Predicate;

import tho.nill.edifact.check.CheckVerteiler;
import tho.nill.edifact.check.PartCheck;

public class CheckBuilder {
	private CheckVerteiler verteiler;
	private String tag;
	private int group;
	private int element;
	private Predicate<String> predicate;

	public CheckBuilder(CheckVerteiler verteiler) {
		this.verteiler = verteiler;
	}

	public SetPosition forTag(String tag) {
		this.tag = tag;
		final CheckBuilder b = this;

		return new SetPosition() {

			@Override
			public SetPredicate atGroupElement(int group, int element) {
				b.group = group;
				b.element = element;
				return b.predicate();
			}

		};
	}

	private SetPredicate predicate() {

		final CheckBuilder b = this;
		return new SetPredicate() {

			public SetErrorMessage withPredicate(Predicate<String> p) {
				b.predicate = p;
				return b.error();
			}

			public SetErrorMessage match(String regexp) {
				b.predicate = new Regexp(regexp);
				return b.error();
			}

			public SetErrorMessage between(int min, int max) {
				b.predicate = new Between(min, max);
				return b.error();
			}

			public SetErrorMessage length(int length) {
				b.predicate = new Between(length, length);
				return b.error();
			}

			public SetErrorMessage max(int max) {
				b.predicate = new Between(0, max);
				return b.error();
			}
			
			public SetErrorMessage min(int max) {
				b.predicate = new Between(0, max);
				return b.error();
			}			

			public SetErrorMessage notBlank() {
				b.predicate = new NotBlank();
				return b.error();
			}

			public SetErrorMessage blank() {
				b.predicate = new Blank();
				return b.error();
			}

			public SetErrorMessage numeric() {
				b.predicate = new Numeric();
				return b.error();
			}

			public SetErrorMessage integer() {
				b.predicate = new Integer();
				return b.error();
			}
		};
	}

	private SetErrorMessage error() {
		final CheckBuilder b = this;
		return new SetErrorMessage() {

			@Override
			public CheckBuilder elseError(String message) {
				b.verteiler.add(tag, new PartCheck(predicate, group, element, message));
				return b;
			}
		};
	}

}
