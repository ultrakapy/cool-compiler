/*
Copyright (c) 2000 The Regents of the University of California.
All rights reserved.

Permission to use, copy, modify, and distribute this software for any
purpose, without fee, and without written agreement is hereby granted,
provided that the above copyright notice and the following two
paragraphs appear in all copies of this software.

IN NO EVENT SHALL THE UNIVERSITY OF CALIFORNIA BE LIABLE TO ANY PARTY FOR
DIRECT, INDIRECT, SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES ARISING OUT
OF THE USE OF THIS SOFTWARE AND ITS DOCUMENTATION, EVEN IF THE UNIVERSITY OF
CALIFORNIA HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

THE UNIVERSITY OF CALIFORNIA SPECIFICALLY DISCLAIMS ANY WARRANTIES,
INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY
AND FITNESS FOR A PARTICULAR PURPOSE.  THE SOFTWARE PROVIDED HEREUNDER IS
ON AN "AS IS" BASIS, AND THE UNIVERSITY OF CALIFORNIA HAS NO OBLIGATION TO
PROVIDE MAINTENANCE, SUPPORT, UPDATES, ENHANCEMENTS, OR MODIFICATIONS.
*/

// This is a project skeleton file

import java.io.PrintStream;
import java.util.Vector;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Hashtable;
import java.util.ArrayList;

/** This class is used for representing the inheritance tree during code
    generation. You will need to fill in some of its methods and
    potentially extend it in other useful ways. */
class CgenClassTable extends SymbolTable {

    /** All classes in the program, represented as CgenNode */
    private Vector nds;

    /** This is the stream to which assembly instructions are output */
    private PrintStream str;

    private int stringclasstag;
    private int intclasstag;
    private int boolclasstag;

    private int objectclasstag;
    private int ioclasstag;
    private int mainclasstag;
    private int nextclasstag;

    Hashtable env = new Hashtable();

    // The following methods emit code for constants and global
    // declarations.

    /** Emits code to start the .data segment and to
     * declare the global names.
     * */
    private void codeGlobalData() {
	// The following global names must be defined first.

	str.print("\t.data\n" + CgenSupport.ALIGN);
	str.println(CgenSupport.GLOBAL + CgenSupport.CLASSNAMETAB);
	str.print(CgenSupport.GLOBAL); 
	CgenSupport.emitProtObjRef(TreeConstants.Main, str);
	str.println("");
	str.print(CgenSupport.GLOBAL); 
	CgenSupport.emitProtObjRef(TreeConstants.Int, str);
	str.println("");
	str.print(CgenSupport.GLOBAL); 
	CgenSupport.emitProtObjRef(TreeConstants.Str, str);
	str.println("");
	str.print(CgenSupport.GLOBAL); 
	BoolConst.falsebool.codeRef(str);
	str.println("");
	str.print(CgenSupport.GLOBAL); 
	BoolConst.truebool.codeRef(str);
	str.println("");
	str.println(CgenSupport.GLOBAL + CgenSupport.INTTAG);
	str.println(CgenSupport.GLOBAL + CgenSupport.BOOLTAG);
	str.println(CgenSupport.GLOBAL + CgenSupport.STRINGTAG);

	// We also need to know the tag of the Int, String, and Bool classes
	// during code generation.

	str.println(CgenSupport.INTTAG + CgenSupport.LABEL 
		    + CgenSupport.WORD + intclasstag);
	str.println(CgenSupport.BOOLTAG + CgenSupport.LABEL 
		    + CgenSupport.WORD + boolclasstag);
	str.println(CgenSupport.STRINGTAG + CgenSupport.LABEL 
		    + CgenSupport.WORD + stringclasstag);

    }

    /** Emits code to start the .text segment and to
     * declare the global names.
     * */
    private void codeGlobalText() {
	str.println(CgenSupport.GLOBAL + CgenSupport.HEAP_START);
	str.print(CgenSupport.HEAP_START + CgenSupport.LABEL);
	str.println(CgenSupport.WORD + 0);
	str.println("\t.text");
	str.print(CgenSupport.GLOBAL);
	CgenSupport.emitInitRef(TreeConstants.Main, str);
	str.println("");
	str.print(CgenSupport.GLOBAL);
	CgenSupport.emitInitRef(TreeConstants.Int, str);
	str.println("");
	str.print(CgenSupport.GLOBAL);
	CgenSupport.emitInitRef(TreeConstants.Str, str);
	str.println("");
	str.print(CgenSupport.GLOBAL);
	CgenSupport.emitInitRef(TreeConstants.Bool, str);
	str.println("");
	str.print(CgenSupport.GLOBAL);
	CgenSupport.emitMethodRef(TreeConstants.Main, TreeConstants.main_meth, str);
	str.println("");
    }

