package kaskell;

import java.util.List;

import expressions.BinaryExpression;
import expressions.Call;
import expressions.DummyBoolean;
import expressions.DummyInteger;
import expressions.Expression;
import expressions.Identifier;
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

	public int calculateBlockLocalVar() {
		int max = 0, maxAux = 0, num = 0;

		for (int i = 0; i < statements.size(); i++) {
			if (statements.get(i) instanceof Block) {
				maxAux = ((Block) statements.get(i)).calculateBlockLocalVar();
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

	public int lengthStackExpressions() {
		int max = 0, maxAux = 0;
		/* Checks each statement */
		for (int i = 0; i < statements.size(); i++) {
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
				maxAux = calculateExpSubTree(condition);
			} else if (statements.get(i) instanceof IfElse) {
				int maxAux1 = 0, maxAux2 = 0, maxAux3 = 0;
				Expression condition = ((IfElse) statements.get(i)).getCondition();
				maxAux = calculateExpSubTree(condition);
			} else if (statements.get(i) instanceof While) {
				int maxAux1 = 0, maxAux2 = 0;
				Expression condition = ((While) statements.get(i)).getCondition();
				maxAux = calculateExpSubTree(condition);
			} else if (statements.get(i) instanceof ArrayAssignment) {
				Expression exp = ((ArrayAssignment) statements.get(i)).getExpression();
				maxAux = calculateExpSubTree(exp);
			} else if (statements.get(i) instanceof StructAssignment) {
				Expression exp = ((StructAssignment) statements.get(i)).getExpression();
				maxAux = calculateExpSubTree(exp);
			} else if (statements.get(i) instanceof Call) {
				
			} else if (statements.get(i) instanceof Block) {
				
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
}
