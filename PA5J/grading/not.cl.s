# start of generated code
	.data
	.align	2
	.globl	class_nameTab
	.globl	Main_protObj
	.globl	Int_protObj
	.globl	String_protObj
	.globl	bool_const0
	.globl	bool_const1
	.globl	_int_tag
	.globl	_bool_tag
	.globl	_string_tag
_int_tag:
	.word	2
_bool_tag:
	.word	3
_string_tag:
	.word	4
	.globl	_MemMgr_INITIALIZER
_MemMgr_INITIALIZER:
	.word	_NoGC_Init
	.globl	_MemMgr_COLLECTOR
_MemMgr_COLLECTOR:
	.word	_NoGC_Collect
	.globl	_MemMgr_TEST
_MemMgr_TEST:
	.word	0
	.word	-1
str_const11:
	.word	4
	.word	5
	.word	String_dispTab
	.word	int_const0
	.byte	0	
	.align	2
	.word	-1
str_const10:
	.word	4
	.word	6
	.word	String_dispTab
	.word	int_const3
	.ascii	"Main"
	.byte	0	
	.align	2
	.word	-1
str_const9:
	.word	4
	.word	6
	.word	String_dispTab
	.word	int_const4
	.ascii	"String"
	.byte	0	
	.align	2
	.word	-1
str_const8:
	.word	4
	.word	6
	.word	String_dispTab
	.word	int_const3
	.ascii	"Bool"
	.byte	0	
	.align	2
	.word	-1
str_const7:
	.word	4
	.word	5
	.word	String_dispTab
	.word	int_const5
	.ascii	"Int"
	.byte	0	
	.align	2
	.word	-1
str_const6:
	.word	4
	.word	5
	.word	String_dispTab
	.word	int_const6
	.ascii	"IO"
	.byte	0	
	.align	2
	.word	-1
str_const5:
	.word	4
	.word	6
	.word	String_dispTab
	.word	int_const4
	.ascii	"Object"
	.byte	0	
	.align	2
	.word	-1
str_const1:
	.word	4
	.word	8
	.word	String_dispTab
	.word	int_const7
	.ascii	"<basic class>"
	.byte	0	
	.align	2
	.word	-1
str_const0:
	.word	4
	.word	7
	.word	String_dispTab
	.word	int_const8
	.ascii	"./not.cl"
	.byte	0	
	.align	2
	.word	-1
int_const8:
	.word	2
	.word	4
	.word	Int_dispTab
	.word	8
	.word	-1
int_const7:
	.word	2
	.word	4
	.word	Int_dispTab
	.word	13
	.word	-1
int_const6:
	.word	2
	.word	4
	.word	Int_dispTab
	.word	2
	.word	-1
int_const5:
	.word	2
	.word	4
	.word	Int_dispTab
	.word	3
	.word	-1
int_const4:
	.word	2
	.word	4
	.word	Int_dispTab
	.word	6
	.word	-1
int_const3:
	.word	2
	.word	4
	.word	Int_dispTab
	.word	4
	.word	-1
int_const2:
	.word	2
	.word	4
	.word	Int_dispTab
	.word	1
	.word	-1
int_const1:
	.word	2
	.word	4
	.word	Int_dispTab
	.word	100
	.word	-1
int_const0:
	.word	2
	.word	4
	.word	Int_dispTab
	.word	0
	.word	-1
bool_const0:
	.word	3
	.word	4
	.word	Bool_dispTab
	.word	0
	.word	-1
bool_const1:
	.word	3
	.word	4
	.word	Bool_dispTab
	.word	1
class_nameTab:
	.word	str_const6
	.word	str_const7
	.word	str_const8
	.word	str_const9
	.word	str_const10
	.word	str_const11
class_objTab:
	.word	Object_protObj
	.word	Object_init
	.word	IO_protObj
	.word	IO_init
	.word	Int_protObj
	.word	Int_init
	.word	Bool_protObj
	.word	Bool_init
	.word	String_protObj
	.word	String_init
	.word	Main_protObj
	.word	Main_init
