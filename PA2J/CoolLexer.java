/*
 *  The scanner definition for COOL.
 */
import java_cup.runtime.Symbol;


class CoolLexer implements java_cup.runtime.Scanner {
	private final int YY_BUFFER_SIZE = 512;
	private final int YY_F = -1;
	private final int YY_NO_STATE = -1;
	private final int YY_NOT_ACCEPT = 0;
	private final int YY_START = 1;
	private final int YY_END = 2;
	private final int YY_NO_ANCHOR = 4;
	private final int YY_BOL = 128;
	private final int YY_EOF = 129;

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
	private java.io.BufferedReader yy_reader;
	private int yy_buffer_index;
	private int yy_buffer_read;
	private int yy_buffer_start;
	private int yy_buffer_end;
	private char yy_buffer[];
	private boolean yy_at_bol;
	private int yy_lexical_state;

	CoolLexer (java.io.Reader reader) {
		this ();
		if (null == reader) {
			throw (new Error("Error: Bad input stream initializer."));
		}
		yy_reader = new java.io.BufferedReader(reader);
	}

	CoolLexer (java.io.InputStream instream) {
		this ();
		if (null == instream) {
			throw (new Error("Error: Bad input stream initializer."));
		}
		yy_reader = new java.io.BufferedReader(new java.io.InputStreamReader(instream));
	}

	private CoolLexer () {
		yy_buffer = new char[YY_BUFFER_SIZE];
		yy_buffer_read = 0;
		yy_buffer_index = 0;
		yy_buffer_start = 0;
		yy_buffer_end = 0;
		yy_at_bol = true;
		yy_lexical_state = YYINITIAL;

/*  Stuff enclosed in %init{ %init} is copied verbatim to the lexer
 *  class constructor, all the extra initialization you want to do should
 *  go here.  Don't remove or modify anything that was there initially. */
    // empty for now
	}