    /** Emits code definitions for boolean constants. */
    private void codeBools(int classtag) {
	BoolConst.falsebool.codeDef(classtag, str);
	BoolConst.truebool.codeDef(classtag, str);
    }

    /** Generates GC choice constants (pointers to GC functions) */
    private void codeSelectGc() {
	str.println(CgenSupport.GLOBAL + "_MemMgr_INITIALIZER");
	str.println("_MemMgr_INITIALIZER:");
	str.println(CgenSupport.WORD 
		    + CgenSupport.gcInitNames[Flags.cgen_Memmgr]);

	str.println(CgenSupport.GLOBAL + "_MemMgr_COLLECTOR");
	str.println("_MemMgr_COLLECTOR:");
	str.println(CgenSupport.WORD 
		    + CgenSupport.gcCollectNames[Flags.cgen_Memmgr]);

	str.println(CgenSupport.GLOBAL + "_MemMgr_TEST");
	str.println("_MemMgr_TEST:");
	str.println(CgenSupport.WORD 
		    + ((Flags.cgen_Memmgr_Test == Flags.GC_TEST) ? "1" : "0"));
    }

    /** Emits code to reserve space for and initialize all of the
     * constants.  Class names should have been added to the string
     * table (in the supplied code, is is done during the construction
     * of the inheritance graph), and code for emitting string constants
     * as a side effect adds the string's length to the integer table.
     * The constants are emmitted by running through the stringtable and
     * inttable and producing code for each entry. */
    private void codeConstants() {
	// Add constants that are required by the code generator.
	AbstractTable.stringtable.addString("");
	AbstractTable.inttable.addString("0");

	AbstractTable.stringtable.codeStringTable(stringclasstag, str);
	AbstractTable.inttable.codeStringTable(intclasstag, str);
	codeBools(boolclasstag);
    }