Object_dispTab:
	.word	Object.abort
	.word	Object.type_name
	.word	Object.copy
IO_dispTab:
	.word	Object.abort
	.word	Object.type_name
	.word	Object.copy
	.word	IO.out_string
	.word	IO.out_int
	.word	IO.in_string
	.word	IO.in_int
Int_dispTab:
	.word	Object.abort
	.word	Object.type_name
	.word	Object.copy
Bool_dispTab:
	.word	Object.abort
	.word	Object.type_name
	.word	Object.copy
String_dispTab:
	.word	Object.abort
	.word	Object.type_name
	.word	Object.copy
	.word	String.length
	.word	String.concat
	.word	String.substr
Main_dispTab:
	.word	Object.abort
	.word	Object.type_name
	.word	Object.copy
	.word	Main.main
	.word	-1
Object_protObj:
	.word	0
	.word	3
	.word	Object_dispTab
	.word	-1
IO_protObj:
	.word	1
	.word	3
	.word	IO_dispTab
	.word	-1
Int_protObj:
	.word	2
	.word	4
	.word	Int_dispTab
	.word	0
	.word	-1
Bool_protObj:
	.word	3
	.word	4
	.word	Bool_dispTab
	.word	0
	.word	-1
String_protObj:
	.word	4
	.word	5
	.word	String_dispTab
	.word	int_const0
	.word	0
	.word	-1
Main_protObj:
	.word	5
	.word	3
	.word	Main_dispTab
	.globl	heap_start
heap_start:
	.word	0
	.text
	.globl	Main_init
	.globl	Int_init
	.globl	String_init
	.globl	Bool_init
	.globl	Main.main
Object_init:
	addiu	$sp $sp -12
	sw	$fp 12($sp)
	sw	$s0 8($sp)
	sw	$ra 4($sp)
	addiu	$fp $sp 4
	move	$s0 $a0
	move	$a0 $s0
	lw	$fp 12($sp)
	lw	$s0 8($sp)
	lw	$ra 4($sp)
	addiu	$sp $sp 12
	jr	$ra	
IO_init:
	addiu	$sp $sp -12
	sw	$fp 12($sp)
	sw	$s0 8($sp)
	sw	$ra 4($sp)
	addiu	$fp $sp 4
	move	$s0 $a0
	jal	Object_init
	move	$a0 $s0
	lw	$fp 12($sp)
	lw	$s0 8($sp)
	lw	$ra 4($sp)
	addiu	$sp $sp 12
	jr	$ra	
Int_init:
	addiu	$sp $sp -12
	sw	$fp 12($sp)
	sw	$s0 8($sp)
	sw	$ra 4($sp)
	addiu	$fp $sp 4
	move	$s0 $a0
	jal	Object_init
	move	$a0 $s0
	lw	$fp 12($sp)
	lw	$s0 8($sp)
	lw	$ra 4($sp)
	addiu	$sp $sp 12
	jr	$ra	
Bool_init:
	addiu	$sp $sp -12
	sw	$fp 12($sp)
	sw	$s0 8($sp)
	sw	$ra 4($sp)
	addiu	$fp $sp 4
	move	$s0 $a0
	jal	Object_init
	move	$a0 $s0
	lw	$fp 12($sp)
	lw	$s0 8($sp)
	lw	$ra 4($sp)
	addiu	$sp $sp 12
	jr	$ra	
String_init:
	addiu	$sp $sp -12
	sw	$fp 12($sp)
	sw	$s0 8($sp)
	sw	$ra 4($sp)
	addiu	$fp $sp 4
	move	$s0 $a0
	jal	Object_init
	move	$a0 $s0
	lw	$fp 12($sp)
	lw	$s0 8($sp)
	lw	$ra 4($sp)
	addiu	$sp $sp 12
	jr	$ra	
