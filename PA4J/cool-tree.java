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

import java.util.ArrayList;


/** Defines simple phylum Program */
abstract class Program extends TreeNode {
    protected Program(int lineNumber) {
        super(lineNumber);
    }
    public abstract void dump_with_types(PrintStream out, int n);
    public abstract void semant();

}


/** Defines simple phylum Class_ */
abstract class Class_ extends TreeNode {
    protected Class_(int lineNumber) {
        super(lineNumber);
    }
    public abstract void dump_with_types(PrintStream out, int n);

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


/** Defines AST constructor 'programc'.
    <p>
    See <a href="TreeNode.html">TreeNode</a> for full documentation. */
class programc extends Program {
    protected Classes classes;
    /** Creates "programc" AST node. 
      *
      * @param lineNumber the line in the source file from which this node came.
      * @param a0 initial value for classes
      */
    public programc(int lineNumber, Classes a1) {
        super(lineNumber);
        classes = a1;
    }
    public TreeNode copy() {
        return new programc(lineNumber, (Classes)classes.copy());
    }
    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "programc\n");
        classes.dump(out, n+2);
    }

    
    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_program");
        for (Enumeration e = classes.getElements(); e.hasMoreElements(); ) {
            // sm: changed 'n + 1' to 'n + 2' to match changes elsewhere
	    ((Class_)e.nextElement()).dump_with_types(out, n + 2);
        }
    }

    public void addAttributesToEnv(class_c c, class_c p, SymbolTable env, 
				   ClassTable ct) {
	// top of inheritance tree reached
	if (p == null || p.parent.equals(TreeConstants.SELF_TYPE)) {
	    return;
	}

	addAttributesToEnv(c, (class_c)env.lookup(p.parent), env, ct);

	for (Enumeration fe = p.features.getElements(); 
	     fe.hasMoreElements(); ) {
	    Object element = fe.nextElement();
	    
	    if (element instanceof attr) {
		attr attr = (attr)element;

		if (env.lookup(attr.name) != null) {
		    ct.semantError(c).
			println("Attribute " + attr.name + 
				" is an attribute of an inherited class.");
		}
		
		env.addId(attr.name, attr.type_decl);
	    }
	}
    }

    public AbstractSymbol key(AbstractSymbol cName, AbstractSymbol mName) {
	return AbstractTable.idtable.addString(cName + "." + mName);
    }

    public void addMethodsToEnv(class_c c, class_c p, SymbolTable env, 
				    SymbolTable mEnv, ClassTable ct) {
	// reached top of inheritance tree. done.
	if (p == null || p.parent.equals(TreeConstants.SELF_TYPE)) {
	    return;
	}

	addMethodsToEnv(c, (class_c)env.lookup(p.parent), env, mEnv, ct);

	//System.out.println("From Parent: " + p.getName());
	for (Enumeration fe = p.features.getElements(); 
	     fe.hasMoreElements(); ) {
	    Object element = fe.nextElement();
	    
	    if (element instanceof method) {
		method m = (method)element;

		ArrayList<AbstractSymbol> existingSig = 
		    ((ArrayList<AbstractSymbol>)mEnv.
		     lookup(key(c.getName(), m.name)));
		int existingSigParamTracker = 0;

		// if redefining method in child make sure # of params match
		if (existingSig != null) {
		    if (m.formals.getLength() != existingSig.size() - 1) {
			ct.semantError(c).
			    println("Incompatible number of formal " + 
				    "parameters in redefined method " +
				    m.name);
		    }
		}
		
		ArrayList<AbstractSymbol> sig = 
		    new ArrayList<AbstractSymbol>();
		
		for (Enumeration eFormals = m.formals.getElements(); 
		     eFormals.hasMoreElements();) {
		    formalc param = (formalc)eFormals.nextElement();

		    // make sure new param types match old param types
		    if (existingSig != null && existingSig.size() > 0) {
			if (!param.type_decl.
			    equals(existingSig.
				   get(existingSigParamTracker++))) {
			    ct.semantError(c).
				println("In redefined method " + m.name +
					", parameter type " + param.type_decl +
					" is different from original type " + 
					existingSig.
					get(existingSigParamTracker - 1));
			}
		    }

		    sig.add(param.type_decl);
		}
		sig.add(m.return_type);
		
		mEnv.addId(key(c.getName(), m.name), sig);
		//System.out.println("Added " + key(c, m) + ": \n" + mEnv.toString());
	    }
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
	/* Create Symbol Table and add class names */
	SymbolTable st = new SymbolTable();
	st.enterScope(); // enter all-class-level scope

	/* Create Symbol Table for method names */
	SymbolTable stMethods = new SymbolTable();
	stMethods.enterScope();

	boolean classMainDefined = false;

	for (Enumeration e = classes.getElements(); e.hasMoreElements(); ) {
	    class_c c = (class_c) e.nextElement();
	    st.addId(c.getName(), c);
	    
	    if (c.getName().equals(TreeConstants.Main)) {
		classMainDefined = true;
	    }
	}

	/* ClassTable constructor may do some semantic analysis */
	ClassTable classTable = new ClassTable(classes, st);
	
	/* some semantic analysis code may go here */

		// error if class Main is not defined
	if (!classMainDefined) {
	    classTable.semantError().println("Class Main is not defined.");
	}

	// add all methods from all classes to mEnv (w/ inherited too)
	Vector classesWithBasic = classes.copyElements();
	classesWithBasic.add(st.lookup(TreeConstants.Object_));
	classesWithBasic.add(st.lookup(TreeConstants.IO));
	classesWithBasic.add(st.lookup(TreeConstants.Int));
	classesWithBasic.add(st.lookup(TreeConstants.Bool));
	classesWithBasic.add(st.lookup(TreeConstants.Str));

	for (Enumeration e = classesWithBasic.elements(); 
	     e.hasMoreElements(); ) {
 	    class_c c = (class_c) e.nextElement();

	    // recursively add all (inherited) methods
	    addMethodsToEnv(c, c, st, stMethods, classTable);
	}


	// typecheck each class
	for (Enumeration e = classes.getElements(); e.hasMoreElements(); ) {
 	    class_c c = (class_c) e.nextElement();
	    //c.dump(System.out, 2);
	    st.enterScope(); // begin class scope

	    // set SELF_TYPE of this class
	    st.addId(TreeConstants.SELF_TYPE, c);

	    // set special ID self
	    st.addId(TreeConstants.self, TreeConstants.SELF_TYPE);

	    // add attrs to env
	    addAttributesToEnv(c, c, st, classTable);

	    // Typecheck each Feature (attrs & methods)
	    for (Enumeration fe = c.features.getElements(); 
		 fe.hasMoreElements(); ) {
		Object element = fe.nextElement();

		// attribute typecheck
		if (element instanceof attr) {
		    attr attr = (attr)element;
		    AbstractSymbol t = typeCheck(classTable, st, stMethods, 
						 c, attr);
		}

		// method typecheck
		if (element instanceof method) {
		    method method = (method)element;
		    // TODO?: M(C,f) = (T1,...,Tn,T0)
		    
		    // add formal params to env before typechecking method expr
		    st.enterScope();
		    
		    st.addId(TreeConstants.self, TreeConstants.SELF_TYPE);
		    for (Enumeration eFormals = method.formals.getElements(); 
			 eFormals.hasMoreElements();) {
			formalc param = (formalc)eFormals.nextElement();

			// check that 'self' is not a param name
			if (param.name.equals(TreeConstants.self)) {
			    classTable.semantError(c).
				println("'self' cannot be the name of a" + 
					" formal parameter.");
			}

			// check for duplicate param name
			if (st.probe(param.name) != null && 
			    !param.name.equals(TreeConstants.self)) {
			    classTable.semantError(c).
				println("Formal parameter " + param.name +
					" is multiply defined.");
			}

			// check that param doesn't have type SELF_TYPE
			if (param.type_decl.equals(TreeConstants.SELF_TYPE)) {
			    classTable.semantError(c).
				println("Formal parameter " + param.name + 
					" cannot have type SELF_TYPE");
			}

			st.addId(param.name, param.type_decl);
		    }
		    
		    AbstractSymbol tExpr = typeCheck(classTable, st, stMethods,
						     c, method.expr);
		    
		    // remove formal parameters after typechecking method expr
		    st.exitScope(); 
		    
		    /*if (method.return_type.equals(TreeConstants.SELF_TYPE)) {
			methos.return_type = 
		    }*/
		    
		    AbstractSymbol tReturnType = method.return_type;
		    class_c cReturnType = (class_c)st.lookup(tReturnType);
		    // error on undefined return type
		    if (cReturnType == null) {
			classTable.semantError(c).
			    println("Undefined return type " 
				    + method.return_type + " in method " + 
				    method.name);
		    }

		    //class_c cExpr = (class_c)st.lookup(tExpr);
		    //System.out.println("method.return_type: " + method.return_type);
		    //System.out.println("tExpr: " + tExpr);
		    //if (cReturnType != null 
		    //&& !classTable.isSubtype(tExpr, tReturnType)) {
		    if (!classTable.isSubtype(st, c,
					      tExpr, tReturnType)) {
			classTable.semantError(c).
			    println(tExpr + " is not a subtype of " 
				    + method.return_type + ".");
		    }
		}
	    }

	    //printEnv(st);
	    st.exitScope(); // end class scope
	}
	//printEnv(st);

	if (classTable.errors()) {
	    System.err.println("Compilation halted due to static semantic errors.");
	    System.exit(1);
	}
    }

    public void printEnv(SymbolTable env) {
	System.out.println(env.toString());
    }

    public AbstractSymbol typeCheck(ClassTable ct, SymbolTable env, 
				    SymbolTable mEnv, class_c currClass,
				    Object obj) {
	// static_dispatch
	if (obj instanceof static_dispatch) {
	    static_dispatch static_dispatch = (static_dispatch)obj;
	    AbstractSymbol t0 = typeCheck(ct, env, mEnv, currClass,
					  static_dispatch.expr);
	    AbstractSymbol t = static_dispatch.type_name;
	    if (!ct.isSubtype(env, currClass, t0, t)) {
		ct.semantError(currClass).
		    println("In static method call " + static_dispatch.name + 
			     ", " + t0 + " is not a subtype of " + t + ".");
	    }

	    ArrayList<AbstractSymbol> methodSig = 
		(ArrayList<AbstractSymbol>)mEnv.
		lookup(key(t, static_dispatch.name));
	    //System.out.println("methodSig: " + methodSig);
	    if (methodSig == null) {
		ct.semantError(currClass).
		    println("Dispatch to undefined method " 
			    + static_dispatch.name);
		static_dispatch.set_type(TreeConstants.Object_);
		return TreeConstants.Object_;
	    }

	    // make sure # of parameters match
	    if (methodSig.size() - 1 != static_dispatch.actual.getLength()) {
		ct.semantError(currClass).
		    println("Number of ctual parameters in method " + 
			    static_dispatch.name + 
			    " do not match number in method signature.");
		static_dispatch.set_type(TreeConstants.Object_);
		return TreeConstants.Object_;
	    }

	    // check that actual params are subtypes of sig params
	    int i = 0;
	    for (Enumeration e = static_dispatch.actual.getElements();
		 e.hasMoreElements();) {
		Expression actualParamExpr = (Expression)e.nextElement();
		AbstractSymbol tActualParam = typeCheck(ct, env, mEnv, 
							currClass, 
							actualParamExpr);
		//class_c classActualParam = (class_c)env.lookup(tActualParam);

		AbstractSymbol tSigParam = methodSig.get(i++);
		//class_c classSigParam = (class_c)env.lookup(tSigParam);
		
		if (!ct.isSubtype(env, currClass, tActualParam, tSigParam)) {
		    ct.semantError(currClass).
			println("In method call " + static_dispatch.name  + 
				", type "+ 
				tActualParam + 
				" is not a subtype of " + 
				tSigParam + ".");
		    static_dispatch.set_type(TreeConstants.Object_);
		}
	    }

	    AbstractSymbol tNplus1 = methodSig.get(methodSig.size() - 1);
	    if (methodSig.get(methodSig.size() - 1).
		equals(TreeConstants.SELF_TYPE)) {
		tNplus1 = t0;
	    }

	    static_dispatch.set_type(tNplus1);
	    return tNplus1;
	}

	// dispatch
	if (obj instanceof dispatch) {
	    dispatch dispatch = (dispatch)obj;
	    AbstractSymbol t0 = typeCheck(ct, env, mEnv, currClass,
					  dispatch.expr);
	    AbstractSymbol t0original = (AbstractSymbol)t0.clone();

	    // TODO?: might have to change to ct.isSubtype(c0, currClass)
	    //System.out.println("expr: " + ((object)dispatch.expr).name);
	    if (t0.equals(TreeConstants.SELF_TYPE)) {
	    //if (ct.isSubtype((class_c)env.lookup(t0), currClass)) {
		t0 = currClass.getName();
	    }

	    ArrayList<AbstractSymbol> methodSig = 
		(ArrayList<AbstractSymbol>)mEnv.lookup(key(t0, dispatch.name));
	    //System.out.println("methodSig: " + methodSig);
	    if (methodSig == null) {
		ct.semantError(currClass).
		    println("Dispatch to undefined method " + dispatch.name);
		dispatch.set_type(TreeConstants.Object_);
		return TreeConstants.Object_;
	    }

	    // make sure # of parameters match
	    if (methodSig.size() - 1 != dispatch.actual.getLength()) {
		ct.semantError(currClass).
		    println("Number of ctual parameters in method " + 
			    dispatch.name + 
			    " do not match number in method signature.");
		dispatch.set_type(TreeConstants.Object_);
		return TreeConstants.Object_;
	    }

	    // check that actual params are subtypes of sig params
	    int i = 0;
	    for (Enumeration e = dispatch.actual.getElements();
		 e.hasMoreElements();) {
		Expression actualParamExpr = (Expression)e.nextElement();
		AbstractSymbol tActualParam = typeCheck(ct, env, mEnv, 
							currClass, 
							actualParamExpr);
		//class_c classActualParam = (class_c)env.lookup(tActualParam);

		AbstractSymbol tSigParam = methodSig.get(i++);
		//class_c classSigParam = (class_c)env.lookup(tSigParam);
		
		if (!ct.isSubtype(env, currClass, tActualParam, tSigParam)) {
		    ct.semantError(currClass).
			println("In method call " + dispatch.name  +", type "+ 
				tActualParam + 
				" is not a subtype of " + 
				tSigParam + ".");
		    dispatch.set_type(TreeConstants.Object_);
		}
	    }

	    AbstractSymbol tNplus1 = methodSig.get(methodSig.size() - 1);
	    if (methodSig.get(methodSig.size() - 1).
		equals(TreeConstants.SELF_TYPE)) {
		tNplus1 = t0original;
	    }

	    dispatch.set_type(tNplus1);
	    return tNplus1;
	}

	// case
	if (obj instanceof typcase) {
	    typcase typcase = (typcase)obj;
	    AbstractSymbol t0 = typeCheck(ct, env, mEnv, currClass, 
					  typcase.expr);
	    
	    ArrayList<AbstractSymbol> caseTypeList = 
		new ArrayList<AbstractSymbol>();
	    ArrayList<AbstractSymbol> caseTypeListBeforeTypeCheck = 
		new ArrayList<AbstractSymbol>();

	    for (Enumeration e = typcase.cases.getElements(); 
		 e.hasMoreElements();) {
		branch b = (branch) e.nextElement();
		//b.dump_with_types(System.out, 2);
		env.enterScope();

		// check for duplicate branch
		//System.out.println(caseTypeListBeforeTypeCheck);
		//System.out.println(b.type_decl);
		if (caseTypeListBeforeTypeCheck.contains(b.type_decl)) {
		    ct.semantError(currClass).
			println("Duplicate branch " + b.type_decl + 
				" in case statement.");
		    typcase.set_type(TreeConstants.Object_);
		}

		env.addId(b.name, b.type_decl);
		caseTypeListBeforeTypeCheck.add(b.type_decl);

		AbstractSymbol t1 = typeCheck(ct, env, mEnv, currClass, b.expr);
		caseTypeList.add(t1);
		env.exitScope();
	    }
	    //System.out.println(caseTypeList);
	    // compute LUB over list of case types
	    AbstractSymbol caseType = caseTypeList.get(0);
	 
	    for (AbstractSymbol t : caseTypeList) {
		caseType = ct.leastUpperBound(env, currClass, caseType, t).
		    getCurrentClass().getName();
	    }

	    typcase.set_type(caseType);
	    return caseType;
	}

	// let: both with and without init
	if (obj instanceof let) {
	    let let = (let)obj;
	    AbstractSymbol t0 = let.type_decl;
	    
	    // with init
	    if (!(let.init instanceof no_expr)) {
		AbstractSymbol t1 = typeCheck(ct, env, mEnv, currClass, 
					      let.init);
		//class_c c0 = (class_c)env.lookup(t0);
		//class_c c1 = (class_c)env.lookup(t1);
		//System.out.println("c0: " + c0.getName());
		//System.out.println("c1: " + c1.getName());
		if (!ct.isSubtype(env, currClass, t1, t0)) {
		    ct.semantError(currClass).println(t1 + 
						      " is not a subtype of " +
						      t0 + 
						      " in initialization.");
		}
	    }

	    // make sure "self" is not bound
	    if (let.identifier.equals(TreeConstants.self)) {
		ct.semantError(currClass).
		    println("'self' cannot be bound in a 'let' expression.");
	    }

	    env.enterScope();
	    env.addId(let.identifier, t0);
	    AbstractSymbol t2 = typeCheck(ct, env, mEnv, currClass, let.body);
	    env.exitScope();

	    let.set_type(t2);
	    return t2;
	}

	// equal
	if (obj instanceof eq) {
	    eq eq = (eq)obj;
	    AbstractSymbol t1 = typeCheck(ct, env, mEnv, currClass, eq.e1);
	    AbstractSymbol t2 = typeCheck(ct, env, mEnv, currClass, eq.e2);

	    // it t1 is Int/Str/Bool then t2 has to be the same type
	    if (t1.equals(TreeConstants.Int) ||
		t1.equals(TreeConstants.Str) ||
		t1.equals(TreeConstants.Bool)) {
		if (t1.equals(t2)) {
		    eq.set_type(TreeConstants.Bool);
		    return TreeConstants.Bool;

		} else {
		    ct.semantError(currClass).
			println("Expressions don't have the same valid type");
		    eq.set_type(TreeConstants.Object_);
		    return TreeConstants.Object_;
		}
	    } 

	    // it t2 is Int/Str/Bool then t1 has to be the same type
	    if (t2.equals(TreeConstants.Int) || t2.equals(TreeConstants.Str) ||
		t2.equals(TreeConstants.Bool)) {
		if (t2.equals(t1)) {
		    eq.set_type(TreeConstants.Bool);
		    return TreeConstants.Bool;

		} else {
		    ct.semantError(currClass).
			println("Expressions don't have the same valid type");
		    eq.set_type(TreeConstants.Object_);
		    return TreeConstants.Object_;
		}
	    } 
	    
	    eq.set_type(TreeConstants.Bool);
	    return TreeConstants.Bool;
	}

	// lt/less-than
	if (obj instanceof lt) {
	    lt lt = (lt)obj;
	    AbstractSymbol t1 = typeCheck(ct, env, mEnv, currClass, lt.e1);
	    AbstractSymbol t2 = typeCheck(ct, env, mEnv, currClass, lt.e2);
	    lt.set_type(TreeConstants.Bool);
	    return TreeConstants.Bool;
	}

	// leq/less-than-or-equal-to
	if (obj instanceof leq) {
	    leq leq = (leq)obj;
	    AbstractSymbol t1 = typeCheck(ct, env, mEnv, currClass, leq.e1);
	    AbstractSymbol t2 = typeCheck(ct, env, mEnv, currClass, leq.e2);
	    leq.set_type(TreeConstants.Bool);
	    return TreeConstants.Bool;
	}


	// not/comp
	if (obj instanceof comp) {
	    comp comp = (comp)obj;
	    AbstractSymbol t1 = typeCheck(ct, env, mEnv, currClass, comp.e1);
	    if (!t1.equals(TreeConstants.Bool)) {
		ct.semantError(currClass).
		    println("Compliment expression must be a Bool");
		comp.set_type(TreeConstants.Object_);
		return TreeConstants.Object_;
	    }

	    comp.set_type(TreeConstants.Bool);
	    return TreeConstants.Bool;
	}

	// neg
	if (obj instanceof neg) {
	    neg neg = (neg)obj;
	    AbstractSymbol t1 = typeCheck(ct, env, mEnv, currClass, neg.e1);
	    if (!t1.equals(TreeConstants.Int)) {
		ct.semantError(currClass).
		    println("Negated expression must be an Int");
		neg.set_type(TreeConstants.Object_);
		return TreeConstants.Object_;
	    }

	    neg.set_type(TreeConstants.Int);
	    return TreeConstants.Int;
	}

	// isvoid
	if (obj instanceof isvoid) {
	    isvoid isvoid = (isvoid)obj;
	    AbstractSymbol t1 = typeCheck(ct, env, mEnv, currClass, isvoid.e1);
	    isvoid.set_type(TreeConstants.Bool);
	    return TreeConstants.Bool;
	}

	// while/loop
	if (obj instanceof loop) {
	    loop loop = (loop)obj;
	    AbstractSymbol t1 = typeCheck(ct, env, mEnv, currClass, loop.pred);
	    if (!t1.equals(TreeConstants.Bool)) {
		ct.semantError(currClass).
		    println("First expression in while loop must be Bool");
	    }

	    AbstractSymbol t2 = typeCheck(ct, env, mEnv, currClass, loop.body);
	    loop.set_type(TreeConstants.Object_);
	    return TreeConstants.Object_;
	}

	// if
	if (obj instanceof cond) {
	    cond cond = (cond)obj;
	    AbstractSymbol t1 = typeCheck(ct, env, mEnv, currClass, cond.pred);
	    if (!t1.equals(TreeConstants.Bool)) {
		ct.semantError(currClass).
		    println("First expression in if statement must be Bool");
	    }

	    AbstractSymbol t2 = typeCheck(ct, env, mEnv, currClass, 
					  cond.then_exp);
	    AbstractSymbol t3 = typeCheck(ct, env, mEnv, currClass,
					  cond.else_exp);

	    //class_c c2 = (class_c)env.lookup(t2);
	    //class_c c3 = (class_c)env.lookup(t3);
	    InheritanceGraph g = ct.leastUpperBound(env, currClass, t2, t3);
	    AbstractSymbol t = g.getCurrentClass().getName();
	    cond.set_type(t);
	    return t;
	}

	// bool_const
	if (obj instanceof bool_const) {
	    bool_const b = (bool_const)obj;
	    b.set_type(TreeConstants.Bool);
	    return TreeConstants.Bool;
	}
	
	// int_const
	if (obj instanceof int_const) {
	    int_const i = (int_const)obj;
	    i.set_type(TreeConstants.Int);
	    return TreeConstants.Int;
	}

	// string_const
	if (obj instanceof string_const) {
	    string_const s = (string_const)obj;
	    s.set_type(TreeConstants.Str);
	    return TreeConstants.Str;
	}

	// plus
	if (obj instanceof plus) {
	    plus plus = (plus)obj;

	    AbstractSymbol t1 = typeCheck(ct, env, mEnv, currClass, plus.e1);
	    if (!t1.equals(TreeConstants.Int)) {
		ct.semantError(currClass).println("Error: e1 should be an Int");
		plus.set_type(TreeConstants.Object_);
		return TreeConstants.Object_;
	    }

	    AbstractSymbol t2 = typeCheck(ct, env, mEnv, currClass, plus.e2);
	    if (!t2.equals(TreeConstants.Int)) {
		ct.semantError(currClass).println("Error: e2 should be an Int");
		plus.set_type(TreeConstants.Object_);
		return TreeConstants.Object_;
	    }

	    plus.set_type(TreeConstants.Int);
	    return TreeConstants.Int;
	}

	// minus/sub
	if (obj instanceof sub) {
	    sub sub = (sub)obj;

	    AbstractSymbol t1 = typeCheck(ct, env, mEnv, currClass, sub.e1);
	    if (!t1.equals(TreeConstants.Int)) {
		ct.semantError(currClass).println("Error: e1 should be an Int");
		sub.set_type(TreeConstants.Object_);
		return TreeConstants.Object_;
	    }

	    AbstractSymbol t2 = typeCheck(ct, env, mEnv, currClass, sub.e2);
	    if (!t2.equals(TreeConstants.Int)) {
		ct.semantError(currClass).println("Error: e2 should be an Int");
		sub.set_type(TreeConstants.Object_);
		return TreeConstants.Object_;
	    }

	    sub.set_type(TreeConstants.Int);
	    return TreeConstants.Int;
	}

	// mul
	if (obj instanceof mul) {
	    mul mul = (mul)obj;

	    AbstractSymbol t1 = typeCheck(ct, env, mEnv, currClass, mul.e1);
	    if (!t1.equals(TreeConstants.Int)) {
		ct.semantError(currClass).println("Error: e1 should be an Int");
		mul.set_type(TreeConstants.Object_);
		return TreeConstants.Object_;
	    }

	    AbstractSymbol t2 = typeCheck(ct, env, mEnv, currClass, mul.e2);
	    if (!t2.equals(TreeConstants.Int)) {
		ct.semantError(currClass).println("Error: e2 should be an Int");
		mul.set_type(TreeConstants.Object_);
		return TreeConstants.Object_;
	    }

	    mul.set_type(TreeConstants.Int);
	    return TreeConstants.Int;
	}

	// divide
	if (obj instanceof divide) {
	    divide divide = (divide)obj;

	    AbstractSymbol t1 = typeCheck(ct, env, mEnv, currClass, divide.e1);
	    if (!t1.equals(TreeConstants.Int)) {
		ct.semantError(currClass).println("Error: e1 should be an Int");
		divide.set_type(TreeConstants.Object_);
		return TreeConstants.Object_;
	    }

	    AbstractSymbol t2 = typeCheck(ct, env, mEnv, currClass, divide.e2);
	    if (!t2.equals(TreeConstants.Int)) {
		ct.semantError(currClass).println("Error: e2 should be an Int");
		divide.set_type(TreeConstants.Object_);
		return TreeConstants.Object_;
	    }

	    divide.set_type(TreeConstants.Int);
	    return TreeConstants.Int;
	}

	if (obj instanceof block) {
	    block block = (block)obj;
	    AbstractSymbol t = TreeConstants.Object_;

	    for (Enumeration e = block.body.getElements(); 
		 e.hasMoreElements(); ) {
		Expression expr = (Expression)e.nextElement();
		t = typeCheck(ct, env, mEnv, currClass, expr);
	    }
	    
	    block.set_type(t);
	    return t;
	}

	if (obj instanceof new_) {
	    new_ new_ = (new_)obj;
	    // TODO: confirm correctness of self logic
	    AbstractSymbol t1 = new_.type_name;
	    //System.out.println("new_: " + t1);
	    if (t1.equals(TreeConstants.SELF_TYPE)) {
		//System.out.println("==== here ===");
		//t1 = currClass.getName();
		t1 = TreeConstants.SELF_TYPE;
	    }

	    new_.set_type(t1);
	    return t1;
	}
	
	if (obj instanceof object) {
	    object var = (object)obj;
	    // Review: added to test cases initwithself.test & newselftype.test
	    if (var.name.equals(TreeConstants.self)) {
		var.set_type(TreeConstants.SELF_TYPE);
		return TreeConstants.SELF_TYPE;
	    }

	    AbstractSymbol varType = (AbstractSymbol)env.lookup(var.name);
	    //System.out.println(var.name);
	    //System.out.println(varType);
	    // check for undeclared IDs
	    if (varType == null) {
		ct.semantError(currClass).println("Undeclared identifier " + 
						  var.name + ".");
		return TreeConstants.Object_;
	    }

	    var.set_type(varType);
	    return varType;
	}

	if (obj instanceof assign) {
	    assign assign = (assign)obj;
	    
	    // cannot assign to self
	    if (assign.name.equals(TreeConstants.self)) {
		ct.semantError(currClass).println("Cannot assign to 'self'.");
		assign.set_type(TreeConstants.Object_);
		return TreeConstants.Object_;
	    }

	    AbstractSymbol t0 = (AbstractSymbol)env.lookup(assign.name);
	    AbstractSymbol t1 = typeCheck(ct, env, mEnv, currClass, 
					  assign.expr);
	    class_c c1 = (class_c)env.lookup(t1);
	    class_c c0 = (class_c)env.lookup(t0);
	    //System.out.println(c1);
	    // Review: potential fix for test case "cells.cl.test"
	    //if (c1 == null || ct.isSubtype(c1, c0)) {
	    //System.out.println("t1: " + t1);
	    //System.out.println("t0: " + t0);
	    //System.out.println("isSubtype(t1,t0): " + ct.isSubtype(env, currClass, t1, t0));
	    if (ct.isSubtype(env, currClass, t1, t0)) {
		assign.set_type(t1);
		return t1;
	    } else {
		ct.semantError(currClass).println("Type " + c1.getName() 
					 + " of assigned expression does not " 
					 + "conform to declared type " 
					 + c0.getName() + " of identifier "
					 + assign.name + ".");
		assign.set_type(TreeConstants.Object_);
		return TreeConstants.Object_;
	    }
	}

	if (obj instanceof attr) {
	    // TODO: confirm correctness of self logic
	    attr attr = (attr)obj;

	    // attribute can't be named 'self'
	    if (attr.name.equals(TreeConstants.self)) {
		ct.semantError(currClass).
		    println("'self' cannot be the name of an attribute.");
		return TreeConstants.Object_;
	    }

	    // TODO: fix -- maybe do check earlier in process
	    // TODO: report attempt at overriding inherited attribute
	    /*System.out.println("lookup(attr.name): " + (AbstractSymbol)env.lookup(attr.name));
	    System.out.println("prob(attr.name): " + (AbstractSymbol)env.lookup(attr.name));
	    System.out.println("currClass: " + currClass.getName());*/
	    /*if ((AbstractSymbol)env.lookup(attr.name) != null) {
		ct.semantError(currClass).
		    println("Attribute " + attr.name + 
			    " is an attribute of an inherited class.");
	    }*/

	    if (attr.init instanceof no_expr) {
		return (AbstractSymbol)env.probe(attr.name);
	    } else {
		AbstractSymbol t0 = (AbstractSymbol)env.probe(attr.name);

		env.enterScope();
		env.addId(TreeConstants.self, currClass.getName());
		AbstractSymbol t1 = typeCheck(ct, env, mEnv, currClass, 
					      attr.init);
		env.exitScope();

		//class_c c1 = (class_c)env.lookup(t1);
		//class_c c0 = (class_c)env.lookup(t0);
		if (ct.isSubtype(env, currClass, t1, t0)) {
		    return t1;
		}
	    }
	}

	return TreeConstants.Object_;
    } // end typeCheck
}


