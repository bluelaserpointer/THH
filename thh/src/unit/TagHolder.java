package unit;

import java.util.ArrayList;

import core.GHQ;

public abstract class TagHolder {
	public abstract ArrayList<Tag> getTags();
}

class Tag{
	public String getName() {
		return GHQ.NOT_NAMED;
	}
}
class PassiveTag extends Tag{
	
}