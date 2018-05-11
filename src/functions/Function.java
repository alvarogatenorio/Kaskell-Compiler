package functions;

import java.util.List;

import expressions.Expression;
import expressions.Identifier;
import kaskell.Block;
import kaskell.ReturnBlock;
import kaskell.SymbolTable;
import types.Type;

public class Function {
	private FunctionHead head;
	private FunctionTail tail;

	public Function(FunctionHead head, FunctionTail tail) {
		this.head = head;
		this.tail = tail;
	}

	public boolean checkType() {
		List<Type> arguments = head.getArguments();
		List<Identifier> variables = tail.getVariables();
		/* When we have a "normal function" */
		if (arguments != null && variables != null) {
			if (arguments.size() == variables.size()) {
				for (int i = 0; i < arguments.size(); i++) {
					/* Setting the types of the variables */
					variables.get(i).setType(arguments.get(i));
				}
				/* If arguments and variables does not match */
			} else {
				System.err.println("TYPE ERROR: in line " + head.getIdentifier().getRow() + " column "
						+ head.getIdentifier().getColumn()
						+ " the number of arguments and identifiers is not the same!");
				return false;
			}
			/* When arguments and variables does not match in other ways */
		} else if ((arguments == null && variables != null) || (arguments != null && variables == null)) {
			System.err.println("TYPE ERROR: in line " + head.getIdentifier().getRow() + " column "
					+ head.getIdentifier().getColumn() + " the number of arguments and identifiers is not the same!");
			return false;
		}
		/*
		 * When there are no arguments and no variables, there is nothing to check until
		 * here
		 */
		Type returnType = head.getReturnType();
		Block returnBlock = tail.getBlock();
		boolean isReturnBlock = returnBlock instanceof ReturnBlock;
		/* If we have an out type */
		if (returnType != null) {
			/* If we don't have return statement */
			if (!isReturnBlock) {
				System.err.println("TYPE ERROR: in line " + head.getIdentifier().getRow() + " column "
						+ head.getIdentifier().getColumn() + " there is no return statement!");
				return false;
			} else {
				/*
				 * When the return statement is wrongly typed or does not match with the out
				 * type
				 */
				Expression returnExpression = ((ReturnBlock) returnBlock).getReturnExpression();
				if (!returnExpression.checkType() || (returnExpression.getType() != returnType)) {
					System.err.println("TYPE ERROR: in line " + head.getIdentifier().getRow() + " column "
							+ head.getIdentifier().getColumn()
							+ " the return statement is wrongly typed or does not match with the out type!");
					return false;
				}
			}
			/* I we don't have an out type but a return block */
		} else {
			if (isReturnBlock) {
				System.err.println("TYPE ERROR: in line " + head.getIdentifier().getRow() + " column "
						+ head.getIdentifier().getColumn()
						+ " we don't have an out type but we have a return statement!");
				return false;
			}
		}
		/* Finally if the body is wrongly typed */
		if (!returnBlock.checkType()) {
			return false;
		}
		return true;
	}

	public boolean checkIdentifiers(SymbolTable symbolTable) {
		/* The tail is the only part with identifiers!! */
		symbolTable.startBlock();
		boolean wellIdentified = tail.checkIdentifiers(symbolTable);
		symbolTable.closeBlock();
		return wellIdentified;
	}

	public Identifier getIdentifier() {
		return this.head.getIdentifier();
	}
}
