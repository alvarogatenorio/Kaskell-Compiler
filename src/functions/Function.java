package functions;

import java.util.List;

import expressions.ArrayIdentifier;
import expressions.BinaryExpression;
import expressions.Call;
import expressions.DummyBoolean;
import expressions.DummyInteger;
import expressions.Expression;
import expressions.Identifier;
import expressions.StructMember;
import expressions.UnaryExpression;
import kaskell.Block;
import kaskell.Instructions;
import kaskell.ReturnBlock;
import kaskell.SymbolTable;
import statements.ArrayAssignment;
import statements.Assignment;
import statements.BasicStatement;
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

	public int lengthStackExpressions() {
		int max = 0, maxAux = 0;
		List<Statement> statements = tail.getBlock().getStatements();
		/* Checks each statement */
		for (int i = 0; i < statements.size(); i++) {
			maxAux=0;
			if (statements.get(i) instanceof Assignment) {
				
				Expression exp = ((Assignment) statements.get(i)).getExpression();
				maxAux = calculateExpSubTree(exp);
				
			} else if (statements.get(i) instanceof Mixed) {
				
				Expression exp = ((Mixed) statements.get(i)).getExpression();
				maxAux = calculateExpSubTree(exp);
				
			} else if (statements.get(i) instanceof For) {
				
				int maxAux1 = 0, maxAux2 = 0, maxAux3 = 0, maxAux4 = 0;

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

				maxAux4 = ((For) statements.get(i)).getBody().lengthStackExpressions();
				
				maxAux = Math.max(maxAux1, Math.max(maxAux2, Math.max(maxAux3, maxAux4)));
				
			} else if (statements.get(i) instanceof If) {
				
				int maxAux1 = 0, maxAux2 = 0;
				Expression condition = ((If) statements.get(i)).getCondition();
			
				maxAux1 = calculateExpSubTree(condition);
				maxAux2 = ((If)statements.get(i)).getBody().lengthStackExpressions();
				
				maxAux = Math.max(maxAux1, maxAux2);
				
			} else if (statements.get(i) instanceof IfElse) {
				
				int maxAux1 = 0, maxAux2 = 0, maxAux3 = 0;
				Expression condition = ((IfElse) statements.get(i)).getCondition();
				
				maxAux1 = calculateExpSubTree(condition);
				maxAux2 = ((IfElse)statements.get(i)).getBody().lengthStackExpressions();
				maxAux3 = ((IfElse)statements.get(i)).getElseBody().lengthStackExpressions();
				
				maxAux = Math.max(maxAux1, Math.max(maxAux2, maxAux3));
				
			} else if (statements.get(i) instanceof While) {
				
				int maxAux1 = 0, maxAux2 = 0;
				Expression condition = ((While) statements.get(i)).getCondition();

				maxAux1 = calculateExpSubTree(condition);
				maxAux2 = ((While)statements.get(i)).getBody().lengthStackExpressions();
				
				maxAux = Math.max(maxAux1, maxAux2);
				
			} else if (statements.get(i) instanceof ArrayAssignment) {
				int maxAux1=0, maxAux2=0;
				ArrayIdentifier ident = (ArrayIdentifier) ((ArrayAssignment) statements.get(i)).getIdentifier();
				Expression exp = ((ArrayAssignment) statements.get(i)).getExpression();
				
				maxAux1 = calculateExpSubTree(ident);
				maxAux2 = calculateExpSubTree(exp);
				maxAux = Math.max(maxAux1, maxAux2);
				
			} else if (statements.get(i) instanceof StructAssignment) {
				int maxAux1=0, maxAux2=0;
				StructMember ident = (StructMember) ((StructAssignment) statements.get(i)).getMember();
				Expression exp = ((StructAssignment) statements.get(i)).getExpression();
				maxAux1 = calculateExpSubTree(exp);
				
				for(int j=0; j < ident.getIdentifiers().size(); j++) {
					if(ident.getIdentifiers().get(j) instanceof ArrayIdentifier) {
						ArrayIdentifier identArr = (ArrayIdentifier) ident.getIdentifiers().get(j);
						maxAux2 = Math.max(maxAux2,calculateExpSubTree(identArr));
					}
				}
				
				maxAux = Math.max(maxAux1, maxAux2);
				
			} else if (statements.get(i) instanceof Call) {
				
				for(int j=0; j< ((Call) statements.get(i)).getVariables().size(); j++) {
					
					maxAux = Math.max(maxAux,calculateExpSubTree(((Call) statements.get(i)).getVariables().get(j)));
				}
				
			} else if (statements.get(i) instanceof Block) {
				
				maxAux = ((Block) statements.get(i)).lengthStackExpressions();
				
			}
			
			max = Math.max(max, maxAux);
		}

		return max;
	}

	private int calculateExpSubTree(Expression exp) {
		int num = 0;
		if (exp instanceof DummyInteger) {
			num = 1;
		} else if (exp instanceof DummyBoolean) {
			num = 1;
		} else if (exp instanceof ArrayIdentifier){

			for(int i=0; i< ((ArrayIdentifier) exp).getCoordinates().size(); i++) {
				num = Math.max(num,calculateExpSubTree(((ArrayIdentifier) exp).getCoordinates().get(i)));
			}
			
		} else if (exp instanceof StructMember){
			StructMember ident = (StructMember) exp;
			for(int j=0; j < ident.getIdentifiers().size(); j++) {
				if(ident.getIdentifiers().get(j) instanceof ArrayIdentifier) {
					ArrayIdentifier identArr = (ArrayIdentifier) ident.getIdentifiers().get(j);
					num = Math.max(num,calculateExpSubTree(identArr));
				}
			}
			
		}else if (exp instanceof Identifier) {
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
