package tho.nill.test.edifact;

import static org.junit.Assert.*;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import org.junit.Test;

import reactor.core.publisher.Flux;
import tho.nill.edifact.Edifact;
import tho.nill.edifact.EdifactReader;
import tho.nill.edifact.EdifactReaderFabric;
import tho.nill.edifact.GroupWithElements;
import tho.nill.edifact.LikeAnEnum;
import tho.nill.edifact.ReadWithBuffer;
import tho.nill.edifact.Segment;
import tho.nill.edifact.SimpleGroup;
import tho.nill.edifact.TagProvider;
import tho.nill.edifact.UNA;
import tho.nill.edifact.check.EdifactError;
import tho.nill.edifact.check.EdifactVerteiler;

public class EdifactTest {
	private boolean errorInStream = false;

	@Test
	public void testSimpleGroup() {
		try {
			SimpleGroup s = new SimpleGroup("123");
			assertEquals("123", s.getGroup());
			assertEquals("123", s.getElement(0));
			assertEquals("", s.getElement(1));
			assertEquals(1, s.getElementCount());
			assertTrue(s.isSimple());
		} catch (Exception e) {
			fail("exception occured");
			e.printStackTrace();
		}
	}

	@Test
	public void testWithElements() {
		try {
			GroupWithElements s = new GroupWithElements("12:3", new String[] { "12", "3" });
			assertEquals("12:3", s.getGroup());
			assertEquals("12", s.getElement(0));
			assertEquals("3", s.getElement(1));
			assertEquals(2, s.getElementCount());
			assertFalse(s.isSimple());
		} catch (Exception e) {
			fail("exception occured");
			e.printStackTrace();
		}
	}

	@Test
	public void testReader() {
		try {
			testReadWithBuffer("UNB+123+124'");
			testReadWithBuffer("");
			testReadWithBuffer("  ");
		} catch (IOException e) {
			fail("exception occured");
			e.printStackTrace();
		}
	}

	@Test
	public void testTagProvider() throws IOException {
		TagProvider p = new TagProvider();
		LikeAnEnum e = p.getOrCreateEnum("XXX");
		assertEquals("XXX", e.name());
		LikeAnEnum e2 = p.getEnum(e.ordinal());
		assertEquals(e2, e);
		assertEquals(Edifact.values().length + 1, p.size());
	}

	@Test
	public void simpleTests() throws IOException {
		istOk("UNB+12:3+124'", "UNB", "12:3", "124");
		istOk("UNB+123+124'", "UNB", "123", "124");
		istOk("UNB+123+1?+24'", "UNB", "123", "1+24");
		istOk("UNB++1?+?24'", "UNB", "", "1+24");
		istOk("UNB'", "UNB");
	}

	@Test
	public void segment2TextTest() {
		try {
			segment2Text("UNB+123+124'");
			segment2Text("UNB+1??23+124'");
			segment2Text("UNB+1??23?+124'");
		} catch (IOException e) {
			fail("exception occured");
			e.printStackTrace();
		}
	}

	private void testReadWithBuffer(String text) throws IOException {
		testReadWithBuffer1(text);
		testReadWithBuffer2(text);
	}

	private void testReadWithBuffer1(String text) throws IOException {
		ByteArrayInputStream in = new ByteArrayInputStream(text.getBytes());
		ReadWithBuffer reader = new ReadWithBuffer(new InputStreamReader(in), 100);
		int pos = 0;
		while (reader.hasNext()) {
			char c = reader.next();
			if (reader.hasNext()) {
				char ct = text.charAt(pos);
				assertEquals(ct, c);
			}
			pos++;
		}
		assertEquals(text.length(), pos);
	}

	private void testReadWithBuffer2(String text) throws IOException {
		ByteArrayInputStream in = new ByteArrayInputStream(text.getBytes());
		ReadWithBuffer reader = new ReadWithBuffer(new InputStreamReader(in), 100);
		int pos = 0;
		while (reader.hasNext()) {
			Character c = reader.next();
			char ct = text.charAt(pos);
			assertEquals(ct, c.charValue());
			pos++;

		}
		assertFalse(reader.hasNext());
		assertEquals(text.length(), pos);
	}

	private void segment2Text(String text) throws IOException {
		ByteArrayInputStream in = new ByteArrayInputStream(text.getBytes());
		ReadWithBuffer reader = new ReadWithBuffer(new InputStreamReader(in), 100);
		UNA una = new UNA();
		StringBuilder buffer = new StringBuilder();
		TagProvider tags = new TagProvider();
		Optional<Segment> result = una.createSegment(buffer, reader, tags);
		assertTrue(result.isPresent());
		assertEquals(text, una.toString(result.get()));
	}

	@Test
	public void withExceptions() throws IOException {
		withException("UNB+123+124");
		withException("'");
	}

	private void withException(String text) {
		try {
			istOk(text, "");
			fail("No Exception!");
		} catch (Exception e) {
			// its okay
		}
	}

	private void istOk(String text, String... erwartet) throws IOException {
		ByteArrayInputStream in = new ByteArrayInputStream(text.getBytes());
		ReadWithBuffer reader = new ReadWithBuffer(new InputStreamReader(in), 100);
		StringBuilder builder = new StringBuilder();
		UNA una = new UNA();
		Optional<String[]> result = una.create(builder, reader);
		if (erwartet == null && result.isPresent()) {
			fail("Kein Ergebns erwartet");
		} else {
			assertEquals(erwartet, result.get());
		}
	}

