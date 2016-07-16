// -*- mode: java -*- 
//
// file: cool-tree.m4
//
// This file defines the AST
//
//////////////////////////////////////////////////////////



import java.util.Enumeration;
import java.io.PrintStream;
import java.util.Vector;
import java.util.Hashtable;
import java.util.ArrayList;


/** Defines simple phylum Program */
abstract class Program extends TreeNode {
    protected Program(int lineNumber) {
        super(lineNumber);
    }
    public abstract void dump_with_types(PrintStream out, int n);
    public abstract void semant();
    public abstract void cgen(PrintStream s);

}


/** Defines simple phylum Class_ */
abstract class Class_ extends TreeNode {
    protected Class_(int lineNumber) {
        super(lineNumber);
    }
    public abstract void dump_with_types(PrintStream out, int n);
    public abstract AbstractSymbol getName();
    public abstract AbstractSymbol getParent();
    public abstract AbstractSymbol getFilename();
    public abstract Features getFeatures();

}


/** Defines list phylum Classes
    <p>
    See <a href="ListNode.html">ListNode</a> for full documentation. */
class Classes extends ListNode {
    public final static Class elementClass = Class_.class;
    /** Returns class of this lists's elements */
    public Class getElementClass() {
        return elementClass;
    }
    protected Classes(int lineNumber, Vector elements) {
        super(lineNumber, elements);
    }
    /** Creates an empty "Classes" list */
    public Classes(int lineNumber) {
        super(lineNumber);
    }
    /** Appends "Class_" element to this list */
    public Classes appendElement(TreeNode elem) {
        addElement(elem);
        return this;
    }
    public TreeNode copy() {
        return new Classes(lineNumber, copyElements());
    }
}


/** Defines simple phylum Feature */
abstract class Feature extends TreeNode {
    protected Feature(int lineNumber) {
        super(lineNumber);
    }
    public abstract void dump_with_types(PrintStream out, int n);

}


/** Defines list phylum Features
    <p>
    See <a href="ListNode.html">ListNode</a> for full documentation. */
class Features extends ListNode {
    public final static Class elementClass = Feature.class;
    /** Returns class of this lists's elements */
    public Class getElementClass() {
        return elementClass;
    }
    protected Features(int lineNumber, Vector elements) {
        super(lineNumber, elements);
    }
    /** Creates an empty "Features" list */
    public Features(int lineNumber) {
        super(lineNumber);
    }
    /** Appends "Feature" element to this list */
    public Features appendElement(TreeNode elem) {
        addElement(elem);
        return this;
    }
    public TreeNode copy() {
        return new Features(lineNumber, copyElements());
    }
}


/** Defines simple phylum Formal */
abstract class Formal extends TreeNode {
    protected Formal(int lineNumber) {
        super(lineNumber);
    }
    public abstract void dump_with_types(PrintStream out, int n);

}


/** Defines list phylum Formals
    <p>
    See <a href="ListNode.html">ListNode</a> for full documentation. */
class Formals extends ListNode {
    public final static Class elementClass = Formal.class;
    /** Returns class of this lists's elements */
    public Class getElementClass() {
        return elementClass;
    }
    protected Formals(int lineNumber, Vector elements) {
        super(lineNumber, elements);
    }
    /** Creates an empty "Formals" list */
    public Formals(int lineNumber) {
        super(lineNumber);
    }
    /** Appends "Formal" element to this list */
    public Formals appendElement(TreeNode elem) {
        addElement(elem);
        return this;
    }
    public TreeNode copy() {
        return new Formals(lineNumber, copyElements());
    }
}


/** Defines simple phylum Expression */
abstract class Expression extends TreeNode {
    protected Expression(int lineNumber) {
        super(lineNumber);
    }
    private AbstractSymbol type = null;                                 
    public AbstractSymbol get_type() { return type; }           
    public Expression set_type(AbstractSymbol s) { type = s; return this; } 
    public abstract void dump_with_types(PrintStream out, int n);
    public void dump_type(PrintStream out, int n) {
        if (type != null)
            { out.println(Utilities.pad(n) + ": " + type.getString()); }
        else
            { out.println(Utilities.pad(n) + ": _no_type"); }
    }
    public abstract void code(PrintStream s, Hashtable env);

}


/** Defines list phylum Expressions
    <p>
    See <a href="ListNode.html">ListNode</a> for full documentation. */
class Expressions extends ListNode {
    public final static Class elementClass = Expression.class;
    /** Returns class of this lists's elements */
    public Class getElementClass() {
        return elementClass;
    }
    protected Expressions(int lineNumber, Vector elements) {
        super(lineNumber, elements);
    }
    /** Creates an empty "Expressions" list */
    public Expressions(int lineNumber) {
        super(lineNumber);
    }
    /** Appends "Expression" element to this list */
    public Expressions appendElement(TreeNode elem) {
        addElement(elem);
        return this;
    }
    public TreeNode copy() {
        return new Expressions(lineNumber, copyElements());
    }
}


/** Defines simple phylum Case */
abstract class Case extends TreeNode {
    protected Case(int lineNumber) {
        super(lineNumber);
    }
    public abstract void dump_with_types(PrintStream out, int n);

}


/** Defines list phylum Cases
    <p>
    See <a href="ListNode.html">ListNode</a> for full documentation. */
class Cases extends ListNode {
    public final static Class elementClass = Case.class;
    /** Returns class of this lists's elements */
    public Class getElementClass() {
        return elementClass;
    }
    protected Cases(int lineNumber, Vector elements) {
        super(lineNumber, elements);
    }
    /** Creates an empty "Cases" list */
    public Cases(int lineNumber) {
        super(lineNumber);
    }
    /** Appends "Case" element to this list */
    public Cases appendElement(TreeNode elem) {
        addElement(elem);
        return this;
    }
    public TreeNode copy() {
        return new Cases(lineNumber, copyElements());
    }
}


/** Defines AST constructor 'program'.
    <p>
    See <a href="TreeNode.html">TreeNode</a> for full documentation. */
class program extends Program {
    public Classes classes;
    /** Creates "program" AST node. 
      *
      * @param lineNumber the line in the source file from which this node came.
      * @param a0 initial value for classes
      */
    public program(int lineNumber, Classes a1) {
        super(lineNumber);
        classes = a1;
    }
    public TreeNode copy() {
        return new program(lineNumber, (Classes)classes.copy());
    }
    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "program\n");
        classes.dump(out, n+2);
    }

    
    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_program");
        for (Enumeration e = classes.getElements(); e.hasMoreElements(); ) {
	    ((Class_)e.nextElement()).dump_with_types(out, n + 1);
        }
    }
    /** This method is the entry point to the semantic checker.  You will
        need to complete it in programming assignment 4.
	<p>
        Your checker should do the following two things:
	<ol>
	<li>Check that the program is semantically correct
	<li>Decorate the abstract syntax tree with type information
        by setting the type field in each Expression node.
        (see tree.h)
	</ol>
	<p>
	You are free to first do (1) and make sure you catch all semantic
    	errors. Part (2) can be done in a second stage when you want
	to test the complete compiler.
    */
    public void semant() {
	/* ClassTable constructor may do some semantic analysis */
	ClassTable classTable = new ClassTable(classes);
	
	/* some semantic analysis code may go here */

	if (classTable.errors()) {
	    System.err.println("Compilation halted due to static semantic errors.");
	    System.exit(1);
	}
    }
    /** This method is the entry point to the code generator.  All of the work
      * of the code generator takes place within CgenClassTable constructor.
      * @param s the output stream 
      * @see CgenClassTable
      * */
    public void cgen(PrintStream s) 
    {
        // spim wants comments to start with '#'
        s.print("# start of generated code\n");

	CgenClassTable codegen_classtable = new CgenClassTable(classes, s);

	s.print("\n# end of generated code\n");
    }

}


/** Defines AST constructor 'class_'.
    <p>
    See <a href="TreeNode.html">TreeNode</a> for full documentation. */
class class_ extends Class_ {
    public AbstractSymbol name;
    public AbstractSymbol parent;
    public Features features;
    public AbstractSymbol filename;
    /** Creates "class_" AST node. 
      *
      * @param lineNumber the line in the source file from which this node came.
      * @param a0 initial value for name
      * @param a1 initial value for parent
      * @param a2 initial value for features
      * @param a3 initial value for filename
      */
    public class_(int lineNumber, AbstractSymbol a1, AbstractSymbol a2, Features a3, AbstractSymbol a4) {
        super(lineNumber);
        name = a1;
        parent = a2;
        features = a3;
        filename = a4;
    }
    public TreeNode copy() {
        return new class_(lineNumber, copy_AbstractSymbol(name), copy_AbstractSymbol(parent), (Features)features.copy(), copy_AbstractSymbol(filename));
    }
    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "class_\n");
        dump_AbstractSymbol(out, n+2, name);
        dump_AbstractSymbol(out, n+2, parent);
        features.dump(out, n+2);
        dump_AbstractSymbol(out, n+2, filename);
    }

    
    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_class");
        dump_AbstractSymbol(out, n + 2, name);
        dump_AbstractSymbol(out, n + 2, parent);
        out.print(Utilities.pad(n + 2) + "\"");
        Utilities.printEscapedString(out, filename.getString());
        out.println("\"\n" + Utilities.pad(n + 2) + "(");
        for (Enumeration e = features.getElements(); e.hasMoreElements();) {
	    ((Feature)e.nextElement()).dump_with_types(out, n + 2);
        }
        out.println(Utilities.pad(n + 2) + ")");
    }
    public AbstractSymbol getName()     { return name; }
    public AbstractSymbol getParent()   { return parent; }
    public AbstractSymbol getFilename() { return filename; }
    public Features getFeatures()       { return features; }

}