/** Defines AST constructor 'class_c'.
    <p>
    See <a href="TreeNode.html">TreeNode</a> for full documentation. */
class class_c extends Class_ {
    protected AbstractSymbol name;
    protected AbstractSymbol parent;
    protected Features features;
    protected AbstractSymbol filename;
    /** Creates "class_c" AST node. 
      *
      * @param lineNumber the line in the source file from which this node came.
      * @param a0 initial value for name
      * @param a1 initial value for parent
      * @param a2 initial value for features
      * @param a3 initial value for filename
      */
    public class_c(int lineNumber, AbstractSymbol a1, AbstractSymbol a2, Features a3, AbstractSymbol a4) {
        super(lineNumber);
        name = a1;
        parent = a2;
        features = a3;
        filename = a4;
    }
    public TreeNode copy() {
        return new class_c(lineNumber, copy_AbstractSymbol(name), copy_AbstractSymbol(parent), (Features)features.copy(), copy_AbstractSymbol(filename));
    }
    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "class_c\n");
        dump_AbstractSymbol(out, n+2, name);
        dump_AbstractSymbol(out, n+2, parent);
        features.dump(out, n+2);
        dump_AbstractSymbol(out, n+2, filename);
    }

    
    public AbstractSymbol getFilename() { return filename; }
    public AbstractSymbol getName()     { return name; }
    public AbstractSymbol getParent()   { return parent; }

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

}