    /** Creates data structures representing basic Cool classes (Object,
     * IO, Int, Bool, String).  Please note: as is this method does not
     * do anything useful; you will need to edit it to make if do what
     * you want.
     * */
    private void installBasicClasses() {
	AbstractSymbol filename 
	    = AbstractTable.stringtable.addString("<basic class>");
	
	// A few special class names are installed in the lookup table
	// but not the class list.  Thus, these classes exist, but are
	// not part of the inheritance hierarchy.  No_class serves as
	// the parent of Object and the other special classes.
	// SELF_TYPE is the self class; it cannot be redefined or
	// inherited.  prim_slot is a class known to the code generator.

	addId(TreeConstants.No_class,
	      new CgenNode(new class_(0,
				      TreeConstants.No_class,
				      TreeConstants.No_class,
				      new Features(0),
				      filename),
			   CgenNode.Basic, this));

	addId(TreeConstants.SELF_TYPE,
	      new CgenNode(new class_(0,
				      TreeConstants.SELF_TYPE,
				      TreeConstants.No_class,
				      new Features(0),
				      filename),
			   CgenNode.Basic, this));
	
	addId(TreeConstants.prim_slot,
	      new CgenNode(new class_(0,
				      TreeConstants.prim_slot,
				      TreeConstants.No_class,
				      new Features(0),
				      filename),
			   CgenNode.Basic, this));

	// The Object class has no parent class. Its methods are
	//        cool_abort() : Object    aborts the program
	//        type_name() : Str        returns a string representation 
	//                                 of class name
	//        copy() : SELF_TYPE       returns a copy of the object

	class_ Object_class = 
	    new class_(0, 
		       TreeConstants.Object_, 
		       TreeConstants.No_class,
		       new Features(0)
			   .appendElement(new method(0, 
					      TreeConstants.cool_abort, 
					      new Formals(0), 
					      TreeConstants.Object_, 
					      new no_expr(0)))
			   .appendElement(new method(0,
					      TreeConstants.type_name,
					      new Formals(0),
					      TreeConstants.Str,
					      new no_expr(0)))
			   .appendElement(new method(0,
					      TreeConstants.copy,
					      new Formals(0),
					      TreeConstants.SELF_TYPE,
					      new no_expr(0))),
		       filename);

	installClass(new CgenNode(Object_class, CgenNode.Basic, this));
	
	// The IO class inherits from Object. Its methods are
	//        out_string(Str) : SELF_TYPE  writes a string to the output
	//        out_int(Int) : SELF_TYPE      "    an int    "  "     "
	//        in_string() : Str            reads a string from the input
	//        in_int() : Int                "   an int     "  "     "

	class_ IO_class = 
	    new class_(0,
		       TreeConstants.IO,
		       TreeConstants.Object_,
		       new Features(0)
			   .appendElement(new method(0,
					      TreeConstants.out_string,
					      new Formals(0)
						  .appendElement(new formal(0,
								     TreeConstants.arg,
								     TreeConstants.Str)),
					      TreeConstants.SELF_TYPE,
					      new no_expr(0)))
			   .appendElement(new method(0,
					      TreeConstants.out_int,
					      new Formals(0)
						  .appendElement(new formal(0,
								     TreeConstants.arg,
								     TreeConstants.Int)),
					      TreeConstants.SELF_TYPE,
					      new no_expr(0)))
			   .appendElement(new method(0,
					      TreeConstants.in_string,
					      new Formals(0),
					      TreeConstants.Str,
					      new no_expr(0)))
			   .appendElement(new method(0,
					      TreeConstants.in_int,
					      new Formals(0),
					      TreeConstants.Int,
					      new no_expr(0))),
		       filename);

	installClass(new CgenNode(IO_class, CgenNode.Basic, this));

	// The Int class has no methods and only a single attribute, the
	// "val" for the integer.

	class_ Int_class = 
	    new class_(0,
		       TreeConstants.Int,
		       TreeConstants.Object_,
		       new Features(0)
			   .appendElement(new attr(0,
					    TreeConstants.val,
					    TreeConstants.prim_slot,
					    new no_expr(0))),
		       filename);

	installClass(new CgenNode(Int_class, CgenNode.Basic, this));

	// Bool also has only the "val" slot.
	class_ Bool_class = 
	    new class_(0,
		       TreeConstants.Bool,
		       TreeConstants.Object_,
		       new Features(0)
			   .appendElement(new attr(0,
					    TreeConstants.val,
					    TreeConstants.prim_slot,
					    new no_expr(0))),
		       filename);

	installClass(new CgenNode(Bool_class, CgenNode.Basic, this));

	// The class Str has a number of slots and operations:
	//       val                              the length of the string
	//       str_field                        the string itself
	//       length() : Int                   returns length of the string
	//       concat(arg: Str) : Str           performs string concatenation
	//       substr(arg: Int, arg2: Int): Str substring selection

	class_ Str_class =
	    new class_(0,
		       TreeConstants.Str,
		       TreeConstants.Object_,
		       new Features(0)
			   .appendElement(new attr(0,
					    TreeConstants.val,
					    TreeConstants.Int,
					    new no_expr(0)))
			   .appendElement(new attr(0,
					    TreeConstants.str_field,
					    TreeConstants.prim_slot,
					    new no_expr(0)))
			   .appendElement(new method(0,
					      TreeConstants.length,
					      new Formals(0),
					      TreeConstants.Int,
					      new no_expr(0)))
			   .appendElement(new method(0,
					      TreeConstants.concat,
					      new Formals(0)
						  .appendElement(new formal(0,
								     TreeConstants.arg, 
								     TreeConstants.Str)),
					      TreeConstants.Str,
					      new no_expr(0)))
			   .appendElement(new method(0,
					      TreeConstants.substr,
					      new Formals(0)
						  .appendElement(new formal(0,
								     TreeConstants.arg,
								     TreeConstants.Int))
						  .appendElement(new formal(0,
								     TreeConstants.arg2,
								     TreeConstants.Int)),
					      TreeConstants.Str,
					      new no_expr(0))),
		       filename);

	installClass(new CgenNode(Str_class, CgenNode.Basic, this));
    }
	
    // The following creates an inheritance graph from
    // a list of classes.  The graph is implemented as
    // a tree of `CgenNode', and class names are placed
    // in the base class symbol table.
    
    private void installClass(CgenNode nd) {
	AbstractSymbol name = nd.getName();
	if (probe(name) != null) return;
	nds.addElement(nd);
	addId(name, nd);
    }

    private void installClasses(Classes cs) {
        for (Enumeration e = cs.getElements(); e.hasMoreElements(); ) {
	    installClass(new CgenNode((Class_)e.nextElement(), 
				       CgenNode.NotBasic, this));
        }
    }

    private void buildInheritanceTree() {
	for (Enumeration e = nds.elements(); e.hasMoreElements(); ) {
	    setRelations((CgenNode)e.nextElement());
	}
    }

    private void setRelations(CgenNode nd) {
	CgenNode parent = (CgenNode)probe(nd.getParent());
	nd.setParentNd(parent);
	parent.addChild(nd);
    }