/** Defines AST constructor 'method'.
    <p>
    See <a href="TreeNode.html">TreeNode</a> for full documentation. */
class method extends Feature {
    public AbstractSymbol name;
    public Formals formals;
    public AbstractSymbol return_type;
    public Expression expr;
    /** Creates "method" AST node. 
      *
      * @param lineNumber the line in the source file from which this node came.
      * @param a0 initial value for name
      * @param a1 initial value for formals
      * @param a2 initial value for return_type
      * @param a3 initial value for expr
      */
    public method(int lineNumber, AbstractSymbol a1, Formals a2, AbstractSymbol a3, Expression a4) {
        super(lineNumber);
        name = a1;
        formals = a2;
        return_type = a3;
        expr = a4;
    }
    public TreeNode copy() {
        return new method(lineNumber, copy_AbstractSymbol(name), (Formals)formals.copy(), copy_AbstractSymbol(return_type), (Expression)expr.copy());
    }
    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "method\n");
        dump_AbstractSymbol(out, n+2, name);
        formals.dump(out, n+2);
        dump_AbstractSymbol(out, n+2, return_type);
        expr.dump(out, n+2);
    }

    
    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_method");
        dump_AbstractSymbol(out, n + 2, name);
        for (Enumeration e = formals.getElements(); e.hasMoreElements();) {
	    ((Formal)e.nextElement()).dump_with_types(out, n + 2);
        }
        dump_AbstractSymbol(out, n + 2, return_type);
	expr.dump_with_types(out, n + 2);
    }

}


/** Defines AST constructor 'attr'.
    <p>
    See <a href="TreeNode.html">TreeNode</a> for full documentation. */
class attr extends Feature {
    public AbstractSymbol name;
    public AbstractSymbol type_decl;
    public Expression init;
    /** Creates "attr" AST node. 
      *
      * @param lineNumber the line in the source file from which this node came.
      * @param a0 initial value for name
      * @param a1 initial value for type_decl
      * @param a2 initial value for init
      */
    public attr(int lineNumber, AbstractSymbol a1, AbstractSymbol a2, Expression a3) {
        super(lineNumber);
        name = a1;
        type_decl = a2;
        init = a3;
    }
    public TreeNode copy() {
        return new attr(lineNumber, copy_AbstractSymbol(name), copy_AbstractSymbol(type_decl), (Expression)init.copy());
    }
    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "attr\n");
        dump_AbstractSymbol(out, n+2, name);
        dump_AbstractSymbol(out, n+2, type_decl);
        init.dump(out, n+2);
    }

    
    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_attr");
        dump_AbstractSymbol(out, n + 2, name);
        dump_AbstractSymbol(out, n + 2, type_decl);
	init.dump_with_types(out, n + 2);
    }

}


/** Defines AST constructor 'formal'.
    <p>
    See <a href="TreeNode.html">TreeNode</a> for full documentation. */
class formal extends Formal {
    public AbstractSymbol name;
    public AbstractSymbol type_decl;
    /** Creates "formal" AST node. 
      *
      * @param lineNumber the line in the source file from which this node came.
      * @param a0 initial value for name
      * @param a1 initial value for type_decl
      */
    public formal(int lineNumber, AbstractSymbol a1, AbstractSymbol a2) {
        super(lineNumber);
        name = a1;
        type_decl = a2;
    }
    public TreeNode copy() {
        return new formal(lineNumber, copy_AbstractSymbol(name), copy_AbstractSymbol(type_decl));
    }
    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "formal\n");
        dump_AbstractSymbol(out, n+2, name);
        dump_AbstractSymbol(out, n+2, type_decl);
    }

    
    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_formal");
        dump_AbstractSymbol(out, n + 2, name);
        dump_AbstractSymbol(out, n + 2, type_decl);
    }

}


/** Defines AST constructor 'branch'.
    <p>
    See <a href="TreeNode.html">TreeNode</a> for full documentation. */
class branch extends Case {
    public AbstractSymbol name;
    public AbstractSymbol type_decl;
    public Expression expr;
    /** Creates "branch" AST node. 
      *
      * @param lineNumber the line in the source file from which this node came.
      * @param a0 initial value for name
      * @param a1 initial value for type_decl
      * @param a2 initial value for expr
      */
    public branch(int lineNumber, AbstractSymbol a1, AbstractSymbol a2, Expression a3) {
        super(lineNumber);
        name = a1;
        type_decl = a2;
        expr = a3;
    }
    public TreeNode copy() {
        return new branch(lineNumber, copy_AbstractSymbol(name), copy_AbstractSymbol(type_decl), (Expression)expr.copy());
    }
    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "branch\n");
        dump_AbstractSymbol(out, n+2, name);
        dump_AbstractSymbol(out, n+2, type_decl);
        expr.dump(out, n+2);
    }

    
    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_branch");
        dump_AbstractSymbol(out, n + 2, name);
        dump_AbstractSymbol(out, n + 2, type_decl);
	expr.dump_with_types(out, n + 2);
    }

}


/** Defines AST constructor 'assign'.
    <p>
    See <a href="TreeNode.html">TreeNode</a> for full documentation. */
class assign extends Expression {
    public AbstractSymbol name;
    public Expression expr;
    /** Creates "assign" AST node. 
      *
      * @param lineNumber the line in the source file from which this node came.
      * @param a0 initial value for name
      * @param a1 initial value for expr
      */
    public assign(int lineNumber, AbstractSymbol a1, Expression a2) {
        super(lineNumber);
        name = a1;
        expr = a2;
    }
    public TreeNode copy() {
        return new assign(lineNumber, copy_AbstractSymbol(name), (Expression)expr.copy());
    }
    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "assign\n");
        dump_AbstractSymbol(out, n+2, name);
        expr.dump(out, n+2);
    }

    
    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_assign");
        dump_AbstractSymbol(out, n + 2, name);
	expr.dump_with_types(out, n + 2);
	dump_type(out, n);
    }
    /** Generates code for this expression.  This method is to be completed 
      * in programming assignment 5.  (You may add or remove parameters as
      * you wish.)
      * @param s the output stream 
      * */
    public void code(PrintStream s, Hashtable env) {
	CgenNode c = (CgenNode)env.get("currClass");
	method m = (method)env.get("currMethod");
	int letCount = (env.get("currLetCount") != null) ? 
	    ((Integer)env.get("currLetCount")).intValue() : 0;
	
	String methodName = (m != null) ? m.name.toString() : null;
	ArrayList<String> sig = 
	    (ArrayList<String>)env.get(c.getName() + "-" + 
				       methodName + "-sigs");
	Vector attrs = (Vector)env.get(c.getName() + "-attrs");

	this.expr.code(s, env);

	// shadow var in let expr
	if (env.get(this.name.toString()) != null) {
	    //System.out.println("HERE");
	    CgenSupport.emitStore(CgenSupport.ACC, 
				 ((Integer)env.
				  get(this.name.toString())).intValue(),
				 CgenSupport.FP, s);
	    //System.out.println("var " + this.name + " changed.");
	    return;
	}

	// if var is a method param, set val on the stack otherwise set attr
	if (sig != null &&
	    sig.contains(this.name.toString())) { // set method param
	    // add 3 words to offset since FT, SELF, and RA are on stack too
	    CgenSupport.emitStore(CgenSupport.ACC, 
				 CgenSupport.STACK_PARAM_OFFSET + 
				 sig.indexOf(this.name.toString()) + 
				  letCount,
				 CgenSupport.FP, s);
	} else if (attrs.contains(this.name.toString())) { // set attr
	    // add 3 words to offset since first attr is at 12
	    CgenSupport.emitStore(CgenSupport.ACC,
				 CgenSupport.CLASS_ATTR_OFFSET + 
				 attrs.indexOf(this.name.toString()),
				 CgenSupport.SELF, s);
	}
    }
}


/** Defines AST constructor 'static_dispatch'.
    <p>
    See <a href="TreeNode.html">TreeNode</a> for full documentation. */
