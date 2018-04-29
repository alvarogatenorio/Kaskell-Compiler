JAVA=java
JAVAC=javac
JFLEX=./"jflex-1.6.1"/bin/jflex
CUPJAR=./"jflex-1.6.1"/lib/java-cup-11a.jar
CUP=$(JAVA) -jar $(CUPJAR) <
CP=.:$(CUPJAR)

Main.class: Main.java KLexer.java

KLexer.java: klexer.flex
	$(JFLEX) klexer.flex
clean:
	rm -f KLexer.java Token.java output.txt *.class *~