Main_init:
	addiu	$sp $sp -12
	sw	$fp 12($sp)
	sw	$s0 8($sp)
	sw	$ra 4($sp)
	addiu	$fp $sp 4
	move	$s0 $a0
	jal	Object_init
	move	$a0 $s0
	lw	$fp 12($sp)
	lw	$s0 8($sp)
	lw	$ra 4($sp)
	addiu	$sp $sp 12
	jr	$ra	
Main.main:
	addiu	$sp $sp -20
	sw	$fp 20($sp)
	sw	$s0 16($sp)
	sw	$ra 12($sp)
	addiu	$fp $sp 4
	move	$s0 $a0
	la	$a0 int_const0
	sw	$a0 0($fp)
	la	$a0 bool_const0
	sw	$a0 4($fp)
label0_loop:
	lw	$a0 0($fp)
	lw	$a0 12($a0)
	sw	$a0 0($sp)
	addiu	$sp $sp -4
	la	$a0 int_const1
	lw	$a0 12($a0)
	lw	$t1 4($sp)
	addiu	$sp $sp 4
	blt	$t1 $a0 label3_lt
	la	$a0 bool_const0
	jal	label4_lt
label3_lt:
	la	$a0 bool_const1
label4_lt:
	la	$t1 bool_const1
	beq	$a0 $t1 label1_loop
	jal	label2_loop
label1_loop:
	lw	$a0 4($fp)
	sw	$a0 0($sp)
	addiu	$sp $sp -4
	lw	$a0 4($fp)
	lw	$a0 12($a0)
	la	$t1 bool_const1
	lw	$t1 12($t1)
	beq	$a0 $t1 label27_comp
	la	$a0 bool_const1
	jal	label28_comp
label27_comp:
	la	$a0 bool_const0
label28_comp:
	lw	$a0 12($a0)
	la	$t1 bool_const1
	lw	$t1 12($t1)
	beq	$a0 $t1 label25_comp
	la	$a0 bool_const1
	jal	label26_comp
label25_comp:
	la	$a0 bool_const0
label26_comp:
	lw	$a0 12($a0)
	la	$t1 bool_const1
	lw	$t1 12($t1)
	beq	$a0 $t1 label23_comp
	la	$a0 bool_const1
	jal	label24_comp
label23_comp:
	la	$a0 bool_const0
label24_comp:
	lw	$a0 12($a0)
	la	$t1 bool_const1
	lw	$t1 12($t1)
	beq	$a0 $t1 label21_comp
	la	$a0 bool_const1
	jal	label22_comp
label21_comp:
	la	$a0 bool_const0
label22_comp:
	lw	$a0 12($a0)
	la	$t1 bool_const1
	lw	$t1 12($t1)
	beq	$a0 $t1 label19_comp
	la	$a0 bool_const1
	jal	label20_comp
label19_comp:
	la	$a0 bool_const0
label20_comp:
	lw	$a0 12($a0)
	la	$t1 bool_const1
	lw	$t1 12($t1)
	beq	$a0 $t1 label17_comp
	la	$a0 bool_const1
	jal	label18_comp
label17_comp:
	la	$a0 bool_const0
label18_comp:
	lw	$a0 12($a0)
	la	$t1 bool_const1
	lw	$t1 12($t1)
	beq	$a0 $t1 label15_comp
	la	$a0 bool_const1
	jal	label16_comp
label15_comp:
	la	$a0 bool_const0
label16_comp:
	lw	$a0 12($a0)
	la	$t1 bool_const1
	lw	$t1 12($t1)
	beq	$a0 $t1 label13_comp
	la	$a0 bool_const1
	jal	label14_comp
label13_comp:
	la	$a0 bool_const0
label14_comp:
	lw	$a0 12($a0)
	la	$t1 bool_const1
	lw	$t1 12($t1)
	beq	$a0 $t1 label11_comp
	la	$a0 bool_const1
	jal	label12_comp
label11_comp:
	la	$a0 bool_const0