/** Defines AST constructor 'method'.
    <p>
    See <a href="TreeNode.html">TreeNode</a> for full documentation. */
class method extends Feature {
    protected AbstractSymbol name;
    protected Formals formals;
    protected AbstractSymbol return_type;
    protected Expression expr;
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
    protected AbstractSymbol name;
    protected AbstractSymbol type_decl;
    protected Expression init;
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


/** Defines AST constructor 'formalc'.
    <p>
    See <a href="TreeNode.html">TreeNode</a> for full documentation. */
class formalc extends Formal {
    protected AbstractSymbol name;
    protected AbstractSymbol type_decl;
    /** Creates "formalc" AST node. 
      *
      * @param lineNumber the line in the source file from which this node came.
      * @param a0 initial value for name
      * @param a1 initial value for type_decl
      */
    public formalc(int lineNumber, AbstractSymbol a1, AbstractSymbol a2) {
        super(lineNumber);
        name = a1;
        type_decl = a2;
    }
    public TreeNode copy() {
        return new formalc(lineNumber, copy_AbstractSymbol(name), copy_AbstractSymbol(type_decl));
    }
    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "formalc\n");
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
    protected AbstractSymbol name;
    protected AbstractSymbol type_decl;
    protected Expression expr;
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
    protected AbstractSymbol name;
    protected Expression expr;
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

}