    /** Constructs a new class table and invokes the code generator */
    public CgenClassTable(Classes cls, PrintStream str) {
	nds = new Vector();

	this.str = str;

	stringclasstag = 4 /* Change to your String class tag here */;
	intclasstag =    2 /* Change to your Int class tag here */;
	boolclasstag =   3 /* Change to your Bool class tag here */;

	objectclasstag = 0;
	ioclasstag = 1;
	mainclasstag = 5;
	nextclasstag = 6;

	enterScope();
	if (Flags.cgen_debug) System.out.println("Building CgenClassTable");
	
	installBasicClasses();
	installClasses(cls);
	buildInheritanceTree();

	code();

	exitScope();
    }

    /** This method is the meat of the code generator.  It is to be
        filled in programming assignment 5 */
    public void code() {
	if (Flags.cgen_debug) System.out.println("coding global data");
	codeGlobalData();

	if (Flags.cgen_debug) System.out.println("choosing gc");
	codeSelectGc();

	if (Flags.cgen_debug) System.out.println("coding constants");
	codeConstants();

	//                 Add your code to emit
	//                   - prototype objects
	//                   - class_nameTab
	//                   - dispatch tables

	// emit class_nameTab
	str.print(CgenSupport.CLASSNAMETAB + CgenSupport.LABEL);
	StringSymbol stringSym = null;
	for (int i = 6; i < AbstractTable.stringtable.tbl.size(); i++) {
	    stringSym = (StringSymbol)AbstractTable.stringtable.tbl.
		elementAt(i);
	    //System.out.println((StringSymbol)AbstractTable.stringtable.tbl.
	    //elementAt(i));
	    if (!stringSym.toString().
		equals(TreeConstants.No_class.toString()) && 
		!stringSym.toString().
		equals(TreeConstants.prim_slot.toString()) &&
		!stringSym.toString().
		equals(TreeConstants.SELF_TYPE.toString())) {
		//System.out.println("What? " + stringSym + "==" + TreeConstants.No_class);
		str.print(CgenSupport.WORD); 
		stringSym.codeRef(str);
		str.println("");
	    }
	}

	// emit class_objTab
	str.print(CgenSupport.CLASSOBJTAB + CgenSupport.LABEL);
	for (Object nd : nds) {
	    CgenNode c = (CgenNode)nd;
	    env.put("currClass", c);

	    // _protObj
	    str.print(CgenSupport.WORD + c.getName() 
		      + CgenSupport.PROTOBJ_SUFFIX); str.println();

	    // _init
	    str.print(CgenSupport.WORD + c.getName() 
		      + CgenSupport.CLASSINIT_SUFFIX); str.println();
	}

	// emit dispatch tables
	for (Object nd : nds) {
	    CgenNode c = (CgenNode)nd;
	    str.print(c.getName() + CgenSupport.DISPTAB_SUFFIX + 
		      CgenSupport.LABEL);

	    LinkedHashMap methods = new LinkedHashMap<String, String>(); 
	    getClassMethods(c, methods);

	    env.put(c.getName() + "-methods", methods.values());

	    int dispTableOffset = 0;
	    for (Object m : methods.values()) {
		str.print(CgenSupport.WORD + (String)m); str.println();
		CgenSupport.dispTableOffsets.put(c.getName() + 
						 "." + m.toString(), 
						 dispTableOffset++);
	    }
	}
	
	// emit prototype objects
	for (Object nd : nds) {
	    CgenNode c = (CgenNode)nd;
	    str.println(CgenSupport.WORD + "-1");
	    str.print(c.getName() + CgenSupport.PROTOBJ_SUFFIX + 
		      CgenSupport.LABEL);
	    
	    // class tag
	    if (c.getName().equals(TreeConstants.Object_)) {
		str.println(CgenSupport.WORD + 
			    Integer.toString(objectclasstag));
	    } else if (c.getName().equals(TreeConstants.IO)) {
		str.println(CgenSupport.WORD + Integer.toString(ioclasstag));
	    } else if (c.getName().equals(TreeConstants.Main)) {
		str.println(CgenSupport.WORD + Integer.toString(mainclasstag));
	    } else if (c.getName().equals(TreeConstants.Int)) {
		str.println(CgenSupport.WORD + Integer.toString(intclasstag));
	    } else if (c.getName().equals(TreeConstants.Bool)) {
		str.println(CgenSupport.WORD + Integer.toString(boolclasstag));
	    } else if (c.getName().equals(TreeConstants.Str)) {
		str.println(CgenSupport.WORD + 
			    Integer.toString(stringclasstag));
	    } else {
		str.println(CgenSupport.WORD + Integer.toString(nextclasstag));
		nextclasstag++;
	    }

	    // object size
	    int protObjSize = 3 + getProtObjAttrCount(c);
	    str.println(CgenSupport.WORD + Integer.toString(protObjSize));

	    // dispatch pointer
	    str.println(CgenSupport.WORD + 
			c.getName() + CgenSupport.DISPTAB_SUFFIX);

	    // attributes
	    env.put(c.getName() + "-attrs", new Vector());

	    if (c.getName().equals(TreeConstants.Str)) {
		str.println(CgenSupport.WORD + CgenSupport.INTCONST_PREFIX + 
			    AbstractTable.inttable.lookup("0").index);
		str.println(CgenSupport.WORD + "0");

		// TODO??: Add string attributes to env[class-attrs]
	    } else {
		//System.out.println("here, c is : " + c.getName());
		setProtObjAttrs(c);
	    }
	}

	if (Flags.cgen_debug) System.out.println("coding global text");
	codeGlobalText();

	//                 Add your code to emit
	//                   - object initializer
	//                   - the class methods
	//                   - etc...

	// emit object inits
	for (Object nd : nds) {
	    CgenNode c = (CgenNode)nd;
	    emitObjInit(c);
	}

	// emit class method definitions
	for (Object nd : nds) {
	    CgenNode c = (CgenNode)nd;

	    if (!c.getName().equals(TreeConstants.Object_) 
		&& !c.getName().equals(TreeConstants.IO)
		&& !c.getName().equals(TreeConstants.Int)
		&& !c.getName().equals(TreeConstants.Bool)
		&& !c.getName().equals(TreeConstants.Str)) {
		for (Enumeration fe = c.getFeatures().getElements();
		     fe.hasMoreElements(); ) {
		    Object e = fe.nextElement();
		
		    if (e instanceof method) {
			method m = (method)e;

			int oldFramePtr = 3 + getMaxLetCount(m.expr);
			
			CgenSupport.emitMethodRef(c.getName(), m.name, str);
			str.print(CgenSupport.LABEL);
			CgenSupport.emitAddiu(CgenSupport.SP, CgenSupport.SP, 
					      (-1 * 4 * oldFramePtr),
					      str);
			CgenSupport.emitStore(CgenSupport.FP, oldFramePtr, 
					      CgenSupport.SP, str);
			CgenSupport.emitStore(CgenSupport.SELF, oldFramePtr-1, 
					      CgenSupport.SP, str);
			CgenSupport.emitStore(CgenSupport.RA, oldFramePtr-2, 
					      CgenSupport.SP, str);
			CgenSupport.emitAddiu(CgenSupport.FP, CgenSupport.SP, 
					      4, str);
			CgenSupport.emitMove(CgenSupport.SELF, 
					     CgenSupport.ACC, str);
			//System.out.println(env);
			Hashtable newEnv = (Hashtable)env.clone();
			newEnv.put("currClass", c);
			newEnv.put("currMethod", m);
			newEnv.put("currLetCount", getMaxLetCount(m.expr));
			m.expr.code(str, newEnv);
		    
			CgenSupport.emitLoad(CgenSupport.FP, oldFramePtr, 
					     CgenSupport.SP, str);
			CgenSupport.emitLoad(CgenSupport.SELF, oldFramePtr-1, 
					     CgenSupport.SP, str);
			CgenSupport.emitLoad(CgenSupport.RA, oldFramePtr-2, 
					     CgenSupport.SP, str);
			CgenSupport.emitAddiu(CgenSupport.SP, CgenSupport.SP, 
					      (4 * oldFramePtr) + 
					      (4 * m.formals.getLength()),
					      str);
			CgenSupport.emitReturn(str);
		    }
		}
	    }
	}
    }

