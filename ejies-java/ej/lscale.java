/*
 *	ej.lscale by Emmanuel Jourdan, Ircam � 02 2005
 *	scale for lists
 *
 *
 *	$Revision: 1.3 $
 *	$Date: 2006/04/10 13:22:57 $
 */

package ej;

import com.cycling74.max.*;
import java.lang.reflect.*;// this time I use reflection instead of interface :-)
import java.util.Arrays;

public class lscale extends ej {
	private static final String[] INLET_ASSIST = new String[]{ "List to be scaled", "Low input value", "High input value", "Low output value", "High output value", "Exponent" };
	private static final String[] OUTLET_ASSIST = new String[]{ "Scaled list"};	

	private float[] a;
	private float[] resultat;
	private float xMin = 0;
	private float xMax = 127;
	private float xRange = xMax - xMin;
	private float yMin = 0;
	private float yMax = 1;
	private float yRange = yMax - yMin;
	private float[] yClip = { yMin, yMax };
	private float expValue = 1;
	private String methodString = "calculeNormal";

	private boolean clip = false;
	
	private Class myClass;
	private Method myMethod;
	
	public lscale(float[] args)	{
		declareTypedIO("alffff", "l");
		createInfoOutlet(true);
		
		declareAttribute("clip", "getClip", "setClip");
		initClass();
		newArgs(args);
		calculeChoice();
		
		setInletAssist(INLET_ASSIST);
		setOutletAssist(OUTLET_ASSIST);
	}
		
	public void bang() {
		calcule();
	}
	
	private void initClass() {
		try {
			myClass = Class.forName( "ej.lscale" );
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void setClip(Atom[] args) {
		clip = args[0].toBoolean();
		calculeChoice();
	}
	
	private int getClip() {
		return (clip ? 1: 0);
	}

	public void inlet(float f) {
		switch (getInlet()) {
			case 0:
				a = new float[1];
				a[0] = f;
				calcule();
				break;
			case 1:
				xMin = f;
				refreshRange();
				break;
			case 2:
				xMax = f;
				refreshRange();
				break;
			case 3:
				yMin = f;
				refreshRange();
				break;
			case 4:
				yMax = f;
				refreshRange();
				break;
			case 5:
				expValue = Math.max(0,Math.min(f, Float.POSITIVE_INFINITY));
				calculeChoice();
				break;
		}
	}

	public void list(float[] args) {
	
		switch (getInlet()) {
			case 0:                // premi�re entr�e... liste � scaler
				a = args;
				calcule();
				break;
			case 1:
				newArgs(args);     // arguments donn�s sous forme de liste
				break;
			default:               // quelle id�e d'envoyer une liste ici
				error("ej.lscale: doesn't expect a list here");
				break; // est-ce vraiment n�cessaire ?
		}
	}
	
	public void anything(String s, Atom[] args) {
		error("ej.lscale: doesn't understand " + s + " " + Atom.toOneString(args));
	}
	
	private void calculeChoice() {
		// choix de la m�thod pour le scaling
		if (expValue == 1)
			methodString = (clip ? "calculeClip" : "calculeNormal");
		else
			methodString = (clip ? "calculeExpClip" : "calculeExp");
		
		try {
			myMethod = myClass.getMethod(methodString, null );
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		refreshRange();
	}
	
	private void refreshRange() {
		xRange = xMax - xMin;
		yRange = yMax - yMin;

		// recalcule le bon min/max utile pour le clip
		yClip[0] = Math.min(yMin, yMax);
		yClip[1] = Math.max(yMin, yMax);
	}
	
	private void calcule() {
		try {
			resultat = new float[a.length];
			myMethod.invoke(this, null);
			outlet(0, resultat);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void calculeNormal() {
		for (int i = 0; i < resultat.length; i++) {
			resultat[i] = ((a[i] - xMin) / xRange) * yRange + yMin;
		}
	}
	
	public void calculeExp() {
		for (int i = 0; i < resultat.length; i++) {
			resultat[i] = (float) Math.pow(((a[i] - xMin) / xRange), expValue) * yRange + yMin;
		}
	}
	
	public void calculeClip() {
		for (int i = 0; i < resultat.length; i++) {
			resultat[i] = (float) Math.max(yClip[0], Math.min(Math.pow(((a[i] - xMin) / xRange), expValue) * yRange + yMin, yClip[1]));
		}
	}
	
	public void calculeExpClip() {
		for (int i = 0; i < resultat.length; i++) {
			resultat[i] = (float) Math.max(yClip[0], Math.min(Math.pow(((a[i] - xMin) / xRange), expValue) * yRange + yMin, yClip[1]));
		}
	}
		
	private void newArgs(float[] args) {
		if (args.length > 0) xMin = args[0];
		if (args.length > 1) xMax = args[1];
		if (args.length > 2) yMin = args[2];
		if (args.length > 3) yMax = args[3];
		if (args.length > 4) expValue = Math.max(0,Math.min(args[4], Float.POSITIVE_INFINITY));
		if (args.length > 5) error("ej.lscale: extra argument");
		
		refreshRange();
	}
}