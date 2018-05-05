package kaskell;
import java.util.HashMap;
import java_cup.runtime.Symbol;

%%
/*-----------------------------------------------
  -----OPTIONS AND DECLARATIONS------------------,
  jflex directives and java code to be used later
  -----------------------------------------------*/

/*Setting the character set*/
%unicode

/*Setting the name of the java output class*/
%class Lexer

/*Customizing the cup generated class names*/
%cupsym sym

/*Making the lexer compatible with cup*/
%cup

/*Counting parameters enabled*/
%line
%column
%char

/*-----Code to be copied into the java output class-----*/
%{
    private Symbol symbol(int type) {
        return new Symbol(type, yyline, yycolumn);
    }
    private Symbol symbol(int type, Object value) {
        return new Symbol(type, yyline, yycolumn, value);
    }
%}

/*Map for keywords*/
%{
    private HashMap<String, Integer> keywords = new HashMap<String, Integer>();
%}

%init{
    keywords.put("kif", sym.IF);
    keywords.put("kelse", sym.ELSE);
    keywords.put("khile", sym.WHILE);
    keywords.put("kor", sym.FOR);
    keywords.put("kinkeger", sym.INTT);
    keywords.put("kool", sym.BOOL);
    keywords.put("X", sym.X);
    keywords.put("korr", sym.OR);
    keywords.put("kand", sym.AND);
    keywords.put("kod", sym.MOD);
    keywords.put("kreturn", sym.RETURN);
    keywords.put("krue", sym.TRUE);
    keywords.put("kalse", sym.FALSE);
%init}

/*-----Macros (regular definitions)-----*/

White = [\ \n\r\t\f]+
Integer = 0 | [1-9][0-9]*
Identifier = [A-Za-z_][A-Za-z_0-9]*
Comment = "$." [^.] ~".$" | "$." "."+ "$"

%%
/*-----------------------
  -----LEXICAL RULES-----
  -----------------------*/

<YYINITIAL> {

    ";"                { System.out.print(" ; "); return symbol(sym.SEMICOLON); }
    ","                { System.out.print(" , "); return symbol(sym.COMMA); }
    "+"                { System.out.print(" + "); return symbol(sym.PLUS); }
    "++"               { System.out.print(" + "); return symbol(sym.PPLUS); }
    "-"                { System.out.print(" - "); return symbol(sym.MINUS); }
    "--"               { System.out.print(" - "); return symbol(sym.MMINUS); }
    "*"                { System.out.print(" * "); return symbol(sym.TIMES); }
    "/"                { System.out.print(" / "); return symbol(sym.DIV); }
    ">"                { System.out.print(" > "); return symbol(sym.GE); }
    "<"                { System.out.print(" < "); return symbol(sym.LE); }
    "="                { System.out.print(" = "); return symbol(sym.EQ); }
    "=="               { System.out.print(" : "); return symbol(sym.EQQ); }
    ":"                { System.out.print(" : "); return symbol(sym.DOTS); }
    "|"                { System.out.print(" | "); return symbol(sym.VERT); }
    "^"                { System.out.print(" ^ "); return symbol(sym.EXP); }
    "("                { System.out.print(" ( "); return symbol(sym.LPAR); }
    ")"                { System.out.print(" ) "); return symbol(sym.RPAR); }
    "["                { System.out.print(" [ "); return symbol(sym.LBRACK); }
    "]"                { System.out.print(" ] "); return symbol(sym.RBRACK); }
    "{"                { System.out.print(" { "); return symbol(sym.LBRACE); }
    "}"                { System.out.print(" } "); return symbol(sym.RBRACE); }

    {Integer} { System.out.print(" "+yytext()+" ");
                return symbol(sym.INT, new Integer(yytext())); }
    {Identifier} { String text = yytext();
                   System.out.print(" "+text+" ");
                   Integer s = keywords.get(text);
                   if (s != null) { return symbol(s); }
                   else { return symbol(sym.IDENT, new Integer(1)); }
          }
    {Comment} {}
    {White} { System.out.print(yytext()); }
}


/*-----Lex error handling-----*/
[^]                    { throw new Error("Illegal character <"+yytext()+">"); }