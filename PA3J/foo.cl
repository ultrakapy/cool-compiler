class A {
      myfunc1() : SELF_TYPE { 42 };
      myfunc2() : SELF_TYPE { "Hello" };
      myfunc3() : SELF_TYPE { true };
      myfunc4() : SELF_TYPE { false };
      myfunc5() : SELF_TYPE { myid };
      myfunc6() : SELF_TYPE { (((31))) };
      myfunc7() : SELF_TYPE { NOT true };
      myfunc8() : SELF_TYPE { 5 = 7 };
      myfunc9() : SELF_TYPE { 2 + 6 };
      myfunc10() : SELF_TYPE { 8 - 1 };
      myfunc11() : SELF_TYPE { 20 * 55 };
      myfunc12() : SELF_TYPE { 50 < 90 };
      myfunc13() : SELF_TYPE { 100 <= 3 };
      myfunc14() : SELF_TYPE { 44 / 11 };
      myfunc15() : SELF_TYPE { newid <- "World!" };
      myfunc16() : SELF_TYPE { new Person };
      myfunc17() : SELF_TYPE { isvoid n * (n + 1) };
      myfunc18() : SELF_TYPE { { 5; 4; 3; } };
      myfunc19() : SELF_TYPE { if (true) then "yes" else "no" fi };
      myfunc20() : SELF_TYPE { while false loop z <- x + y pool };
      myattr1 : SELF_TYPE;
      myattr1 : SELF_TYPE <- 55;
      myfunc21(myformal1 : SELF_TYPE, myformal2: SELF_TYPE) : SELF_TYPE { 30 };
      myfunc22a() : SELF_TYPE { mydispatch1() };
      myfunc22b() : SELF_TYPE { mydispatch1(true) };
      myfunc22c() : SELF_TYPE { mydispatch1(33,44,55) };
      myfunc23() : SELF_TYPE { e1.id1() };
      myfunc24() : SELF_TYPE { { e1.id1(24); e2.id2("yes", false); } };
      myfunc25() : SELF_TYPE { e1@Tid1.id1() };
      myfunc26() : SELF_TYPE {{ e1@Tid1.id1(24); e2@Tid2.id2("yes", false); }};
      myfunc27() : SELF_TYPE {
        case "message" of id1 : Tid1 => 77; esac
      };
      myfunc28() : SELF_TYPE {
        case true of id1 : Tid1 => 50; id2 : Tid2 => 1; esac
      };
      myfunc29() : SELF_TYPE {
        let id1 : Tid1 in 19 + 1 
      };
      myfunc30() : SELF_TYPE {
        let id1 : Tid1 <- 5 in 19 + 1 
      };
      myfunc31() : SELF_TYPE {
        let id1 : Tid1, id2 : Tid2 in 19 + 1 
      };
      myfunc32() : SELF_TYPE {
        let id1 : Tid1 <- 70, id2 : Tid2 <- 10 in 19 + 1 
      };
      myfunc33() : SELF_TYPE {
        let id1 : Tid1, id2 : Tid2 <- 10 in 19 + 1 
      };
      myfunc34() : SELF_TYPE {
        let id1 : Tid1 <- 100, id2 : Tid2 in 19 + 1 
      };
      myfunc35() : SELF_TYPE {
        let id1 : Tid1 <- 50, id2 : Tid2 <- 10, id3 : Tid3 <- 20 in 19 + 1 
      };
};
