package tho.nill.edifact;

import java.util.Optional;

import lombok.Data;

@Data
public class UNA implements Segment {
	private char componentSeparator;
	private char elementSeparator;
	private char decimalSeparator;
	private char escapeChar;
	private char emptySpace;
	private char segmentTerminator;

	public UNA() {
		this(':', '+', ',', '?', ' ', '\'');
	}

	public UNA(char componentSeparator, char elementSeparator, char decimalSeparator, char escapeChar, char emptySpace,
			char segmentTerminator) {
		super();
		this.componentSeparator = componentSeparator;
		this.elementSeparator = elementSeparator;
		this.decimalSeparator = decimalSeparator;
		this.escapeChar = escapeChar;
		this.emptySpace = emptySpace;
		this.segmentTerminator = segmentTerminator;
	}

	@Override
	public LikeAnEnum getTag() {
		return Edifact.UNA;
	}

	@Override
	public ElementGroup[] getGroups() {
		return null;
	}

	@Override
	public ElementGroup getGroup(int index) {
		return null;
	}

	@Override
	public int getGroupCount() {
		return 0;
	}

	@Override
	public String getGroupAsText(int index) {
		return "";
	}

	public Optional<Segment> createSegment(StringBuilder buffer, ReadWithBuffer reader, TagProvider tags) {
		Optional<String[]> segmentation = create(buffer, reader);
		if (segmentation.isPresent()) {
			String[] presegment = segmentation.get();
			LikeAnEnum tag = tags.getEnum(presegment[0]);
			ElementGroup[] groups = new ElementGroup[presegment.length - 1];
			for (int i = 1; i < presegment.length; i++) {
				groups[i - 1] = createGroup(presegment[i]);
			}
			Segment segment = new DefaultSegment(tag, groups);
			return Optional.of(segment);
		}
		return Optional.empty();
	}

	public String toString(Segment segment) {
		StringBuilder builder = new StringBuilder();
		escape(builder, segment.getTag().name());
		for (int i = 0; i < segment.getGroupCount(); i++) {
			builder.append(this.elementSeparator);
			toString(builder, segment.getGroup(i));
		}
		builder.append(this.segmentTerminator);
		return builder.toString();
	}

	private ElementGroup createGroup(String text) {
		String[] groupsAsText = create(text, this.componentSeparator);
		if (groupsAsText.length == 1) {
			return new SimpleGroup(text);
		}
		return new GroupWithElements(text, groupsAsText);
	}

	public Optional<String[]> create(StringBuilder buffer, ReadWithBuffer reader) {
		while (reader.hasNext()) {
			char c = reader.next();
			if (notLinefeed(c)) { // ignore \n \r
				if (c == escapeChar) {
					deEscape(reader, buffer);
				} else {
					if (c == segmentTerminator) {
						String text = buffer.toString();
						if (text == null || text.isBlank()) {
							throw new EdifactException("Text to shart or empty");
						}
						return Optional.of(create(text, elementSeparator));
					}
					if (c == emptySpace) {
						buffer.append(' ');
					} else {
						buffer.append(c);
					}
				}
			}
		}
		return Optional.empty();
	}

	private void deEscape(ReadWithBuffer reader, StringBuilder buffer) {
		if (!reader.hasNext()) {
			throw new EdifactException("wrong end of stream");
		}
		char c = reader.next();
		buffer.append(escapeChar); // we need it in the String
		buffer.append(c);
	}

	private boolean notLinefeed(char c) {
		return !(c == '\n' || c == '\r');
	}

	private String[] create(String text, char separator) {
		int count = count(text, separator);
		String subtext[] = new String[count];
		int textLength = text.length();
		int i = 0;
		char c;
		StringBuilder buffer = new StringBuilder(textLength);

		for (int pos = 0; pos < textLength; pos++) {
			c = text.charAt(pos);
			if (c == escapeChar) {
				pos++;
				c = text.charAt(pos);
				buffer.append(c);
			} else if (c == separator) {
				subtext[i] = buffer.toString();
				i++;
				buffer = new StringBuilder(textLength);
			} else {
				buffer.append(c);
			}
		}
		subtext[i] = buffer.toString();
		return subtext;
	}

	private int count(String text, char separator) {
		int count = 1;
		char c;
		int textLength = text.length();
		for (int pos = 0; pos < textLength; pos++) {
			c = text.charAt(pos);
			if (c == escapeChar) {
				pos++;
				c = text.charAt(pos);
			} else if (c == separator) {
				count++;
			}
		}
		return count;
	}

	private void toString(StringBuilder builder, ElementGroup group) {
		if (group.isSimple()) {
			escape(builder, group.getGroup());
		} else {
			boolean first = true;
			for (int i = 0; i < group.getElementCount(); i++) {
				if (!first) {
					builder.append(this.componentSeparator);
				}
				first = false;
				escape(builder, group.getElement(i));
			}
		}

	}

	private void escape(StringBuilder builder, String text) {
		for (int i = 0; i < text.length(); i++) {
			char c = text.charAt(i);
			if (isSpecialChar(c)) {
				builder.append(escapeChar);
			}
			builder.append(c);
		}
	}

	private boolean isSpecialChar(char c) {
		return c == escapeChar || c == componentSeparator || c == segmentTerminator || c == decimalSeparator
				|| c == emptySpace || c == elementSeparator;
	}

}