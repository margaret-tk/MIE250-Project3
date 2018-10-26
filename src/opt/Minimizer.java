package opt;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.TreeSet;

import poly.PolyException;
import poly.Polynomial;
import poly.Term;
import util.Vector;

/** The main optimization package: see project handout for functionality and definitions
 *  of terminology defined here.
 * 
 * @author ssanner@mie.utoronto.ca
 *
 */
public class Minimizer {
	
	private double _eps;			// tolerance
	private int    _maxIter;		// maximum number of iterations
	private double _stepSize;		// step size alpha
	private Vector _x0;	   			// starting point
	private Vector _lastx; 			// last point found
	private double _lastObjVal;		// last obj fn value found
	private double _lastGradNorm;   // last gradient norm found
	private long _compTime;			// computation time needed
	private int _nIter;				// no. of iterations needed

	private HashMap<String,Polynomial> _var2gradp; // cached Polynomials for gradient expressions

	/** Default constructor -- you may want to add to it, but you don't have to
	 * 
	 */
	public Minimizer() {
		_eps = 0.001;
		_maxIter = 100;
		_stepSize = 0.05;
		_x0 = new Vector();
		_lastx = new Vector();
	}
	
	// Getters -- we've filled these in since they are trivial
	public double getEps()      { return _eps; }
	public int getMaxIter()     { return _maxIter; }
	public double getStepSize() { return _stepSize; }
	public Vector getX0() 		{ return _x0; }
	public double getLastObjVal()   { return _lastObjVal; }
	public double getLastGradNorm() { return _lastGradNorm; }
	public Vector getLastPoint() { return _lastx; }
	public int getNIter()       { return _nIter; }
	public long getCompTime()   { return _compTime; }
	
	// Setters -- we've filled these in since they are trivial
	public void setEps(double e)      { _eps = e; }
	public void setMaxIter(int m)     { _maxIter = m; }
	public void setStepSize(double s) { _stepSize = s; }
	
	/** Set the initial starting point of gradient descent
	 * 
	 * @param x0
	 */
	public void setX0(Vector x0) { 
		_x0.clear();
		_x0.setAll(x0);
	}
	
	
	//method that creates gradient hashmap (un-evaluated)
	public HashMap<String,Polynomial> getGradient(Polynomial p)  {
	
		TreeSet<String> pAllVars = new TreeSet<String>(p.getAllVars()); 
		_var2gradp = new HashMap<String, Polynomial>();
		for(String each: pAllVars ) {
			 
			_var2gradp.put(each, p.differentiate(each));
		}
		return _var2gradp;
	}
	
	
	
	///////////////////////////////////////////////////////////////////////////////
	// TODO: Your methods here!  You should add some helper methods that facilitate
	//       the implementation of the methods below.
	///////////////////////////////////////////////////////////////////////////////

	/** Run the steepest descent algorithm -- see handout though some pseudocode is provided below.
	 * 
	 * @param p Polynomial to minimize
	 * @throws Exception
	 */
	public void minimize(Polynomial p) throws Exception {
		
		// TODO: Build the partial derivatives of p for all variables in p
		getGradient(p);
	
		// Start the gradient descent
		_nIter = 0;
		long start = System.currentTimeMillis();

		// Initialize the starting point and last objective evaluation for starting point 
		_lastx.clear();
		_lastx.setAll(_x0);
		_lastObjVal = p.evaluate(_x0);
		_lastGradNorm = Double.MAX_VALUE;
		
		// TODO: Main descent loop
		//	while (iterations is less than maximum iterations allowed AND gradient norm is greater than eps) {
		//	  increment iterations
		//	  compute gradient and gradient norm
		//	  compute new point by adding current point and the negation of the step size times the gradient
		//	  evaluate the objective at the new point
		//	  print iteration number, new point, objective at new point, i.e.
		//      System.out.format("At iteration %d: %s objective value = %.3f\n", _nIter, _lastx, _lastObjVal);
		//	}
		
		while ( _nIter < _maxIter && _lastGradNorm >= _eps ) {
			_nIter++;
			
			Vector grad = new Vector(); //evaluated gradient 
			
			_lastGradNorm = 0;
			
			//fill evaluated gradient vector hashmap<string, double>
			for(String each:  _var2gradp.keySet()) {
				grad.set(each,  _var2gradp.get(each).evaluate(_lastx));
			}
			
			//calculating the evaluated gradient norm 
			for (String each: grad.seeVector().keySet()) {
				_lastGradNorm += Math.pow(grad.getVal(each), 2.00);
			}
			_lastGradNorm = Math.sqrt(_lastGradNorm);
	
			//finding new point
			_lastx = _lastx.sum(grad.scalarMult(-1* _stepSize));
			
			//new obj value 
			_lastObjVal = p.evaluate(_lastx);
			
			//print out current status 
			System.out.format("At iteration %d: %s objective value = %.3f\n", _nIter, _lastx, _lastObjVal);

		}
		
		// Record the end time
		_compTime = System.currentTimeMillis()-start; 
	}
	
	
	/** Print minimization result details
	 * 
	 * @param ps - usually System.out
	 */
	public void printResults(PrintStream ps) {
		ps.println();
		ps.println("Number of iterations: " + getNIter());
		ps.println("Last gradient norm:   " + String.format("%6.4f", getLastGradNorm()));
		ps.println("Point at termination: " + getLastPoint());
		ps.println("Time elapsed:         " + getCompTime() + " ms");
		ps.println();
	}

	/** Print parameter settings
	 * 
	 * @param ps - usually System.out
	 */
	public void printParams(PrintStream ps) {
		ps.println();
		ps.println("Tolerance (epsilon): " + getEps());
		ps.println("Maximum iterations:  " + getMaxIter());
		ps.println("Step size (alpha):   " + getStepSize());
		ps.println("Starting point (x0): " + getX0());
		ps.println();
	}

	/** An example testing the Minimizer with expected output.  Make sure this works!
	 *  
	 * @param args - unused
	 * @throws Exception if any errors occur (when implemented correctly, there should
	 *         be no Exceptions/errors below)
	 */
	public static void main(String[] args) throws Exception {
		
		// Assign starting point {x=1.0}
		Vector x0 = new Vector();
		x0.set("x", 1.0);
		//x0.set("y", 2.0);

		// Initialize polynomial and minimizer
		Polynomial p = new Polynomial("10*x^2 + -40*x + 40");
		Minimizer  m = new Minimizer();		
		m.setX0(x0);

		System.out.println("Polynomial: " + p);

		// Run minimizer and view result at termination
		m.minimize(p);
		System.out.format("At termination: %s objective value = %.3f\n", m._lastx, m._lastObjVal);

		// Output of the test case above should read:
		// ===============================================
		// Polynomial: 10.000*x^2 + -40.000*x + 40.000
		// At iteration 1: { x=2.0000 } objective value = 0.000
		// At iteration 2: { x=2.0000 } objective value = 0.000
		// At termination: { x=2.0000 } objective value = 0.000
		// ===============================================
		
		// [Optional] View the cached gradient expressions if you've cached them
		for (String var : p.getAllVars())
			  System.out.println("Gradient of " + var + " is " + m._var2gradp.get(var));
	}
}
