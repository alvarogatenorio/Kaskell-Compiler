package kaskell;

import types.Type;

/* Just a dummy interface to make things easier with the symbol table */
public interface Definition {
	/* Returns null if it is nonsense */
	public Type getDefinitionType();
	
	public int getAddress();
}
