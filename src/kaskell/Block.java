package kaskell;

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

/*Remember a block is also a statement!*/
public class Block implements Statement {
	private List<Statement> statements;

	public Block(List<Statement> statements) {
		this.statements = statements;
	}

	public List<Statement> getStatements() {
		return this.statements;
	}

	/* Checks each statement identifier */
	public boolean checkIdentifiers(SymbolTable symbolTable) {
		boolean wellIdentified = true;
		/* There may be a return block with only a return statement... */
		if (statements != null) {
			for (int i = 0; i < statements.size(); i++) {
				/* Recursive case */
				if (statements.get(i) instanceof Block) {
					symbolTable.startBlock();
					wellIdentified = wellIdentified && statements.get(i).checkIdentifiers(symbolTable);
					symbolTable.closeBlock();
				} else {
					wellIdentified = wellIdentified && statements.get(i).checkIdentifiers(symbolTable);
				}
			}
		}
		return wellIdentified;
	}

	/* Checks each statement, taking care of the return block case */
	@Override
	public boolean checkType() {
		boolean wellTyped = true;
		if (statements != null) {
			for (int i = 0; i < statements.size(); i++) {
				wellTyped = wellTyped && statements.get(i).checkType();
			}
		}
		return wellTyped;
	}

	/*
	 * Generates the code of each block, taking into account lonely expressions
	 */
	public void generateCode(Instructions instructions) {
		if (statements != null) {
			for (int i = 0; i < statements.size(); i++) {
				/*
				 * Only generate lonely expressions code if is something like i++ or i-- or
				 * function calls
				 */
				if (statements.get(i) instanceof Expression) {
					if (statements.get(i) instanceof UnaryExpression) {
						if (((UnaryExpression) statements.get(i)).expressionIsIdentifier()
								&& ((UnaryExpression) statements.get(i)).isMinusMinusOrIsPlusPlus()) {
							statements.get(i).generateCode(instructions);
						}
					}
					if (statements.get(i) instanceof Call) {
						statements.get(i).generateCode(instructions);
					}
				} else {
					statements.get(i).generateCode(instructions);
				}
			}
		}
	}

	public int calculateBlockLocalVar() {
		/* Max carries the maximum number of local variables living in a sub block */
		int max = 0, num = 0;
		/* This may happen with a return block with nothing but a single return */
		if (statements == null) {
			return 0;
		}
		/* Checks each statement in the block */
		for (int i = 0; i < statements.size(); i++) {
			/* Checks each kind of statement */
			if (statements.get(i) instanceof Block) { /* Recursive case */
				int maxAux = ((Block) statements.get(i)).calculateBlockLocalVar();
				max = Math.max(max, maxAux);
			} else if (statements.get(i) instanceof Declaration) { /* Just adds the size of the declaration */
				num = num + (((Declaration) statements.get(i)).getSize());
			} else if (statements.get(i) instanceof Mixed) { /* Analogous to the declaration case */
				num = num + (((Mixed) statements.get(i)).getSize());
			} else if (statements.get(i) instanceof For) {
				BasicStatement init = ((For) statements.get(i)).getForTuple().getInitial();
				/*
				 * The for tuple only adds one variable if the initial basic statement is mixed
				 */
				if (init instanceof Mixed) {
					num = num + (((Mixed) init).getSize());
				}
				/* We also check go for recursion in its block */
				num = num + ((For) statements.get(i)).getBody().calculateBlockLocalVar();
			} else if (statements.get(i) instanceof If) {
				/* Go for recursion in its block */
				num = num + ((If) statements.get(i)).getBody().calculateBlockLocalVar();
			} else if (statements.get(i) instanceof IfElse) {
				/* Go for recursion in its blocks */
				num = num + ((IfElse) statements.get(i)).getBody().calculateBlockLocalVar()
						+ ((IfElse) statements.get(i)).getElseBody().calculateBlockLocalVar();
			} else if (statements.get(i) instanceof While) {
				/* Go for recursion in its block */
				num = num + ((While) statements.get(i)).getBody().calculateBlockLocalVar();
			}
		}
		/*
		 * We add the max of the nested blocks, because, we get the max of the size of
		 * the nested blocks of the same level
		 */
		num = num + max;
		return num;
	}