    public int getMaxLetCount(Expression e) {
	if (e instanceof assign) {
	    assign assign = (assign)e;
	    return getMaxLetCount(assign.expr);
	}

	if (e instanceof block) {
	    block block = (block)e;
	    
	    int maxBlockLets = 0;
	    for (Enumeration es = block.body.getElements(); 
		 es.hasMoreElements(); ) {
		Expression expr = (Expression)es.nextElement();
		int letsInOneExpr = getMaxLetCount(expr);
		maxBlockLets = Math.max(letsInOneExpr, maxBlockLets);
	    }
	    return maxBlockLets;
	}

	if (e instanceof bool_const) {
	    return 0;
	}

	if (e instanceof comp) {
	    comp comp = (comp)e;
	    return getMaxLetCount(comp.e1);
	}

	if (e instanceof cond) {
	    cond cond = (cond)e;
	    int pred_lets = getMaxLetCount(cond.pred);
	    int then_lets = getMaxLetCount(cond.then_exp);
	    int else_lets = getMaxLetCount(cond.else_exp);
	    return Math.max(pred_lets, Math.max(then_lets, else_lets));
	}

	if (e instanceof dispatch) {
	    dispatch dispatch = (dispatch)e;
	    int expr_lets = getMaxLetCount(dispatch.expr);
	    
	    int maxDispatchLets = expr_lets;
	    for (Enumeration es = dispatch.actual.getElements(); 
		 es.hasMoreElements(); ) {
		Expression expr = (Expression)es.nextElement();
		int letsInOneExpr = getMaxLetCount(expr);
		maxDispatchLets = Math.max(letsInOneExpr, maxDispatchLets);
	    }
	    return maxDispatchLets;
	}

	if (e instanceof divide) {
	    divide divide = (divide)e;
	    int lets_e1 = getMaxLetCount(divide.e1);
	    int lets_e2 = getMaxLetCount(divide.e2);
	    return Math.max(lets_e1, lets_e2);
	}

	if (e instanceof eq) {
	    eq eq = (eq)e;
	    int lets_e1 = getMaxLetCount(eq.e1);
	    int lets_e2 = getMaxLetCount(eq.e2);
	    return Math.max(lets_e1, lets_e2);
	}

	if (e instanceof int_const) {
	    return 0;
	}

	if (e instanceof isvoid) {
	    isvoid isvoid = (isvoid)e;
	    return getMaxLetCount(isvoid.e1);
	}

	if (e instanceof leq) {
	    leq leq = (leq)e;
	    int lets_e1 = getMaxLetCount(leq.e1);
	    int lets_e2 = getMaxLetCount(leq.e2);
	    return Math.max(lets_e1, lets_e2);
	}

	if (e instanceof let) {
	    let let = (let)e;
	    int init_lets = getMaxLetCount(let.init);
	    int body_lets = 1 + getMaxLetCount(let.body);
	    return Math.max(init_lets, body_lets);
	}

	if (e instanceof loop) {
	    loop loop = (loop)e;
	    int pred_lets = getMaxLetCount(loop.pred);
	    int body_lets = getMaxLetCount(loop.body);
	    return Math.max(pred_lets, body_lets);
	}

	if (e instanceof lt) {
	    lt lt = (lt)e;
	    int lets_e1 = getMaxLetCount(lt.e1);
	    int lets_e2 = getMaxLetCount(lt.e2);
	    return Math.max(lets_e1, lets_e2);
	}

	if (e instanceof mul) {
	    mul mul = (mul)e;
	    int lets_e1 = getMaxLetCount(mul.e1);
	    int lets_e2 = getMaxLetCount(mul.e2);
	    return Math.max(lets_e1, lets_e2);
	}

	if (e instanceof neg) {
	    neg neg = (neg)e;
	    return getMaxLetCount(neg.e1);
	}

	if (e instanceof new_) {
	    return 0;
	}

	if (e instanceof no_expr) {
	    return 0;
	}

	if (e instanceof object) {
	    return 0;
	}

	if (e instanceof plus) {
	    plus plus = (plus)e;
	    int lets_e1 = getMaxLetCount(plus.e1);
	    int lets_e2 = getMaxLetCount(plus.e2);
	    return Math.max(lets_e1, lets_e2);
	}

	if (e instanceof static_dispatch) {
	    static_dispatch static_dispatch = (static_dispatch)e;
	    int expr_lets = getMaxLetCount(static_dispatch.expr);
	    
	    int maxDispatchLets = expr_lets;
	    for (Enumeration es = static_dispatch.actual.getElements(); 
		 es.hasMoreElements(); ) {
		Expression expr = (Expression)es.nextElement();
		int letsInOneExpr = getMaxLetCount(expr);
		maxDispatchLets = Math.max(letsInOneExpr, maxDispatchLets);
	    }
	    return maxDispatchLets;
	}

	if (e instanceof string_const) {
	    return 0;
	}

	if (e instanceof sub) {
	    sub sub = (sub)e;
	    int lets_e1 = getMaxLetCount(sub.e1);
	    int lets_e2 = getMaxLetCount(sub.e2);
	    return Math.max(lets_e1, lets_e2);
	}

	if (e instanceof typcase) {
	    typcase typcase = (typcase)e;
	    int expr_lets = getMaxLetCount(typcase.expr);
	    
	    int maxCaseLets = expr_lets;
	    for (Enumeration cases = typcase.cases.getElements();
		 cases.hasMoreElements(); ) {
		branch branch = (branch)cases.nextElement();
		int letsInOneCaseBranch = getMaxLetCount(branch.expr);
		maxCaseLets = Math.max(letsInOneCaseBranch, maxCaseLets);
	    }
	    return maxCaseLets;
	}
		
	// TODO -- Confirm: this should never happen
	System.out.println("Oops.. getMaxLetCount is wrong: " + e);
	return 10000;
    }

