package statements;

import expressions.Expression;
import expressions.Identifier;
import functions.FunctionTail;
import kaskell.Definition;
import kaskell.Instructions;
import kaskell.SymbolTable;
import types.Type;

public class Mixed implements BasicStatement, Definition {

	/*
	 * This class structure is simpler than the Declaration one, because we don't
	 * allow to declare and initialize StrucTypes or arrays of any type, primitive
	 * types
	 */

	private Type type;
	private Expression expression;
	private Identifier identifier;
	private int address;
	private boolean insideFunction;
	private FunctionTail functionInside;

	public Mixed(Type type, Identifier identifier, Expression expression) {
		this.type = type;
		this.identifier = identifier;
		this.expression = expression;
		this.address = -1;
	}

	/* Just checks the type */
	@Override
	public boolean checkType() {
		/* Checks the expression typed and type */
		if (!expression.checkType()) {
			return false;
		}
		/*
		 * Getting the actual type of the expression, remember identifiers are an
		 * special case, suppose "a" is the identifier of an array, the "brute" type
		 * will be an array, but maybe the actual expression is a[5]...
		 */
		Type expressionType;
		if (expression instanceof Identifier) {
			expressionType = ((Identifier) expression).getSimpleType();
		} else {
			expressionType = expression.getType();
		}
		if (!expressionType.equals(type)) {
			System.err.println("TYPE ERROR: in line " + (this.expression.getRow() + 1) + " column "
					+ (this.expression.getColumn() + 1) + " fatal error the expression typed wrong for this place!");
			return false;
		}
		return true;
	}

	/*
	 * A mixed statement is a definition, so it tries to insert in the symbol table,
	 * but first checks if the expression is well identified (that's why the order
	 * is important)
	 */
	@Override
	public boolean checkIdentifiers(SymbolTable symbolTable) {
		/* The order is important! */
		boolean wellIdentified = expression.checkIdentifiers(symbolTable)
				&& symbolTable.insertIdentifier(identifier, this);
		/*
		 * This is made to assign type and address to the identifier, which is important
		 * in code generation
		 */
		if (wellIdentified) {
			if (insideFunction) {
				this.identifier.setInsideFunction(true);
				this.identifier.setFunctionInside(functionInside);
			} else {
				this.address = 5 + symbolTable.getAccumulation() - this.getSize();
			}
			identifier.checkIdentifiers(symbolTable);
		}
		return wellIdentified;
	}

	/* Just returns the type */
	@Override
	public Type getDefinitionType() {
		return this.type;
	}

	public int getAddress() {
		return this.address;
	}

	public int getSize() {
		return type.getSize();
	}

	public Expression getExpression() {
		return this.expression;
	}

	@Override
	public void generateCode(Instructions instructions) {
		/*
		 * As declarations don't generate code, it generates the code of the assignment
		 */
		Assignment aux = new Assignment(identifier, expression);
		aux.generateCode(instructions);
	}

	public void setInsideFunction(boolean insideFunction) {
		this.insideFunction = insideFunction;
	}

	public void setFunctionInside(FunctionTail functionInside) {
		this.functionInside = functionInside;
	}
}