label12_comp:
	lw	$a0 12($a0)
	la	$t1 bool_const1
	lw	$t1 12($t1)
	beq	$a0 $t1 label9_comp
	la	$a0 bool_const1
	jal	label10_comp
label9_comp:
	la	$a0 bool_const0
label10_comp:
	lw	$a0 12($a0)
	la	$t1 bool_const1
	lw	$t1 12($t1)
	beq	$a0 $t1 label7_comp
	la	$a0 bool_const1
	jal	label8_comp
label7_comp:
	la	$a0 bool_const0
label8_comp:
	lw	$t1 4($sp)
	addiu	$sp $sp 4
	move	$t2 $a0
	la	$a0 bool_const1
	beq	$t1 $t2 label5_eq
	la	$a1 bool_const0
	jal	equality_test
label5_eq:
	lw	$a0 4($fp)
	sw	$a0 0($sp)
	addiu	$sp $sp -4
	lw	$a0 4($fp)
	lw	$a0 12($a0)
	la	$t1 bool_const1
	lw	$t1 12($t1)
	beq	$a0 $t1 label51_comp
	la	$a0 bool_const1
	jal	label52_comp
label51_comp:
	la	$a0 bool_const0
label52_comp:
	lw	$a0 12($a0)
	la	$t1 bool_const1
	lw	$t1 12($t1)
	beq	$a0 $t1 label49_comp
	la	$a0 bool_const1
	jal	label50_comp
label49_comp:
	la	$a0 bool_const0
label50_comp:
	lw	$a0 12($a0)
	la	$t1 bool_const1
	lw	$t1 12($t1)
	beq	$a0 $t1 label47_comp
	la	$a0 bool_const1
	jal	label48_comp
label47_comp:
	la	$a0 bool_const0
label48_comp:
	lw	$a0 12($a0)
	la	$t1 bool_const1
	lw	$t1 12($t1)
	beq	$a0 $t1 label45_comp
	la	$a0 bool_const1
	jal	label46_comp
label45_comp:
	la	$a0 bool_const0
label46_comp:
	lw	$a0 12($a0)
	la	$t1 bool_const1
	lw	$t1 12($t1)
	beq	$a0 $t1 label43_comp
	la	$a0 bool_const1
	jal	label44_comp
label43_comp:
	la	$a0 bool_const0
label44_comp:
	lw	$a0 12($a0)
	la	$t1 bool_const1
	lw	$t1 12($t1)
	beq	$a0 $t1 label41_comp
	la	$a0 bool_const1
	jal	label42_comp
label41_comp:
	la	$a0 bool_const0
label42_comp:
	lw	$a0 12($a0)
	la	$t1 bool_const1
	lw	$t1 12($t1)
	beq	$a0 $t1 label39_comp
	la	$a0 bool_const1
	jal	label40_comp
label39_comp:
	la	$a0 bool_const0
label40_comp:
	lw	$a0 12($a0)
	la	$t1 bool_const1
	lw	$t1 12($t1)
	beq	$a0 $t1 label37_comp
	la	$a0 bool_const1
	jal	label38_comp
label37_comp:
	la	$a0 bool_const0
label38_comp:
	lw	$a0 12($a0)
	la	$t1 bool_const1
	lw	$t1 12($t1)
	beq	$a0 $t1 label35_comp
	la	$a0 bool_const1
	jal	label36_comp
label35_comp:
	la	$a0 bool_const0
label36_comp:
	lw	$a0 12($a0)
	la	$t1 bool_const1
	lw	$t1 12($t1)
	beq	$a0 $t1 label33_comp
	la	$a0 bool_const1
	jal	label34_comp
label33_comp:
	la	$a0 bool_const0
label34_comp:
	lw	$a0 12($a0)
	la	$t1 bool_const1
	lw	$t1 12($t1)
	beq	$a0 $t1 label31_comp
	la	$a0 bool_const1
	jal	label32_comp