	public List<Segment> testeEdifactReader(String text) {
		ByteArrayInputStream in = new ByteArrayInputStream(text.getBytes());
		EdifactReader edifactReader = new EdifactReader(new InputStreamReader(in), new TagProvider(), 50);
		List<Segment> segmente = new ArrayList<>();
		while (edifactReader.hasNext()) {
			segmente.add(edifactReader.next());
		}
		return segmente;
	}

	@Test
	public void testeEdifactReader1() {
		List<Segment> l = testeEdifactReader("UNB+123+124'");
		assertEquals(1, l.size());
		Segment unb = l.get(0);
		assertEquals("UNB", unb.getTag().name());
		assertEquals(2, unb.getGroupCount());
		assertEquals("123", unb.getGroup(0).getElement(0).toString());
		assertEquals("124", unb.getGroup(1).getElement(0).toString());

	}

	@Test
	public void testeEdifactReader2() {
		List<Segment> l = testeEdifactReader("UNB+123+124'UNB+223+224'");
		assertEquals(2, l.size());
		Segment unb = l.get(1);
		assertEquals("UNB", unb.getTag().name());
		assertEquals(2, unb.getGroupCount());
		assertEquals("223", unb.getGroup(0).getElement(0).toString());
		assertEquals("224", unb.getGroup(1).getElement(0).toString());
	}

	@Test
	public void testeEdifactReader3() {
		List<Segment> l = testeEdifactReader("UNB+123+124'UNB+223+224");
		assertEquals(1, l.size());
		Segment unb = l.get(0);
		assertEquals("UNB", unb.getTag().name());
		assertEquals(2, unb.getGroupCount());
		assertEquals("123", unb.getGroup(0).getElement(0).toString());
		assertEquals("124", unb.getGroup(1).getElement(0).toString());
	}

	@Test
	public void testeEdifactReader4() {
		List<Segment> l = testeEdifactReader("UNA:-,? *UNB-123-124*UNB-223-224*");
		assertEquals(3, l.size());
		Segment una = l.get(0);
		assertEquals("UNA", una.getTag().name());
		Segment unb = l.get(1);
		assertEquals("UNB", unb.getTag().name());
		assertEquals(2, unb.getGroupCount());
		assertEquals("123", unb.getGroup(0).getElement(0).toString());
		assertEquals("124", unb.getGroup(1).getElement(0).toString());
	}

	@Test
	public void testeEdifactReader5() {
		List<Segment> l = testeEdifactReader("UNB+12:3+124'");
		assertEquals(1, l.size());
		Segment unb = l.get(0);
		assertEquals("UNB", unb.getTag().name());
		assertEquals(2, unb.getGroupCount());
		assertEquals(2, unb.getGroup(0).getElementCount());
		assertEquals("12", unb.getGroup(0).getElement(0).toString());
		assertEquals("3", unb.getGroup(0).getElement(1).toString());
		assertEquals("124", unb.getGroup(1).getElement(0).toString());
	}

	@Test
	public void testeDateiEinlesen() throws FileNotFoundException {
		TagProvider tags = new TagProvider();
		tags.getOrCreateEnum("IDK");
		tags.getOrCreateEnum("VDT");
		tags.getOrCreateEnum("FKT");
		tags.getOrCreateEnum("VKG");
		tags.getOrCreateEnum("NAM");
		tags.getOrCreateEnum("ANS");
		tags.getOrCreateEnum("DFU");
		tags.getOrCreateEnum("UEM");
		tags.getOrCreateEnum("ASP");

		EdifactReaderFabric edifactReaderFabric = new EdifactReaderFabric("src/test/resources/edifact.test", tags, 50);
		EdifactVerteiler verteiler = new EdifactVerteiler(tags);
		Flux.fromIterable(edifactReaderFabric).map(verteiler).doAfterTerminate(new Runnable() {
			final EdifactVerteiler v = verteiler;

			@Override
			public void run() {
				assertTrue(v.getErrors().isEmpty());

			}
		}).subscribe(new Consumer<Segment>() {

			@Override
			public void accept(Segment segment) {
			}

		});
	}

	@Test
	public void testeFehlerhafteDateiEinlesen() throws FileNotFoundException {
		TagProvider tags = new TagProvider();
		tags.getOrCreateEnum("IDK");
		tags.getOrCreateEnum("VDT");
		tags.getOrCreateEnum("FKT");
		tags.getOrCreateEnum("VKG");
		tags.getOrCreateEnum("NAM");
		tags.getOrCreateEnum("ANS");
		tags.getOrCreateEnum("DFU");
		tags.getOrCreateEnum("UEM");
		tags.getOrCreateEnum("ASP");

		EdifactReaderFabric edifactReaderFabric = new EdifactReaderFabric("src/test/resources/wrong.test", tags, 50);
		EdifactVerteiler verteiler = new EdifactVerteiler(tags);
		Flux.fromIterable(edifactReaderFabric).map(verteiler)
				/*
				 * .doOnError(new Consumer<Throwable>() {
				 * 
				 * 
				 * public void accept(Throwable t) { error(); }
				 * 
				 * 
				 * })
				 */
				.doAfterTerminate(new Runnable() {
					final EdifactVerteiler v = verteiler;

					@Override
					public void run() {

						for (EdifactError e : v.getErrors()) {
							System.out.println(e.getMessages());
						}
						assertEquals(3, v.getErrors().size());

					}
				}).subscribe(new Consumer<Segment>() {

					@Override
					public void accept(Segment t) {
					}

				});
	}

	private void error() {
		errorInStream = true;

	}

}