	private boolean yy_eof_done = false;
	private final int STRING = 1;
	private final int YYINITIAL = 0;
	private final int COMMENT = 2;
	private final int yy_state_dtrans[] = {
		0,
		189,
		54
	};
	private void yybegin (int state) {
		yy_lexical_state = state;
	}
	private int yy_advance ()
		throws java.io.IOException {
		int next_read;
		int i;
		int j;

		if (yy_buffer_index < yy_buffer_read) {
			return yy_buffer[yy_buffer_index++];
		}

		if (0 != yy_buffer_start) {
			i = yy_buffer_start;
			j = 0;
			while (i < yy_buffer_read) {
				yy_buffer[j] = yy_buffer[i];
				++i;
				++j;
			}
			yy_buffer_end = yy_buffer_end - yy_buffer_start;
			yy_buffer_start = 0;
			yy_buffer_read = j;
			yy_buffer_index = j;
			next_read = yy_reader.read(yy_buffer,
					yy_buffer_read,
					yy_buffer.length - yy_buffer_read);
			if (-1 == next_read) {
				return YY_EOF;
			}
			yy_buffer_read = yy_buffer_read + next_read;
		}

		while (yy_buffer_index >= yy_buffer_read) {
			if (yy_buffer_index >= yy_buffer.length) {
				yy_buffer = yy_double(yy_buffer);
			}
			next_read = yy_reader.read(yy_buffer,
					yy_buffer_read,
					yy_buffer.length - yy_buffer_read);
			if (-1 == next_read) {
				return YY_EOF;
			}
			yy_buffer_read = yy_buffer_read + next_read;
		}
		return yy_buffer[yy_buffer_index++];
	}
	private void yy_move_end () {
		if (yy_buffer_end > yy_buffer_start &&
		    '\n' == yy_buffer[yy_buffer_end-1])
			yy_buffer_end--;
		if (yy_buffer_end > yy_buffer_start &&
		    '\r' == yy_buffer[yy_buffer_end-1])
			yy_buffer_end--;
	}
	private boolean yy_last_was_cr=false;
	private void yy_mark_start () {
		yy_buffer_start = yy_buffer_index;
	}
	private void yy_mark_end () {
		yy_buffer_end = yy_buffer_index;
	}
	private void yy_to_mark () {
		yy_buffer_index = yy_buffer_end;
		yy_at_bol = (yy_buffer_end > yy_buffer_start) &&
		            ('\r' == yy_buffer[yy_buffer_end-1] ||
		             '\n' == yy_buffer[yy_buffer_end-1] ||
		             2028/*LS*/ == yy_buffer[yy_buffer_end-1] ||
		             2029/*PS*/ == yy_buffer[yy_buffer_end-1]);
	}
	private java.lang.String yytext () {
		return (new java.lang.String(yy_buffer,
			yy_buffer_start,
			yy_buffer_end - yy_buffer_start));
	}
	private int yylength () {
		return yy_buffer_end - yy_buffer_start;
	}
	private char[] yy_double (char buf[]) {
		int i;
		char newbuf[];
		newbuf = new char[2*buf.length];
		for (i = 0; i < buf.length; ++i) {
			newbuf[i] = buf[i];
		}
		return newbuf;
	}
	private final int YY_E_INTERNAL = 0;
	private final int YY_E_MATCH = 1;
	private java.lang.String yy_error_string[] = {
		"Error: Internal error.\n",
		"Error: Unmatched input.\n"
	};
	private void yy_error (int code,boolean fatal) {
		java.lang.System.out.print(yy_error_string[code]);
		java.lang.System.out.flush();
		if (fatal) {
			throw new Error("Fatal Error.\n");
		}
	}
	private int[][] unpackFromString(int size1, int size2, String st) {
		int colonIndex = -1;
		String lengthString;
		int sequenceLength = 0;
		int sequenceInteger = 0;

		int commaIndex;
		String workString;

		int res[][] = new int[size1][size2];
		for (int i= 0; i < size1; i++) {
			for (int j= 0; j < size2; j++) {
				if (sequenceLength != 0) {
					res[i][j] = sequenceInteger;
					sequenceLength--;
					continue;
				}
				commaIndex = st.indexOf(',');
				workString = (commaIndex==-1) ? st :
					st.substring(0, commaIndex);
				st = st.substring(commaIndex+1);
				colonIndex = workString.indexOf(':');
				if (colonIndex == -1) {
					res[i][j]=Integer.parseInt(workString);
					continue;
				}
				lengthString =
					workString.substring(colonIndex+1);
				sequenceLength=Integer.parseInt(lengthString);
				workString=workString.substring(0,colonIndex);
				sequenceInteger=Integer.parseInt(workString);
				res[i][j] = sequenceInteger;
				sequenceLength--;
			}
		}
		return res;
	}
	private int yy_acpt[] = {
		/* 0 */ YY_NO_ANCHOR,
		/* 1 */ YY_NO_ANCHOR,
		/* 2 */ YY_NO_ANCHOR,
		/* 3 */ YY_NO_ANCHOR,
		/* 4 */ YY_NO_ANCHOR,
		/* 5 */ YY_NO_ANCHOR,
		/* 6 */ YY_NO_ANCHOR,
		/* 7 */ YY_NO_ANCHOR,
		/* 8 */ YY_NO_ANCHOR,
		/* 9 */ YY_NO_ANCHOR,
		/* 10 */ YY_NO_ANCHOR,
		/* 11 */ YY_NO_ANCHOR,
		/* 12 */ YY_NO_ANCHOR,
		/* 13 */ YY_NO_ANCHOR,
		/* 14 */ YY_NO_ANCHOR,
		/* 15 */ YY_NO_ANCHOR,
		/* 16 */ YY_NO_ANCHOR,
		/* 17 */ YY_NO_ANCHOR,
		/* 18 */ YY_NO_ANCHOR,
		/* 19 */ YY_NO_ANCHOR,
		/* 20 */ YY_NO_ANCHOR,
		/* 21 */ YY_NO_ANCHOR,
		/* 22 */ YY_NO_ANCHOR,
		/* 23 */ YY_NO_ANCHOR,
		/* 24 */ YY_NO_ANCHOR,
		/* 25 */ YY_NO_ANCHOR,
		/* 26 */ YY_NO_ANCHOR,
		/* 27 */ YY_NO_ANCHOR,
		/* 28 */ YY_NO_ANCHOR,
		/* 29 */ YY_NO_ANCHOR,
		/* 30 */ YY_NO_ANCHOR,
		/* 31 */ YY_NO_ANCHOR,
		/* 32 */ YY_NO_ANCHOR,
		/* 33 */ YY_NO_ANCHOR,
		/* 34 */ YY_NO_ANCHOR,
		/* 35 */ YY_NOT_ACCEPT,
		/* 36 */ YY_NO_ANCHOR,
		/* 37 */ YY_NO_ANCHOR,
		/* 38 */ YY_NO_ANCHOR,
		/* 39 */ YY_NO_ANCHOR,
		/* 40 */ YY_NO_ANCHOR,
		/* 41 */ YY_NO_ANCHOR,
		/* 42 */ YY_NO_ANCHOR,
		/* 43 */ YY_NO_ANCHOR,
		/* 44 */ YY_NO_ANCHOR,
		/* 45 */ YY_NO_ANCHOR,
		/* 46 */ YY_NO_ANCHOR,
		/* 47 */ YY_NO_ANCHOR,
		/* 48 */ YY_NO_ANCHOR,
		/* 49 */ YY_NO_ANCHOR,
		/* 50 */ YY_NO_ANCHOR,
		/* 51 */ YY_NO_ANCHOR,
		/* 52 */ YY_NO_ANCHOR,
		/* 53 */ YY_NO_ANCHOR,
		/* 54 */ YY_NO_ANCHOR,
		/* 55 */ YY_NO_ANCHOR,
		/* 56 */ YY_NO_ANCHOR,
		/* 57 */ YY_NO_ANCHOR,
		/* 58 */ YY_NO_ANCHOR,
		/* 59 */ YY_NO_ANCHOR,
		/* 60 */ YY_NO_ANCHOR,
		/* 61 */ YY_NO_ANCHOR,
		/* 62 */ YY_NO_ANCHOR,
		/* 63 */ YY_NO_ANCHOR,
		/* 64 */ YY_NO_ANCHOR,
		/* 65 */ YY_NO_ANCHOR,
		/* 66 */ YY_NO_ANCHOR,
		/* 67 */ YY_NO_ANCHOR,
		/* 68 */ YY_NO_ANCHOR,
		/* 69 */ YY_NOT_ACCEPT,
		/* 70 */ YY_NO_ANCHOR,
		/* 71 */ YY_NO_ANCHOR,
		/* 72 */ YY_NO_ANCHOR,
		/* 73 */ YY_NO_ANCHOR,
		/* 74 */ YY_NO_ANCHOR,
		/* 75 */ YY_NO_ANCHOR,
		/* 76 */ YY_NO_ANCHOR,
		/* 77 */ YY_NO_ANCHOR,
		/* 78 */ YY_NO_ANCHOR,
		/* 79 */ YY_NO_ANCHOR,
		/* 80 */ YY_NO_ANCHOR,
		/* 81 */ YY_NO_ANCHOR,
		/* 82 */ YY_NO_ANCHOR,
		/* 83 */ YY_NO_ANCHOR,
		/* 84 */ YY_NO_ANCHOR,
		/* 85 */ YY_NO_ANCHOR,
		/* 86 */ YY_NO_ANCHOR,
		/* 87 */ YY_NO_ANCHOR,
		/* 88 */ YY_NO_ANCHOR,
		/* 89 */ YY_NOT_ACCEPT,
		/* 90 */ YY_NO_ANCHOR,
		/* 91 */ YY_NO_ANCHOR,
		/* 92 */ YY_NO_ANCHOR,
		/* 93 */ YY_NO_ANCHOR,
		/* 94 */ YY_NO_ANCHOR,
		/* 95 */ YY_NO_ANCHOR,
		/* 96 */ YY_NOT_ACCEPT,
		/* 97 */ YY_NO_ANCHOR,
		/* 98 */ YY_NO_ANCHOR,
		/* 99 */ YY_NO_ANCHOR,
		/* 100 */ YY_NO_ANCHOR,
		/* 101 */ YY_NOT_ACCEPT,
		/* 102 */ YY_NO_ANCHOR,
		/* 103 */ YY_NO_ANCHOR,
		/* 104 */ YY_NO_ANCHOR,
		/* 105 */ YY_NO_ANCHOR,
		/* 106 */ YY_NOT_ACCEPT,
		/* 107 */ YY_NO_ANCHOR,
		/* 108 */ YY_NO_ANCHOR,
		/* 109 */ YY_NO_ANCHOR,
		/* 110 */ YY_NOT_ACCEPT,
		/* 111 */ YY_NO_ANCHOR,
		/* 112 */ YY_NO_ANCHOR,
		/* 113 */ YY_NO_ANCHOR,
		/* 114 */ YY_NOT_ACCEPT,
		/* 115 */ YY_NO_ANCHOR,
		/* 116 */ YY_NO_ANCHOR,
		/* 117 */ YY_NO_ANCHOR,
		/* 118 */ YY_NO_ANCHOR,
		/* 119 */ YY_NO_ANCHOR,
		/* 120 */ YY_NO_ANCHOR,
		/* 121 */ YY_NO_ANCHOR,
		/* 122 */ YY_NO_ANCHOR,
		/* 123 */ YY_NO_ANCHOR,
		/* 124 */ YY_NO_ANCHOR,
		/* 125 */ YY_NO_ANCHOR,
		/* 126 */ YY_NO_ANCHOR,
		/* 127 */ YY_NO_ANCHOR,
		/* 128 */ YY_NO_ANCHOR,
		/* 129 */ YY_NO_ANCHOR,
		/* 130 */ YY_NO_ANCHOR,
		/* 131 */ YY_NO_ANCHOR,
		/* 132 */ YY_NO_ANCHOR,
		/* 133 */ YY_NO_ANCHOR,
		/* 134 */ YY_NO_ANCHOR,
		/* 135 */ YY_NO_ANCHOR,
		/* 136 */ YY_NO_ANCHOR,
		/* 137 */ YY_NO_ANCHOR,
		/* 138 */ YY_NO_ANCHOR,
		/* 139 */ YY_NO_ANCHOR,
		/* 140 */ YY_NO_ANCHOR,
		/* 141 */ YY_NO_ANCHOR,
		/* 142 */ YY_NO_ANCHOR,
		/* 143 */ YY_NO_ANCHOR,
		/* 144 */ YY_NO_ANCHOR,
		/* 145 */ YY_NO_ANCHOR,
		/* 146 */ YY_NO_ANCHOR,
		/* 147 */ YY_NO_ANCHOR,
		/* 148 */ YY_NO_ANCHOR,
		/* 149 */ YY_NO_ANCHOR,
		/* 150 */ YY_NO_ANCHOR,
		/* 151 */ YY_NO_ANCHOR,
		/* 152 */ YY_NO_ANCHOR,
		/* 153 */ YY_NO_ANCHOR,
		/* 154 */ YY_NO_ANCHOR,
		/* 155 */ YY_NO_ANCHOR,
		/* 156 */ YY_NO_ANCHOR,
		/* 157 */ YY_NO_ANCHOR,
		/* 158 */ YY_NO_ANCHOR,
		/* 159 */ YY_NO_ANCHOR,
		/* 160 */ YY_NO_ANCHOR,
		/* 161 */ YY_NO_ANCHOR,
		/* 162 */ YY_NO_ANCHOR,
		/* 163 */ YY_NO_ANCHOR,
		/* 164 */ YY_NO_ANCHOR,
		/* 165 */ YY_NO_ANCHOR,
		/* 166 */ YY_NO_ANCHOR,
		/* 167 */ YY_NO_ANCHOR,
		/* 168 */ YY_NO_ANCHOR,
		/* 169 */ YY_NO_ANCHOR,
		/* 170 */ YY_NO_ANCHOR,
		/* 171 */ YY_NO_ANCHOR,
		/* 172 */ YY_NO_ANCHOR,
		/* 173 */ YY_NO_ANCHOR,
		/* 174 */ YY_NO_ANCHOR,
		/* 175 */ YY_NO_ANCHOR,
		/* 176 */ YY_NO_ANCHOR,
		/* 177 */ YY_NO_ANCHOR,
		/* 178 */ YY_NO_ANCHOR,
		/* 179 */ YY_NO_ANCHOR,
		/* 180 */ YY_NO_ANCHOR,
		/* 181 */ YY_NO_ANCHOR,
		/* 182 */ YY_NO_ANCHOR,
		/* 183 */ YY_NO_ANCHOR,
		/* 184 */ YY_NO_ANCHOR,
		/* 185 */ YY_NO_ANCHOR,
		/* 186 */ YY_NO_ANCHOR,
		/* 187 */ YY_NO_ANCHOR,
		/* 188 */ YY_NO_ANCHOR,
		/* 189 */ YY_NO_ANCHOR,
		/* 190 */ YY_NO_ANCHOR,
		/* 191 */ YY_NO_ANCHOR,
		/* 192 */ YY_NO_ANCHOR,
		/* 193 */ YY_NO_ANCHOR,
		/* 194 */ YY_NO_ANCHOR,
		/* 195 */ YY_NO_ANCHOR,
		/* 196 */ YY_NO_ANCHOR,
		/* 197 */ YY_NO_ANCHOR,
		/* 198 */ YY_NO_ANCHOR,
		/* 199 */ YY_NO_ANCHOR,
		/* 200 */ YY_NO_ANCHOR,
		/* 201 */ YY_NO_ANCHOR,
		/* 202 */ YY_NO_ANCHOR,
		/* 203 */ YY_NO_ANCHOR,
		/* 204 */ YY_NO_ANCHOR,
		/* 205 */ YY_NO_ANCHOR,
		/* 206 */ YY_NO_ANCHOR,
		/* 207 */ YY_NO_ANCHOR,
		/* 208 */ YY_NO_ANCHOR,
		/* 209 */ YY_NO_ANCHOR,
		/* 210 */ YY_NO_ANCHOR,
		/* 211 */ YY_NO_ANCHOR,
		/* 212 */ YY_NO_ANCHOR,
		/* 213 */ YY_NO_ANCHOR,
		/* 214 */ YY_NO_ANCHOR,
		/* 215 */ YY_NO_ANCHOR,
		/* 216 */ YY_NO_ANCHOR,
		/* 217 */ YY_NO_ANCHOR,
		/* 218 */ YY_NO_ANCHOR
	};
	private int yy_cmap[] = unpackFromString(1,130,
"13:9,47,9,47:2,7,13:18,47,13,6,13:5,10,12,11,61,60,14,59,62,48:10,56,57,55," +
"1,2,13,58,3,4,5,40,22,26,51,32,25,51:2,19,51,28,39,42,51,33,20,34,46,38,44," +
"51,50,51,13,8,13:2,49,13,17,52,15,37,21,24,52,29,23,52:2,16,52,27,36,41,52," +
"30,18,31,45,35,43,52:3,53,13,54,63,13,0:2")[0];

