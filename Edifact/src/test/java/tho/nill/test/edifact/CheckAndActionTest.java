package tho.nill.test.edifact;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Optional;
import java.util.function.Predicate;

import org.junit.Test;

import tho.nill.edifact.ReadWithBuffer;
import tho.nill.edifact.Segment;
import tho.nill.edifact.TagProvider;
import tho.nill.edifact.UNA;
import tho.nill.edifact.check.CheckVerteiler;
import tho.nill.edifact.check.EdifactError;
import tho.nill.edifact.check.PartSetter;
import tho.nill.edifact.check.TagAction;
import tho.nill.edifact.check.builder.Between;
import tho.nill.edifact.check.builder.Blank;
import tho.nill.edifact.check.builder.CheckBuilder;
import tho.nill.edifact.check.builder.Min;
import tho.nill.edifact.check.builder.NotBlank;
import tho.nill.edifact.check.builder.Numeric;
import tho.nill.edifact.check.builder.Regexp;

public class CheckAndActionTest {

	@Test
	public void testAction() {

		TestZelt h = new TestZelt();
		PartSetter s = new PartSetter(h::setX, 1, 0);
		TagAction a = new TagAction(x -> "UNH".equals(x.name()), h::create);

		
		Segment seg = createSegment("UNH+123+456+789'");
		s.perform(seg);
		a.perform(seg);
		
		assertEquals("456", h.getX());
		assertEquals("create", h.getLastCommand());
	}
	
	@Test
	public void testCheck() {

		CheckVerteiler verteiler = new CheckVerteiler(new TagProvider());
		CheckBuilder builder = verteiler.build();
		builder.forTag("UNH").atGroup(0).between(0, 1).elseError("test");
		builder.forTag("UNZ").atGroup(0).match("12").elseError("test");
		builder.forTag("UNZ").atGroup(0).blank().elseError("test");
		builder.forTag("UNZ").atGroup(0).notBlank().elseError("test");
		builder.forTag("UNZ").atGroup(0).numeric().elseError("test");
		builder.forTag("UNZ").atGroup(0).integer().elseError("test");
		builder.forTag("UNZ").atGroup(0).max(4).elseError("test");
		builder.forTag("UNZ").atGroup(0).min(4).elseError("test");
		builder.forTag("UNZ").atGroup(0).length(4).elseError("test");
		builder.forTag("UNZ").atGroup(0).withPredicate(x -> "2".equals(x)).elseError("test");


		
		Segment seg1 = createSegment("UNH+1+456+789'");
	    Segment seg2 = createSegment("UNH+123+456+789'");
		verteiler.apply(seg1);
		verteiler.apply(seg2);
	
		
		assertEquals(1, verteiler.getErrors().size());
		EdifactError e = verteiler.getErrors().get(0);
		assertEquals(2,e.getSegmentPosition());
	}
	
	@Test
	public void testePredicate() {
		Predicate<String> p = new Regexp("^[0-9]*$");
		checkPredicate(p,true,"","123");
		checkPredicate(p,false,"e","1e23");
		p = new Between(0, 3);
		checkPredicate(p,true,"","w","ae","123");
		checkPredicate(p,false,"rrrrr","rwrrr","rrrrrae","rrrrrrrr123");
		p = new Min(3);
		checkPredicate(p,true,"tttttt","ttttw","aert","123");
		checkPredicate(p,false,"","0","22");
		p = new Blank();
		checkPredicate(p,true,"","  ");
		checkPredicate(p,false,"333","0","22");
		p = new NotBlank();
		checkPredicate(p,true,"333","0","22");
		checkPredicate(p,false,"","  ");
		p = new Numeric();
		checkPredicate(p,true,"","333","0","22","123,34","123,345");
		checkPredicate(p,false,"e","  ","rr","123-34","123,");
		p = new tho.nill.edifact.check.builder.Integer();
		checkPredicate(p,true,"","333","0","22");
		checkPredicate(p,false,"e","  ","rr","123,34");
		
	}
	public void checkPredicate(Predicate<String> p,boolean result,String... texte) {
		for(String text : texte) {
			assertEquals(result, p.test(text));
		}
	}
	
	private Segment createSegment(String text)  {
		ByteArrayInputStream in = new ByteArrayInputStream(text.getBytes());
		ReadWithBuffer reader = new ReadWithBuffer(new InputStreamReader(in), 100);
		UNA una = new UNA();
		StringBuilder buffer = new StringBuilder();
		TagProvider tags = new TagProvider();
		return una.createSegment(buffer, reader, tags).get();
	}
// ßdef( ................ )
// ßsave( .................. )
// ßref(  << ................. )
// ßtangle( ............ )
// ßwave( ............... )	
}

