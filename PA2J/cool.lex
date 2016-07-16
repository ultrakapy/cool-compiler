/*
 *  The scanner definition for COOL.
 */

import java_cup.runtime.Symbol;

%%

%{

/*  Stuff enclosed in %{ %} is copied verbatim to the lexer class
 *  definition, all the extra variables/functions you want to use in the
 *  lexer actions should go here.  Don't remove or modify anything that
 *  was there initially.  */

    // Max size of string constants
    static int MAX_STR_CONST = 1025;

    // For assembling string constants
    StringBuffer string_buf = new StringBuffer();

    private int curr_lineno = 0;
    int get_curr_lineno() {
	return curr_lineno;
    }

    private AbstractSymbol filename;

    void set_filename(String fname) {
	filename = AbstractTable.stringtable.addString(fname);
    }

    AbstractSymbol curr_filename() {
	return filename;
    }

    // To keep track of next free string table slot
    private int curr_string_table_index = 0;

    Symbol getKeywordSymbol(int tokenConstant) {
      return new Symbol(tokenConstant);
    }

    Symbol getAbstractSymbol(String yytext, int tokenConstant) {
      String str = yytext;
      int len = str.length();
      StringSymbol strSym = new StringSymbol(str, 
					     len, 
					     curr_string_table_index++);

      return new Symbol(tokenConstant, strSym);
    }

    Symbol getStringSymbol(String yytext) {
      String str = yytext;
      
      // note: no longer need to do this here.. i have separate regex rule
      // if string contains an unescaped newline, return error
      //if (str.contains("\n") && !str.contains("\\\n"))
      //  return new Symbol(TokenConstants.ERROR, "Unterminated string constant");
          
      if (str.contains("\0"))
	return new Symbol(TokenConstants.ERROR, "String contains null character.");
      
      //str = str.replace("\\0", "0"); // replace two chars '\0' with just 0

      str = str.replace("\\b", "\b"); // replace two chars '\b' with 1 char
      str = str.replace("\\t", "\t"); // replace two chars '\t' with 1 char
      str = str.replace("\\n", "\n"); // replace two chars '\n' with 1 char
      str = str.replace("\\f", "\f"); // replace two chars '\f' with 1 char

      // replace all \c with c except for \b \t \n \f per the Cool spec
      for (int i = 0; i <= 127; i++) {
	String c = Character.toString((char) i);
  	if (c != "b" && c != "t" && c != "n" && c != "f") {
	  str = str.replace("\\" + c, c);
	}
      }

      int len = str.length();
      
      //System.out.println(str);
      //System.out.println("String length: " + len);
      if (len > MAX_STR_CONST)
	return new Symbol(TokenConstants.ERROR, "String constant too long");
      
      return getAbstractSymbol(str, TokenConstants.STR_CONST);
    }

    void countLines(String str) {
      String[] lines = str.split("\n");
      if (lines != null) {
	curr_lineno += lines.length;
	//System.out.println("Line #" + curr_lineno + " : " + str);
	//System.out.println("Lines: " + lines.length);
	//System.out.println();
      }
    }

    private boolean leftParenExists = false;
    private boolean leftBraceExists = false;

    private java.util.Stack commentStack = new java.util.Stack();


%}

%init{

/*  Stuff enclosed in %init{ %init} is copied verbatim to the lexer
 *  class constructor, all the extra initialization you want to do should
 *  go here.  Don't remove or modify anything that was there initially. */

    // empty for now
%init}

%eofval{

/*  Stuff enclosed in %eofval{ %eofval} specifies java code that is
 *  executed when end-of-file is reached.  If you use multiple lexical
 *  states and want to do something special if an EOF is encountered in
 *  one of those states, place your code in the switch statement.
 *  Ultimately, you should return the EOF symbol, or your lexer won't
 *  work.  */

    switch(yy_lexical_state) {
    case YYINITIAL:
	/* nothing special to do in the initial state */
      //yybegin(COMMENT);
      //System.out.println("Special EOF");
      //return new Symbol(TokenConstants.EOF, "EOF in string constant");
      break;
	/*case STRING:
      return new Symbol(TokenConstants.ERROR, "EOF in string constant");*/
    case COMMENT:
      //System.out.println("--- EOF in comment");
      yybegin(YYINITIAL);
      return new Symbol(TokenConstants.ERROR, "EOF in comment");
    }
    return new Symbol(TokenConstants.EOF);
%eofval}

%class CoolLexer
%cup

%state STRING
%state COMMENT

lowercase_letter=[a-z]
uppercase_letter=[A-Z]
letter=[a-zA-Z]
digit=[0-9]
letter_digit_underscore=[a-zA-Z0-9_]
whitespace=[" "\n\f\r\t\013]*