class static_dispatch extends Expression {
    public Expression expr;
    public AbstractSymbol type_name;
    public AbstractSymbol name;
    public Expressions actual;
    /** Creates "static_dispatch" AST node. 
      *
      * @param lineNumber the line in the source file from which this node came.
      * @param a0 initial value for expr
      * @param a1 initial value for type_name
      * @param a2 initial value for name
      * @param a3 initial value for actual
      */
    public static_dispatch(int lineNumber, Expression a1, AbstractSymbol a2, AbstractSymbol a3, Expressions a4) {
        super(lineNumber);
        expr = a1;
        type_name = a2;
        name = a3;
        actual = a4;
    }
    public TreeNode copy() {
        return new static_dispatch(lineNumber, (Expression)expr.copy(), copy_AbstractSymbol(type_name), copy_AbstractSymbol(name), (Expressions)actual.copy());
    }
    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "static_dispatch\n");
        expr.dump(out, n+2);
        dump_AbstractSymbol(out, n+2, type_name);
        dump_AbstractSymbol(out, n+2, name);
        actual.dump(out, n+2);
    }

    
    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_static_dispatch");
	expr.dump_with_types(out, n + 2);
        dump_AbstractSymbol(out, n + 2, type_name);
        dump_AbstractSymbol(out, n + 2, name);
        out.println(Utilities.pad(n + 2) + "(");
        for (Enumeration e = actual.getElements(); e.hasMoreElements();) {
	    ((Expression)e.nextElement()).dump_with_types(out, n + 2);
        }
        out.println(Utilities.pad(n + 2) + ")");
	dump_type(out, n);
    }
    /** Generates code for this expression.  This method is to be completed 
      * in programming assignment 5.  (You may add or remove parameters as
      * you wish.)
      * @param s the output stream 
      * */
    public void code(PrintStream s, Hashtable env) { // begin static_dispatch
	int lbl1 = CgenSupport.LC++;
	CgenNode c = (CgenNode)env.get("currClass");

	//System.out.println(env.get(this.type_name + "-methods"));
	int methodOffset = 
	    CgenSupport.
	    removeClassNamesFromMethods(new Vector((java.util.Collection)
						   env.get(this.type_name + 
							   "-methods"))).
	    indexOf(this.name.getString());
	
	// push all args onto the stack
	if (this.actual.getLength() > 0) {
	    CgenSupport.emitAddiu(CgenSupport.SP, CgenSupport.SP, 
				  -4 * this.actual.getLength(), s);
	}

	// if String class and substr method then reverse args
	if (this.type_name.toString().equals(TreeConstants.Str.toString()) && 
	    methodOffset == 5) {
		Expression stringIndex = (Expression)actual.getNth(0);
		stringIndex.code(s, env);
		CgenSupport.emitStore(CgenSupport.ACC, 2, CgenSupport.SP, s);
		Expression stringLength = (Expression)actual.getNth(1);
		stringLength.code(s, env);
		CgenSupport.emitStore(CgenSupport.ACC, 1, CgenSupport.SP, s);
	} else {
	    for (int i = 0; i < this.actual.getLength(); i++) {
		Expression e = (Expression)actual.getNth(i);
		e.code(s, env);
		CgenSupport.emitStore(CgenSupport.ACC, i + 1, 
				      CgenSupport.SP, s);
	    }
	}

	this.expr.code(s, env); // get class name
	
	CgenSupport.emitBne(CgenSupport.ACC, CgenSupport.ZERO, lbl1, s);

	CgenSupport.emitPartialLoadAddress(CgenSupport.ACC, s);
	((StringSymbol)AbstractTable.stringtable.
	 lookup(c.getFilename().toString())).codeRef(s);
	s.println();
	CgenSupport.emitLoadImm(CgenSupport.T1, 1, s);
	CgenSupport.emitJal("_dispatch_abort", s);

	CgenSupport.emitLabelDef(lbl1, s);
	CgenSupport.emitLoadAddress(CgenSupport.T1, this.type_name + 
				    CgenSupport.DISPTAB_SUFFIX, s);

	if (methodOffset == -1) {
	    System.out.println("static_dispatch offset: " + methodOffset);
	    System.out.println("f: " + this.type_name + "." + 
			       this.name.getString());
	}
	CgenSupport.emitLoad(CgenSupport.T1, methodOffset, CgenSupport.T1, s);
	CgenSupport.emitJalr(CgenSupport.T1, s);
    } // end static_dispatch code


}


/** Defines AST constructor 'dispatch'.
    <p>
    See <a href="TreeNode.html">TreeNode</a> for full documentation. */
class dispatch extends Expression {
    public Expression expr;
    public AbstractSymbol name;
    public Expressions actual;
    /** Creates "dispatch" AST node. 
      *
      * @param lineNumber the line in the source file from which this node came.
      * @param a0 initial value for expr
      * @param a1 initial value for name
      * @param a2 initial value for actual
      */
    public dispatch(int lineNumber, Expression a1, AbstractSymbol a2, Expressions a3) {
        super(lineNumber);
        expr = a1;
        name = a2;
        actual = a3;
    }
    public TreeNode copy() {
        return new dispatch(lineNumber, (Expression)expr.copy(), copy_AbstractSymbol(name), (Expressions)actual.copy());
    }
    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "dispatch\n");
        expr.dump(out, n+2);
        dump_AbstractSymbol(out, n+2, name);
        actual.dump(out, n+2);
    }

    
    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_dispatch");
	expr.dump_with_types(out, n + 2);
        dump_AbstractSymbol(out, n + 2, name);
        out.println(Utilities.pad(n + 2) + "(");
        for (Enumeration e = actual.getElements(); e.hasMoreElements();) {
	    ((Expression)e.nextElement()).dump_with_types(out, n + 2);
        }
        out.println(Utilities.pad(n + 2) + ")");
	dump_type(out, n);
    }
    /** Generates code for this expression.  This method is to be completed 
      * in programming assignment 5.  (You may add or remove parameters as
      * you wish.)
      * @param s the output stream 
      * */
    public void code(PrintStream s, Hashtable env) {	
	int lbl1 = CgenSupport.LC++;
	CgenNode c = (CgenNode)env.get("currClass");

	String t0 = this.expr.get_type().toString();
	//System.out.println("c: " + c.getName());
	//System.out.println();System.out.println("this.expr.get_type(): "+t0);
	if (this.expr.get_type().equals(TreeConstants.SELF_TYPE)) {
	    //System.out.println("***** is self-type");
	    t0 = c.getName().toString();
	} 
	//System.out.println("this.expr.get_type(): " + t0);
	//System.out.println(env.get(t0 + "-methods"));
	int methodOffset = 
	    CgenSupport.
	    removeClassNamesFromMethods(new Vector((java.util.Collection)
						   env.get(t0 + 
							   "-methods"))).
	    indexOf(this.name.getString());
	
	// push all args onto the stack
	if (this.actual.getLength() > 0) {
	    CgenSupport.emitAddiu(CgenSupport.SP, CgenSupport.SP, 
				  -4 * this.actual.getLength(), s);
	}
	
	// if String class and substr method then reverse args
	//System.out.println("Class : " + t0);
	//System.out.println("Method offset: " + methodOffset);
	if (t0.toString().equals(TreeConstants.Str.toString()) && 
	    methodOffset == 5) {
		Expression stringIndex = (Expression)actual.getNth(0);
		stringIndex.code(s, env);
		CgenSupport.emitStore(CgenSupport.ACC, 2, CgenSupport.SP, s);
		Expression stringLength = (Expression)actual.getNth(1);
		stringLength.code(s, env);
		CgenSupport.emitStore(CgenSupport.ACC, 1, CgenSupport.SP, s);
	} else {
	    //System.out.println("this.actual.getLength(): " + this.actual.getLength());
	    //if (this.actual.getLength() > 0) return;
	    //System.out.println("Begin params for : " + t0);
	    for (int i = 0; i < this.actual.getLength(); i++) {
		Expression e = (Expression)actual.getNth(i);
		//System.out.println("t0: " + t0);
		//e.dump_with_types(System.out, 2);
		e.code(s, env);
		//System.out.println("done");
		//System.out.println("t0: " + t0);
		//e.dump_with_types(System.out, 2);
		CgenSupport.emitStore(CgenSupport.ACC, i + 1, 
				      CgenSupport.SP, s);
		//System.out.println("t0: " + t0); System.out.println("L: " + e);
	    }
	    //System.out.println("End params for : " + t0);
	    //System.out.println("t0: " + t0);
	}

	//System.out.println("t0: " + t0);
	//this.expr.dump_with_types(System.out, 2);
	this.expr.code(s, env); // get class name
	//System.out.println("t0: " + t0);
	
	CgenSupport.emitBne(CgenSupport.ACC, CgenSupport.ZERO, lbl1, s);
	
	CgenSupport.emitPartialLoadAddress(CgenSupport.ACC, s);
	((StringSymbol)AbstractTable.stringtable.
	 lookup(c.getFilename().toString())).codeRef(s);
	s.println();
	CgenSupport.emitLoadImm(CgenSupport.T1, 1, s);
	CgenSupport.emitJal("_dispatch_abort", s);

	CgenSupport.emitLabelDef(lbl1, s);
	CgenSupport.emitLoad(CgenSupport.T1, CgenSupport.DISPTABLE_OFFSET,
			     CgenSupport.ACC, s);
	
	if (methodOffset == -1) {
	    System.out.println(env.get(t0 + "-methods"));
	    System.out.println("offset: " + methodOffset);
	    System.out.println("f: " + t0 + "." + 
			       this.name.getString());
	}
	
	CgenSupport.emitLoad(CgenSupport.T1, methodOffset, CgenSupport.T1, s);
	CgenSupport.emitJalr(CgenSupport.T1, s);

    } // end of dispatch
}


