package functions;

import java.util.List;

import expressions.Expression;
import expressions.Identifier;
import kaskell.Block;
import kaskell.Instructions;
import kaskell.ReturnBlock;
import kaskell.SymbolTable;
import types.Type;

public class Function {
	private FunctionHead head;
	private FunctionTail tail;
	private int address;

	public Function(FunctionHead head, FunctionTail tail) {
		this.head = head;
		this.tail = tail;
		this.tail.setType(head.getReturnType());
		this.tail.setArguments(head.getArguments());
		this.address = 0;
	}

	public boolean checkType() {
		/* First get the variables (identifiers) and arguments (types) */
		List<Type> arguments = head.getArguments();
		List<Identifier> variables = tail.getVariables();

		/* When we have a "normal" function */
		if (arguments != null && variables != null) {
			/* If arguments and variables does not match */
			if (arguments.size() != variables.size()) {
				System.err.println("TYPE ERROR: in line " + (head.getIdentifier().getRow() + 1) + " column "
						+ (head.getIdentifier().getColumn() + 1)
						+ " the number of arguments and identifiers is not the same!");
				return false;
			}
			/* When arguments and variables does not match in other ways */
		} else if ((arguments == null && variables != null) || (arguments != null && variables == null)) {
			System.err.println("TYPE ERROR: in line " + (head.getIdentifier().getRow() + 1) + " column "
					+ (head.getIdentifier().getColumn() + 1)
					+ " the number of arguments and identifiers is not the same!");
			return false;
		}
		/*
		 * When there are no arguments and no variables, there is nothing to check until
		 * here
		 */

		/* Get the return type and the block */
		Type returnType = head.getReturnType();
		Block returnBlock = tail.getBlock();

		/* If the body is wrongly typed */
		if (!returnBlock.checkType()) {
			return false;
		}

		boolean isReturnBlock = returnBlock instanceof ReturnBlock;
		/* If we have an out type */
		if (returnType != null) {
			/* If we don't have return statement */
			if (!isReturnBlock) {
				System.err.println("TYPE ERROR: in line " + (head.getIdentifier().getRow() + 1) + " column "
						+ (head.getIdentifier().getColumn() + 1) + " there is no return statement!");
				return false;
			} else {
				/*
				 * When the return statement is wrongly typed or does not match with the out
				 * type
				 */
				Expression returnExpression = ((ReturnBlock) returnBlock).getReturnExpression();
				if (!returnExpression.checkType() || (!returnExpression.getType().equals(returnType))) {
					System.err.println("TYPE ERROR: in line " + (head.getIdentifier().getRow() + 1) + " column "
							+ (head.getIdentifier().getColumn() + 1)
							+ " the return statement is wrongly typed or does not match with the out type!");
					return false;
				}
			}
			/* I we don't have an out type but a return block */
		} else {
			if (isReturnBlock) {
				System.err.println("TYPE ERROR: in line " + (head.getIdentifier().getRow() + 1) + " column "
						+ (head.getIdentifier().getColumn() + 1)
						+ " we don't have an out type but we have a return statement!");
				return false;
			}
		}
		return true;
	}

	/*
	 * Just checks the tail, the function identifier was inserted earlier in the
	 * program. All the constants must be computed here
	 */
	public boolean checkIdentifiers(SymbolTable symbolTable) {
		symbolTable.startBlock();
		/* Variables are considered to be in the same scope as the body */
		boolean wellIdentified = head.checkIdentifiers(symbolTable) && tail.checkIdentifiers(symbolTable);
		symbolTable.closeBlock();
		return wellIdentified;
	}

	public Identifier getIdentifier() {
		return this.head.getIdentifier();
	}

	public FunctionTail getTail() {
		return this.tail;
	}

	public void generateCode(Instructions instructions) {
		int numMarc = staticMarc();
		int lengthPExp = tail.getBlock().lengthStackExpressions();
		instructions.addComment("{ Function code }\n");
		instructions.add("ssp " + numMarc + ";\n");
		this.address = instructions.getCounter();
		this.tail.setAddress(this.address);
		instructions.add("sep " + lengthPExp + ";\n");
		int jumpFrom = instructions.size();
		instructions.add("");
		int jumpTo = instructions.getCounter() + 1;
		instructions.set(jumpFrom, instructions.get(jumpFrom) + "ujp " + jumpTo + ";\n");
		tail.getBlock().generateCode(instructions);
		if (head.getReturnType() != null) {
			instructions.add("lda 0 0;\n");
			instructions.add("retf;\n");
		} else {
			instructions.add("retp;\n");
		}
		instructions.addComment("{ End funtion code }\n");
	}

	private int staticMarc() {
		int parameters = 0, localVar = 0;
		Block block = tail.getBlock();
		/*
		 * Arguments, as the complex type (StructType and arrays) are passed by
		 * reference, their size is 1, and the others are integers or booleans, so their
		 * size is also 1
		 */
		parameters = head.getArguments().size();

		/*
		 * For the local variables, we need to go through all the block recursively,
		 * looking for the declarations, or the mixed ones
		 */
		localVar = block.calculateBlockLocalVar();

		/*
		 * We add 5 that correspond to the 5 values of the organizations cells of the
		 * data area
		 */
		return 5 + parameters + localVar;
	}

	public int getAddress() {
		return this.address;
	}
}