	/* Really long, but easy */
	public int lengthStackExpressions() {
		int max = 0, maxAux = 0;
		if (statements == null) {
			return 0;
		}
		/* Checks each statement of the block */
		for (int i = 0; i < statements.size(); i++) {
			maxAux = 0;
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
				maxAux2 = ((If) statements.get(i)).getBody().lengthStackExpressions();
				maxAux = Math.max(maxAux1, maxAux2);
			} else if (statements.get(i) instanceof IfElse) {
				int maxAux1 = 0, maxAux2 = 0, maxAux3 = 0;
				Expression condition = ((IfElse) statements.get(i)).getCondition();
				maxAux1 = calculateExpSubTree(condition);
				maxAux2 = ((IfElse) statements.get(i)).getBody().lengthStackExpressions();
				maxAux3 = ((IfElse) statements.get(i)).getElseBody().lengthStackExpressions();
				maxAux = Math.max(maxAux1, Math.max(maxAux2, maxAux3));
			} else if (statements.get(i) instanceof While) {
				int maxAux1 = 0, maxAux2 = 0;
				Expression condition = ((While) statements.get(i)).getCondition();
				maxAux1 = calculateExpSubTree(condition);
				maxAux2 = ((While) statements.get(i)).getBody().lengthStackExpressions();
				maxAux = Math.max(maxAux1, maxAux2);
			} else if (statements.get(i) instanceof ArrayAssignment) {
				int maxAux1 = 0, maxAux2 = 0;
				ArrayIdentifier ident = (ArrayIdentifier) ((ArrayAssignment) statements.get(i)).getIdentifier();
				Expression exp = ((ArrayAssignment) statements.get(i)).getExpression();
				maxAux1 = calculateExpSubTree(ident);
				maxAux2 = calculateExpSubTree(exp);
				maxAux = Math.max(maxAux1, maxAux2);
			} else if (statements.get(i) instanceof StructAssignment) {
				int maxAux1 = 0, maxAux2 = 0;
				StructMember ident = (StructMember) ((StructAssignment) statements.get(i)).getMember();
				Expression exp = ((StructAssignment) statements.get(i)).getExpression();
				maxAux1 = calculateExpSubTree(exp);
				for (int j = 0; j < ident.getIdentifiers().size(); j++) {
					if (ident.getIdentifiers().get(j) instanceof ArrayIdentifier) {
						ArrayIdentifier identArr = (ArrayIdentifier) ident.getIdentifiers().get(j);
						maxAux2 = Math.max(maxAux2, calculateExpSubTree(identArr));
					}
				}
				maxAux = Math.max(maxAux1, maxAux2);
			} else if (statements.get(i) instanceof Call) {
				for (int j = 0; j < ((Call) statements.get(i)).getVariables().size(); j++) {
					maxAux = Math.max(maxAux, calculateExpSubTree(((Call) statements.get(i)).getVariables().get(j)));
				}
			} else if (statements.get(i) instanceof Block) {
				maxAux = ((Block) statements.get(i)).lengthStackExpressions();
			}
			max = Math.max(max, maxAux);
		}
		return max;
	}

	/*
	 * Computes the stack length of an expression, it may have been prettier if we
	 * split this in the different expression classes (maybe in the future)
	 */
	private int calculateExpSubTree(Expression exp) {
		int num = 0;
		if (exp instanceof DummyInteger) { /* Simple case */
			num = 1;
		} else if (exp instanceof DummyBoolean) { /* Simple case */
			num = 1;
		} else if (exp instanceof ArrayIdentifier) {
			/* We must take the maximum of each coordinate evaluation expression */
			for (int i = 0; i < ((ArrayIdentifier) exp).getCoordinates().size(); i++) {
				/* Notice this will be at least one */
				num = Math.max(num, calculateExpSubTree(((ArrayIdentifier) exp).getCoordinates().get(i)));
			}
		} else if (exp instanceof StructMember) {
			/* Analogous to the array case, and using it recursively */
			StructMember ident = (StructMember) exp;
			for (int j = 0; j < ident.getIdentifiers().size(); j++) {
				if (ident.getIdentifiers().get(j) instanceof ArrayIdentifier) {
					ArrayIdentifier identArr = (ArrayIdentifier) ident.getIdentifiers().get(j);
					num = Math.max(num, calculateExpSubTree(identArr));
				}
			}

		} else if (exp instanceof Identifier) { /* Simple case */
			num = 1;
		} else if (exp instanceof UnaryExpression) {
			/* Take into account the special cases */
			Expression nextExp = ((UnaryExpression) exp).getExpression();
			num = calculateExpSubTree(nextExp);
			if (((UnaryExpression) exp).isMinusMinusOrIsPlusPlus()) {
				num = num + 1;
			}
		} else if (exp instanceof BinaryExpression) {
			if (((BinaryExpression) exp).isExponentialOrModulus()) {
				/* To do */
			} else {
				/* Take the maximum of both expressions */
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
}
