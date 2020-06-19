package tho.nill.edifact.check;

import java.util.List;

import lombok.Data;
import tho.nill.edifact.Segment;

@Data
public class EdifactError {
	private Segment segment;
	private List<String> messages;
	private int segmentPosition;
	
	
	public EdifactError(Segment segment, List<String> messages, int segmentPosition) {
		super();
		this.segment = segment;
		this.messages = messages;
		this.segmentPosition = segmentPosition;
	}

}
