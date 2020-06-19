package tho.nill.edifact;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Optional;
import lombok.NonNull;

public class ReadWithBuffer implements Iterator<Character> {
	private InputStreamReader reader;
	private boolean ok = true;
	private char[] buffer;
	private int readedCount;
	private int currentPosition;
	private Exception ex;
	private Character c = null;

	public ReadWithBuffer(@NonNull InputStreamReader reader, int length) {
		super();
		assert length > 10;
		this.reader = reader;
		buffer = new char[length];
	}

	@Override
	public boolean hasNext() {
		readChar();
		return c != null;
	}

	@Override
	// Ein Iterator muss eine NoSuchElementException erzeugen, wenn es kein nächstes Element gibt
	public Character next() {
		if (hasNext()) {
			return returnCharacter();
		} else {
			throw new NoSuchElementException();
		}
	}

	public Exception getException() {
		return ex;
	}

	private Character returnCharacter() {
		char returnC = c.charValue();
		c = null;
		return returnC;
	}


	private void readChar() {
		if (c == null) {
			readBuffer();
			readCharFromBuffer();
		} 
	}

	private void readCharFromBuffer() {
		if ( ok && currentPosition < readedCount) {
			c = buffer[currentPosition];
			currentPosition++;
		}
	}

	private void readBuffer() {
		if (ok && (currentPosition == readedCount)) {
			readFromReader();
		}
	}

	private void readFromReader() {
		try {
			currentPosition = 0;
			readedCount = reader.read(buffer);
			ok = (readedCount > 0);
		} catch (IOException e) {
			ex = e;
			ok = false;
		}
	}

}