	private int yy_rmap[] = unpackFromString(1,219,
"0,1,2,1,3,4,5,6,1,7,8,9,1:2,10,1:8,11,1,12,1:2,13,14,15,14:2,1:2,16,14:3,3," +
"14:7,1,3,14:4,3,17,5,18,1,19,20,21,22,1,23,24,3,25,3:2,12,3:3,14,3:5,14,3:3" +
",26,27,28,29,26,30,31,32,33,34,35,36,37,38,30,39,40,41,42,43,44,45,46,47,48" +
",49,50,51,52,53,54,55,56,57,58,59,60,61,62,63,64,65,66,67,68,69,70,71,72,73" +
",74,75,76,77,78,79,80,81,82,83,84,85,86,87,88,89,90,91,92,93,94,95,96,97,98" +
",99,100,101,102,103,104,105,106,107,108,109,110,111,112,113,114,115,116,117" +
",118,119,120,121,122,123,124,125,126,127,128,129,130,131,14,132,133,134,135" +
",136,137,138,139,140,141,142,143,144,145,146,147,148,149,150,151,152,153,15" +
"4,155,156,157,158")[0];

	private int yy_nxt[][] = unpackFromString(159,64,
"1,2,3,4:2,190,5,58,3,58,6,7,8,3,9,10,145,191,194,144,218,196,193,61,86,59,8" +
"4,148,147,191:2,198,4:2,195,191,93,191,4,91,4,200,197,202,208,191,4,58,11,3" +
",4:2,191,12,13,14,15,16,17,18,19,20,21,22,-1:66,23,-1:64,4:3,-1:9,4:32,-1,4" +
":5,-1:12,5:5,24,5,60,25,5:54,-1:11,26,-1:64,27,-1:65,28,-1:52,191:3,-1:9,19" +
"1,204,150,191:29,-1,191:5,-1:59,11,-1:16,33,-1:12,34,-1:52,35,-1:61,69:5,-1" +
",69:2,25,69:54,-1,28:6,-1,28,-1,28:54,-1:3,191:3,-1:9,191:32,-1,191:5,-1:14" +
",191:3,-1:9,191:14,179,191:17,-1,191:5,-1:15,89,-1:59,1,83:5,92,83:3,55,56," +
"83:2,146,83:49,-1:12,57,-1:58,58,-1,58,-1:37,58,-1:19,4:3,-1:9,4:3,210,4,21" +
"1,4:5,65,4,66,4:18,-1,4:5,-1:12,5:5,85,5,60,64,5:54,-1:3,191:3,-1:9,191:3,1" +
"62,191:5,29,191:2,30,164,191:18,-1,191:5,-1:12,96:5,24,96,114,-1,96:54,-1,1" +
"06:5,24,106,110,25,106:54,-1:3,4:3,-1:9,4:17,214,4:14,-1,4:5,-1:12,83:9,-1:" +
"2,83:52,-1:3,4:3,-1:9,4:10,67,4:21,-1,4:5,-1:12,96:5,24,96,101,-1,96:54,-1:" +
"3,166,191:2,-1:9,191:2,168,191:5,31,191:23,-1,191:5,-1:12,97:5,83,97:2,88,6" +
"9:2,97:52,-1:5,47,-1:59,90:6,83,90,83,28:2,90:52,-1:3,4:3,-1:9,4:11,68,4:20" +
",-1,4:5,-1:12,92:5,87,92,99,88,5:2,92:52,-1:3,191:3,-1:9,191:9,32,191:22,-1" +
",191:5,-1:12,102:5,87,102,118,83,96:2,102:52,-1,111:5,87,111,115,88,106:2,1" +
"11:52,-1,96:5,24,96:2,-1,96:54,-1:3,4:3,-1:9,4:19,70,4:12,-1,4:5,-1:12,92:5" +
",104,92,99,95,5:2,92:52,-1:3,191:3,-1:9,191:16,36,191:15,-1,191:5,-1:12,96:" +
"5,85,96:2,-1,96:54,-1,102:5,87,102:2,83,96:2,102:52,-1:3,4:3,-1:9,4:29,71,4" +
":2,-1,4:5,-1:12,102:5,87,102,107,83,96:2,102:52,-1:3,191:3,-1:9,191:28,37,1" +
"91:3,-1,191:5,-1:12,106:5,24,106:2,25,106:54,-1,102:5,104,102:2,83,96:2,102" +
":52,-1:3,4:3,-1:9,4:19,72,4:12,-1,4:5,-1:14,191:3,-1:9,191:16,38,191:15,-1," +
"191:5,-1:12,106:5,63,106:2,64,106:54,-1,111:5,87,111:2,88,106:2,111:52,-1:3" +
",4:3,-1:9,4:7,39,4:24,-1,4:5,-1:14,191:3,-1:9,191:6,73,191:25,-1,191:5,-1:1" +
"2,96:5,63,96:2,-1,96:54,-1,111:5,94,111:2,95,106:2,111:52,-1:3,4:3,-1:9,4:2" +
"6,74:2,4:4,-1,4:5,-1:14,191:3,-1:9,191:26,40,191:5,-1,191:5,-1:12,102:5,94," +
"102:2,83,96:2,102:52,-1:3,4:3,-1:9,4:6,75:2,4:24,-1,4:5,-1:14,191:3,-1:9,19" +
"1:9,41,191:22,-1,191:5,-1:14,4:2,76,-1:9,4:32,-1,4:5,-1:14,191:3,-1:9,191:6" +
",42:2,191:24,-1,191:5,-1:14,4:3,-1:9,4:12,77,4:19,-1,4:5,-1:14,191:3,-1:9,4" +
"3,191:31,-1,191:5,-1:14,4:3,-1:9,4:13,77,4:18,-1,4:5,-1:14,191:2,43,-1:9,19" +
"1:32,-1,191:5,-1:14,4:3,-1:9,4,78,4:30,-1,4:5,-1:14,191:3,-1:9,191:12,44,19" +
"1:19,-1,191:5,-1:14,4:3,-1:9,4:4,78,4:27,-1,4:5,-1:14,191:3,-1:9,191:6,45:2" +
",191:24,-1,191:5,-1:14,4:3,-1:9,4:5,48,4:26,-1,4:5,-1:14,191:3,-1:9,191,46," +
"191:30,-1,191:5,-1:14,4:3,-1:9,4:7,80,4:24,-1,4:5,-1:14,191:3,-1:9,191:3,79" +
",191:28,-1,191:5,-1:14,4:3,-1:9,4:25,81,4:6,-1,4:5,-1:14,191:3,-1:9,191:7,4" +
"9,191:24,-1,191:5,-1:14,4:3,-1:9,4:5,82,4:26,-1,4:5,-1:14,191:3,-1:9,191:6," +
"49,191:25,-1,191:5,-1:14,4:3,-1:9,4:7,53,4:24,-1,4:5,-1:14,191:3,-1:9,191:6" +
",50,191:25,-1,191:5,-1:14,191:3,-1:9,191:22,51,191:9,-1,191:5,-1:14,191:3,-" +
"1:9,191:3,52,191:28,-1,191:5,-1:13,62,-1:64,4:3,-1:9,4:7,98,4:16,151,4:7,-1" +
",4:5,-1:14,191:3,-1:9,191:6,100:2,191:13,152,191:10,-1,191:5,-1:12,83:9,-1:" +
"2,83:2,90,83:49,-1:3,4:3,-1:9,4:7,103,4:16,108,4:7,-1,4:5,-1:14,191:3,-1:9," +
"191:6,105,191:14,109,191:10,-1,191:5,-1:14,4:3,-1:9,4:5,112,4:26,-1,4:5,-1:" +
"14,191:3,-1:9,191:3,113,191:28,-1,191:5,-1:14,4:3,-1:9,4:24,116,4:7,-1,4:5," +
"-1:14,191:3,-1:9,191:21,117,191:10,-1,191:5,-1:14,4:3,-1:9,4:3,119,4,119,4:" +
"26,-1,4:5,-1:14,191:3,-1:9,191,120,191:30,-1,191:5,-1:14,121,4:2,-1:9,4:32," +
"-1,4:5,-1:14,191:3,-1:9,191:3,122,191,122,191:26,-1,191:5,-1:14,4:3,-1:9,4:" +
"7,123,4:24,-1,4:5,-1:14,191:3,-1:9,191:2,124,191:29,-1,191:5,-1:14,4:3,-1:9" +
",4:7,125,4:24,-1,4:5,-1:14,191:3,-1:9,191:2,126,191:29,-1,191:5,-1:14,4:3,-" +
"1:9,4:24,127,4:7,-1,4:5,-1:14,191:3,-1:9,191:20,178,191:11,-1,191:5,-1:14,4" +
":3,-1:9,4:24,129,4:7,-1,4:5,-1:14,191:3,-1:9,191:17,209,191:14,-1,191:5,-1:" +
"14,4:3,-1:9,4:5,131,4:26,-1,4:5,-1:14,191:3,-1:9,191:4,180,191:27,-1,191:5," +
"-1:14,4:3,-1:9,4:4,133,4:27,-1,4:5,-1:14,191:3,-1:9,191,181,191:30,-1,191:5" +
",-1:14,4:3,-1:9,4:8,135,4:23,-1,4:5,-1:14,191:3,-1:9,191:6,128,191:25,-1,19" +
"1:5,-1:14,4:3,-1:9,4:10,135,4:21,-1,4:5,-1:14,191:3,-1:9,191:30,130:2,-1,19" +
"1:5,-1:14,4:3,-1:9,4:19,137,4:12,-1,4:5,-1:14,191:3,-1:9,191:21,132,191:10," +
"-1,191:5,-1:14,4:3,-1:9,4:27,139,4:4,-1,4:5,-1:14,191:3,-1:9,191:8,182,191," +
"182,191:21,-1,191:5,-1:14,191:3,-1:9,191:3,134,191:28,-1,191:5,-1:14,191:3," +
"-1:9,191:21,183,191:10,-1,191:5,-1:14,191:3,-1:9,191:6,184,191:25,-1,191:5," +
"-1:14,191:3,-1:9,191:5,136,191:26,-1,191:5,-1:14,191:3,-1:9,191:3,138,191:2" +
"8,-1,191:5,-1:14,191:3,-1:9,191,140,191:30,-1,191:5,-1:14,191:3,-1:9,191:8," +
"141,191:23,-1,191:5,-1:14,191:3,-1:9,191:15,185,191:16,-1,191:5,-1:14,191:3" +
",-1:9,191:8,187,191:23,-1,191:5,-1:14,191:3,-1:9,191:10,188,191:21,-1,191:5" +
",-1:14,191:3,-1:9,191:16,142,191:15,-1,191:5,-1:14,191:3,-1:9,191:19,142,19" +
"1:12,-1,191:5,-1:11,1,143,3,4:2,190,5,58,3,58,6,7,8,3,9,10,145,191,194,144," +
"218,196,193,61,86,59,84,148,147,191:2,198,4:2,195,191,93,191,4,91,4,200,197" +
",202,208,191,4,58,11,3,4:2,191,12,13,14,15,16,17,18,19,20,21,22,-1:3,149,4:" +
"2,-1:9,4:4,199,4:27,-1,4:5,-1:14,191:3,-1:9,191:15,186,191:16,-1,191:5,-1:1" +
"4,4:3,-1:9,4,153,4:2,153,155,4:26,-1,4:5,-1:14,191:3,-1:9,191:6,154,191:25," +
"-1,191:5,-1:14,4:3,-1:9,4:14,157,4:2,159,4:14,-1,4:5,-1:14,191:3,-1:9,191,1" +
"56,191,158,156,160,191:26,-1,191:5,-1:14,4:3,-1:9,4:21,161,4:2,163,4:7,-1,4" +
":5,-1:14,191:3,-1:9,191:14,170,172,191:2,172,191:13,-1,191:5,-1:14,165,4:2," +
"-1:9,4:32,-1,4:5,-1:14,191:3,-1:9,191:21,174,191:10,-1,191:5,-1:14,4:3,-1:9" +
",4:10,167,4:21,-1,4:5,-1:14,191:3,-1:9,191:14,176,191:17,-1,191:5,-1:14,4:3" +
",-1:9,4:21,169,4:10,-1,4:5,-1:14,191:3,-1:9,191:2,177,191:29,-1,191:5,-1:14" +
",4:3,-1:9,4:24,171,4:7,-1,4:5,-1:14,4:3,-1:9,4:10,173,4:21,-1,4:5,-1:14,4:3" +
",-1:9,4:32,-1,4:2,175,4:2,-1:14,4:3,-1:9,4:17,201,4:14,-1,4:5,-1:14,191:3,-" +
"1:9,191:6,192,191:25,-1,191:5,-1:14,4:3,-1:9,4:20,203,4:11,-1,4:5,-1:14,4:3" +
",-1:9,4:23,205,4:8,-1,4:5,-1:14,4:3,-1:9,4:18,206,4:13,-1,4:5,-1:14,4:3,-1:" +
"9,4:19,207,4:12,-1,4:5,-1:14,4:3,-1:9,4:7,212,4:24,-1,4:5,-1:14,4:3,-1:9,4:" +
"32,-1,4,213,4:3,-1:14,4:3,-1:9,4:11,215,4:20,-1,4:5,-1:14,4:3,-1:9,4:4,216," +
"4:27,-1,4:5,-1:14,4:3,-1:9,4:7,217,4:24,-1,4:5,-1:11");

