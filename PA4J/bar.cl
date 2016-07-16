(* redefine basic classes *)
(*
class Object {};
--class IO {};
--class Int {};
--class String {};
--class Bool {};
*)

(* declare same class name more than once *)

class Hello {
};
class Hello {
};


(* declare class with same name as a built-in class *)
(*class IO {

};
*)

(* non-existent parent class *)
(*
class A inherits B {

};
*)

(* circular dependency *)
(*
class A inherits B {

};
class B inherits A {

};
*)

(* inherits from Int, Bool, and String classes *)
(*
class E3 inherits Int {
};
class E4 inherits Bool {
};
class E5 inherits String {
};
class E6 inherits String {
};
*)

(* inherit from same class *)
(*
class Z inherits Z {

};
*)