label31_comp:
	la	$a0 bool_const0
label32_comp:
	lw	$t1 4($sp)
	addiu	$sp $sp 4
	move	$t2 $a0
	la	$a0 bool_const1
	beq	$t1 $t2 label29_eq
	la	$a1 bool_const0
	jal	equality_test
label29_eq:
	lw	$a0 4($fp)
	sw	$a0 0($sp)
	addiu	$sp $sp -4
	lw	$a0 4($fp)
	lw	$a0 12($a0)
	la	$t1 bool_const1
	lw	$t1 12($t1)
	beq	$a0 $t1 label75_comp
	la	$a0 bool_const1
	jal	label76_comp
label75_comp:
	la	$a0 bool_const0
label76_comp:
	lw	$a0 12($a0)
	la	$t1 bool_const1
	lw	$t1 12($t1)
	beq	$a0 $t1 label73_comp
	la	$a0 bool_const1
	jal	label74_comp
label73_comp:
	la	$a0 bool_const0
label74_comp:
	lw	$a0 12($a0)
	la	$t1 bool_const1
	lw	$t1 12($t1)
	beq	$a0 $t1 label71_comp
	la	$a0 bool_const1
	jal	label72_comp
label71_comp:
	la	$a0 bool_const0
label72_comp:
	lw	$a0 12($a0)
	la	$t1 bool_const1
	lw	$t1 12($t1)
	beq	$a0 $t1 label69_comp
	la	$a0 bool_const1
	jal	label70_comp
label69_comp:
	la	$a0 bool_const0
label70_comp:
	lw	$a0 12($a0)
	la	$t1 bool_const1
	lw	$t1 12($t1)
	beq	$a0 $t1 label67_comp
	la	$a0 bool_const1
	jal	label68_comp
label67_comp:
	la	$a0 bool_const0
label68_comp:
	lw	$a0 12($a0)
	la	$t1 bool_const1
	lw	$t1 12($t1)
	beq	$a0 $t1 label65_comp
	la	$a0 bool_const1
	jal	label66_comp
label65_comp:
	la	$a0 bool_const0
label66_comp:
	lw	$a0 12($a0)
	la	$t1 bool_const1
	lw	$t1 12($t1)
	beq	$a0 $t1 label63_comp
	la	$a0 bool_const1
	jal	label64_comp
label63_comp:
	la	$a0 bool_const0
label64_comp:
	lw	$a0 12($a0)
	la	$t1 bool_const1
	lw	$t1 12($t1)
	beq	$a0 $t1 label61_comp
	la	$a0 bool_const1
	jal	label62_comp
label61_comp:
	la	$a0 bool_const0
label62_comp:
	lw	$a0 12($a0)
	la	$t1 bool_const1
	lw	$t1 12($t1)
	beq	$a0 $t1 label59_comp
	la	$a0 bool_const1
	jal	label60_comp
label59_comp:
	la	$a0 bool_const0
label60_comp:
	lw	$a0 12($a0)
	la	$t1 bool_const1
	lw	$t1 12($t1)
	beq	$a0 $t1 label57_comp
	la	$a0 bool_const1
	jal	label58_comp
label57_comp:
	la	$a0 bool_const0
label58_comp:
	lw	$a0 12($a0)
	la	$t1 bool_const1
	lw	$t1 12($t1)
	beq	$a0 $t1 label55_comp
	la	$a0 bool_const1
	jal	label56_comp
label55_comp:
	la	$a0 bool_const0
label56_comp:
	lw	$t1 4($sp)
	addiu	$sp $sp 4
	move	$t2 $a0
	la	$a0 bool_const1
	beq	$t1 $t2 label53_eq
	la	$a1 bool_const0
	jal	equality_test
label53_eq:
	lw	$a0 4($fp)
	sw	$a0 0($sp)
	addiu	$sp $sp -4
	lw	$a0 4($fp)
	lw	$a0 12($a0)
	la	$t1 bool_const1
	lw	$t1 12($t1)
	beq	$a0 $t1 label99_comp
	la	$a0 bool_const1
	jal	label100_comp