	public java_cup.runtime.Symbol next_token ()
		throws java.io.IOException {
		int yy_lookahead;
		int yy_anchor = YY_NO_ANCHOR;
		int yy_state = yy_state_dtrans[yy_lexical_state];
		int yy_next_state = YY_NO_STATE;
		int yy_last_accept_state = YY_NO_STATE;
		boolean yy_initial = true;
		int yy_this_accept;

		yy_mark_start();
		yy_this_accept = yy_acpt[yy_state];
		if (YY_NOT_ACCEPT != yy_this_accept) {
			yy_last_accept_state = yy_state;
			yy_mark_end();
		}
		while (true) {
			if (yy_initial && yy_at_bol) yy_lookahead = YY_BOL;
			else yy_lookahead = yy_advance();
			yy_next_state = YY_F;
			yy_next_state = yy_nxt[yy_rmap[yy_state]][yy_cmap[yy_lookahead]];
			if (YY_EOF == yy_lookahead && true == yy_initial) {

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
			}
			if (YY_F != yy_next_state) {
				yy_state = yy_next_state;
				yy_initial = false;
				yy_this_accept = yy_acpt[yy_state];
				if (YY_NOT_ACCEPT != yy_this_accept) {
					yy_last_accept_state = yy_state;
					yy_mark_end();
				}
			}
			else {
				if (YY_NO_STATE == yy_last_accept_state) {
					throw (new Error("Lexical Error: Unmatched Input."));
				}
				else {
					yy_anchor = yy_acpt[yy_last_accept_state];
					if (0 != (YY_END & yy_anchor)) {
						yy_move_end();
					}
					yy_to_mark();
					switch (yy_last_accept_state) {
					case 0:
						{ 
  //System.out.println("Got whitespace"); 
  //int len = yytext().length();
  //System.out.println("Got whitespace of length: " + Integer.toString(len));
  countLines(yytext());
}
					case -2:
						break;
					case 1:
						
					case -3:
						break;
					case 2:
						{ //System.out.println("Got equals op: " + yytext()); 
  return new Symbol(TokenConstants.EQ);
}
					case -4:
						break;
					case 3:
						{ //System.out.println("!!!!!!!!!!!!! ERRORS: " + yytext()); 
  String str = yytext();
  countLines(str);
  return new Symbol(TokenConstants.ERROR, str);
}
					case -5:
						break;
					case 4:
						{ //System.out.println("Got Type IDs"); 
  String str = yytext();
  countLines(str);
  return getAbstractSymbol(str, TokenConstants.TYPEID);
}
					case -6:
						break;
					case 5:
						{
  //System.out.println("Found EOF: " + yytext());
  countLines(yytext());
  return new Symbol(TokenConstants.ERROR, "EOF in string constant");
}
					case -7:
						break;
					case 6:
						{ //System.out.println("Got Left Paren: " + yytext()); 
  leftParenExists = true;
  return new Symbol(TokenConstants.LPAREN);
}
					case -8:
						break;
					case 7:
						{ //System.out.println("Got mult op: " + yytext()); 
  return new Symbol(TokenConstants.MULT);
}
					case -9:
						break;
					case 8:
						{ //System.out.println("Got Right Paren: " + yytext()); 
  //if (leftParenExists) {
    return new Symbol(TokenConstants.RPAREN);
    //} else {
    //leftParenExists = false;
    //return new Symbol(TokenConstants.ERROR, "Unmatched )");
    //}
}
					case -10:
						break;
					case 9:
						{ //System.out.println("Got minus op: " + yytext()); 
  return new Symbol(TokenConstants.MINUS);
}
					case -11:
						break;
					case 10:
						{ //System.out.println("Got Object IDs"); 
  String str = yytext();
  countLines(str);
  return getAbstractSymbol(str, TokenConstants.OBJECTID);
}
					case -12:
						break;
					case 11:
						{ 
  //System.out.println("Got Integer: " + yytext()); 
  //System.out.println("stbl idx: " + Integer.toString(curr_string_table_index));
  String str = yytext();
  int len = str.length();
  IntSymbol intSym = new IntSymbol(str, len, curr_string_table_index++);
  countLines(str);
  return new Symbol(TokenConstants.INT_CONST, intSym);
}
					case -13:
						break;
					case 12:
						{ //System.out.println("Got Left Brace: " + yytext()); 
  leftBraceExists = true;
  return new Symbol(TokenConstants.LBRACE);
}
					case -14:
						break;
					case 13:
						{ //System.out.println("Got Right Brace: " + yytext()); 
  if (leftBraceExists) {
    return new Symbol(TokenConstants.RBRACE);
  } else {
    leftBraceExists = false;
    return new Symbol(TokenConstants.ERROR, "Unmatched }");
  }
}
					case -15:
						break;
					case 14:
						{ //System.out.println("Got less than op: " + yytext()); 
  return new Symbol(TokenConstants.LT);
}
					case -16:
						break;
					case 15:
						{ //System.out.println("Got colon: " + yytext()); 
  return new Symbol(TokenConstants.COLON);
}
					case -17:
						break;
					case 16:
						{ //System.out.println("Got semi-colon: " + yytext()); 
  return new Symbol(TokenConstants.SEMI);
}
					case -18:
						break;
					case 17:
						{ //System.out.println("Got @ sign: " + yytext()); 
  return new Symbol(TokenConstants.AT);
}
					case -19:
						break;
					case 18:
						{ //System.out.println("Got period: " + yytext()); 
  return new Symbol(TokenConstants.DOT);
}
					case -20:
						break;
					case 19:
						{ //System.out.println("Got comma: " + yytext()); 
  return new Symbol(TokenConstants.COMMA);
}
					case -21:
						break;
					case 20:
						{ //System.out.println("Got plus op: " + yytext()); 
  return new Symbol(TokenConstants.PLUS);
}
					case -22:
						break;
					case 21:
						{ //System.out.println("Got div op: " + yytext()); 
  return new Symbol(TokenConstants.DIV);
}
					case -23:
						break;
					case 22:
						{ //System.out.println("Got neg op: " + yytext()); 
  return new Symbol(TokenConstants.NEG);
}
					case -24:
						break;
					case 23:
						{ //System.out.println("Got case expression op: " + yytext()); 
  return new Symbol(TokenConstants.DARROW);
}
					case -25:
						break;
					case 24:
						{
  //System.out.println("String: " + yytext());
  String str = yytext();
  countLines(str);
  //System.out.println("Current Line #: "+ curr_lineno);
  return getStringSymbol(str.substring(1, str.length() - 1));
}
					case -26:
						break;
					case 25:
						{
  //System.out.println("Log - unterminated string constant: " + yytext());
  String str = yytext();
  countLines(str);
  if (str.contains("\0"))
    return new Symbol(TokenConstants.ERROR, "String contains null character.");
  return new Symbol(TokenConstants.ERROR, "Unterminated string constant");
}
					case -27:
						break;
					case 26:
						{
  //System.out.println("--- Open Comment ---");
  //System.out.println("open: " + yytext());
  countLines(yytext());
  commentStack.push("(*");
  yybegin(COMMENT);
}
					case -28:
						break;
					case 27:
						{
  // close comment is outside of comment so return unmatched error
  return new Symbol(TokenConstants.ERROR, "Unmatched *)");
}
					case -29:
						break;
					case 28:
						{ 
  //System.out.println("Got one line comment: " + yytext()); 
  //return getAbstractSymbol(yytext(), TokenConstants.STR_CONST);
  countLines(yytext());
}
					case -30:
						break;
					case 29:
						{ //System.out.println("Got Keyword: " + yytext()); 
  return getKeywordSymbol(TokenConstants.IF);
}
					case -31:
						break;
					case 30:
						{ //System.out.println("Got Keyword: " + yytext()); 
  return getKeywordSymbol(TokenConstants.IN);
}
					case -32:
						break;
					case 31:
						{ //System.out.println("Got Keyword: " + yytext()); 
  return getKeywordSymbol(TokenConstants.FI);
}
					case -33:
						break;
					case 32:
						{ //System.out.println("Got Keyword: " + yytext()); 
  return getKeywordSymbol(TokenConstants.OF);
}
					case -34:
						break;
					case 33:
						{ //System.out.println("Got less than or equal op: " + yytext()); 
  return new Symbol(TokenConstants.LE);
}
					case -35:
						break;
					case 34:
						{ //System.out.println("Got Assignment operator: " + yytext()); 
  return new Symbol(TokenConstants.ASSIGN);
}
					case -36:
						break;
					case 36:
						{ //System.out.println("Got Keyword: " + yytext()); 
  return getKeywordSymbol(TokenConstants.LET);
}
					case -37:
						break;
					case 37:
						{ //System.out.println("Got Keyword: " + yytext()); 
  return getKeywordSymbol(TokenConstants.NEW);
}
					case -38:
						break;
					case 38:
						{ //System.out.println("Got Keyword: " + yytext()); 
  return getKeywordSymbol(TokenConstants.NOT);
}
					case -39:
						break;
					case 39:
						{ //System.out.println("Got Keyword: " + yytext()); 
  return getKeywordSymbol(TokenConstants.CASE);
}
					case -40:
						break;
					case 40:
						{ //System.out.println("Got Keyword: " + yytext()); 
  return getKeywordSymbol(TokenConstants.LOOP);
}
					case -41:
						break;
					case 41:
						{ //System.out.println("Got self ID"); 
  return getAbstractSymbol(yytext(), TokenConstants.OBJECTID);
}
					case -42:
						break;
					case 42:
						{ //System.out.println("Got Keyword: " + yytext()); 
  return getKeywordSymbol(TokenConstants.ELSE);
}
					case -43:
						break;
					case 43:
						{ //System.out.println("Got Keyword: " + yytext()); 
  return getKeywordSymbol(TokenConstants.ESAC);
}
					case -44:
						break;
					case 44:
						{ //System.out.println("Got Keyword: " + yytext()); 
  return getKeywordSymbol(TokenConstants.THEN);
}
					case -45:
						break;
					case 45:
						{ //System.out.println("Got Keyword: " + yytext()); 
  return new Symbol(TokenConstants.BOOL_CONST, true);
}
					case -46:
						break;
					case 46:
						{ //System.out.println("Got Keyword: " + yytext()); 
  return getKeywordSymbol(TokenConstants.POOL);
}
					case -47:
						break;
					case 47:
						{ /* Sample lexical rule for "=>" arrow.
                                     Further lexical rules should be defined
                                     here, after the last %% separator */
                                  return new Symbol(TokenConstants.DARROW); }
					case -48:
						break;
					case 48:
						{ //System.out.println("Got Keyword: " + yytext()); 
  return getKeywordSymbol(TokenConstants.CLASS);
}
					case -49:
						break;
					case 49:
						{ //System.out.println("Got Keyword: " + yytext()); 
  return new Symbol(TokenConstants.BOOL_CONST, false);
}
					case -50:
						break;
					case 50:
						{ //System.out.println("Got Keyword: " + yytext()); 
  return getKeywordSymbol(TokenConstants.WHILE);
}
					case -51:
						break;
					case 51:
						{ //System.out.println("Got Keyword: " + yytext()); 
  return getKeywordSymbol(TokenConstants.ISVOID);
}
					case -52:
						break;
					case 52:
						{ //System.out.println("Got Keyword: " + yytext()); 
  return getKeywordSymbol(TokenConstants.INHERITS);
}
					case -53:
						break;
					case 53:
						{ //System.out.println("Got SELF_TYPE ID"); 
  return getAbstractSymbol(yytext(), TokenConstants.TYPEID);
}
					case -54:
						break;
					case 54:
						{
  // update lineno variable
  countLines(yytext());
}
					case -55:
						break;
					case 55:
						{
  //System.out.println("Comment catchall");
}
					case -56:
						break;
					case 56:
						{
  //System.out.println("HERE: *");
}
					case -57:
						break;
					case 57:
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
					case -58:
						break;
					case 58:
						{ 
  //System.out.println("Got whitespace"); 
  //int len = yytext().length();
  //System.out.println("Got whitespace of length: " + Integer.toString(len));
  countLines(yytext());
}
					case -59:
						break;
					case 59:
						{ //System.out.println("Got Type IDs"); 
  String str = yytext();
  countLines(str);
  return getAbstractSymbol(str, TokenConstants.TYPEID);
}
					case -60:
						break;
					case 60:
						{
  //System.out.println("Found EOF: " + yytext());
  countLines(yytext());
  return new Symbol(TokenConstants.ERROR, "EOF in string constant");
}
					case -61:
						break;
					case 61:
						{ //System.out.println("Got Object IDs"); 
  String str = yytext();
  countLines(str);
  return getAbstractSymbol(str, TokenConstants.OBJECTID);
}
					case -62:
						break;
					case 62:
						{ //System.out.println("Got case expression op: " + yytext()); 
  return new Symbol(TokenConstants.DARROW);
}
					case -63:
						break;
					case 63:
						{
  //System.out.println("String: " + yytext());
  String str = yytext();
  countLines(str);
  //System.out.println("Current Line #: "+ curr_lineno);
  return getStringSymbol(str.substring(1, str.length() - 1));
}
					case -64:
						break;
					case 64:
						{
  //System.out.println("Log - unterminated string constant: " + yytext());
  String str = yytext();
  countLines(str);
  if (str.contains("\0"))
    return new Symbol(TokenConstants.ERROR, "String contains null character.");
  return new Symbol(TokenConstants.ERROR, "Unterminated string constant");
}
					case -65:
						break;
					case 65:
						{ //System.out.println("Got Keyword: " + yytext()); 
  return getKeywordSymbol(TokenConstants.IF);
}
					case -66:
						break;
					case 66:
						{ //System.out.println("Got Keyword: " + yytext()); 
  return getKeywordSymbol(TokenConstants.IN);
}
					case -67:
						break;
					case 67:
						{ //System.out.println("Got Keyword: " + yytext()); 
  return getKeywordSymbol(TokenConstants.FI);
}
					case -68:
						break;
					case 68:
						{ //System.out.println("Got Keyword: " + yytext()); 
  return getKeywordSymbol(TokenConstants.OF);
}
					case -69:
						break;
					case 70:
						{ //System.out.println("Got Keyword: " + yytext()); 
  return getKeywordSymbol(TokenConstants.LET);
}
					case -70:
						break;
					case 71:
						{ //System.out.println("Got Keyword: " + yytext()); 
  return getKeywordSymbol(TokenConstants.NEW);
}
					case -71:
						break;
					case 72:
						{ //System.out.println("Got Keyword: " + yytext()); 
  return getKeywordSymbol(TokenConstants.NOT);
}
					case -72:
						break;
					case 73:
						{ //System.out.println("Got Keyword: " + yytext()); 
  return getKeywordSymbol(TokenConstants.CASE);
}
					case -73:
						break;
					case 74:
						{ //System.out.println("Got Keyword: " + yytext()); 
  return getKeywordSymbol(TokenConstants.LOOP);
}
					case -74:
						break;
					case 75:
						{ //System.out.println("Got Keyword: " + yytext()); 
  return getKeywordSymbol(TokenConstants.ELSE);
}
					case -75:
						break;
					case 76:
						{ //System.out.println("Got Keyword: " + yytext()); 
  return getKeywordSymbol(TokenConstants.ESAC);
}
					case -76:
						break;
					case 77:
						{ //System.out.println("Got Keyword: " + yytext()); 
  return getKeywordSymbol(TokenConstants.THEN);
}
					case -77:
						break;
					case 78:
						{ //System.out.println("Got Keyword: " + yytext()); 
  return getKeywordSymbol(TokenConstants.POOL);
}
					case -78:
						break;
					case 79:
						{ //System.out.println("Got Keyword: " + yytext()); 
  return getKeywordSymbol(TokenConstants.CLASS);
}
					case -79:
						break;
					case 80:
						{ //System.out.println("Got Keyword: " + yytext()); 
  return getKeywordSymbol(TokenConstants.WHILE);
}
					case -80:
						break;
					case 81:
						{ //System.out.println("Got Keyword: " + yytext()); 
  return getKeywordSymbol(TokenConstants.ISVOID);
}
					case -81:
						break;
					case 82:
						{ //System.out.println("Got Keyword: " + yytext()); 
  return getKeywordSymbol(TokenConstants.INHERITS);
}
					case -82:
						break;
					case 83:
						{
  // update lineno variable
  countLines(yytext());
}
					case -83:
						break;
					case 84:
						{ //System.out.println("Got Type IDs"); 
  String str = yytext();
  countLines(str);
  return getAbstractSymbol(str, TokenConstants.TYPEID);
}
					case -84:
						break;
					case 85:
						{
  //System.out.println("Found EOF: " + yytext());
  countLines(yytext());
  return new Symbol(TokenConstants.ERROR, "EOF in string constant");
}
					case -85:
						break;
					case 86:
						{ //System.out.println("Got Object IDs"); 
  String str = yytext();
  countLines(str);
  return getAbstractSymbol(str, TokenConstants.OBJECTID);
}
					case -86:
						break;
					case 87:
						{
  //System.out.println("String: " + yytext());
  String str = yytext();
  countLines(str);
  //System.out.println("Current Line #: "+ curr_lineno);
  return getStringSymbol(str.substring(1, str.length() - 1));
}
					case -87:
						break;
					case 88:
						{
  //System.out.println("Log - unterminated string constant: " + yytext());
  String str = yytext();
  countLines(str);
  if (str.contains("\0"))
    return new Symbol(TokenConstants.ERROR, "String contains null character.");
  return new Symbol(TokenConstants.ERROR, "Unterminated string constant");
}
					case -88:
						break;
					case 90:
						{
  // update lineno variable
  countLines(yytext());
}
					case -89:
						break;
					case 91:
						{ //System.out.println("Got Type IDs"); 
  String str = yytext();
  countLines(str);
  return getAbstractSymbol(str, TokenConstants.TYPEID);
}
					case -90:
						break;
					case 92:
						{
  //System.out.println("Found EOF: " + yytext());
  countLines(yytext());
  return new Symbol(TokenConstants.ERROR, "EOF in string constant");
}
					case -91:
						break;
					case 93:
						{ //System.out.println("Got Object IDs"); 
  String str = yytext();
  countLines(str);
  return getAbstractSymbol(str, TokenConstants.OBJECTID);
}
					case -92:
						break;
					case 94:
						{
  //System.out.println("String: " + yytext());
  String str = yytext();
  countLines(str);
  //System.out.println("Current Line #: "+ curr_lineno);
  return getStringSymbol(str.substring(1, str.length() - 1));
}
					case -93:
						break;
					case 95:
						{
  //System.out.println("Log - unterminated string constant: " + yytext());
  String str = yytext();
  countLines(str);
  if (str.contains("\0"))
    return new Symbol(TokenConstants.ERROR, "String contains null character.");
  return new Symbol(TokenConstants.ERROR, "Unterminated string constant");
}
					case -94:
						break;
					case 97:
						{
  // update lineno variable
  countLines(yytext());
}
					case -95:
						break;
					case 98:
						{ //System.out.println("Got Type IDs"); 
  String str = yytext();
  countLines(str);
  return getAbstractSymbol(str, TokenConstants.TYPEID);
}
					case -96:
						break;
					case 99:
						{
  //System.out.println("Found EOF: " + yytext());
  countLines(yytext());
  return new Symbol(TokenConstants.ERROR, "EOF in string constant");
}
					case -97:
						break;
					case 100:
						{ //System.out.println("Got Object IDs"); 
  String str = yytext();
  countLines(str);
  return getAbstractSymbol(str, TokenConstants.OBJECTID);
}
					case -98:
						break;
					case 102:
						{
  // update lineno variable
  countLines(yytext());
}
					case -99:
						break;
					case 103:
						{ //System.out.println("Got Type IDs"); 
  String str = yytext();
  countLines(str);
  return getAbstractSymbol(str, TokenConstants.TYPEID);
}
					case -100:
						break;
					case 104:
						{
  //System.out.println("Found EOF: " + yytext());
  countLines(yytext());
  return new Symbol(TokenConstants.ERROR, "EOF in string constant");
}
					case -101:
						break;
					case 105:
						{ //System.out.println("Got Object IDs"); 
  String str = yytext();
  countLines(str);
  return getAbstractSymbol(str, TokenConstants.OBJECTID);
}
					case -102:
						break;
					case 107:
						{
  // update lineno variable
  countLines(yytext());
}
					case -103:
						break;
					case 108:
						{ //System.out.println("Got Type IDs"); 
  String str = yytext();
  countLines(str);
  return getAbstractSymbol(str, TokenConstants.TYPEID);
}
					case -104:
						break;
					case 109:
						{ //System.out.println("Got Object IDs"); 
  String str = yytext();
  countLines(str);
  return getAbstractSymbol(str, TokenConstants.OBJECTID);
}
					case -105:
						break;
					case 111:
						{
  // update lineno variable
  countLines(yytext());
}
					case -106:
						break;
					case 112:
						{ //System.out.println("Got Type IDs"); 
  String str = yytext();
  countLines(str);
  return getAbstractSymbol(str, TokenConstants.TYPEID);
}
					case -107:
						break;
					case 113:
						{ //System.out.println("Got Object IDs"); 
  String str = yytext();
  countLines(str);
  return getAbstractSymbol(str, TokenConstants.OBJECTID);
}
					case -108:
						break;
					case 115:
						{
  // update lineno variable
  countLines(yytext());
}
					case -109:
						break;
					case 116:
						{ //System.out.println("Got Type IDs"); 
  String str = yytext();
  countLines(str);
  return getAbstractSymbol(str, TokenConstants.TYPEID);
}
					case -110:
						break;
					case 117:
						{ //System.out.println("Got Object IDs"); 
  String str = yytext();
  countLines(str);
  return getAbstractSymbol(str, TokenConstants.OBJECTID);
}
					case -111:
						break;
					case 118:
						{
  // update lineno variable
  countLines(yytext());
}
					case -112:
						break;
					case 119:
						{ //System.out.println("Got Type IDs"); 
  String str = yytext();
  countLines(str);
  return getAbstractSymbol(str, TokenConstants.TYPEID);
}
					case -113:
						break;
					case 120:
						{ //System.out.println("Got Object IDs"); 
  String str = yytext();
  countLines(str);
  return getAbstractSymbol(str, TokenConstants.OBJECTID);
}
					case -114:
						break;
					case 121:
						{ //System.out.println("Got Type IDs"); 
  String str = yytext();
  countLines(str);
  return getAbstractSymbol(str, TokenConstants.TYPEID);
}
					case -115:
						break;
					case 122:
						{ //System.out.println("Got Object IDs"); 
  String str = yytext();
  countLines(str);
  return getAbstractSymbol(str, TokenConstants.OBJECTID);
}
					case -116:
						break;
					case 123:
						{ //System.out.println("Got Type IDs"); 
  String str = yytext();
  countLines(str);
  return getAbstractSymbol(str, TokenConstants.TYPEID);
}
					case -117:
						break;
					case 124:
						{ //System.out.println("Got Object IDs"); 
  String str = yytext();
  countLines(str);
  return getAbstractSymbol(str, TokenConstants.OBJECTID);
}
					case -118:
						break;
					case 125:
						{ //System.out.println("Got Type IDs"); 
  String str = yytext();
  countLines(str);
  return getAbstractSymbol(str, TokenConstants.TYPEID);
}
					case -119:
						break;
					case 126:
						{ //System.out.println("Got Object IDs"); 
  String str = yytext();
  countLines(str);
  return getAbstractSymbol(str, TokenConstants.OBJECTID);
}
					case -120:
						break;
					case 127:
						{ //System.out.println("Got Type IDs"); 
  String str = yytext();
  countLines(str);
  return getAbstractSymbol(str, TokenConstants.TYPEID);
}
					case -121:
						break;
					case 128:
						{ //System.out.println("Got Object IDs"); 
  String str = yytext();
  countLines(str);
  return getAbstractSymbol(str, TokenConstants.OBJECTID);
}
					case -122:
						break;
					case 129:
						{ //System.out.println("Got Type IDs"); 
  String str = yytext();
  countLines(str);
  return getAbstractSymbol(str, TokenConstants.TYPEID);
}
					case -123:
						break;
					case 130:
						{ //System.out.println("Got Object IDs"); 
  String str = yytext();
  countLines(str);
  return getAbstractSymbol(str, TokenConstants.OBJECTID);
}
					case -124:
						break;
					case 131:
						{ //System.out.println("Got Type IDs"); 
  String str = yytext();
  countLines(str);
  return getAbstractSymbol(str, TokenConstants.TYPEID);
}
					case -125:
						break;
					case 132:
						{ //System.out.println("Got Object IDs"); 
  String str = yytext();
  countLines(str);
  return getAbstractSymbol(str, TokenConstants.OBJECTID);
}
					case -126:
						break;
					case 133:
						{ //System.out.println("Got Type IDs"); 
  String str = yytext();
  countLines(str);
  return getAbstractSymbol(str, TokenConstants.TYPEID);
}
					case -127:
						break;
					case 134:
						{ //System.out.println("Got Object IDs"); 
  String str = yytext();
  countLines(str);
  return getAbstractSymbol(str, TokenConstants.OBJECTID);
}
					case -128:
						break;
					case 135:
						{ //System.out.println("Got Type IDs"); 
  String str = yytext();
  countLines(str);
  return getAbstractSymbol(str, TokenConstants.TYPEID);
}
					case -129:
						break;
					case 136:
						{ //System.out.println("Got Object IDs"); 
  String str = yytext();
  countLines(str);
  return getAbstractSymbol(str, TokenConstants.OBJECTID);
}
					case -130:
						break;
					case 137:
						{ //System.out.println("Got Type IDs"); 
  String str = yytext();
  countLines(str);
  return getAbstractSymbol(str, TokenConstants.TYPEID);
}
					case -131:
						break;
					case 138:
						{ //System.out.println("Got Object IDs"); 
  String str = yytext();
  countLines(str);
  return getAbstractSymbol(str, TokenConstants.OBJECTID);
}
					case -132:
						break;
					case 139:
						{ //System.out.println("Got Type IDs"); 
  String str = yytext();
  countLines(str);
  return getAbstractSymbol(str, TokenConstants.TYPEID);
}
					case -133:
						break;
					case 140:
						{ //System.out.println("Got Object IDs"); 
  String str = yytext();
  countLines(str);
  return getAbstractSymbol(str, TokenConstants.OBJECTID);
}
					case -134:
						break;
					case 141:
						{ //System.out.println("Got Object IDs"); 
  String str = yytext();
  countLines(str);
  return getAbstractSymbol(str, TokenConstants.OBJECTID);
}
					case -135:
						break;
					case 142:
						{ //System.out.println("Got Object IDs"); 
  String str = yytext();
  countLines(str);
  return getAbstractSymbol(str, TokenConstants.OBJECTID);
}
					case -136:
						break;
					case 143:
						{ //System.out.println("Got equals op: " + yytext()); 
  return new Symbol(TokenConstants.EQ);
}
					case -137:
						break;
					case 144:
						{ //System.out.println("Got Type IDs"); 
  String str = yytext();
  countLines(str);
  return getAbstractSymbol(str, TokenConstants.TYPEID);
}
					case -138:
						break;
					case 145:
						{ //System.out.println("Got Object IDs"); 
  String str = yytext();
  countLines(str);
  return getAbstractSymbol(str, TokenConstants.OBJECTID);
}
					case -139:
						break;
					case 146:
						{
  // update lineno variable
  countLines(yytext());
}
					case -140:
						break;
					case 147:
						{ //System.out.println("Got Type IDs"); 
  String str = yytext();
  countLines(str);
  return getAbstractSymbol(str, TokenConstants.TYPEID);
}
					case -141:
						break;
					case 148:
						{ //System.out.println("Got Object IDs"); 
  String str = yytext();
  countLines(str);
  return getAbstractSymbol(str, TokenConstants.OBJECTID);
}
					case -142:
						break;
					case 149:
						{ //System.out.println("Got Type IDs"); 
  String str = yytext();
  countLines(str);
  return getAbstractSymbol(str, TokenConstants.TYPEID);
}
					case -143:
						break;
					case 150:
						{ //System.out.println("Got Object IDs"); 
  String str = yytext();
  countLines(str);
  return getAbstractSymbol(str, TokenConstants.OBJECTID);
}
					case -144:
						break;
					case 151:
						{ //System.out.println("Got Type IDs"); 
  String str = yytext();
  countLines(str);
  return getAbstractSymbol(str, TokenConstants.TYPEID);
}
					case -145:
						break;
					case 152:
						{ //System.out.println("Got Object IDs"); 
  String str = yytext();
  countLines(str);
  return getAbstractSymbol(str, TokenConstants.OBJECTID);
}
					case -146:
						break;
					case 153:
						{ //System.out.println("Got Type IDs"); 
  String str = yytext();
  countLines(str);
  return getAbstractSymbol(str, TokenConstants.TYPEID);
}
					case -147:
						break;
					case 154:
						{ //System.out.println("Got Object IDs"); 
  String str = yytext();
  countLines(str);
  return getAbstractSymbol(str, TokenConstants.OBJECTID);
}
					case -148:
						break;
					case 155:
						{ //System.out.println("Got Type IDs"); 
  String str = yytext();
  countLines(str);
  return getAbstractSymbol(str, TokenConstants.TYPEID);
}
					case -149:
						break;
					case 156:
						{ //System.out.println("Got Object IDs"); 
  String str = yytext();
  countLines(str);
  return getAbstractSymbol(str, TokenConstants.OBJECTID);
}
					case -150:
						break;
					case 157:
						{ //System.out.println("Got Type IDs"); 
  String str = yytext();
  countLines(str);
  return getAbstractSymbol(str, TokenConstants.TYPEID);
}
					case -151:
						break;
					case 158:
						{ //System.out.println("Got Object IDs"); 
  String str = yytext();
  countLines(str);
  return getAbstractSymbol(str, TokenConstants.OBJECTID);
}
					case -152:
						break;
					case 159:
						{ //System.out.println("Got Type IDs"); 
  String str = yytext();
  countLines(str);
  return getAbstractSymbol(str, TokenConstants.TYPEID);
}
					case -153:
						break;
					case 160:
						{ //System.out.println("Got Object IDs"); 
  String str = yytext();
  countLines(str);
  return getAbstractSymbol(str, TokenConstants.OBJECTID);
}
					case -154:
						break;
					case 161:
						{ //System.out.println("Got Type IDs"); 
  String str = yytext();
  countLines(str);
  return getAbstractSymbol(str, TokenConstants.TYPEID);
}
					case -155:
						break;
					case 162:
						{ //System.out.println("Got Object IDs"); 
  String str = yytext();
  countLines(str);
  return getAbstractSymbol(str, TokenConstants.OBJECTID);
}
					case -156:
						break;
					case 163:
						{ //System.out.println("Got Type IDs"); 
  String str = yytext();
  countLines(str);
  return getAbstractSymbol(str, TokenConstants.TYPEID);
}
					case -157:
						break;
					case 164:
						{ //System.out.println("Got Object IDs"); 
  String str = yytext();
  countLines(str);
  return getAbstractSymbol(str, TokenConstants.OBJECTID);
}
					case -158:
						break;
					case 165:
						{ //System.out.println("Got Type IDs"); 
  String str = yytext();
  countLines(str);
  return getAbstractSymbol(str, TokenConstants.TYPEID);
}
					case -159:
						break;
					case 166:
						{ //System.out.println("Got Object IDs"); 
  String str = yytext();
  countLines(str);
  return getAbstractSymbol(str, TokenConstants.OBJECTID);
}
					case -160:
						break;
					case 167:
						{ //System.out.println("Got Type IDs"); 
  String str = yytext();
  countLines(str);
  return getAbstractSymbol(str, TokenConstants.TYPEID);
}
					case -161:
						break;
					case 168:
						{ //System.out.println("Got Object IDs"); 
  String str = yytext();
  countLines(str);
  return getAbstractSymbol(str, TokenConstants.OBJECTID);
}
					case -162:
						break;
					case 169:
						{ //System.out.println("Got Type IDs"); 
  String str = yytext();
  countLines(str);
  return getAbstractSymbol(str, TokenConstants.TYPEID);
}
					case -163:
						break;
					case 170:
						{ //System.out.println("Got Object IDs"); 
  String str = yytext();
  countLines(str);
  return getAbstractSymbol(str, TokenConstants.OBJECTID);
}
					case -164:
						break;
					case 171:
						{ //System.out.println("Got Type IDs"); 
  String str = yytext();
  countLines(str);
  return getAbstractSymbol(str, TokenConstants.TYPEID);
}
					case -165:
						break;
					case 172:
						{ //System.out.println("Got Object IDs"); 
  String str = yytext();
  countLines(str);
  return getAbstractSymbol(str, TokenConstants.OBJECTID);
}
					case -166:
						break;
					case 173:
						{ //System.out.println("Got Type IDs"); 
  String str = yytext();
  countLines(str);
  return getAbstractSymbol(str, TokenConstants.TYPEID);
}
					case -167:
						break;
					case 174:
						{ //System.out.println("Got Object IDs"); 
  String str = yytext();
  countLines(str);
  return getAbstractSymbol(str, TokenConstants.OBJECTID);
}
					case -168:
						break;
					case 175:
						{ //System.out.println("Got Type IDs"); 
  String str = yytext();
  countLines(str);
  return getAbstractSymbol(str, TokenConstants.TYPEID);
}
					case -169:
						break;
					case 176:
						{ //System.out.println("Got Object IDs"); 
  String str = yytext();
  countLines(str);
  return getAbstractSymbol(str, TokenConstants.OBJECTID);
}
					case -170:
						break;
					case 177:
						{ //System.out.println("Got Object IDs"); 
  String str = yytext();
  countLines(str);
  return getAbstractSymbol(str, TokenConstants.OBJECTID);
}
					case -171:
						break;
					case 178:
						{ //System.out.println("Got Object IDs"); 
  String str = yytext();
  countLines(str);
  return getAbstractSymbol(str, TokenConstants.OBJECTID);
}
					case -172:
						break;
					case 179:
						{ //System.out.println("Got Object IDs"); 
  String str = yytext();
  countLines(str);
  return getAbstractSymbol(str, TokenConstants.OBJECTID);
}
					case -173:
						break;
					case 180:
						{ //System.out.println("Got Object IDs"); 
  String str = yytext();
  countLines(str);
  return getAbstractSymbol(str, TokenConstants.OBJECTID);
}
					case -174:
						break;
					case 181:
						{ //System.out.println("Got Object IDs"); 
  String str = yytext();
  countLines(str);
  return getAbstractSymbol(str, TokenConstants.OBJECTID);
}
					case -175:
						break;
					case 182:
						{ //System.out.println("Got Object IDs"); 
  String str = yytext();
  countLines(str);
  return getAbstractSymbol(str, TokenConstants.OBJECTID);
}
					case -176:
						break;
					case 183:
						{ //System.out.println("Got Object IDs"); 
  String str = yytext();
  countLines(str);
  return getAbstractSymbol(str, TokenConstants.OBJECTID);
}
					case -177:
						break;
					case 184:
						{ //System.out.println("Got Object IDs"); 
  String str = yytext();
  countLines(str);
  return getAbstractSymbol(str, TokenConstants.OBJECTID);
}
					case -178:
						break;
					case 185:
						{ //System.out.println("Got Object IDs"); 
  String str = yytext();
  countLines(str);
  return getAbstractSymbol(str, TokenConstants.OBJECTID);
}
					case -179:
						break;
					case 186:
						{ //System.out.println("Got Object IDs"); 
  String str = yytext();
  countLines(str);
  return getAbstractSymbol(str, TokenConstants.OBJECTID);
}
					case -180:
						break;
					case 187:
						{ //System.out.println("Got Object IDs"); 
  String str = yytext();
  countLines(str);
  return getAbstractSymbol(str, TokenConstants.OBJECTID);
}
					case -181:
						break;
					case 188:
						{ //System.out.println("Got Object IDs"); 
  String str = yytext();
  countLines(str);
  return getAbstractSymbol(str, TokenConstants.OBJECTID);
}
					case -182:
						break;
					case 189:
						{ 
  //System.out.println("Got whitespace"); 
  //int len = yytext().length();
  //System.out.println("Got whitespace of length: " + Integer.toString(len));
  countLines(yytext());
}
					case -183:
						break;
					case 190:
						{ //System.out.println("Got Type IDs"); 
  String str = yytext();
  countLines(str);
  return getAbstractSymbol(str, TokenConstants.TYPEID);
}
					case -184:
						break;
					case 191:
						{ //System.out.println("Got Object IDs"); 
  String str = yytext();
  countLines(str);
  return getAbstractSymbol(str, TokenConstants.OBJECTID);
}
					case -185:
						break;
					case 192:
						{ //System.out.println("Got Object IDs"); 
  String str = yytext();
  countLines(str);
  return getAbstractSymbol(str, TokenConstants.OBJECTID);
}
					case -186:
						break;
					case 193:
						{ //System.out.println("Got Type IDs"); 
  String str = yytext();
  countLines(str);
  return getAbstractSymbol(str, TokenConstants.TYPEID);
}
					case -187:
						break;
					case 194:
						{ //System.out.println("Got Object IDs"); 
  String str = yytext();
  countLines(str);
  return getAbstractSymbol(str, TokenConstants.OBJECTID);
}
					case -188:
						break;
					case 195:
						{ //System.out.println("Got Type IDs"); 
  String str = yytext();
  countLines(str);
  return getAbstractSymbol(str, TokenConstants.TYPEID);
}
					case -189:
						break;
					case 196:
						{ //System.out.println("Got Object IDs"); 
  String str = yytext();
  countLines(str);
  return getAbstractSymbol(str, TokenConstants.OBJECTID);
}
					case -190:
						break;
					case 197:
						{ //System.out.println("Got Type IDs"); 
  String str = yytext();
  countLines(str);
  return getAbstractSymbol(str, TokenConstants.TYPEID);
}
					case -191:
						break;
					case 198:
						{ //System.out.println("Got Object IDs"); 
  String str = yytext();
  countLines(str);
  return getAbstractSymbol(str, TokenConstants.OBJECTID);
}
					case -192:
						break;
					case 199:
						{ //System.out.println("Got Type IDs"); 
  String str = yytext();
  countLines(str);
  return getAbstractSymbol(str, TokenConstants.TYPEID);
}
					case -193:
						break;
					case 200:
						{ //System.out.println("Got Object IDs"); 
  String str = yytext();
  countLines(str);
  return getAbstractSymbol(str, TokenConstants.OBJECTID);
}
					case -194:
						break;
					case 201:
						{ //System.out.println("Got Type IDs"); 
  String str = yytext();
  countLines(str);
  return getAbstractSymbol(str, TokenConstants.TYPEID);
}
					case -195:
						break;
					case 202:
						{ //System.out.println("Got Object IDs"); 
  String str = yytext();
  countLines(str);
  return getAbstractSymbol(str, TokenConstants.OBJECTID);
}
					case -196:
						break;
					case 203:
						{ //System.out.println("Got Type IDs"); 
  String str = yytext();
  countLines(str);
  return getAbstractSymbol(str, TokenConstants.TYPEID);
}
					case -197:
						break;
					case 204:
						{ //System.out.println("Got Object IDs"); 
  String str = yytext();
  countLines(str);
  return getAbstractSymbol(str, TokenConstants.OBJECTID);
}
					case -198:
						break;
					case 205:
						{ //System.out.println("Got Type IDs"); 
  String str = yytext();
  countLines(str);
  return getAbstractSymbol(str, TokenConstants.TYPEID);
}
					case -199:
						break;
					case 206:
						{ //System.out.println("Got Type IDs"); 
  String str = yytext();
  countLines(str);
  return getAbstractSymbol(str, TokenConstants.TYPEID);
}
					case -200:
						break;
					case 207:
						{ //System.out.println("Got Type IDs"); 
  String str = yytext();
  countLines(str);
  return getAbstractSymbol(str, TokenConstants.TYPEID);
}
					case -201:
						break;
					case 208:
						{ //System.out.println("Got Type IDs"); 
  String str = yytext();
  countLines(str);
  return getAbstractSymbol(str, TokenConstants.TYPEID);
}
					case -202:
						break;
					case 209:
						{ //System.out.println("Got Object IDs"); 
  String str = yytext();
  countLines(str);
  return getAbstractSymbol(str, TokenConstants.OBJECTID);
}
					case -203:
						break;
					case 210:
						{ //System.out.println("Got Type IDs"); 
  String str = yytext();
  countLines(str);
  return getAbstractSymbol(str, TokenConstants.TYPEID);
}
					case -204:
						break;
					case 211:
						{ //System.out.println("Got Type IDs"); 
  String str = yytext();
  countLines(str);
  return getAbstractSymbol(str, TokenConstants.TYPEID);
}
					case -205:
						break;
					case 212:
						{ //System.out.println("Got Type IDs"); 
  String str = yytext();
  countLines(str);
  return getAbstractSymbol(str, TokenConstants.TYPEID);
}
					case -206:
						break;
					case 213:
						{ //System.out.println("Got Type IDs"); 
  String str = yytext();
  countLines(str);
  return getAbstractSymbol(str, TokenConstants.TYPEID);
}
					case -207:
						break;
					case 214:
						{ //System.out.println("Got Type IDs"); 
  String str = yytext();
  countLines(str);
  return getAbstractSymbol(str, TokenConstants.TYPEID);
}
					case -208:
						break;
					case 215:
						{ //System.out.println("Got Type IDs"); 
  String str = yytext();
  countLines(str);
  return getAbstractSymbol(str, TokenConstants.TYPEID);
}
					case -209:
						break;
					case 216:
						{ //System.out.println("Got Type IDs"); 
  String str = yytext();
  countLines(str);
  return getAbstractSymbol(str, TokenConstants.TYPEID);
}
					case -210:
						break;
					case 217:
						{ //System.out.println("Got Type IDs"); 
  String str = yytext();
  countLines(str);
  return getAbstractSymbol(str, TokenConstants.TYPEID);
}
					case -211:
						break;
					case 218:
						{ //System.out.println("Got Type IDs"); 
  String str = yytext();
  countLines(str);
  return getAbstractSymbol(str, TokenConstants.TYPEID);
}
					case -212:
						break;
					default:
						yy_error(YY_E_INTERNAL,false);
					case -1:
					}
					yy_initial = true;
					yy_state = yy_state_dtrans[yy_lexical_state];
					yy_next_state = YY_NO_STATE;
					yy_last_accept_state = YY_NO_STATE;
					yy_mark_start();
					yy_this_accept = yy_acpt[yy_state];
					if (YY_NOT_ACCEPT != yy_this_accept) {
						yy_last_accept_state = yy_state;
						yy_mark_end();
					}
				}
			}
		}
	}
}
