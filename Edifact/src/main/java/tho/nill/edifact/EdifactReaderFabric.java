package tho.nill.edifact;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Iterator;
import java.util.Optional;

public class EdifactReaderFabric implements Iterable<Segment> {
	private TagProvider tags;
	private int length;
	private String filename;
	
	public EdifactReaderFabric(String filename,TagProvider tags, int length) {
		super();
		this.tags = tags;
		this.length = length;
		this.filename = filename;
	}

	@Override
	public Iterator<Segment> iterator() {
		try {
			return new EdifactReader(new FileReader(filename), tags, length);
		} catch (FileNotFoundException e) {
			throw new EdifactException(e);
		}
	}

}
