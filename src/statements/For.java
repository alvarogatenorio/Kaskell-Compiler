package statements;

import java.util.List;

import expressions.Expression;
import kaskell.Block;
import kaskell.Instructions;
import kaskell.SymbolTable;

public class For extends ComplexStatement {
	private ForTuple conditions;

	public For(ForTuple conditions, Block body) {
		this.conditions = conditions;
		this.body = body;
	}

	/* Just checks conditions and body */
	@Override
	public boolean checkType() {
		return this.conditions.checkType() && this.body.checkType();
	}

	/* Checks the conditions and the body */
	@Override
	public boolean checkIdentifiers(SymbolTable symbolTable) {
		boolean wellIdentified = conditions.checkIdentifiers(symbolTable);
		if (wellIdentified) {
			symbolTable.startBlock();
			wellIdentified = body.checkIdentifiers(symbolTable);
			symbolTable.closeBlock();
		}
		return wellIdentified;
	}

	@Override
	public void generateCode(Instructions instructions) {
		instructions.addComment("{ For loop }\n");
		conditions.getInitial().generateCode(instructions);
		List<Statement> aux = this.body.getStatements();
		aux.add(conditions.getLoopEpilogue());
		Block auxBody = new Block(aux);
		While auxLoop = new While((Expression) conditions.getCondition(), auxBody);
		auxLoop.generateCode(instructions);
		instructions.addComment("{ End for loop }\n");
	}

}
