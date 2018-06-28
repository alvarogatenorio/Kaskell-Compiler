package statements;

import expressions.Expression;
import kaskell.Block;
import kaskell.Instructions;
import kaskell.SymbolTable;

public class IfElse extends If {
	private Block elseBody;

	public IfElse(Expression condition, Block thenBody, Block elseBody) {
		super(condition, thenBody);
		this.elseBody = elseBody;
	}

	/* Checks the if-then part and the else block */
	public boolean checkType() {
		return super.checkType() && this.elseBody.checkType();
	}

	/* Checks the conditions, the "then" block and the "else" block */
	public boolean checkIdentifiers(SymbolTable symbolTable) {
		boolean wellIdentified = super.checkIdentifiers(symbolTable);
		if (wellIdentified) {
			symbolTable.startBlock();
			wellIdentified = elseBody.checkIdentifiers(symbolTable);
			symbolTable.closeBlock();
		}
		return wellIdentified;
	}

	public void generateCode(Instructions instructions) {
		instructions.addComment("{ Two-branch if }\n");
		this.condition.generateCode(instructions);
		/* Dummy entry to be filled with label information */
		int jumpFrom1 = instructions.size();
		instructions.add("");
		this.body.generateCode(instructions);
		/* Dummy entry to be filled with label information */
		int jumpFrom2 = instructions.size();
		instructions.add("");
		int jumpTo1 = instructions.getCounter() + 1;
		this.elseBody.generateCode(instructions);
		int jumpTo2 = instructions.getCounter() + 1;
		instructions.addComment("{ End of two-branch if }\n");
		instructions.set(jumpFrom1, instructions.get(jumpFrom1) + "fjp " + jumpTo1 + ";\n");
		instructions.set(jumpFrom2, instructions.get(jumpFrom2) + "ujp " + jumpTo2 + ";\n");
	}

}