double_quote=\"
string_contents=([^\"])*

one_line_comment="--".*
open_comment="(*"
comment_contents=[^"(*"]*
close_comment="*)"

%%

<YYINITIAL>"=>ABC"			{ /* Sample lexical rule for "=>" arrow.
                                     Further lexical rules should be defined
                                     here, after the last %% separator */
                                  return new Symbol(TokenConstants.DARROW); }


\"(([^\"\n])*(\\\")*)
{
  //System.out.println("Found EOF: " + yytext());
  countLines(yytext());
  return new Symbol(TokenConstants.ERROR, "EOF in string constant");
}

(\"(([^\"])*\n))
{
  //System.out.println("Log - unterminated string constant: " + yytext());
  String str = yytext();
  countLines(str);
  if (str.contains("\0"))
    return new Symbol(TokenConstants.ERROR, "String contains null character.");

  return new Symbol(TokenConstants.ERROR, "Unterminated string constant");
}

(\"([^\"\n])*\")|(\"([^\"\n])*(\\\n)*(\\\")*([^\"\n])*\")
{
  //System.out.println("String: " + yytext());
  String str = yytext();
  countLines(str);
  //System.out.println("Current Line #: "+ curr_lineno);
  return getStringSymbol(str.substring(1, str.length() - 1));
}


{open_comment}
{
  //System.out.println("--- Open Comment ---");
  //System.out.println("open: " + yytext());
  countLines(yytext());
  commentStack.push("(*");
  yybegin(COMMENT);
}

<COMMENT>{open_comment}
{
  commentStack.push("(*");
}

<COMMENT>{close_comment}
{
  //System.out.println("Current Line #: "+ curr_lineno);
  //System.out.println(commentStack);
  commentStack.pop();
  if (commentStack.empty()) {
    //System.out.println("End of comment");
    commentStack = new java.util.Stack();
    yybegin(YYINITIAL);
  }
}

<COMMENT>{comment_contents}
{
  // update lineno variable
  countLines(yytext());
}

{close_comment}
{
  // close comment is outside of comment so return unmatched error
  return new Symbol(TokenConstants.ERROR, "Unmatched *)");
}

<COMMENT>"*"
{
  //System.out.println("HERE: *");
}

<COMMENT>.
{
  //System.out.println("Comment catchall");
}

{one_line_comment}
{ 
  //System.out.println("Got one line comment: " + yytext()); 
  //return getAbstractSymbol(yytext(), TokenConstants.STR_CONST);
  countLines(yytext());
}





("class"|"CLASS") 
{ //System.out.println("Got Keyword: " + yytext()); 
  return getKeywordSymbol(TokenConstants.CLASS);
}

("else"|"Else"|"eLse"|"elSe"|"elsE"|"ELse"|"ElSe"|"ElsE"|"eLSe"|"eLsE"|"elSE"|"eLSE"|"ElSE"|"ELsE"|"ELSe"|"ELSE") 
{ //System.out.println("Got Keyword: " + yytext()); 
  return getKeywordSymbol(TokenConstants.ELSE);
}

("if"|"IF") 
{ //System.out.println("Got Keyword: " + yytext()); 
  return getKeywordSymbol(TokenConstants.IF);
}

("fi"|"FI") 
{ //System.out.println("Got Keyword: " + yytext()); 
  return getKeywordSymbol(TokenConstants.FI);
}

("in"|"IN") 
{ //System.out.println("Got Keyword: " + yytext()); 
  return getKeywordSymbol(TokenConstants.IN);
}

("inherits"|"INHERITS"|"iNHerITs") 
{ //System.out.println("Got Keyword: " + yytext()); 
  return getKeywordSymbol(TokenConstants.INHERITS);
}

("isvoid"|"ISVOID"|"IsvoiD") 
{ //System.out.println("Got Keyword: " + yytext()); 
  return getKeywordSymbol(TokenConstants.ISVOID);
}

("let"|"LET"|"lEt") 
{ //System.out.println("Got Keyword: " + yytext()); 
  return getKeywordSymbol(TokenConstants.LET);
}

("loop"|"LOOP"|"LOOp") 
{ //System.out.println("Got Keyword: " + yytext()); 
  return getKeywordSymbol(TokenConstants.LOOP);
}

("pool"|"POOL"|"PoOl") 
{ //System.out.println("Got Keyword: " + yytext()); 
  return getKeywordSymbol(TokenConstants.POOL);
}

("then"|"THEN"|"ThEn") 
{ //System.out.println("Got Keyword: " + yytext()); 
  return getKeywordSymbol(TokenConstants.THEN);
}

("while"|"WHILE"|"whIle") 
{ //System.out.println("Got Keyword: " + yytext()); 
  return getKeywordSymbol(TokenConstants.WHILE);
}

("case"|"CASE") 
{ //System.out.println("Got Keyword: " + yytext()); 
  return getKeywordSymbol(TokenConstants.CASE);
}

("esac"|"ESAC"|"eSaC") 
{ //System.out.println("Got Keyword: " + yytext()); 
  return getKeywordSymbol(TokenConstants.ESAC);
}

("new"|"NEW") 
{ //System.out.println("Got Keyword: " + yytext()); 
  return getKeywordSymbol(TokenConstants.NEW);
}

("of"|"OF") 
{ //System.out.println("Got Keyword: " + yytext()); 
  return getKeywordSymbol(TokenConstants.OF);
}

("not"|"NOT") 
{ //System.out.println("Got Keyword: " + yytext()); 
  return getKeywordSymbol(TokenConstants.NOT);
}

("true"|"tRue"|"trUe"|"truE"|"tRUe"|"tRuE"|"trUE"|"tRUE") 
{ //System.out.println("Got Keyword: " + yytext()); 
  return new Symbol(TokenConstants.BOOL_CONST, true);
}

("false"|"fALSE"|"fALSE") 
{ //System.out.println("Got Keyword: " + yytext()); 
  return new Symbol(TokenConstants.BOOL_CONST, false);
}

{whitespace} 
{ 
  //System.out.println("Got whitespace"); 
  //int len = yytext().length();
  //System.out.println("Got whitespace of length: " + Integer.toString(len));
  countLines(yytext());
}

{digit}+ 
{ 
  //System.out.println("Got Integer: " + yytext()); 
  //System.out.println("stbl idx: " + Integer.toString(curr_string_table_index));
  String str = yytext();
  int len = str.length();
  IntSymbol intSym = new IntSymbol(str, len, curr_string_table_index++);

  countLines(str);

  return new Symbol(TokenConstants.INT_CONST, intSym);
}

"SELF_TYPE" 
{ //System.out.println("Got SELF_TYPE ID"); 
  return getAbstractSymbol(yytext(), TokenConstants.TYPEID);
}

{uppercase_letter}({letter_digit_underscore})* 
{ //System.out.println("Got Type IDs"); 
  String str = yytext();
  countLines(str);
  return getAbstractSymbol(str, TokenConstants.TYPEID);
}

"self" 
{ //System.out.println("Got self ID"); 
  return getAbstractSymbol(yytext(), TokenConstants.OBJECTID);
}

{lowercase_letter}({letter_digit_underscore})* 
{ //System.out.println("Got Object IDs"); 
  String str = yytext();
  countLines(str);
  return getAbstractSymbol(str, TokenConstants.OBJECTID);
}

"(" 
{ //System.out.println("Got Left Paren: " + yytext()); 
  leftParenExists = true;
  return new Symbol(TokenConstants.LPAREN);
}

")" 
{ //System.out.println("Got Right Paren: " + yytext()); 
  //if (leftParenExists) {
    return new Symbol(TokenConstants.RPAREN);
    //} else {
    //leftParenExists = false;
    //return new Symbol(TokenConstants.ERROR, "Unmatched )");
    //}
}

"{" 
{ //System.out.println("Got Left Brace: " + yytext()); 
  leftBraceExists = true;
  return new Symbol(TokenConstants.LBRACE);
}

"}" 
{ //System.out.println("Got Right Brace: " + yytext()); 
  if (leftBraceExists) {
    return new Symbol(TokenConstants.RBRACE);
  } else {
    leftBraceExists = false;
    return new Symbol(TokenConstants.ERROR, "Unmatched }");
  }
}

"<-" 
{ //System.out.println("Got Assignment operator: " + yytext()); 
  return new Symbol(TokenConstants.ASSIGN);
}

":" 
{ //System.out.println("Got colon: " + yytext()); 
  return new Symbol(TokenConstants.COLON);
}

";" 
{ //System.out.println("Got semi-colon: " + yytext()); 
  return new Symbol(TokenConstants.SEMI);
}

"@" 
{ //System.out.println("Got @ sign: " + yytext()); 
  return new Symbol(TokenConstants.AT);
}

"." 
{ //System.out.println("Got period: " + yytext()); 
  return new Symbol(TokenConstants.DOT);
}

"," 
{ //System.out.println("Got comma: " + yytext()); 
  return new Symbol(TokenConstants.COMMA);
}

"+" 
{ //System.out.println("Got plus op: " + yytext()); 
  return new Symbol(TokenConstants.PLUS);
}

"-" 
{ //System.out.println("Got minus op: " + yytext()); 
  return new Symbol(TokenConstants.MINUS);
}

"*" 
{ //System.out.println("Got mult op: " + yytext()); 
  return new Symbol(TokenConstants.MULT);
}

"/" 
{ //System.out.println("Got div op: " + yytext()); 
  return new Symbol(TokenConstants.DIV);
}

"~" 
{ //System.out.println("Got neg op: " + yytext()); 
  return new Symbol(TokenConstants.NEG);
}

"<" 
{ //System.out.println("Got less than op: " + yytext()); 
  return new Symbol(TokenConstants.LT);
}

"<=" 
{ //System.out.println("Got less than or equal op: " + yytext()); 
  return new Symbol(TokenConstants.LE);
}

"=" 
{ //System.out.println("Got equals op: " + yytext()); 
  return new Symbol(TokenConstants.EQ);
}

"=>" 
{ //System.out.println("Got case expression op: " + yytext()); 
  return new Symbol(TokenConstants.DARROW);
}


. 
{ //System.out.println("!!!!!!!!!!!!! ERRORS: " + yytext()); 
  String str = yytext();
  countLines(str);
  return new Symbol(TokenConstants.ERROR, str);
}
