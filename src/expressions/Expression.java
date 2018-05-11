package expressions;

import statements.Statement;
import types.Type;

public interface Expression extends Statement {
	public Type getType();

}