/** Defines AST constructor 'static_dispatch'.
    <p>
    See <a href="TreeNode.html">TreeNode</a> for full documentation. */
class static_dispatch extends Expression {
    protected Expression expr;
    protected AbstractSymbol type_name;
    protected AbstractSymbol name;
    protected Expressions actual;
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

}


/** Defines AST constructor 'dispatch'.
    <p>
    See <a href="TreeNode.html">TreeNode</a> for full documentation. */
class dispatch extends Expression {
    protected Expression expr;
    protected AbstractSymbol name;
    protected Expressions actual;
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

}


/** Defines AST constructor 'cond'.
    <p>
    See <a href="TreeNode.html">TreeNode</a> for full documentation. */
class cond extends Expression {
    protected Expression pred;
    protected Expression then_exp;
    protected Expression else_exp;
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

}


/** Defines AST constructor 'loop'.
    <p>
    See <a href="TreeNode.html">TreeNode</a> for full documentation. */
class loop extends Expression {
    protected Expression pred;
    protected Expression body;
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

}


/** Defines AST constructor 'typcase'.
    <p>
    See <a href="TreeNode.html">TreeNode</a> for full documentation. */
class typcase extends Expression {
    protected Expression expr;
    protected Cases cases;
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

}


/** Defines AST constructor 'block'.
    <p>
    See <a href="TreeNode.html">TreeNode</a> for full documentation. */
