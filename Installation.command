#!/bin/bash
# This file must be saved in UTF-8 (because of the sortcuts)

################################
# Installations Methodes
################################
function doInstallation {
	if [ -e "$C74Folder" ] ; then

		if [[ ! -e "$C74Folder/ejies" ]] ; then
			mkdir "$C74Folder/ejies";
		fi
		
		echo -ne "- objects ($C74Folder/ejies/ejies-obj)"
		cp -R "$InstallationSourceFolder"/ejies-obj "$C74Folder/ejies/" && echo -ne "... done.\n"

		echo -ne "- help files ($C74Folder/ejies/ejies-help)"
		cp -R "$InstallationSourceFolder"/ejies-help "$C74Folder/ejies/" && echo -ne "... done.\n"
		
		echo -ne "- init files ($C74Folder/init/)"
		cp -R "$InstallationSourceFolder"/ejies-init/* "$C74Folder/init/" && echo -ne "... done.\n"
		
		echo -ne "- jsui files ($C74Folder/jsui-library/)"
		cp -R "$InstallationSourceFolder"/ejies-jsui/* "$C74Folder/jsui-library/" && echo -ne "... done.\n"
		
		echo -ne "- jsextensions file ($C74Folder/jsextensions/)"
		cp -R "$InstallationSourceFolder"/ejies-jsextensions/* "$C74Folder/jsextensions/" && echo -ne "... done.\n"
	
		echo -ne "- java lib file ($C74Folder/java/lib/ej.jar)"
		cp -R "$InstallationSourceFolder"/ejies-java/ej.jar "$C74Folder/java/lib/" && echo -ne "... done.\n"
	else
		echo -ne "Sorry, $C74Folder doesn't exist. Init, jsui and jsextensions can't be installed.\n"
	fi
	
	if [ -e "$maxAppFolder" ] ; then
		echo -ne "- extras file ($maxAppFolder/patches/extras/)"
		cp "$InstallationSourceFolder"/ejies-extras/ejies-overview.maxpat "$maxAppFolder/patches/extras/" && echo -ne "... done.\n"
	
		echo -ne "- prototypes ($maxAppFolder/patches/object-prototypes/)"
		cp -R "$InstallationSourceFolder"/ejies-prototypes/* "$maxAppFolder/patches/object-prototypes/" && echo -ne "... done.\n"
	
# 		echo -ne "- inspectors ($maxAppFolder/patches/inspectors)"
# 		cp "$InstallationSourceFolder"/ejies-insp/* "$maxAppFolder/patches/inspectors/" && echo -ne "... done.\n"
		
		echo -ne "- images ($maxAppFolder/patches/picts)"
		cp -R "$InstallationSourceFolder"/ejies-pict/* "$maxAppFolder/patches/picts/" && echo -ne "... done.\n"
		
		echo -ne "- ejies-javadoc ($maxAppFolder/java-doc/)"
		cp -R "$InstallationSourceFolder"/ejies-javadoc "$maxAppFolder/java-doc/" && echo -ne "... done.\n"		
	else
		echo -ne "Sorry, $maxAppFolder/ doesn't exist. Extra and prototypes can't be installed.\n"
	fi

	echo -ne "\n";
}

################################




################################
# Installation process
################################
clear
echo "------------------------"
echo "-- ejies Installation --"
echo "------------------------"
echo ""
echo ""


################################
# making PATH
################################
InstallationCommandPath=$0
InstallationSourceFolder=$(dirname "$InstallationCommandPath")


################################
#  Version checking
################################
installedAtLeastOneVersion=0;

echo -ne "Checking version... ";
if [ -e "/Applications/Max5" ]; then
	maxAppFolder="/Applications/Max5";
	C74Folder="$maxAppFolder/Cycling '74";
	
	echo "Installing ejies for Max 5, in $maxAppFolder:";
	doInstallation;
	installedAtLeastOneVersion=1;
fi

if [ -e "/Applications/Max6" ]; then
	maxAppFolder="/Applications/Max6";
	C74Folder="$maxAppFolder/Cycling '74";
	
	echo "Installing ejies for Max 6, in $maxAppFolder:";
	doInstallation;
	installedAtLeastOneVersion=1;
fi

# on my C74 computer...
if [ -e "/sysbuild/Development" ]; then
	maxAppFolder="/sysbuild/Development";
	C74Folder="$maxAppFolder/Cycling '74";
	
	echo "Installing ejies for Max 6, in $maxAppFolder:";
	doInstallation;
	installedAtLeastOneVersion=1;
fi

if [[ $installedAtLeastOneVersion == 0 ]]; then
	echo "Max 5 or 6 is not installed in the /Applications folder. The ejies's automatic installation is not possible. Sorry...";
	exit 1;
fi


################################
# Fin de l'installation
echo -ne "\nend of the installation... enjoy!\n"
echo -ne "(you can quit the Terminal now...)\n"

sleep 5 # under Snow Leopard it seems to quit really fast the terminal after installing

exit 0;