label99_comp:
	la	$a0 bool_const0
label100_comp:
	lw	$a0 12($a0)
	la	$t1 bool_const1
	lw	$t1 12($t1)
	beq	$a0 $t1 label97_comp
	la	$a0 bool_const1
	jal	label98_comp
label97_comp:
	la	$a0 bool_const0
label98_comp:
	lw	$a0 12($a0)
	la	$t1 bool_const1
	lw	$t1 12($t1)
	beq	$a0 $t1 label95_comp
	la	$a0 bool_const1
	jal	label96_comp
label95_comp:
	la	$a0 bool_const0
label96_comp:
	lw	$a0 12($a0)
	la	$t1 bool_const1
	lw	$t1 12($t1)
	beq	$a0 $t1 label93_comp
	la	$a0 bool_const1
	jal	label94_comp
label93_comp:
	la	$a0 bool_const0
label94_comp:
	lw	$a0 12($a0)
	la	$t1 bool_const1
	lw	$t1 12($t1)
	beq	$a0 $t1 label91_comp
	la	$a0 bool_const1
	jal	label92_comp
label91_comp:
	la	$a0 bool_const0
label92_comp:
	lw	$a0 12($a0)
	la	$t1 bool_const1
	lw	$t1 12($t1)
	beq	$a0 $t1 label89_comp
	la	$a0 bool_const1
	jal	label90_comp
label89_comp:
	la	$a0 bool_const0
label90_comp:
	lw	$a0 12($a0)
	la	$t1 bool_const1
	lw	$t1 12($t1)
	beq	$a0 $t1 label87_comp
	la	$a0 bool_const1
	jal	label88_comp
label87_comp:
	la	$a0 bool_const0
label88_comp:
	lw	$a0 12($a0)
	la	$t1 bool_const1
	lw	$t1 12($t1)
	beq	$a0 $t1 label85_comp
	la	$a0 bool_const1
	jal	label86_comp
label85_comp:
	la	$a0 bool_const0
label86_comp:
	lw	$a0 12($a0)
	la	$t1 bool_const1
	lw	$t1 12($t1)
	beq	$a0 $t1 label83_comp
	la	$a0 bool_const1
	jal	label84_comp
label83_comp:
	la	$a0 bool_const0
label84_comp:
	lw	$a0 12($a0)
	la	$t1 bool_const1
	lw	$t1 12($t1)
	beq	$a0 $t1 label81_comp
	la	$a0 bool_const1
	jal	label82_comp
label81_comp:
	la	$a0 bool_const0
label82_comp:
	lw	$a0 12($a0)
	la	$t1 bool_const1
	lw	$t1 12($t1)
	beq	$a0 $t1 label79_comp
	la	$a0 bool_const1
	jal	label80_comp
label79_comp:
	la	$a0 bool_const0
label80_comp:
	lw	$t1 4($sp)
	addiu	$sp $sp 4
	move	$t2 $a0
	la	$a0 bool_const1
	beq	$t1 $t2 label77_eq
	la	$a1 bool_const0
	jal	equality_test
label77_eq:
	lw	$a0 4($fp)
	sw	$a0 0($sp)
	addiu	$sp $sp -4
	lw	$a0 4($fp)
	lw	$a0 12($a0)
	la	$t1 bool_const1
	lw	$t1 12($t1)
	beq	$a0 $t1 label123_comp
	la	$a0 bool_const1
	jal	label124_comp
label123_comp:
	la	$a0 bool_const0
label124_comp:
	lw	$a0 12($a0)
	la	$t1 bool_const1
	lw	$t1 12($t1)
	beq	$a0 $t1 label121_comp
	la	$a0 bool_const1
	jal	label122_comp
label121_comp:
	la	$a0 bool_const0
