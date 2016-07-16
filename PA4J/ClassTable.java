import java.io.PrintStream;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

/* This class is a representation of the inheritance hierarchy of all classes
 * in a Cool program (including built-in classes).
 */
class InheritanceGraph {
    private ArrayList<InheritanceGraph> subclasses;
    private class_c parent_class;
    private class_c current_class;
    private int level;

    public InheritanceGraph(class_c c, class_c parent, int level) {
	this.current_class = c;
	this.parent_class = parent;
	this.subclasses = new ArrayList<InheritanceGraph>();
	this.level = level;
    }

    public InheritanceGraph findParentGraph(InheritanceGraph g) {
	if (g.getParentClass().getName().equals(this.current_class.getName())) {
	    return this;
	} else {
	    for (InheritanceGraph ig : this.subclasses) {
		InheritanceGraph potentialParent = ig.findParentGraph(g);
		if (potentialParent != null) {
		    return potentialParent;
		}
	    }
	}
	return null;
    }

    public InheritanceGraph findParentClass(class_c c) {
	if (c.getParent().equals(this.current_class.getName())) {
	    return this;
	} else {
	    for (InheritanceGraph ig : this.subclasses) {
		InheritanceGraph potentialParent = ig.findParentClass(c);
		if (potentialParent != null) {
		    return potentialParent;
		}
	    }
	}
	return null;
    }

    public InheritanceGraph findClass(class_c c) {
	if (c.getName().equals(this.current_class.getName())) {
	    return this;
	} else {
	    for (InheritanceGraph ig : this.subclasses) {
		InheritanceGraph potentialCurrentClass = ig.findClass(c);
		if (potentialCurrentClass != null) {
		    return potentialCurrentClass;
		}
	    }
	}
	return null;
    }

    public class_c getCurrentClass() {
	return this.current_class;
    }

    public class_c getParentClass() {
	return this.parent_class;
    }

    public ArrayList<InheritanceGraph> getSubclasses() {
	return this.subclasses;
    }

    public int size() {
	int s = 1;
	for (InheritanceGraph ig : this.subclasses) {
	    s += ig.size();
	}
	return s;
    }

    public int getLevel() {
	return this.level;
    }

    public boolean contains(class_c c) {
	if (c.getName().equals(this.current_class.getName())) {
	    return true;
	} else {
	    for (InheritanceGraph ig : this.subclasses) {
		if (ig.contains(c)) {
		    return true;
		}
	    }
	}
	return false;
    }

    public boolean containsParent(class_c c) {
	if (c.getParent().equals(this.current_class.getName())) {
	    return true;
	} else {
	    for (InheritanceGraph ig : this.subclasses) {
		if (ig.containsParent(c)) {
		    return true;
		}
	    }
	}
	return false;
    }

    public void addClass(class_c c) {
	this.subclasses.add(new InheritanceGraph(c, 
						 this.current_class, 
						 this.level + 1));
    }
}

/** This class may be used to contain the semantic information such as
 * the inheritance graph.  You may use it or not as you like: it is only
 * here to provide a container for the supplied methods.  */
class ClassTable {
    private int semantErrors;
    private PrintStream errorStream;

    public InheritanceGraph ig;
    private Hashtable definedClasses;