class block extends Expression {
    protected Expressions body;
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

}


/** Defines AST constructor 'let'.
    <p>
    See <a href="TreeNode.html">TreeNode</a> for full documentation. */
class let extends Expression {
    protected AbstractSymbol identifier;
    protected AbstractSymbol type_decl;
    protected Expression init;
    protected Expression body;
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

}


/** Defines AST constructor 'plus'.
    <p>
    See <a href="TreeNode.html">TreeNode</a> for full documentation. */
class plus extends Expression {
    protected Expression e1;
    protected Expression e2;
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

}


/** Defines AST constructor 'sub'.
    <p>
    See <a href="TreeNode.html">TreeNode</a> for full documentation. */
class sub extends Expression {
    protected Expression e1;
    protected Expression e2;
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

}


/** Defines AST constructor 'mul'.
    <p>
    See <a href="TreeNode.html">TreeNode</a> for full documentation. */
class mul extends Expression {
    protected Expression e1;
    protected Expression e2;
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

}


/** Defines AST constructor 'divide'.
    <p>
    See <a href="TreeNode.html">TreeNode</a> for full documentation. */
class divide extends Expression {
    protected Expression e1;
    protected Expression e2;
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

}


/** Defines AST constructor 'neg'.
    <p>
    See <a href="TreeNode.html">TreeNode</a> for full documentation. */
