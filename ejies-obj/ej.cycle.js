/*	ej.cycle.js by Emmanuel Jourdan, Ircam - 02 2004	send any type of data to the next outlet 	$Revision: 1.4 $	$Date: 2006/07/31 09:22:25 $*/// global codevar NbOutlets = 1;const MAXINOUT = 64;	// nombre maximum de sortiesvar WhichOutlet = 0; 	// sur quelle outlet on envoie les informations...var SignalWarning = 1;	// pour afficher un message d'erreur quand il y a du signal qui arrive// traitement des argumentsif (jsarguments.length>1 && typeof jsarguments[1] == "number")	NbOutlets = Math.min(jsarguments[1],MAXINOUT);	// outlet (minimum 1, max 64)if (jsarguments.length>2)	error(this, "extra arguments...");inlets = 1;outlets = NbOutlets;// assistancesetinletassist(0, "Data to send to successive outlets");for ( i=0 ; i < NbOutlets ; i++) { setoutletassist(i,"outlet " + (i+1)); }function set(v){	if (typeof v == "number")		WhichOutlet = ejies.clip(v,1, NbOutlets) - 1; // depuis l'ext�rieur il s'agit des entr�es 1 � NbOutlets	else		error(this, "wrong argument for message set");}function signal(){	if (SignalWarning)		error(this, "doesn't work with signal");	SignalWarning = 0;}function anything(){	outlet((WhichOutlet++ % NbOutlets), arrayfromargs(messagename, arguments));}anything.immediate = 1;// Pour la compilation automatique// autowatch = 1;// post("Compiled...\n");