/** Defines AST constructor 'cond'.
    <p>
    See <a href="TreeNode.html">TreeNode</a> for full documentation. */
class cond extends Expression {
    public Expression pred;
    public Expression then_exp;
    public Expression else_exp;
    /** Creates "cond" AST node. 
      *
      * @param lineNumber the line in the source file from which this node came.
      * @param a0 initial value for pred
      * @param a1 initial value for then_exp
      * @param a2 initial value for else_exp
      */
    public cond(int lineNumber, Expression a1, Expression a2, Expression a3) {
        super(lineNumber);
        pred = a1;
        then_exp = a2;
        else_exp = a3;
    }
    public TreeNode copy() {
        return new cond(lineNumber, (Expression)pred.copy(), (Expression)then_exp.copy(), (Expression)else_exp.copy());
    }
    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "cond\n");
        pred.dump(out, n+2);
        then_exp.dump(out, n+2);
        else_exp.dump(out, n+2);
    }

    
    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_cond");
	pred.dump_with_types(out, n + 2);
	then_exp.dump_with_types(out, n + 2);
	else_exp.dump_with_types(out, n + 2);
	dump_type(out, n);
    }
    /** Generates code for this expression.  This method is to be completed 
      * in programming assignment 5.  (You may add or remove parameters as
      * you wish.)
      * @param s the output stream 
      * */
    public void code(PrintStream s, Hashtable env) {
	int lbl1 = CgenSupport.LC++;
	int lbl2 = CgenSupport.LC++;

	this.pred.code(s, env);
	CgenSupport.emitLoadBool(CgenSupport.T1, new BoolConst(true), s);
	CgenSupport.emitBeq(CgenSupport.ACC, CgenSupport.T1, lbl1, "if", s);
	this.else_exp.code(s, env);
	CgenSupport.emitJal("label" + lbl2 + "_if", s);
	CgenSupport.emitLabelDef(lbl1, "if", s);
	this.then_exp.code(s, env); // exec if body
	CgenSupport.emitLabelDef(lbl2, "if", s); // end of if
    }


}


/** Defines AST constructor 'loop'.
    <p>
    See <a href="TreeNode.html">TreeNode</a> for full documentation. */
class loop extends Expression {
    public Expression pred;
    public Expression body;
    /** Creates "loop" AST node. 
      *
      * @param lineNumber the line in the source file from which this node came.
      * @param a0 initial value for pred
      * @param a1 initial value for body
      */
    public loop(int lineNumber, Expression a1, Expression a2) {
        super(lineNumber);
        pred = a1;
        body = a2;
    }
    public TreeNode copy() {
        return new loop(lineNumber, (Expression)pred.copy(), (Expression)body.copy());
    }
    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "loop\n");
        pred.dump(out, n+2);
        body.dump(out, n+2);
    }

    
    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_loop");
	pred.dump_with_types(out, n + 2);
	body.dump_with_types(out, n + 2);
	dump_type(out, n);
    }
    /** Generates code for this expression.  This method is to be completed 
      * in programming assignment 5.  (You may add or remove parameters as
      * you wish.)
      * @param s the output stream 
      * */
    public void code(PrintStream s, Hashtable env) {
	int lbl1 = CgenSupport.LC++;
	int lbl2 = CgenSupport.LC++;
	int lbl3 = CgenSupport.LC++;

	CgenSupport.emitLabelDef(lbl1, "loop", s);
	this.pred.code(s, env);
	//CgenSupport.emitLoadImm(CgenSupport.T1, 1, s);
	CgenSupport.emitLoadBool(CgenSupport.T1, new BoolConst(true), s);
	CgenSupport.emitBeq(CgenSupport.ACC, CgenSupport.T1, lbl2, "loop", s);
	CgenSupport.emitJal("label" + lbl3 + "_loop", s);
	CgenSupport.emitLabelDef(lbl2, "loop", s);
	this.body.code(s, env); // exec loop body
	CgenSupport.emitJal("label" + lbl1 + "_loop", s); // jump to loop pred
	CgenSupport.emitLabelDef(lbl3, "loop", s); // end of loop
	CgenSupport.emitLoadImm(CgenSupport.ACC, 0, s); // set to void?
    }


}


/** Defines AST constructor 'typcase'.
    <p>
    See <a href="TreeNode.html">TreeNode</a> for full documentation. */
class typcase extends Expression {
    public Expression expr;
    public Cases cases;
    /** Creates "typcase" AST node. 
      *
      * @param lineNumber the line in the source file from which this node came.
      * @param a0 initial value for expr
      * @param a1 initial value for cases
      */
    public typcase(int lineNumber, Expression a1, Cases a2) {
        super(lineNumber);
        expr = a1;
        cases = a2;
    }
    public TreeNode copy() {
        return new typcase(lineNumber, (Expression)expr.copy(), (Cases)cases.copy());
    }
    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "typcase\n");
        expr.dump(out, n+2);
        cases.dump(out, n+2);
    }

    
    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_typcase");
	expr.dump_with_types(out, n + 2);
        for (Enumeration e = cases.getElements(); e.hasMoreElements();) {
	    ((Case)e.nextElement()).dump_with_types(out, n + 2);
        }
	dump_type(out, n);
    }
    /** Generates code for this expression.  This method is to be completed 
      * in programming assignment 5.  (You may add or remove parameters as
      * you wish.)
      * @param s the output stream 
      * */
    public void code(PrintStream s, Hashtable env) {
    }


}


/** Defines AST constructor 'block'.
    <p>
    See <a href="TreeNode.html">TreeNode</a> for full documentation. */
class block extends Expression {
    public Expressions body;
    /** Creates "block" AST node. 
      *
      * @param lineNumber the line in the source file from which this node came.
      * @param a0 initial value for body
      */
    public block(int lineNumber, Expressions a1) {
        super(lineNumber);
        body = a1;
    }
    public TreeNode copy() {
        return new block(lineNumber, (Expressions)body.copy());
    }
    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "block\n");
        body.dump(out, n+2);
    }

    
    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_block");
        for (Enumeration e = body.getElements(); e.hasMoreElements();) {
	    ((Expression)e.nextElement()).dump_with_types(out, n + 2);
        }
	dump_type(out, n);
    }
    /** Generates code for this expression.  This method is to be completed 
      * in programming assignment 5.  (You may add or remove parameters as
      * you wish.)
      * @param s the output stream 
      * */
    public void code(PrintStream s, Hashtable env) {
	for (Enumeration e = body.getElements(); e.hasMoreElements();) {
	    Expression expr = (Expression)e.nextElement();
	    expr.code(s, env);
	}
    }


}


/** Defines AST constructor 'let'.
    <p>
    See <a href="TreeNode.html">TreeNode</a> for full documentation. */
