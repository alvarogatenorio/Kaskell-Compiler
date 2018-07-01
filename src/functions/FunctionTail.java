package functions;

import java.util.HashMap;
import java.util.List;

import expressions.Identifier;
import kaskell.Block;
import kaskell.Definition;
import kaskell.SymbolTable;
import types.Type;

public class FunctionTail implements Definition {
	private List<Identifier> variables;
	private List<Type> arguments;
	private Block body;
	private Type type;
	private int address;
	private HashMap<String, Type> parameterMap;
	private HashMap<String, Integer> parameterAddressMap;

	public FunctionTail(List<Identifier> variables, Block body) {
		this.variables = variables;
		this.body = body;
		this.arguments = null;
		this.type = null;
		this.address = -1;
		this.parameterMap = new HashMap<String, Type>();
		this.parameterAddressMap = new HashMap<String, Integer>();
	}

	public boolean checkIdentifiers(SymbolTable symbolTable) {
		boolean wellIdentified = true;
		/* Insert the new identifiers */
		if (variables != null) {
			for (int i = 0; i < variables.size(); i++) {
				wellIdentified = wellIdentified && symbolTable.insertIdentifier(variables.get(i), this);
				variables.get(i).setAddress(5 + i);
				this.parameterAddressMap.put(variables.get(i).toString(), variables.get(i).getAddress());
			}
		}
		/* Setting types and filling the map */
		if (arguments != null && variables != null) {
			if (variables.size() == arguments.size()) {
				for (int i = 0; i < variables.size(); i++) {
					variables.get(i).setType(arguments.get(i));
					parameterMap.put(variables.get(i).toString(), arguments.get(i));
				}
			}
		}
		/* Check the block itself (it could be a return block, no problem) */
		if (wellIdentified) {
			body.setInsideFunction(this);
			wellIdentified = body.checkIdentifiers(symbolTable);
		}
		return wellIdentified;
	}

	public List<Identifier> getVariables() {
		return variables;
	}

	public Block getBlock() {
		return body;
	}

	public void setType(Type type) {
		this.type = type;
	}

	@Override
	public Type getDefinitionType() {
		return type;
	}

	public void setArguments(List<Type> arguments) {
		this.arguments = arguments;
	}

	public List<Type> getArguments() {
		return this.arguments;
	}

	/* Used when generating the code of the function */
	public void setAddress(int a) {
		this.address = a;
	}

	/* Used when generating the code of the call */
	public int getAddress() {
		return this.address;
	}

	public Type searchParameterType(String id) {
		return this.parameterMap.get(id);
	}

	public int searchParameterAddress(String id) {
		return this.parameterAddressMap.get(id);
	}
}