    public void emitObjInit(CgenNode c) {	
	AbstractSymbol sym = c.getName();
	Hashtable newEnv = (Hashtable)env.clone();
	newEnv.put("currClass", c);

	CgenSupport.emitInitRef(sym, str); str.print(CgenSupport.LABEL);

	// calculate max # of lets in class attrs
	int maxLetsInClassAttr = 0;
	for (Enumeration fe = c.getFeatures().getElements();
	     fe.hasMoreElements(); ) {
	    Object e = fe.nextElement();	    
	    if (e instanceof attr) {
		attr a = (attr)e;
		int letsInAttrInit = getMaxLetCount(a.init);
		maxLetsInClassAttr = Math.max(letsInAttrInit, 
					      maxLetsInClassAttr);
	    }
	}

	int oldFramePtr = 3 + maxLetsInClassAttr;

	CgenSupport.emitAddiu(CgenSupport.SP, CgenSupport.SP,
			      (-1 * 4 * oldFramePtr), str);
	CgenSupport.emitStore(CgenSupport.FP, oldFramePtr, CgenSupport.SP, str);
	CgenSupport.emitStore(CgenSupport.SELF, oldFramePtr-1, CgenSupport.SP, 
			      str);
	CgenSupport.emitStore(CgenSupport.RA, oldFramePtr-2, CgenSupport.SP, 
			      str);
	CgenSupport.emitAddiu(CgenSupport.FP, CgenSupport.SP, 4, str);
	CgenSupport.emitMove(CgenSupport.SELF, CgenSupport.ACC, str);

	int parentAttrCount = 0;

	if (!sym.equals(TreeConstants.Object_)) {
	    CgenSupport.emitJal(c.getParentNd().getName() + 
				CgenSupport.CLASSINIT_SUFFIX, str);
	    parentAttrCount = getProtObjAttrCount(c.getParentNd());
	    //System.out.println(sym + " parentAttrCount: " + parentAttrCount);
	}

	// emit code to init attributes

	// first attribute (offset 3 words or 12 bytes) + parent attr count
	int offset = 3 + parentAttrCount;

	for (Enumeration fe = c.getFeatures().getElements();
	     fe.hasMoreElements(); ) {
	    Object e = fe.nextElement();
	    
	    if (e instanceof attr) {
		attr a = (attr)e;

		if (!(a.init instanceof no_expr)) {
		    a.init.code(str, newEnv);
		    CgenSupport.emitStore(CgenSupport.ACC, offset, 
					  CgenSupport.SELF, str);
		}
		offset += 1; // point to next attribute
	    }
	}

	CgenSupport.emitMove(CgenSupport.ACC, CgenSupport.SELF, str);
	CgenSupport.emitLoad(CgenSupport.FP, oldFramePtr, CgenSupport.SP, str);
	CgenSupport.emitLoad(CgenSupport.SELF, oldFramePtr-1, CgenSupport.SP, 
			     str);
	CgenSupport.emitLoad(CgenSupport.RA, oldFramePtr-2, CgenSupport.SP, 
			     str);
	CgenSupport.emitAddiu(CgenSupport.SP, CgenSupport.SP,
			      (4 * oldFramePtr), str);
	CgenSupport.emitReturn(str);
    }