class let extends Expression {
    public AbstractSymbol identifier;
    public AbstractSymbol type_decl;
    public Expression init;
    public Expression body;
    /** Creates "let" AST node. 
      *
      * @param lineNumber the line in the source file from which this node came.
      * @param a0 initial value for identifier
      * @param a1 initial value for type_decl
      * @param a2 initial value for init
      * @param a3 initial value for body
      */
    public let(int lineNumber, AbstractSymbol a1, AbstractSymbol a2, Expression a3, Expression a4) {
        super(lineNumber);
        identifier = a1;
        type_decl = a2;
        init = a3;
        body = a4;
    }
    public TreeNode copy() {
        return new let(lineNumber, copy_AbstractSymbol(identifier), copy_AbstractSymbol(type_decl), (Expression)init.copy(), (Expression)body.copy());
    }
    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "let\n");
        dump_AbstractSymbol(out, n+2, identifier);
        dump_AbstractSymbol(out, n+2, type_decl);
        init.dump(out, n+2);
        body.dump(out, n+2);
    }

    
    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_let");
	dump_AbstractSymbol(out, n + 2, identifier);
	dump_AbstractSymbol(out, n + 2, type_decl);
	init.dump_with_types(out, n + 2);
	body.dump_with_types(out, n + 2);
	dump_type(out, n);
    }
    /** Generates code for this expression.  This method is to be completed 
      * in programming assignment 5.  (You may add or remove parameters as
      * you wish.)
      * @param s the output stream 
      * */
    public void code(PrintStream s, Hashtable env) {
	/*	CgenNode c = (CgenNode)env.get("currClass");
	method m = (method)env.get("currMethod");
	String methodName = (m != null) ? m.name.toString() : null;
	ArrayList<String> sig = 
	    (ArrayList<String>)env.get(c.getName() + "-" + 
					   methodName + "-sigs");
	int methodParamCount = 0;
	if (sig != null) {
	    methodParamCount = sig.size();
	}
	//System.out.println("methodParamCount: " + methodParamCount);*/

	Hashtable newEnv = (Hashtable)env.clone();
	if (newEnv.get("letParam") == null) {
	    newEnv.put("letParam", new Integer(0));
	} else {
	    newEnv.put("letParam", 
		       new Integer(((Integer)newEnv.get("letParam")).intValue() 
				   + 1));
	}

	newEnv.put(this.identifier.toString(), newEnv.get("letParam"));
	//System.out.println("let id: " + this.identifier);
	//System.out.println("nEnv(letParam): "+ (Integer)newEnv.get("letParam"));
	//System.out.println("nEnv: " + newEnv); System.out.println();

	if (!(this.init instanceof no_expr)) {
	    this.init.code(s, env);
	    CgenSupport.emitStore(CgenSupport.ACC, 
				  ((Integer)newEnv.get("letParam")).intValue(),
				  CgenSupport.FP, s);
	} else {
	    if (this.type_decl.equals(TreeConstants.Int)) {
		CgenSupport.emitLoadInt(CgenSupport.ACC, 
					(IntSymbol)AbstractTable.inttable.
					lookup("0"), s);
		CgenSupport.emitStore(CgenSupport.ACC, 
				      ((Integer)newEnv.
				       get("letParam")).intValue(),
				      CgenSupport.FP, s);
	    } else if (this.type_decl.equals(TreeConstants.Bool)) {
		CgenSupport.emitLoadBool(CgenSupport.ACC, 
					 new BoolConst(false), s);
		CgenSupport.emitStore(CgenSupport.ACC, 
				      ((Integer)newEnv.
				       get("letParam")).intValue(),
				      CgenSupport.FP, s);
	    } else if (this.type_decl.equals(TreeConstants.Str)) {
		CgenSupport.emitLoadString(CgenSupport.ACC, 
					   (StringSymbol)AbstractTable.
					   stringtable.lookup(""), s);
		CgenSupport.emitStore(CgenSupport.ACC, 
				      ((Integer)newEnv.
				       get("letParam")).intValue(),
				      CgenSupport.FP, s);
	    } else {
		CgenSupport.emitStore(CgenSupport.ZERO, 
				      ((Integer)newEnv.
				       get("letParam")).intValue(), 
				      CgenSupport.FP, s);
	    }
	}

	this.body.code(s, newEnv);
    }
}


/** Defines AST constructor 'plus'.
    <p>
    See <a href="TreeNode.html">TreeNode</a> for full documentation. */
class plus extends Expression {
    public Expression e1;
    public Expression e2;
    /** Creates "plus" AST node. 
      *
      * @param lineNumber the line in the source file from which this node came.
      * @param a0 initial value for e1
      * @param a1 initial value for e2
      */
    public plus(int lineNumber, Expression a1, Expression a2) {
        super(lineNumber);
        e1 = a1;
        e2 = a2;
    }
    public TreeNode copy() {
        return new plus(lineNumber, (Expression)e1.copy(), (Expression)e2.copy());
    }
    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "plus\n");
        e1.dump(out, n+2);
        e2.dump(out, n+2);
    }

    
    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_plus");
	e1.dump_with_types(out, n + 2);
	e2.dump_with_types(out, n + 2);
	dump_type(out, n);
    }
    /** Generates code for this expression.  This method is to be completed 
      * in programming assignment 5.  (You may add or remove parameters as
      * you wish.)
      * @param s the output stream 
      * */
    public void code(PrintStream s, Hashtable env) {
	this.e1.code(s, env);
	CgenSupport.emitStore(CgenSupport.ACC, 0, CgenSupport.SP, s);
	CgenSupport.emitAddiu(CgenSupport.SP, CgenSupport.SP, -4, s);

	CgenSupport.emitLoad(CgenSupport.ACC, 3, CgenSupport.ACC, s);
	CgenSupport.emitStore(CgenSupport.ACC, 0, CgenSupport.SP, s);
	CgenSupport.emitAddiu(CgenSupport.SP, CgenSupport.SP, -4, s);

	this.e2.code(s, env);

	CgenSupport.emitLoad(CgenSupport.T1, 1, CgenSupport.SP, s);
	CgenSupport.emitAddiu(CgenSupport.SP, CgenSupport.SP, 4, s);

	CgenSupport.emitLoad(CgenSupport.ACC, 3, CgenSupport.ACC, s);
	CgenSupport.emitAdd(CgenSupport.S1, 
			    CgenSupport.T1, CgenSupport.ACC, s);

	CgenSupport.emitLoad(CgenSupport.ACC, 1, CgenSupport.SP, s);
	CgenSupport.emitJal("Object.copy", s);
	CgenSupport.emitStore(CgenSupport.S1, 3, CgenSupport.ACC, s);
	CgenSupport.emitAddiu(CgenSupport.SP, CgenSupport.SP, 4, s);	
    }


}


/** Defines AST constructor 'sub'.
    <p>
    See <a href="TreeNode.html">TreeNode</a> for full documentation. */
class sub extends Expression {
    public Expression e1;
    public Expression e2;
    /** Creates "sub" AST node. 
      *
      * @param lineNumber the line in the source file from which this node came.
      * @param a0 initial value for e1
      * @param a1 initial value for e2
      */
    public sub(int lineNumber, Expression a1, Expression a2) {
        super(lineNumber);
        e1 = a1;
        e2 = a2;
    }
    public TreeNode copy() {
        return new sub(lineNumber, (Expression)e1.copy(), (Expression)e2.copy());
    }
    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "sub\n");
        e1.dump(out, n+2);
        e2.dump(out, n+2);
    }

    
    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_sub");
	e1.dump_with_types(out, n + 2);
	e2.dump_with_types(out, n + 2);
	dump_type(out, n);
    }
    /** Generates code for this expression.  This method is to be completed 
      * in programming assignment 5.  (You may add or remove parameters as
      * you wish.)
      * @param s the output stream 
      * */
    public void code(PrintStream s, Hashtable env) {
	this.e1.code(s, env);
	CgenSupport.emitStore(CgenSupport.ACC, 0, CgenSupport.SP, s);
	CgenSupport.emitAddiu(CgenSupport.SP, CgenSupport.SP, -4, s);

	CgenSupport.emitLoad(CgenSupport.ACC, 3, CgenSupport.ACC, s);
	CgenSupport.emitStore(CgenSupport.ACC, 0, CgenSupport.SP, s);
	CgenSupport.emitAddiu(CgenSupport.SP, CgenSupport.SP, -4, s);

	this.e2.code(s, env);

	CgenSupport.emitLoad(CgenSupport.T1, 1, CgenSupport.SP, s);
	CgenSupport.emitAddiu(CgenSupport.SP, CgenSupport.SP, 4, s);

	CgenSupport.emitLoad(CgenSupport.ACC, 3, CgenSupport.ACC, s);
	CgenSupport.emitSub(CgenSupport.S1, 
			    CgenSupport.T1, CgenSupport.ACC, s);

	CgenSupport.emitLoad(CgenSupport.ACC, 1, CgenSupport.SP, s);
	CgenSupport.emitJal("Object.copy", s);
	CgenSupport.emitStore(CgenSupport.S1, 3, CgenSupport.ACC, s);
	CgenSupport.emitAddiu(CgenSupport.SP, CgenSupport.SP, 4, s);
    }
}


/** Defines AST constructor 'mul'.
    <p>
    See <a href="TreeNode.html">TreeNode</a> for full documentation. */
class mul extends Expression {
    public Expression e1;
    public Expression e2;
    /** Creates "mul" AST node. 
      *
      * @param lineNumber the line in the source file from which this node came.
      * @param a0 initial value for e1
      * @param a1 initial value for e2
      */
    public mul(int lineNumber, Expression a1, Expression a2) {
        super(lineNumber);
        e1 = a1;
        e2 = a2;
    }
    public TreeNode copy() {
        return new mul(lineNumber, (Expression)e1.copy(), (Expression)e2.copy());
    }
    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "mul\n");
        e1.dump(out, n+2);
        e2.dump(out, n+2);
    }

    
    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_mul");
	e1.dump_with_types(out, n + 2);
	e2.dump_with_types(out, n + 2);
	dump_type(out, n);
    }
    /** Generates code for this expression.  This method is to be completed 
      * in programming assignment 5.  (You may add or remove parameters as
      * you wish.)
      * @param s the output stream 
      * */
    public void code(PrintStream s, Hashtable env) {
	this.e1.code(s, env);
	CgenSupport.emitStore(CgenSupport.ACC, 0, CgenSupport.SP, s);
	CgenSupport.emitAddiu(CgenSupport.SP, CgenSupport.SP, -4, s);

	CgenSupport.emitLoad(CgenSupport.ACC, 3, CgenSupport.ACC, s);
	CgenSupport.emitStore(CgenSupport.ACC, 0, CgenSupport.SP, s);
	CgenSupport.emitAddiu(CgenSupport.SP, CgenSupport.SP, -4, s);

	this.e2.code(s, env);

	CgenSupport.emitLoad(CgenSupport.T1, 1, CgenSupport.SP, s);
	CgenSupport.emitAddiu(CgenSupport.SP, CgenSupport.SP, 4, s);

	CgenSupport.emitLoad(CgenSupport.ACC, 3, CgenSupport.ACC, s);
	CgenSupport.emitMul(CgenSupport.S1, 
			    CgenSupport.T1, CgenSupport.ACC, s);

	CgenSupport.emitLoad(CgenSupport.ACC, 1, CgenSupport.SP, s);
	CgenSupport.emitJal("Object.copy", s);
	CgenSupport.emitStore(CgenSupport.S1, 3, CgenSupport.ACC, s);
	CgenSupport.emitAddiu(CgenSupport.SP, CgenSupport.SP, 4, s);
    }
}


