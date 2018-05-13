package expressions;

import statements.BasicStatement;
import types.Type;

public interface Expression extends BasicStatement {
	public Type getType();

	public int getRow();

	public int getColumn();
}
