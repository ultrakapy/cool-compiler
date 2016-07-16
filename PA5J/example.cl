(*  Example cool program testing as many aspects of the code generator
    as possible.
 *)
(*
class A inherits IO {
  test() : SELF_TYPE {
    new SELF_TYPE
  };

};

class Main inherits IO{
  main() : IO {
    out_string((new A).type_name())
  };
};
*)

class A {
      myA() : Int { 2 };
};

class Main inherits IO{
  main() : IO {
    {out_string((new A).type_name()); out_string("\n");}
  };
};

(*
class A {
  x : Int <- x + 1;
  printX():Object {{
    (new IO).out_string("x is ");
    (new IO).out_int(x);
    (new IO).out_string("\n");
  }}; 
  new_st():A {
    new SELF_TYPE
  };
  bump():Object {
    x <- x + 1
  };
};

class Main {
  main():Object {
    let
      a1 : A <- new A,     -- 'x' is 1
      a2 : A
    in {
      (new IO).out_string(a1.type_name()); (new IO).out_string("\n");
      a1.printX();
      a1.bump();
      a1.bump();
      a1.bump();
      a1.printX();         -- 'x' is 4
      a2 <- a1.new_st();   -- 'x' is 5 if copy of 'a1' made!
      a2.bump();
      (new IO).out_string(a2.type_name()); (new IO).out_string("\n");
      (*a2.printX();         -- a2.x should be 1
      a1.printX();         -- make sure it's still 4*)
    }
  };
};
*)

(*

(*class Main {
  io:IO <- new IO;

  main():Object {{
    io.out_string(  (not (new Bool)).type_name()  );
    io.out_string("\n");

    (*io.out_int(  (new Int) + 1  );
    io.out_string("\n");

    io.out_string(  (new String).substr(0,0).type_name()  );
    io.out_string("\n");*)
  }};
};
*)

class A {
  a1 : Int <- 99;
  a2 : Bool <- true;
};

class B inherits A {
  b1 : Int <- 2;
};

class C inherits B {
  c1 : String <- "I am class C";
  duplicate() : C {
    new SELF_TYPE
  };
};


class Main inherits IO {
  x : Int <- 5;
  y : Int;
  z : Int <- 7;
  v : Int <- let u:Int in 99;
  main() : Int { let a1:Int <- 88,a2:String,a3:Bool,a4:A,a5:Int,a6:Int,a6:Int,a8:Int,a9:Int,a10:B in a1 };
  (*main() : C { (new C).duplicate() };*)
  (*main() : IO { if true then self@IO.out_int(50 + 7) else out_int(0) fi };*)
  (*main() : IO { {y <- z + 2; self@IO.out_int(y);} };*)
  (*hello(w:Bool, t:String) : Bool { {z; t; w <- true;} };*)
};

*)