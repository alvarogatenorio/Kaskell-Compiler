package expressions;

public class Identifier implements Expression {
	private String s;

	public Identifier(String s) {
		this.s = s;
	}
}
