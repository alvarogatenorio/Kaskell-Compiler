package statements;

import expressions.Expression;
import kaskell.Block;
import kaskell.Instructions;
import kaskell.SymbolTable;
import types.Type;
import types.Types;

public class While extends ComplexStatement {
	private Expression condition;

	public While(Expression condition, Block body) {
		this.condition = condition;
		this.body = body;
	}

	/* Checks if the condition is well typed and boolean, then checks the body */
	@Override
	public boolean checkType() {

		if ((condition.getType() == null) || (!condition.getType().equals(new Type(Types.BOOLEAN)))) {
			System.err.println("TYPE ERROR: in line " + (this.condition.getRow() + 1) + " column "
					+ (this.condition.getColumn() + 1) + " fatal error the condition is not Koolean type!");
			return false;
		}

		return condition.checkType() && body.checkType();
	}

	/* Just checks the condition and the body block */
	@Override
	public boolean checkIdentifiers(SymbolTable symbolTable) {
		boolean wellIdentified = condition.checkIdentifiers(symbolTable);
		if (wellIdentified) {
			symbolTable.startBlock();
			wellIdentified = body.checkIdentifiers(symbolTable);
			symbolTable.closeBlock();
		}
		return wellIdentified;
	}

	@Override
	public void generateCode(Instructions instructions) {
		instructions.addComment("{ While }\n");
		int jumpTo1 = instructions.getCounter() + 1;
		condition.generateCode(instructions);
		int jumpFrom2 = instructions.size();
		instructions.add("");
		body.generateCode(instructions);
		int jumpFrom1 = instructions.size();
		instructions.add("");
		int jumpTo2 = instructions.getCounter() + 1;
		instructions.addComment("{ End of while }\n");
		instructions.set(jumpFrom1, instructions.get(jumpFrom1) + "ujp " + jumpTo1 + ";\n");
		instructions.set(jumpFrom2, instructions.get(jumpFrom2) + "fjp " + jumpTo2 + ";\n");
	}
}
