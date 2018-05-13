package expressions;

import java.util.List;

import kaskell.SymbolTable;
import types.Type;

public class Call implements Expression {
	private Identifier identifier;
	private List<Expression> variables;
	private List<Type> arguments;
	private Type type;

	public Call(Identifier identifier, List<Expression> arguments) {
		this.identifier = identifier;
		this.variables = arguments;
		this.type = null;
		this.arguments = null;
	}

	@Override
	public boolean checkType() {
		if (variables.size() == arguments.size()) {
			for (int i = 0; i < variables.size(); i++) {
				if (variables.get(i).checkType() && !(variables.get(i).getType().equals(arguments.get(i)))) {
					int num=i+1;
					System.err.println("TYPE ERROR: in line " + (this.getRow() + 1) + " column "
							+ (this.getColumn() + 1)
							+ " the variable type number"+ num + "does not match with the argument type number"+ num +"!");
					return false;
				}
			}
			return true;
		}
		else {
			System.err.println("TYPE ERROR: in line " + (this.getRow() + 1) + " column "
					+ (this.getColumn() + 1)
					+ " the size of the call variables and the arguments doesn't match!");
			return false;
		}
	}

	/*
	 * Checks if the function identifier is defined, and also checks if the list of
	 * expressions (arguments) is well identified
	 */
	@Override
	public boolean checkIdentifiers(SymbolTable symbolTable) {
		boolean wellIdentified = symbolTable.searchFunctionIdentifier(identifier);
		if (variables != null) {
			for (int i = 0; i < variables.size(); i++) {
				wellIdentified = wellIdentified && variables.get(i).checkIdentifiers(symbolTable);
			}
		}
		if (wellIdentified && (type == null) && (arguments == null)) {
			type = symbolTable.searchCallType(identifier);
			arguments = symbolTable.searchCallArguments(identifier);
		}
		return wellIdentified;
	}

	@Override
	public Type getType() {
		return type;
	}

	@Override
	public int getRow() {
		return this.identifier.getRow();
	}

	@Override
	public int getColumn() {
		return this.identifier.getColumn();
	}
}
