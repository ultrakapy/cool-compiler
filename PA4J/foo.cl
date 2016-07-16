class Main { main(x:Int, y:Bool, z:String) : SELF_TYPE { self }; };


(*class A {
	moo:A;
};

class B inherits A {
	moo:B;
};
*)


class G inherits F { };

class F inherits A {
      anFmethod(arg1 : Bool) : Int { 1 };
      (*anFmethod(arg1 : Bool) : Int { 1 };*)
};


class A inherits Object {
      v1 : Int;
      (*v2 : Int;*)
      (*v3 : String <- v1 + v2;*)

      (*mymethod() : Bool {
      		 11=55
      		 (*if (true) then new G else new F  fi*)

      		 (*v1 <- 5*)
		 (*v2 <- 7;*)  
		
      };*)
};
(*
class E1 inherits Object {
};
class E2 inherits IO {
};


class C {
	(*a : Int;
	b : Bool;
	init(x : Int, y : Bool) : C {
           {
		a <- x;
		b <- y;
		self;
           }
	};*)
};

Class Main {
	(*main():C {
	  (new C).init(1,true)
	};*)
};

class D inherits C {

};
*)