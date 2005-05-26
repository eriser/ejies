/*
	ej.cc.js (change color) by Emmanuel Jourdan, Ircam - 01 2005
	Allows you to rename any named object.
 */

// global code

var ejies = EjiesUtils();	// lien vers ejies-extension.js

inlets = 1;
outlets = 0;
setinletassist(0, "symbol");
setoutletassist(0, "nothing here...");

var Couleur;
var NomObjet;
var GlobalState = 1;

if (jsarguments.length == 2) {
	if (jsarguments[1] == 0)
		GlobalState = 0;
	else if (jsarguments[1] == 1 ) { ; }	 // GlobalState reste � 1
	else if (jsarguments[1] == "ej")
		ej();								// mes param�tres
	else
		perror("wrong argument");
}
if (jsarguments.length > 2)
	perror("extra arguments...");

function anything()
{
	var SourceDestination = arrayfromargs(messagename, arguments);
	NomObjet = SourceDestination[0];
	Couleur = SourceDestination[1];

	if (typeof NomObjet == "string" && typeof Couleur == "number")
		ExecuteOperation(ChangeColor);
	else
		perror("wrong argument, must be [symbol] [int]");
}

function ExecuteOperation(toto)
{
	if (GlobalState)
		this.patcher.applydeep(toto);
	else
		this.patcher.apply(toto);
}
ExecuteOperation.local = 1;

function ChangeColor(MyObj)
{
	if (MyObj.maxclass == NomObjet )
		MyObj.colorindex = Couleur;
	return true;	// pour que l'iteration continue
}
ChangeColor.local = 1;

function resetall()
{
	ExecuteOperation(ResetAllColors);
}

function ResetAllColors(MyObj)
{
	MyObj.colorindex = 0;
	return true;
}
ResetAllColors.local = 1;

function global(a)
{
	if (a == 1 ||�a == 0)
		GlobalState = a;
	else
		perror("wrong argument for message global (1 or 0 expected)");
}


function ej() // une fonction d�di�e
{
	var GlobalStateTmp = GlobalState;      // sauvegarde la valeur actuelle
	global(1);

	resetall();
	InternalExecuteOperation("send",6);
	InternalExecuteOperation("receive",6);
	InternalExecuteOperation("send~",6);
	InternalExecuteOperation("receive~",6);
	InternalExecuteOperation("value",6);
	InternalExecuteOperation("forward",6);
	InternalExecuteOperation("coll",14);
	InternalExecuteOperation("patcher",5);
	InternalExecuteOperation("poly~",5);
	InternalExecuteOperation("loadbang",4);
	InternalExecuteOperation("loadmess",4);
	InternalExecuteOperation("closebang",4);
	InternalExecuteOperation("thispatcher",4);

	GlobalState = GlobalStateTmp;          // restauration de la valeur
}

function InternalExecuteOperation(a, b)
{
	NomObjet = a;
	Couleur = b;
	this.patcher.applydeep(ChangeColor);
}
InternalExecuteOperation.local = 1;

function perror()
{
	ejies.scriptname = "ej.cc.js";
	ejies.perror(arguments);
}
perror.local = 1;

// Pour la compilation automatique
// autowatch = 1;
// post("Compiled...\n");