/** Defines AST constructor 'divide'.
    <p>
    See <a href="TreeNode.html">TreeNode</a> for full documentation. */
class divide extends Expression {
    public Expression e1;
    public Expression e2;
    /** Creates "divide" AST node. 
      *
      * @param lineNumber the line in the source file from which this node came.
      * @param a0 initial value for e1
      * @param a1 initial value for e2
      */
    public divide(int lineNumber, Expression a1, Expression a2) {
        super(lineNumber);
        e1 = a1;
        e2 = a2;
    }
    public TreeNode copy() {
        return new divide(lineNumber, (Expression)e1.copy(), (Expression)e2.copy());
    }
    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "divide\n");
        e1.dump(out, n+2);
        e2.dump(out, n+2);
    }

    
    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_divide");
	e1.dump_with_types(out, n + 2);
	e2.dump_with_types(out, n + 2);
	dump_type(out, n);
    }
    /** Generates code for this expression.  This method is to be completed 
      * in programming assignment 5.  (You may add or remove parameters as
      * you wish.)
      * @param s the output stream 
      * */
    public void code(PrintStream s, Hashtable env) {
	this.e1.code(s, env);
	CgenSupport.emitStore(CgenSupport.ACC, 0, CgenSupport.SP, s);
	CgenSupport.emitAddiu(CgenSupport.SP, CgenSupport.SP, -4, s);

	CgenSupport.emitLoad(CgenSupport.ACC, 3, CgenSupport.ACC, s);
	CgenSupport.emitStore(CgenSupport.ACC, 0, CgenSupport.SP, s);
	CgenSupport.emitAddiu(CgenSupport.SP, CgenSupport.SP, -4, s);

	this.e2.code(s, env);

	CgenSupport.emitLoad(CgenSupport.T1, 1, CgenSupport.SP, s);
	CgenSupport.emitAddiu(CgenSupport.SP, CgenSupport.SP, 4, s);

	CgenSupport.emitLoad(CgenSupport.ACC, 3, CgenSupport.ACC, s);
	CgenSupport.emitDiv(CgenSupport.S1, 
			    CgenSupport.T1, CgenSupport.ACC, s);

	CgenSupport.emitLoad(CgenSupport.ACC, 1, CgenSupport.SP, s);
	CgenSupport.emitJal("Object.copy", s);
	CgenSupport.emitStore(CgenSupport.S1, 3, CgenSupport.ACC, s);
	CgenSupport.emitAddiu(CgenSupport.SP, CgenSupport.SP, 4, s);
    }
}


/** Defines AST constructor 'neg'.
    <p>
    See <a href="TreeNode.html">TreeNode</a> for full documentation. */
class neg extends Expression {
    public Expression e1;
    /** Creates "neg" AST node. 
      *
      * @param lineNumber the line in the source file from which this node came.
      * @param a0 initial value for e1
      */
    public neg(int lineNumber, Expression a1) {
        super(lineNumber);
        e1 = a1;
    }
    public TreeNode copy() {
        return new neg(lineNumber, (Expression)e1.copy());
    }
    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "neg\n");
        e1.dump(out, n+2);
    }

    
    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_neg");
	e1.dump_with_types(out, n + 2);
	dump_type(out, n);
    }
    /** Generates code for this expression.  This method is to be completed 
      * in programming assignment 5.  (You may add or remove parameters as
      * you wish.)
      * @param s the output stream 
      * */
    public void code(PrintStream s, Hashtable env) {
	this.e1.code(s, env);

	CgenSupport.emitLoad(CgenSupport.T1, 3, CgenSupport.ACC, s);
	CgenSupport.emitNeg(CgenSupport.S1, CgenSupport.T1, s);
	
	CgenSupport.emitJal("Object.copy", s);
	CgenSupport.emitStore(CgenSupport.S1, 3, CgenSupport.ACC, s);
    }
}


/** Defines AST constructor 'lt'.
    <p>
    See <a href="TreeNode.html">TreeNode</a> for full documentation. */
class lt extends Expression {
    public Expression e1;
    public Expression e2;
    /** Creates "lt" AST node. 
      *
      * @param lineNumber the line in the source file from which this node came.
      * @param a0 initial value for e1
      * @param a1 initial value for e2
      */
    public lt(int lineNumber, Expression a1, Expression a2) {
        super(lineNumber);
        e1 = a1;
        e2 = a2;
    }
    public TreeNode copy() {
        return new lt(lineNumber, (Expression)e1.copy(), (Expression)e2.copy());
    }
    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "lt\n");
        e1.dump(out, n+2);
        e2.dump(out, n+2);
    }

    
    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_lt");
	e1.dump_with_types(out, n + 2);
	e2.dump_with_types(out, n + 2);
	dump_type(out, n);
    }
    /** Generates code for this expression.  This method is to be completed 
      * in programming assignment 5.  (You may add or remove parameters as
      * you wish.)
      * @param s the output stream 
      * */
    public void code(PrintStream s, Hashtable env) {
	int lbl1 = CgenSupport.LC++;
	int lbl2 = CgenSupport.LC++;

	this.e1.code(s, env);
	CgenSupport.emitLoad(CgenSupport.ACC, 3, CgenSupport.ACC, s);
	CgenSupport.emitStore(CgenSupport.ACC, 0, CgenSupport.SP, s); //1st par
	CgenSupport.emitAddiu(CgenSupport.SP, CgenSupport.SP, -4, s);
	this.e2.code(s, env); // 2nd par in 12($a0)
	CgenSupport.emitLoad(CgenSupport.ACC, 3, CgenSupport.ACC, s);
	CgenSupport.emitLoad(CgenSupport.T1, 1, CgenSupport.SP, s);
	CgenSupport.emitAddiu(CgenSupport.SP, CgenSupport.SP, 4, s);
	CgenSupport.emitBlt(CgenSupport.T1, CgenSupport.ACC, lbl1, "lt", s);
	CgenSupport.emitLoadBool(CgenSupport.ACC, new BoolConst(false), s);
	CgenSupport.emitJal("label" + 
			    lbl2 + "_lt", s); // jump to end of lt comparison
	CgenSupport.emitLabelDef(lbl1, "lt",  s);
	CgenSupport.emitLoadBool(CgenSupport.ACC, new BoolConst(true), s);
	CgenSupport.emitLabelDef(lbl2, "lt", s); // end of lt comparison
    }
}


/** Defines AST constructor 'eq'.
    <p>
    See <a href="TreeNode.html">TreeNode</a> for full documentation. */
class eq extends Expression {
    public Expression e1;
    public Expression e2;
    /** Creates "eq" AST node. 
      *
      * @param lineNumber the line in the source file from which this node came.
      * @param a0 initial value for e1
      * @param a1 initial value for e2
      */
    public eq(int lineNumber, Expression a1, Expression a2) {
        super(lineNumber);
        e1 = a1;
        e2 = a2;
    }
    public TreeNode copy() {
        return new eq(lineNumber, (Expression)e1.copy(), (Expression)e2.copy());
    }
    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "eq\n");
        e1.dump(out, n+2);
        e2.dump(out, n+2);
    }

    
    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_eq");
	e1.dump_with_types(out, n + 2);
	e2.dump_with_types(out, n + 2);
	dump_type(out, n);
    }
    /** Generates code for this expression.  This method is to be completed 
      * in programming assignment 5.  (You may add or remove parameters as
      * you wish.)
      * @param s the output stream 
      * */
    public void code(PrintStream s, Hashtable env) {
	int lbl1 = CgenSupport.LC++;
	int lbl2 = CgenSupport.LC++;

	this.e1.code(s, env);
	CgenSupport.emitStore(CgenSupport.ACC, 0, CgenSupport.SP, s); //1st par
	CgenSupport.emitAddiu(CgenSupport.SP, CgenSupport.SP, -4, s);
	this.e2.code(s, env); // 2nd par in $a0
	CgenSupport.emitLoad(CgenSupport.T1, 1, CgenSupport.SP, s);
	CgenSupport.emitAddiu(CgenSupport.SP, CgenSupport.SP, 4, s);
	CgenSupport.emitMove(CgenSupport.T2, CgenSupport.ACC, s);

	CgenSupport.emitLoadBool(CgenSupport.ACC, new BoolConst(true), s);
	CgenSupport.emitBeq(CgenSupport.T1, CgenSupport.T2, lbl1, "eq", s);
	CgenSupport.emitLoadBool(CgenSupport.A1, new BoolConst(false), s);
	CgenSupport.emitJal("equality_test", s);
	CgenSupport.emitLabelDef(lbl1, "eq", s);	
    }
}


