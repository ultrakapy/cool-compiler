(*  Example cool program testing as many aspects of the code generator
    as possible.
 *)

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
  main() : A { let a1:Int <- 88,a2:String,a3:Bool,a4:A,a5:Int,a6:Int,a6:Int,a8:Int,a9:Int,a10:B in a10 };
  (*main() : C { (new C).duplicate() };*)
  (*main() : IO { if true then self@IO.out_int(50 + 7) else out_int(0) fi };*)
  (*main() : IO { {y <- z + 2; self@IO.out_int(y);} };*)
  (*hello(w:Bool, t:String) : Bool { {z; t; w <- true;} };*)
};