    public int getProtObjAttrCount(CgenNode c) {
	int attrcount = 0;

	if (c.getParentNd().getName().equals(TreeConstants.No_class)) {
	    for (Enumeration fe = c.getFeatures().getElements();
		 fe.hasMoreElements(); ) {
		Object e = fe.nextElement();
		
		if (e instanceof attr) {
		    attrcount++;
		}
	    }
	    return attrcount;
	} 

	attrcount += getProtObjAttrCount(c.getParentNd());

	for (Enumeration fe = c.getFeatures().getElements();
	     fe.hasMoreElements(); ) {
	    Object e = fe.nextElement();
	    
	    if (e instanceof attr) {
		attrcount++;
	    }
	}
	return attrcount;
    }

    public void setProtObjAttrs(CgenNode c) {
	//System.out.println("Setting class: " + c.getName());
	if (c.getParentNd().getName().equals(TreeConstants.No_class)) {
	    for (Enumeration fe = c.getFeatures().getElements();
		 fe.hasMoreElements(); ) {
		Object e = fe.nextElement();
		
		if (e instanceof attr) {
		    attr attr = (attr)e;

		    if (attr.type_decl.equals(TreeConstants.Int)) {
			str.print(CgenSupport.WORD);
			((IntSymbol)AbstractTable.inttable.lookup("0")).
			    codeRef(str);
			str.println();
		    } else if (attr.type_decl.equals(TreeConstants.Bool)) {
			str.print(CgenSupport.WORD);
			(new BoolConst(false)).codeRef(str); str.println();
		    } else if (attr.type_decl.equals(TreeConstants.Str)) {
			str.print(CgenSupport.WORD);
			((StringSymbol)AbstractTable.stringtable.lookup("")).
			    codeRef(str);
			str.println();
		    } else {
			str.println(CgenSupport.WORD + "0");
		    }
		    
		    Vector currClassAttrs = (Vector)env.get(c.getName() + 
							    "-attrs");
		    if (currClassAttrs != null &&
			!currClassAttrs.contains(attr.name.toString()) && 
			!c.basic()) {
			currClassAttrs.add(attr.name.toString());
			env.put(c.getName() + "-attrs", currClassAttrs);
		    }
		}
	    }
	    return;
	} 

	setProtObjAttrs(c.getParentNd());
	
	for (Enumeration fe = c.getFeatures().getElements();
	     fe.hasMoreElements(); ) {
	    Object e = fe.nextElement();
	    
	    if (e instanceof attr) {
		attr attr = (attr)e;

		if (attr.type_decl.equals(TreeConstants.Int)) {
		    str.print(CgenSupport.WORD);
		    ((IntSymbol)AbstractTable.inttable.lookup("0")).
			codeRef(str);
		    str.println();
		} else if (attr.type_decl.equals(TreeConstants.Bool)) {
		    str.print(CgenSupport.WORD);
		    (new BoolConst(false)).codeRef(str); str.println();
		} else if (attr.type_decl.equals(TreeConstants.Str)) {
		    str.print(CgenSupport.WORD);
		    ((StringSymbol)AbstractTable.stringtable.lookup("")).
			codeRef(str);
		    str.println();
		} else {
		    str.println(CgenSupport.WORD + "0");
		}

		Vector currClassAttrs = (Vector)env.get(c.getName() + "-attrs");
		if (currClassAttrs != null && 
		    !currClassAttrs.contains(attr.name.toString()) && 
		    !c.basic()) {
		    currClassAttrs.add(attr.name.toString());
		    env.put(c.getName() + "-attrs", currClassAttrs);
		}
	    }
	}
	
	// add parent attrs to current class
	Vector parentAttrs = (Vector)
	    ((Vector)env.get(c.getParentNd().getName() + "-attrs")).clone();

	if (parentAttrs != null) {
	    if (env.get(c.getName() + "-attrs") != null) {
		parentAttrs.addAll((Vector)env.get(c.getName() + "-attrs"));
	    }
	    env.put(c.getName() + "-attrs", parentAttrs);
	}
	//System.out.println(c.getName() + "-attrs: " + env.get(c.getName() + "-attrs"));
    }