class neg extends Expression {
    protected Expression e1;
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

}


/** Defines AST constructor 'lt'.
    <p>
    See <a href="TreeNode.html">TreeNode</a> for full documentation. */
class lt extends Expression {
    protected Expression e1;
    protected Expression e2;
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

}


/** Defines AST constructor 'eq'.
    <p>
    See <a href="TreeNode.html">TreeNode</a> for full documentation. */
class eq extends Expression {
    protected Expression e1;
    protected Expression e2;
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

}


/** Defines AST constructor 'leq'.
    <p>
    See <a href="TreeNode.html">TreeNode</a> for full documentation. */
class leq extends Expression {
    protected Expression e1;
    protected Expression e2;
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

}


/** Defines AST constructor 'comp'.
    <p>
    See <a href="TreeNode.html">TreeNode</a> for full documentation. */
class comp extends Expression {
    protected Expression e1;
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

}


/** Defines AST constructor 'int_const'.
    <p>
    See <a href="TreeNode.html">TreeNode</a> for full documentation. */
class int_const extends Expression {
    protected AbstractSymbol token;
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

}


/** Defines AST constructor 'bool_const'.
    <p>
    See <a href="TreeNode.html">TreeNode</a> for full documentation. */
class bool_const extends Expression {
    protected Boolean val;
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

}


/** Defines AST constructor 'string_const'.
    <p>
    See <a href="TreeNode.html">TreeNode</a> for full documentation. */
class string_const extends Expression {
    protected AbstractSymbol token;
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

}


/** Defines AST constructor 'new_'.
    <p>
    See <a href="TreeNode.html">TreeNode</a> for full documentation. */
class new_ extends Expression {
    protected AbstractSymbol type_name;
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

}


/** Defines AST constructor 'isvoid'.
    <p>
    See <a href="TreeNode.html">TreeNode</a> for full documentation. */
class isvoid extends Expression {
    protected Expression e1;
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

}


/** Defines AST constructor 'object'.
    <p>
    See <a href="TreeNode.html">TreeNode</a> for full documentation. */
class object extends Expression {
    protected AbstractSymbol name;
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

}