label122_comp:
	lw	$a0 12($a0)
	la	$t1 bool_const1
	lw	$t1 12($t1)
	beq	$a0 $t1 label119_comp
	la	$a0 bool_const1
	jal	label120_comp
label119_comp:
	la	$a0 bool_const0
label120_comp:
	lw	$a0 12($a0)
	la	$t1 bool_const1
	lw	$t1 12($t1)
	beq	$a0 $t1 label117_comp
	la	$a0 bool_const1
	jal	label118_comp
label117_comp:
	la	$a0 bool_const0
label118_comp:
	lw	$a0 12($a0)
	la	$t1 bool_const1
	lw	$t1 12($t1)
	beq	$a0 $t1 label115_comp
	la	$a0 bool_const1
	jal	label116_comp
label115_comp:
	la	$a0 bool_const0
label116_comp:
	lw	$a0 12($a0)
	la	$t1 bool_const1
	lw	$t1 12($t1)
	beq	$a0 $t1 label113_comp
	la	$a0 bool_const1
	jal	label114_comp
label113_comp:
	la	$a0 bool_const0
label114_comp:
	lw	$a0 12($a0)
	la	$t1 bool_const1
	lw	$t1 12($t1)
	beq	$a0 $t1 label111_comp
	la	$a0 bool_const1
	jal	label112_comp
label111_comp:
	la	$a0 bool_const0
label112_comp:
	lw	$a0 12($a0)
	la	$t1 bool_const1
	lw	$t1 12($t1)
	beq	$a0 $t1 label109_comp
	la	$a0 bool_const1
	jal	label110_comp
label109_comp:
	la	$a0 bool_const0
label110_comp:
	lw	$a0 12($a0)
	la	$t1 bool_const1
	lw	$t1 12($t1)
	beq	$a0 $t1 label107_comp
	la	$a0 bool_const1
	jal	label108_comp
label107_comp:
	la	$a0 bool_const0
label108_comp:
	lw	$a0 12($a0)
	la	$t1 bool_const1
	lw	$t1 12($t1)
	beq	$a0 $t1 label105_comp
	la	$a0 bool_const1
	jal	label106_comp
label105_comp:
	la	$a0 bool_const0
label106_comp:
	lw	$a0 12($a0)
	la	$t1 bool_const1
	lw	$t1 12($t1)
	beq	$a0 $t1 label103_comp
	la	$a0 bool_const1
	jal	label104_comp
label103_comp:
	la	$a0 bool_const0
label104_comp:
	lw	$t1 4($sp)
	addiu	$sp $sp 4
	move	$t2 $a0
	la	$a0 bool_const1
	beq	$t1 $t2 label101_eq
	la	$a1 bool_const0
	jal	equality_test
label101_eq:
	lw	$a0 0($fp)
	sw	$a0 0($sp)
	addiu	$sp $sp -4
	lw	$a0 12($a0)
	sw	$a0 0($sp)
	addiu	$sp $sp -4
	la	$a0 int_const2
	lw	$t1 4($sp)
	addiu	$sp $sp 4
	lw	$a0 12($a0)
	add	$s1 $t1 $a0
	lw	$a0 4($sp)
	jal	Object.copy
	sw	$s1 12($a0)
	addiu	$sp $sp 4
	sw	$a0 0($fp)
	jal	label0_loop
label2_loop:
	li	$a0 0
	lw	$a0 4($fp)
	la	$t1 bool_const1
	beq	$a0 $t1 label125_if
	la	$a0 int_const0
	jal	label126_if
label125_if:
	move	$a0 $s0
	bne	$a0 $zero label127
	la	$a0 str_const0
	li	$t1 1
	jal	_dispatch_abort
label127:
	lw	$t1 8($a0)
	lw	$t1 0($t1)
	jalr	$t1
label126_if:
	lw	$fp 20($sp)
	lw	$s0 16($sp)
	lw	$ra 12($sp)
	addiu	$sp $sp 20
	jr	$ra	

# end of generated code
