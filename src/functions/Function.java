package functions;

import java.util.List;

import expressions.BinaryExpression;
import expressions.DummyBoolean;
import expressions.DummyInteger;
import expressions.Expression;
import expressions.Identifier;
import expressions.UnaryExpression;
import kaskell.Block;
import kaskell.Instructions;
import kaskell.ReturnBlock;
import kaskell.SymbolTable;
import statements.ArrayAssignment;
import statements.Assignment;
import statements.BasicStatement;
import statements.Declaration;
import statements.For;
import statements.If;
import statements.IfElse;
import statements.Mixed;
import statements.Statement;
import statements.StructAssignment;
import statements.While;
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
		int lengthPExp = lengthStackExpressions();
		instructions.addComment("{ Function code }\n");
		instructions.add("ssp " + numMarc + ";\n");
		this.address = instructions.size();
		this.tail.setAddress(this.address);
		instructions.add("sep " + lengthPExp + ";\n");
		int jumpFrom = instructions.size();
		instructions.add("");
		int jumpTo = instructions.getCounter() + 1;
		instructions.set(jumpFrom, instructions.get(jumpFrom) + "ujp " + jumpTo + ";\n");
		tail.getBlock().generateCode(instructions);
		if (head.getReturnType() != null) {
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
		localVar = calculateBlockLocalVar(block);

		/*
		 * We add 5 that correspond to the 5 values of the organizations cells of the
		 * data area
		 */
		return 5 + parameters + localVar;
	}

	private int calculateBlockLocalVar(Block block) {
		int max = 0, maxAux = 0, num = 0;
		List<Statement> statements = block.getStatements();

		for (int i = 0; i < statements.size(); i++) {
			if (statements.get(i) instanceof Block) {
				maxAux = calculateBlockLocalVar((Block) statements.get(i));
				if (maxAux > max) {
					max = maxAux;
				}
			} else if (statements.get(i) instanceof Declaration) {
				num = num + (((Declaration) statements.get(i)).getSize());
			} else if (statements.get(i) instanceof Mixed) {
				num = num + (((Mixed) statements.get(i)).getSize());
			} else if (statements.get(i) instanceof For) {
				BasicStatement init = ((For) statements.get(i)).getForTuple().getInitial();
				if (init instanceof Mixed) {
					num = num + (((Mixed) init).getSize());
				}
			}
		}
		/*
		 * We add the max of the nested blocks, because, we get the max of the size of
		 * the nested blocks of the same level
		 */
		num = num + max;
		return num;
	}

	private int lengthStackExpressions() {
		int max = 0, maxAux = 0;
		List<Statement> statements = tail.getBlock().getStatements();

		for (int i = 0; i < statements.size(); i++) {
			if (statements.get(i) instanceof Assignment) {
				Expression exp = ((Assignment) statements.get(i)).getExpression();
				maxAux = calculateExpSubTree(exp);
			} else if (statements.get(i) instanceof Mixed) {
				Expression exp = ((Mixed) statements.get(i)).getExpression();
				maxAux = calculateExpSubTree(exp);
			} else if (statements.get(i) instanceof For) {
				int maxAux1 = 0, maxAux2 = 0, maxAux3 = 0;

				/* first component of the for tuple */
				BasicStatement init = ((For) statements.get(i)).getForTuple().getInitial();
				if (init instanceof Assignment) {
					Expression exp = ((Assignment) init).getExpression();
					maxAux1 = calculateExpSubTree(exp);
				} else if (init instanceof Mixed) {
					Expression exp = ((Mixed) init).getExpression();
					maxAux1 = calculateExpSubTree(exp);
				}

				/* second component of the for tuple */
				Expression condition = (Expression) ((For) statements.get(i)).getForTuple().getCondition();
				maxAux2 = calculateExpSubTree(condition);

				/* third component of the for tuple */
				BasicStatement loopEpilogue = ((For) statements.get(i)).getForTuple().getLoopEpilogue();
				if (loopEpilogue instanceof Assignment) {
					Expression exp = ((Assignment) loopEpilogue).getExpression();
					maxAux3 = calculateExpSubTree(exp);
				} else if (loopEpilogue instanceof Expression) {
					maxAux3 = calculateExpSubTree((Expression) loopEpilogue);
				}

				maxAux = Math.max(maxAux1, Math.max(maxAux2, maxAux3));
			} else if (statements.get(i) instanceof If) {
				Expression condition = ((If) statements.get(i)).getCondition();
				maxAux = calculateExpSubTree(condition);
			} else if (statements.get(i) instanceof IfElse) {
				Expression condition = ((IfElse) statements.get(i)).getCondition();
				maxAux = calculateExpSubTree(condition);
			} else if (statements.get(i) instanceof While) {
				Expression condition = ((While) statements.get(i)).getCondition();
				maxAux = calculateExpSubTree(condition);
			} else if (statements.get(i) instanceof ArrayAssignment) {
				Expression exp = ((ArrayAssignment) statements.get(i)).getExpression();
				maxAux = calculateExpSubTree(exp);
			} else if (statements.get(i) instanceof StructAssignment) {
				Expression exp = ((StructAssignment) statements.get(i)).getExpression();
				maxAux = calculateExpSubTree(exp);
			}
			Math.max(max, maxAux);
		}

		return max;
	}

	private int calculateExpSubTree(Expression exp) {
		int num = 0;
		if (exp instanceof DummyInteger) {
			num = 1;
		} else if (exp instanceof DummyBoolean) {
			num = 1;
		} else if (exp instanceof Identifier) {
			num = 1;
		} else if (exp instanceof UnaryExpression) {
			Expression nextExp = ((UnaryExpression) exp).getExpression();
			num = calculateExpSubTree(nextExp);
			if (((UnaryExpression) exp).isMinusMinusOrIsPlusPlus()) {
				num = num + 1;
			}
		} else if (exp instanceof BinaryExpression) {
			if (((BinaryExpression) exp).isExponentialOrModulus()) {
				/* To do */
			} else {
				Expression leftExp = ((BinaryExpression) exp).getLeftExpression();
				Expression rightExp = ((BinaryExpression) exp).getRightExpression();
				int leftNum, rightNum;
				leftNum = calculateExpSubTree(leftExp);
				rightNum = calculateExpSubTree(rightExp);
				num = Math.max(leftNum, rightNum) + 1;
			}
		}
		return num;
	}

	public int getAddress() {
		return this.address;
	}
}
