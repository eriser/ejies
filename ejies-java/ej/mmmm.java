/*
 *	ej.mmmm by Emmanuel Jourdan, Ircam � 12 2005
 *	output the min, median, mean, maximum on a int/float stream
 *
 *	$Revision: 1.2 $
 *	$Date: 2006/01/09 18:39:52 $
 */

package ej;

import com.cycling74.max.*;
import java.util.Arrays;

public class mmmm extends ej
{
	private static final String[] INLET_ASSIST = new String[]{ "int/float" };
	private static final String[] OUTLET_ASSIST = new String[]{ "minimum", "median", "mean", "maximum", "list of values / dumpout" };
	private int window;
	private boolean verbose;
	private float[] a;
	private float[] b;
	private float[] c;
	private double somme = 0;	// soyons fous... un double
	private int idx;		// index du tableau ou sera stock�e la prohaine valeur
	private boolean arrayFull = false;
	
	// si il n'y a pas d'arguments
	public mmmm()
	{
		this(3, false);
	}

	// si il y a un argument
	public mmmm(int ws)
	{
		this(ws, false);
	}
	
	// il y a deux arguments
	public mmmm(int ws, boolean verbeux)
	{
		declareIO(1,4);
		declareAttribute("window", null, "setWindow");
		declareAttribute("verbose");

		setWindow(ws);
		verbose = verbeux;
		
		setInletAssist(INLET_ASSIST);
		setOutletAssist(OUTLET_ASSIST);
	}
	
	private void setWindow(int i)
	{
		if (i < 3)
			window = 3;
		else
			window = i;

		reset();
	}
	
	public void bang()
	{
		sortie();
	}
    
	public void inlet(float f)
	{
		// re�oit les int et les float
		addToList(f);
		sortie();
	}

	private void addToList(float val)
	{
		if ( idx == (window - 1) ) {	// remet � z�ro le compteur
			a[window - 1] = val;
			idx = 0;
			arrayFull = true;
		} else
			a[idx++] = val;
	}

	public void clear()
	{
		// clear is a synonym for reset();
		reset();
	}
	
	public void reset()
	{
		a = new float[window];
		b = new float[window];
		c = new float[window];
		idx = 0;
		arrayFull = false;
	}
	
	private void sortie()
	{
		if  (! arrayFull)
			return;

		// copy array... �a �xiste dans Java... c'est magique
		System.arraycopy(a, 0, b, 0, a.length);
		
		if (verbose)
			outputWindow();

		Arrays.sort(b);
		outlet(3, b[b.length - 1]);
		outlet(2, moyenne(b));
		outlet(1, b[b.length / 2]);
		outlet(0, b[0]);
	}
        
	private double moyenne(float tab[])
	{
		int i;
		somme = 0;

		for (i = 0; i < tab.length; i++) {
			somme += tab[i];
		}
		
		return (somme / tab.length);
	}
	
	private void outputWindow()
	{
		int i;
		for (i = 0; i < c.length; i++) {
			c[i] = a[(i + idx) % window];
		}

		outlet(4, c);
	}
}