/** Defines AST constructor 'leq'.
    <p>
    See <a href="TreeNode.html">TreeNode</a> for full documentation. */
class leq extends Expression {
    public Expression e1;
    public Expression e2;
    /** Creates "leq" AST node. 
      *
      * @param lineNumber the line in the source file from which this node came.
      * @param a0 initial value for e1
      * @param a1 initial value for e2
      */
    public leq(int lineNumber, Expression a1, Expression a2) {
        super(lineNumber);
        e1 = a1;
        e2 = a2;
    }
    public TreeNode copy() {
        return new leq(lineNumber, (Expression)e1.copy(), (Expression)e2.copy());
    }
    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "leq\n");
        e1.dump(out, n+2);
        e2.dump(out, n+2);
    }

    
    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_leq");
	e1.dump_with_types(out, n + 2);
	e2.dump_with_types(out, n + 2);
	dump_type(out, n);
    }
    /** Generates code for this expression.  This method is to be completed 
      * in programming assignment 5.  (You may add or remove parameters as
      * you wish.)
      * @param s the output stream 
      * */
    public void code(PrintStream s, Hashtable env) {
	int lbl1 = CgenSupport.LC++;
	int lbl2 = CgenSupport.LC++;

	this.e1.code(s, env);
	CgenSupport.emitLoad(CgenSupport.ACC, 3, CgenSupport.ACC, s);
	CgenSupport.emitStore(CgenSupport.ACC, 0, CgenSupport.SP, s); //1st par
	CgenSupport.emitAddiu(CgenSupport.SP, CgenSupport.SP, -4, s);
	this.e2.code(s, env); // 2nd par in 12($a0)
	CgenSupport.emitLoad(CgenSupport.ACC, 3, CgenSupport.ACC, s);
	CgenSupport.emitLoad(CgenSupport.T1, 1, CgenSupport.SP, s);
	CgenSupport.emitAddiu(CgenSupport.SP, CgenSupport.SP, 4, s);
	CgenSupport.emitBleq(CgenSupport.T1, CgenSupport.ACC, lbl1, "lt", s);
	CgenSupport.emitLoadBool(CgenSupport.ACC, new BoolConst(false), s);
	CgenSupport.emitJal("label" + 
			    lbl2 + "_lt", s); // jump to end of lt comparison
	CgenSupport.emitLabelDef(lbl1, "lt",  s);
	CgenSupport.emitLoadBool(CgenSupport.ACC, new BoolConst(true), s);
	CgenSupport.emitLabelDef(lbl2, "lt", s); // end of lt comparison
    }
}


/** Defines AST constructor 'comp'.
    <p>
    See <a href="TreeNode.html">TreeNode</a> for full documentation. */
class comp extends Expression {
    public Expression e1;
    /** Creates "comp" AST node. 
      *
      * @param lineNumber the line in the source file from which this node came.
      * @param a0 initial value for e1
      */
    public comp(int lineNumber, Expression a1) {
        super(lineNumber);
        e1 = a1;
    }
    public TreeNode copy() {
        return new comp(lineNumber, (Expression)e1.copy());
    }
    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "comp\n");
        e1.dump(out, n+2);
    }

    
    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_comp");
	e1.dump_with_types(out, n + 2);
	dump_type(out, n);
    }
    /** Generates code for this expression.  This method is to be completed 
      * in programming assignment 5.  (You may add or remove parameters as
      * you wish.)
      * @param s the output stream 
      * */
    public void code(PrintStream s, Hashtable env) {
	int lbl1 = CgenSupport.LC++;
	int lbl2 = CgenSupport.LC++;

	this.e1.code(s, env);
	//CgenSupport.emitLoadImm(CgenSupport.T1, 1, s);
	CgenSupport.emitLoad(CgenSupport.ACC, CgenSupport.CLASS_ATTR_OFFSET,
			     CgenSupport.ACC, s);
	CgenSupport.emitLoadBool(CgenSupport.T1, new BoolConst(true), s);
	CgenSupport.emitLoad(CgenSupport.T1, CgenSupport.CLASS_ATTR_OFFSET,
			     CgenSupport.T1, s);
	
	CgenSupport.emitBeq(CgenSupport.ACC, CgenSupport.T1, lbl1, "comp", s);
	//CgenSupport.emitLoadImm(CgenSupport.ACC, 1, s); // load true (comp)
	CgenSupport.emitLoadBool(CgenSupport.ACC, new BoolConst(true), s);
	CgenSupport.emitJal("label" + 
			    lbl2 + "_comp", s); // jump to end of comp
	CgenSupport.emitLabelDef(lbl1, "comp", s);
	//CgenSupport.emitLoadImm(CgenSupport.ACC, 0, s); // load false (comp)
	CgenSupport.emitLoadBool(CgenSupport.ACC, new BoolConst(false), s);
	CgenSupport.emitLabelDef(lbl2, "comp", s); // end of comp
    }


}


/** Defines AST constructor 'int_const'.
    <p>
    See <a href="TreeNode.html">TreeNode</a> for full documentation. */
class int_const extends Expression {
    public AbstractSymbol token;
    /** Creates "int_const" AST node. 
      *
      * @param lineNumber the line in the source file from which this node came.
      * @param a0 initial value for token
      */
    public int_const(int lineNumber, AbstractSymbol a1) {
        super(lineNumber);
        token = a1;
    }
    public TreeNode copy() {
        return new int_const(lineNumber, copy_AbstractSymbol(token));
    }
    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "int_const\n");
        dump_AbstractSymbol(out, n+2, token);
    }

    
    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_int");
	dump_AbstractSymbol(out, n + 2, token);
	dump_type(out, n);
    }
    /** Generates code for this expression.  This method method is provided
      * to you as an example of code generation.
      * @param s the output stream 
      * */
    public void code(PrintStream s, Hashtable env) {
	CgenSupport.emitLoadInt(CgenSupport.ACC,
                                (IntSymbol)AbstractTable.inttable.lookup(token.getString()), s);
    }

}


/** Defines AST constructor 'bool_const'.
    <p>
    See <a href="TreeNode.html">TreeNode</a> for full documentation. */
class bool_const extends Expression {
    public Boolean val;
    /** Creates "bool_const" AST node. 
      *
      * @param lineNumber the line in the source file from which this node came.
      * @param a0 initial value for val
      */
    public bool_const(int lineNumber, Boolean a1) {
        super(lineNumber);
        val = a1;
    }
    public TreeNode copy() {
        return new bool_const(lineNumber, copy_Boolean(val));
    }
    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "bool_const\n");
        dump_Boolean(out, n+2, val);
    }

    
    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_bool");
	dump_Boolean(out, n + 2, val);
	dump_type(out, n);
    }
    /** Generates code for this expression.  This method method is provided
      * to you as an example of code generation.
      * @param s the output stream 
      * */
    public void code(PrintStream s, Hashtable env) {
	CgenSupport.emitLoadBool(CgenSupport.ACC, new BoolConst(val), s);
    }

}


/** Defines AST constructor 'string_const'.
    <p>
    See <a href="TreeNode.html">TreeNode</a> for full documentation. */
class string_const extends Expression {
    public AbstractSymbol token;
    /** Creates "string_const" AST node. 
      *
      * @param lineNumber the line in the source file from which this node came.
      * @param a0 initial value for token
      */
    public string_const(int lineNumber, AbstractSymbol a1) {
        super(lineNumber);
        token = a1;
    }
    public TreeNode copy() {
        return new string_const(lineNumber, copy_AbstractSymbol(token));
    }
    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "string_const\n");
        dump_AbstractSymbol(out, n+2, token);
    }

    
    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_string");
	out.print(Utilities.pad(n + 2) + "\"");
	Utilities.printEscapedString(out, token.getString());
	out.println("\"");
	dump_type(out, n);
    }
    /** Generates code for this expression.  This method method is provided
      * to you as an example of code generation.
      * @param s the output stream 
      * */
    public void code(PrintStream s, Hashtable env) {
	CgenSupport.emitLoadString(CgenSupport.ACC,
                                   (StringSymbol)AbstractTable.stringtable.lookup(token.getString()), s);
    }

}


/** Defines AST constructor 'new_'.
    <p>
    See <a href="TreeNode.html">TreeNode</a> for full documentation. */
