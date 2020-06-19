package tho.nill.edifact;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import lombok.NonNull;

public class TagProvider {
	private Hashtable<String,LikeAnEnum> enumsAsHash;
	private List<LikeAnEnum> enumsAsList;
	private int nextOrdinal;

	public TagProvider() {
		super();
		enumsAsHash = new Hashtable<>();
		enumsAsList = new ArrayList<>();
		
		for (Edifact v : Edifact.values()) { // include standard tags
			put(v);
		}
	}

	public LikeAnEnum getEnum(@NonNull String name) {
		return enumsAsHash.get(name);
	}
	
	public LikeAnEnum getEnum(int ordinal) {
		assert ordinal >= 0;
		return enumsAsList.get(ordinal);
	}
	
	public LikeAnEnum getOrCreateEnum(@NonNull String name) {
		LikeAnEnum e = getEnum(name);
		if (e==null) {
			e = create(name);
		}
		return e;
	}

	private LikeAnEnum put( LikeAnEnum tag) {
		enumsAsHash.put(tag.name(),tag);
		if (nextOrdinal<=tag.ordinal()) {
			nextOrdinal = tag.ordinal()+1;
		}
		if (tag.ordinal() != enumsAsList.size()) {
			throw new EdifactException("AlmostEnum not in order");
		};
		enumsAsList.add(tag);
		return tag;
	}
	

	private synchronized LikeAnEnum create(String name) {
		return put(new AlmostEnum(nextOrdinal,name));
	}

	public int size() {
		return nextOrdinal;
	}

}