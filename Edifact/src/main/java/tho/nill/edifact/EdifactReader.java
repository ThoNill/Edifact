package tho.nill.edifact;

import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Optional;

public class EdifactReader implements Iterator<Segment> {
	private boolean readUNA = true;
	private UNA una = new UNA();
	private ReadWithBuffer reader;
	private TagProvider tags;
	private Segment nextSegment;

	public EdifactReader(InputStreamReader streamReader, TagProvider tags, int length) {
		super();
		reader = new ReadWithBuffer(streamReader, length);
		this.tags = tags;
		this.nextSegment = null;
	}

	@Override
	public boolean hasNext() {
		if (nextSegment == null) {
			Optional<Segment> opt = readNextSegment();
			if (opt.isPresent()) {
				nextSegment = opt.get();
				return true;
			}
			return false;
		}
		return true;
	}

	@Override
	// Ein Iterator muss eine NoSuchElementException erzeugen, wenn es kein nächstes Element gibt
	public Segment next() {
		if (!hasNext()) {
			throw new NoSuchElementException("Hat keine Elemente");
		}
		return returnSegment();
	}

	private Segment returnSegment() {
		Segment seg = nextSegment;
		nextSegment = null;
		return seg;
	}

	private Optional<Segment> readNextSegment() {
		Optional<Segment> opt = null;
		if (readUNA) {
			readUNA = false;
			opt = readUNA();
		} else {
			opt = readSegment(new StringBuilder());
		}
		return opt;
	}

	private Optional<Segment> readUNA() {
		char u = reader.next();
		char n = reader.next();
		char a = reader.next();
		if (u == 'U' && n == 'N' && a == 'A') {
			// UNA:+,? '
			char group = reader.next();
			char seg = reader.next();
			char dec = reader.next();
			char escape = reader.next();
			char empty = reader.next();
			char term = reader.next();
			this.una = new UNA(group, seg, dec, escape, empty, term);
			throwExceptionIfNeeded();
			return Optional.of(una);
		} else {
			StringBuilder builder = new StringBuilder();
			builder.append(u);
			builder.append(n);
			builder.append(a);
			return readSegment(builder);
		}
	}

	private Optional<Segment> readSegment(StringBuilder buffer) {
		Optional<Segment> segment = una.createSegment(buffer, reader, tags);
		throwExceptionIfNeeded();
		return segment;
	}

	private void throwExceptionIfNeeded() {
		if (reader.getException() != null) {
			throw new EdifactException(reader.getException());
		}
	}

}