class new_ extends Expression {
    public AbstractSymbol type_name;
    /** Creates "new_" AST node. 
      *
      * @param lineNumber the line in the source file from which this node came.
      * @param a0 initial value for type_name
      */
    public new_(int lineNumber, AbstractSymbol a1) {
        super(lineNumber);
        type_name = a1;
    }
    public TreeNode copy() {
        return new new_(lineNumber, copy_AbstractSymbol(type_name));
    }
    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "new_\n");
        dump_AbstractSymbol(out, n+2, type_name);
    }

    
    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_new");
	dump_AbstractSymbol(out, n + 2, type_name);
	dump_type(out, n);
    }
    /** Generates code for this expression.  This method is to be completed 
      * in programming assignment 5.  (You may add or remove parameters as
      * you wish.)
      * @param s the output stream 
      * */
    public void code(PrintStream s, Hashtable env) {
	CgenNode c = (CgenNode)env.get("currClass");
	AbstractSymbol t0 = this.type_name;
	//System.out.println("c: " + c.getName());
	//System.out.println("this.type_name: " + t0);
	//System.out.println("New " + this.type_name);
	if (this.type_name.equals(TreeConstants.SELF_TYPE)) {
	    CgenSupport.emitLoadAddress(CgenSupport.T1, 
					CgenSupport.CLASSOBJTAB, s);
	    CgenSupport.emitLoad(CgenSupport.T2, 0, CgenSupport.SELF, s);
	    CgenSupport.emitSll(CgenSupport.T2, CgenSupport.T2, 3, s);
	    CgenSupport.emitAddu(CgenSupport.T1, CgenSupport.T1, 
				 CgenSupport.T2, s); // new class T
	    CgenSupport.emitStore(CgenSupport.T1, 0, CgenSupport.SP, s);
	    CgenSupport.emitAddiu(CgenSupport.SP, CgenSupport.SP, -4, s);
	    //CgenSupport.emitMove(CgenSupport.SELF, CgenSupport.ACC, s);
	    CgenSupport.emitLoad(CgenSupport.ACC, 0, CgenSupport.T1, s);
	    CgenSupport.emitJal("Object.copy", s);
	    //CgenSupport.emitMove(CgenSupport.SELF, CgenSupport.ACC, s);
	    CgenSupport.emitLoad(CgenSupport.T1, 1, CgenSupport.SP, s);
	    CgenSupport.emitAddiu(CgenSupport.SP, CgenSupport.SP, 4, s);
	    CgenSupport.emitLoad(CgenSupport.T1, 1, CgenSupport.T1, s);
	    CgenSupport.emitJalr(CgenSupport.T1, s);
	    //CgenSupport.emitMove(CgenSupport.ACC, CgenSupport.SELF, s);
	    
	    //System.out.println("here");
	    //t0 = c.getName();
	    //env.put("SELF_TYPE", 
	    /*CgenSupport.emitLoadAddress(CgenSupport.T1, 
					CgenSupport.CLASSOBJTAB, s);
	    CgenSupport.emitLoad(CgenSupport.T2, 0, CgenSupport.SELF, s);
	    CgenSupport.emitSll(CgenSupport.T2, CgenSupport.T2, 3, s);
	    CgenSupport.emitAddu(CgenSupport.T1, CgenSupport.T1, 
				 CgenSupport.T2, s);
	    CgenSupport.emitMove(CgenSupport.S1, CgenSupport.T1, s);
	    CgenSupport.emitLoad(CgenSupport.ACC, 0, CgenSupport.T1, s);
	    CgenSupport.emitJal("Object.copy", s);
	    CgenSupport.emitLoad(CgenSupport.T1, 1, CgenSupport.S1, s);
	    CgenSupport.emitJalr(CgenSupport.T1, s);*/
	} else {
	    CgenSupport.emitLoadAddress(CgenSupport.ACC, 
					t0.toString() + 
					CgenSupport.PROTOBJ_SUFFIX, 
					s);
	    CgenSupport.emitJal("Object.copy", s);
	    CgenSupport.emitJal(t0.toString() + 
				CgenSupport.CLASSINIT_SUFFIX, s);
	}
    }
}


/** Defines AST constructor 'isvoid'.
    <p>
    See <a href="TreeNode.html">TreeNode</a> for full documentation. */
class isvoid extends Expression {
    public Expression e1;
    /** Creates "isvoid" AST node. 
      *
      * @param lineNumber the line in the source file from which this node came.
      * @param a0 initial value for e1
      */
    public isvoid(int lineNumber, Expression a1) {
        super(lineNumber);
        e1 = a1;
    }
    public TreeNode copy() {
        return new isvoid(lineNumber, (Expression)e1.copy());
    }
    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "isvoid\n");
        e1.dump(out, n+2);
    }

    
    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_isvoid");
	e1.dump_with_types(out, n + 2);
	dump_type(out, n);
    }
    /** Generates code for this expression.  This method is to be completed 
      * in programming assignment 5.  (You may add or remove parameters as
      * you wish.)
      * @param s the output stream 
      * */
    public void code(PrintStream s, Hashtable env) {
	this.e1.code(s, env);

	int lbl1 = CgenSupport.LC++;
	int lbl2 = CgenSupport.LC++;
	CgenSupport.emitBeqz(CgenSupport.ACC, lbl1, s);
	CgenSupport.emitLoadBool(CgenSupport.ACC, new BoolConst(false), s);
	CgenSupport.emitJal("label" + lbl2, s);
	CgenSupport.emitLabelDef(lbl1, s);
	CgenSupport.emitLoadBool(CgenSupport.ACC, new BoolConst(true), s);
	CgenSupport.emitLabelDef(lbl2, s);
    }


}


/** Defines AST constructor 'no_expr'.
    <p>
    See <a href="TreeNode.html">TreeNode</a> for full documentation. */
class no_expr extends Expression {
    /** Creates "no_expr" AST node. 
      *
      * @param lineNumber the line in the source file from which this node came.
      */
    public no_expr(int lineNumber) {
        super(lineNumber);
    }
    public TreeNode copy() {
        return new no_expr(lineNumber);
    }
    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "no_expr\n");
    }

    
    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_no_expr");
	dump_type(out, n);
    }
    /** Generates code for this expression.  This method is to be completed 
      * in programming assignment 5.  (You may add or remove parameters as
      * you wish.)
      * @param s the output stream 
      * */
    public void code(PrintStream s, Hashtable env) {
    }


}


/** Defines AST constructor 'object'.
    <p>
    See <a href="TreeNode.html">TreeNode</a> for full documentation. */
class object extends Expression {
    public AbstractSymbol name;
    /** Creates "object" AST node. 
      *
      * @param lineNumber the line in the source file from which this node came.
      * @param a0 initial value for name
      */
    public object(int lineNumber, AbstractSymbol a1) {
        super(lineNumber);
        name = a1;
    }
    public TreeNode copy() {
        return new object(lineNumber, copy_AbstractSymbol(name));
    }
    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "object\n");
        dump_AbstractSymbol(out, n+2, name);
    }

    
    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_object");
	dump_AbstractSymbol(out, n + 2, name);
	dump_type(out, n);
    }
    /** Generates code for this expression.  This method is to be completed 
      * in programming assignment 5.  (You may add or remove parameters as
      * you wish.)
      * @param s the output stream 
      * */
    public void code(PrintStream s, Hashtable env) {
	CgenNode c = (CgenNode)env.get("currClass");
	method m = (method)env.get("currMethod");
	int letCount = (env.get("currLetCount") != null) ? 
	    ((Integer)env.get("currLetCount")).intValue() : 0;

	//System.out.println("var name: " + this.name);
	if (this.name.equals(TreeConstants.self)) {
	    //System.out.println("here-: class= " + c.getName());
	    CgenSupport.emitMove(CgenSupport.ACC, CgenSupport.SELF, s);
	} else {
	    //System.out.println("NULL: " + m);
	    String methodName = (m != null) ? m.name.toString() : null;
	    ArrayList<String> sig = 
		(ArrayList<String>)env.get(c.getName() + "-" + 
					   methodName + "-sigs");

	    // load var from let expr
	    //System.out.println(this.name.toString() + ": " + env.get(this.name.toString()));
	    //System.out.println(env);
	    if (env.get(this.name.toString()) != null) {
		//System.out.println("HERE: " + this.name.toString());
		CgenSupport.emitLoad(CgenSupport.ACC, 
				     ((Integer)env.
				      get(this.name.toString())).intValue(),
				     CgenSupport.FP, s);
		return;
	    }
	    
	    Vector attrs = (Vector)env.get(c.getName() + "-attrs");
	    //System.out.println(c.getName() + "-attrs " + attrs);
	    // if var is a method param, get from the stack otherwise get attr
	    if (sig != null && 
		sig.contains(this.name.toString())) { // get method param
		// add 3 words to offset since FT, SELF, and RA are on stack too
		CgenSupport.emitLoad(CgenSupport.ACC, 
				     CgenSupport.STACK_PARAM_OFFSET + 
				     sig.indexOf(this.name.toString()) + 
				     letCount,
				     CgenSupport.FP, s);
	    } else if (attrs.contains(this.name.toString())) { // get attr
		// add 3 words to offset since first attr is at 12
		//System.out.println("c: " + c.getName());
		//System.out.println("HERE: " + this.name + " = " + (CgenSupport.CLASS_ATTR_OFFSET + attrs.indexOf(this.name.toString())));
		CgenSupport.emitLoad(CgenSupport.ACC,
				     CgenSupport.CLASS_ATTR_OFFSET + 
				     attrs.indexOf(this.name.toString()),
				     CgenSupport.SELF, s);
	    }
	}
    }
}