    /** Creates data structures representing basic Cool classes (Object,
     * IO, Int, Bool, String).  Please note: as is this method does not
     * do anything useful; you will need to edit it to make if do what
     * you want.
     * */
    private void installBasicClasses(SymbolTable st) {
	AbstractSymbol filename 
	    = AbstractTable.stringtable.addString("<basic class>");
	
	// The following demonstrates how to create dummy parse trees to
	// refer to basic Cool classes.  There's no need for method
	// bodies -- these are already built into the runtime system.

	// IMPORTANT: The results of the following expressions are
	// stored in local variables.  You will want to do something
	// with those variables at the end of this method to make this
	// code meaningful.

	// The Object class has no parent class. Its methods are
	//        cool_abort() : Object    aborts the program
	//        type_name() : Str        returns a string representation 
	//                                 of class name
	//        copy() : SELF_TYPE       returns a copy of the object

	class_c Object_class = 
	    new class_c(0, 
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
	
	// The IO class inherits from Object. Its methods are
	//        out_string(Str) : SELF_TYPE  writes a string to the output
	//        out_int(Int) : SELF_TYPE      "    an int    "  "     "
	//        in_string() : Str            reads a string from the input
	//        in_int() : Int                "   an int     "  "     "

	class_c IO_class = 
	    new class_c(0,
		       TreeConstants.IO,
		       TreeConstants.Object_,
		       new Features(0)
			   .appendElement(new method(0,
					      TreeConstants.out_string,
					      new Formals(0)
						  .appendElement(new formalc(0,
								     TreeConstants.arg,
								     TreeConstants.Str)),
					      TreeConstants.SELF_TYPE,
					      new no_expr(0)))
			   .appendElement(new method(0,
					      TreeConstants.out_int,
					      new Formals(0)
						  .appendElement(new formalc(0,
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

	// The Int class has no methods and only a single attribute, the
	// "val" for the integer.

	class_c Int_class = 
	    new class_c(0,
		       TreeConstants.Int,
		       TreeConstants.Object_,
		       new Features(0)
			   .appendElement(new attr(0,
					    TreeConstants.val,
					    TreeConstants.prim_slot,
					    new no_expr(0))),
		       filename);

	// Bool also has only the "val" slot.
	class_c Bool_class = 
	    new class_c(0,
		       TreeConstants.Bool,
		       TreeConstants.Object_,
		       new Features(0)
			   .appendElement(new attr(0,
					    TreeConstants.val,
					    TreeConstants.prim_slot,
					    new no_expr(0))),
		       filename);

	// The class Str has a number of slots and operations:
	//       val                              the length of the string
	//       str_field                        the string itself
	//       length() : Int                   returns length of the string
	//       concat(arg: Str) : Str           performs string concatenation
	//       substr(arg: Int, arg2: Int): Str substring selection

	class_c Str_class =
	    new class_c(0,
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
						  .appendElement(new formalc(0,
								     TreeConstants.arg, 
								     TreeConstants.Str)),
					      TreeConstants.Str,
					      new no_expr(0)))
			   .appendElement(new method(0,
					      TreeConstants.substr,
					      new Formals(0)
						  .appendElement(new formalc(0,
								     TreeConstants.arg,
								     TreeConstants.Int))
						  .appendElement(new formalc(0,
								     TreeConstants.arg2,
								     TreeConstants.Int)),
					      TreeConstants.Str,
					      new no_expr(0))),
		       filename);

	/* Do somethind with Object_class, IO_class, Int_class,
           Bool_class, and Str_class here */
	ig = new InheritanceGraph(Object_class, null, 0);
	ig.addClass(IO_class);
	ig.addClass(Int_class);
	ig.addClass(Bool_class);
	ig.addClass(Str_class);
	//System.out.println("InheritanceGraph size: " + ig.subclassCount());

	// add base classes to Symbol Table
	st.addId(Object_class.getName(), Object_class);
	st.addId(IO_class.getName(), IO_class);
	st.addId(Int_class.getName(), Int_class);
	st.addId(Bool_class.getName(), Bool_class);
	st.addId(Str_class.getName(), Str_class);
    }

    public boolean isSubtype(SymbolTable env, 
			     class_c c, 
			     AbstractSymbol t1, 
			     AbstractSymbol t2) {
	
	if (t1.equals(TreeConstants.SELF_TYPE)
	    && t2.equals(TreeConstants.SELF_TYPE)) {
	    return true;
	} else if (t1.equals(TreeConstants.SELF_TYPE)
		   && !t2.equals(TreeConstants.SELF_TYPE)) {
	    return isSubtype(c, (class_c)env.lookup(t2));
	} else if (!t1.equals(TreeConstants.SELF_TYPE)
		   && t2.equals(TreeConstants.SELF_TYPE)) {
	    return false;
	} 
	else {
	    //System.out.println("t1: " + t1);
	    //System.out.println("t2: " + t2);
	    /*if (t1.toString().equals("CellularAutomaton")) {
		System.out.println("t1=t2: " + t1.equals(t2));
		System.out.println("lookup(t1): " + (class_c)env.lookup(t1));
		System.out.println("lookup(t2): " + (class_c)env.lookup(t2));
		System.out.println(env.toString());
	    }*/
	    if (t1.equals(t2)) {
		return true;
	    }

	    return isSubtype((class_c)env.lookup(t1),
				   (class_c)env.lookup(t2));
	}
    }

    public boolean isSubtype(class_c c1, class_c c2) {
	if (c1 == null || c2 == null) {
	    return false;
	}

	return isSubtype(ig.findClass(c1), ig.findClass(c2));
    }

    public boolean isSubtype(InheritanceGraph ig1, InheritanceGraph ig2) {
	//System.out.println("In isSubclass: ");
	if (ig1.getCurrentClass().getName().equals(TreeConstants.Object_) &&
	    !ig2.getCurrentClass().getName().equals(TreeConstants.Object_)) {
	    return false;
	}

	if (ig1.getCurrentClass().getName().
	    equals(ig2.getCurrentClass().getName())) {
	    return true;
	} else {
	    return isSubtype(ig.findParentGraph(ig1), ig2);
	}
    }

    public InheritanceGraph leastUpperBound(SymbolTable env,
					    class_c c,
					    AbstractSymbol t1, 
					    AbstractSymbol t2) {
	if (t1.equals(TreeConstants.SELF_TYPE)
	    && t2.equals(TreeConstants.SELF_TYPE)) {
	    return ig.findClass((class_c)env.lookup(t1));
	} else if (t1.equals(TreeConstants.SELF_TYPE)
		   && !t2.equals(TreeConstants.SELF_TYPE)) {
	    return leastUpperBound(c, (class_c)env.lookup(t2));
	} else if (!t1.equals(TreeConstants.SELF_TYPE)
		   && t2.equals(TreeConstants.SELF_TYPE)) {
	    return leastUpperBound((class_c)env.lookup(t1), c);
	} 
	else {
	    return leastUpperBound((class_c)env.lookup(t1),
				   (class_c)env.lookup(t2));
	}
    }

    public InheritanceGraph leastUpperBound(class_c c1, class_c c2) {
	return leastUpperBound(ig.findClass(c1), ig.findClass(c2));
    }
    
    public InheritanceGraph leastUpperBound(InheritanceGraph subIg1, 
				   InheritanceGraph subIg2) {
	if (subIg1.getCurrentClass().getName().equals(subIg2.getCurrentClass().getName())) {
	    return subIg1;
	}
	
	/*
	System.out.println(subIg1.getCurrentClass().getName() + ": level = " +
			   subIg1.getLevel());
	System.out.println(subIg2.getCurrentClass().getName() + ": level = " +
			   subIg2.getLevel());
	*/
	if (subIg1.getLevel() < subIg2.getLevel()) {
	    return leastUpperBound(subIg1, ig.findParentGraph(subIg2));
	} else if (subIg1.getLevel() > subIg2.getLevel()) {
	    return leastUpperBound(ig.findParentGraph(subIg1), subIg2);
	} else {
	    return leastUpperBound(ig.findParentGraph(subIg1),
				   ig.findParentGraph(subIg2));
	}
    }
	
    public void PrintSubclasses(InheritanceGraph g) {
	//System.out.println();
	System.out.println(g.getCurrentClass().getName() + ": " + 
			   g.getSubclasses().size() + " (LEVEL = " + 
			   g.getLevel() + ")");
	for (InheritanceGraph ag : g.getSubclasses()) {
	    PrintSubclasses(ag);
	}
    }

    public ClassTable(Classes cls, SymbolTable st) {
	semantErrors = 0;
	errorStream = System.err;
	
	/* fill this in */
	installBasicClasses(st);

	// make sure there are no class redefinitions
	this.definedClasses = new Hashtable();
	for (Enumeration e = cls.getElements(); e.hasMoreElements(); ) {
	    class_c c = (class_c) e.nextElement();

	    if (this.definedClasses.get(c.getName()) != null) {
		semantError(c).println("Class " + c.getName() 
				       + " was previously defined.");
		break;
	    } else {
		this.definedClasses.put(c.getName(), new Object());
	    }
	}

	// add classes from AST to the inheritance graph
	int passes = 0;
	boolean allClassesInstalled = false;
	
	do {
	    allClassesInstalled = installOtherClasses(cls, st);
	    passes++;
	    
	    if (errors()) {
		break;
	    }
	} while (!allClassesInstalled && passes < cls.getLength());
	
	/*System.out.println("Passes: " + passes);
	System.out.println("InheritanceGraph size: " + ig.size());
	PrintSubclasses(ig);*/
	/*InheritanceGraph testIg1 = ig.findClass((class_c)cls.getNth(0));
	InheritanceGraph testIg2 = ig.findClass((class_c)cls.getNth(2));*/
	/*System.out.println("LUB(" +
			   testIg1.getCurrentClass().getName() + ", " + 
			   testIg2.getCurrentClass().getName() + "): " 
			   + leastUpperBound(testIg1, testIg2).getCurrentClass().getName());*/
	/*System.out.println("isSubclass("  +
			   testIg1.getCurrentClass().getName() + ", " + 
			   testIg2.getCurrentClass().getName() + "): " 
			   + isSubclass(testIg1, testIg2));*/

    }

    public boolean installOtherClasses(Classes cls, SymbolTable st) {
	for (Enumeration e = cls.getElements(); e.hasMoreElements(); ) {
	    class_c c = (class_c) e.nextElement();

	    // error if c inherits from Int, Bool, or String base classes
	    if (c.getParent().equals(TreeConstants.Int) ||
		c.getParent().equals(TreeConstants.Bool) ||
		c.getParent().equals(TreeConstants.Str) ||
		c.getParent().equals(TreeConstants.SELF_TYPE)) {
		semantError(c).println("Class " + c.getName() 
				       + " cannot inherit class " + 
				       c.getParent() + ".");
		return false;
	    }

	    // cannot redefine basic classes
	    if (c.getName().equals(TreeConstants.Object_) ||
		c.getName().equals(TreeConstants.IO) ||
		c.getName().equals(TreeConstants.Int) ||
		c.getName().equals(TreeConstants.Str) ||
		c.getName().equals(TreeConstants.Bool) ||
		c.getName().equals(TreeConstants.SELF_TYPE)) {
		semantError(c).println("Redefinition of basic class " + 
				       c.getName() + ".");
		return false;
	    }

	    // cannot inherit from same class
	    if (!c.getName().equals(TreeConstants.Object_) && 
		c.getName().equals(c.getParent())) {
		semantError(c).println("Class " + c.getName() + 
				       " cannot inherit itself.");
		return false;
	    }

	    // if class inherits from undefined class, error out
	    if (st.lookup(c.getParent()) == null) {
		semantError(c).println("Class " + c.getName() + 
				       " inherits from an undefined class " 
				       + c.getParent()  + ".");
		return false;
	    }

	    // if class is not in the inheritance graph already
	    if (!ig.contains(c)) {
		InheritanceGraph parent = ig.findParentClass(c);

		//System.out.println(c.getName());
		//System.out.println(c.getParent());
		//System.out.println(((class_c)st.lookup(c.getParent())).getParent());
		if (c.getName().equals(((class_c)st.lookup(c.getParent())).
				       getParent())) {
		    semantError(c).println("Classes " + c.getName() + 
					   " and " + 
					   c.getParent()
					   + " cannot inherit from each " +
					   "other.");
		    return false;
		}
		
		if (parent != null) {
		    parent.getSubclasses().
			add(new InheritanceGraph(c, 
						 parent.getCurrentClass(), 
						 parent.getLevel() + 1));
		}
	    }
	}
	return ig.size() == (cls.getLength() + 5);
    }


    /** Prints line number and file name of the given class.
     *
     * Also increments semantic error count.
     *
     * @param c the class
     * @return a print stream to which the rest of the error message is
     * to be printed.
     *
     * */
    public PrintStream semantError(class_c c) {
	return semantError(c.getFilename(), c);
    }

    /** Prints the file name and the line number of the given tree node.
     *
     * Also increments semantic error count.
     *
     * @param filename the file name
     * @param t the tree node
     * @return a print stream to which the rest of the error message is
     * to be printed.
     *
     * */
    public PrintStream semantError(AbstractSymbol filename, TreeNode t) {
	errorStream.print(filename + ":" + t.getLineNumber() + ": ");
	return semantError();
    }

    /** Increments semantic error count and returns the print stream for
     * error messages.
     *
     * @return a print stream to which the error message is
     * to be printed.
     *
     * */
    public PrintStream semantError() {
	semantErrors++;
	return errorStream;
    }

    /** Returns true if there are any static semantic errors. */
    public boolean errors() {
	return semantErrors != 0;
    }
}
			  
    