    /* Get all methods of a class (including those inherited in order) */
    public void getClassMethods(CgenNode c, 
				LinkedHashMap<String, String> methods) {
	if (c.getParentNd().getName().equals(TreeConstants.No_class)) {
	    for (Enumeration fe = c.getFeatures().getElements();
		 fe.hasMoreElements(); ) {
		Object e = fe.nextElement();
		
		if (e instanceof method) {
		    method m = (method)e;
		    methods.put(m.name.toString(), c.getName() + "." + m.name);

		    // add method sigs to env
		    ArrayList<String> sig = new ArrayList<String>();
		
		    for (Enumeration eFormals = m.formals.getElements(); 
			 eFormals.hasMoreElements();) {
			formal param = (formal)eFormals.nextElement();
			sig.add(param.name.toString());
		    }
		    env.put(c.getName() + "-" + m.name.toString() + "-sigs",
			    sig);
		}
		
	    }
	    return;
	} 
	
	getClassMethods(c.getParentNd(), methods);

	for (Enumeration fe = c.getFeatures().getElements();
	     fe.hasMoreElements(); ) {
	    Object e = fe.nextElement();

	    if (e instanceof method) {
		method m = (method)e;
		methods.put(m.name.toString(), c.getName() + "." + m.name);

		// add method sigs to env
		ArrayList<String> sig = new ArrayList<String>();
		
		for (Enumeration eFormals = m.formals.getElements(); 
		     eFormals.hasMoreElements();) {
		    formal param = (formal)eFormals.nextElement();
		    sig.add(param.name.toString());
		}
		env.put(c.getName() + "-" + m.name.toString() + "-sigs",
			sig);
	    }
	}
    }

    /** Gets the root of the inheritance tree */
    public CgenNode root() {
	return (CgenNode)probe(TreeConstants.Object_);
    }
}
			  
    
