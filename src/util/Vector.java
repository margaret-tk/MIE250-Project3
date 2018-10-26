package util;

import java.util.Arrays;
import java.util.HashMap;



/** Implements a vector with *named* indices.  For example { x=1.0 y=2.0 } is a 2D
 *  vector with the first dimension named "x" and the second dimension named "y"
 *  and having respective values 1.0 and 2.0 in these dimensions.
 *  
 *  TODO: Implement all methods required to support the functionality of the project
 *        and that described in Vector.main(...) below.
 * 
 * @author ssanner@mie.utoronto.ca
 *
 */
public class Vector {

	private HashMap<String,Double> _hmVar2Value; // This maps dimension variable names to values
	
	
	/** Constructor of an initially empty Vector
	 * 
	 */
	public Vector() {
		_hmVar2Value = new HashMap<String, Double>();
	}

	/** Constructor that parses a String s like "{ x=-1 y=-2.0 z=3d }" into 
	 *  the internal HashMap representation of the Vector.  See usage in main().
	 * 
	 * @param s
	 */
	public Vector(String s) throws VectorException {
		this();
		try {
		String[] split = s.split("\\s");
		int length = split.length;
		for (int index = 0; index < length-2; index++) {
				String[] subsplit = split[index+1].split("=");
			_hmVar2Value.put(subsplit[0], Double.valueOf(subsplit[1]));
		}
		}
		catch (Exception e){
			throw new VectorException("Exception");
		}	
} 
		
	
	/** Removes (clears) all (key,value) pairs from the Vector representation
	 * 
	 */
	public void clear() {
		_hmVar2Value.clear();
	}
	
	public HashMap<String,Double> seeVector() {
		return _hmVar2Value;
	}

	/** Sets a specific var to the value val in *this*, i.e., var=val
	 * 
	 * @param var - label of Vector index to change
	 * @param val - value to change it to
	 */
	public void set(String var, double val) {
		_hmVar2Value.put(var, val);
	}

	public void keySet() {
		_hmVar2Value.keySet();
	}
	
	public double getVal(String key) {
		return _hmVar2Value.get(key);
	}
	/** Sets all entries in *this* Vector to match entries in x
	 *  (if additional variables are in *this*, they remain unchanged) 
	 * 
	 * @param x
	 */
	public void setAll(Vector x) { 
		_hmVar2Value.putAll(x._hmVar2Value);
	}

	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("{ ");
		for (String key: _hmVar2Value.keySet()) {
			sb.append(key);
			double value = _hmVar2Value.get(key);
			sb.append(String.format("=%6.4f ", value));
		}
		sb.append("}");
	return sb.toString();
	}

	/** Create new Vector that is a sum of this. and v
	 * 
	 * 
	 * @param Vector v
	 * @return Vector sum 
	 */
	public Vector sum(Vector v) throws VectorException {
		try {
		Vector sum = new Vector();	
		for(String key: _hmVar2Value.keySet()) { //iterate through vector keys, add 
			double valSum = _hmVar2Value.get(key) + v.getVal(key);
			sum.set(key, valSum);
		}
		return sum;
		} catch (Exception e) {
			throw new VectorException("Vector exception");
		}
	}
	
	
	/** Method that creates new vector that is a scalar multiple of this 
	 * 
	 * @param d
	 * @return
	 */
	public Vector scalarMult(Double d) throws VectorException {
		try {
		Vector mult = new Vector(); 
		for(String key: _hmVar2Value.keySet()) { //iterate through vector, multiply each val by d 
			double valMult = _hmVar2Value.get(key) * d;
			mult.set(key, valMult);
		}
		return mult;
		}catch (Exception e) {
			throw new VectorException("Vector Exception");
		}
	}
	
	public double computeL2Norm() {
		double norm;
		double sumOfSquareVals = 0;
		for(String key: _hmVar2Value.keySet()) {
			sumOfSquareVals =sumOfSquareVals +  (_hmVar2Value.get(key) *_hmVar2Value.get(key));
		}
		norm = Math.sqrt(sumOfSquareVals);
	return norm;
	}
	
	public boolean equals(Object o) {
		if (o instanceof Vector) {
			Vector v = (Vector)o; // This is called a cast (or downcast)... we can do it since we			                     
			// know from the if statement that o is actually of subtype Vector
			for (String key: _hmVar2Value.keySet()) {
				if(_hmVar2Value.get(key) != v.getVal(key)) return false;
				else return true;
			}
			 // If two Vectors mismatch at any index, they are not equal
			return true; // Everything matched... objects are equal!
		} else // if we get here "(o instanceof Vector)" was false
			return false; // Two objects cannot be equal if they don't have the same class type
	}
	
	
	///////////////////////////////////////////////////////////////////////////////
	// TODO: Add your methods here!  You'll need more than those above to make
	//       main() work below.
	///////////////////////////////////////////////////////////////////////////////
	
	/** Your Vector class should implement the core functionality below and produce
	 *  **all** of the expected outputs below.  **These will be tested for grading.**
	 * 
	 *  When initially developing the code, comment out lines below that you have
	 *  not implemented yet.  This will allow your code to compile for incremental
	 *  testing.
	 *  
	 * @param args (unused -- ignore)
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {

		// Make vector: vec1[x y z] = [1 2 3]
		Vector vec1 = new Vector();
		vec1.set("x", 1.0);
		vec1.set("y", 2.0);
		vec1.set("z", 3.0);
		
		// Make vector: vec2[x y z] = [-3 -2 -1]
		Vector vec2 = new Vector();
		vec2.set("x", -3.0);
		vec2.set("y", -2.0);
		vec2.set("z", -1.0);
		
		// Make vector: vec3[x y z] = vec4[x y z] = [-1 -2 -3]
		Vector vec3 = new Vector("{ x=-1 y=-2.0 z=3d }");
		//System.out.println("testing1");
		//System.out.println(vec3.toString());
		Vector vec4 = new Vector(vec3.toString());
		//System.out.println("testing2");
		// Hint: all numbers below are formatted with String.format("%s=%6.4f ", var, val)
		//       ... you may want to use this in your Vector.toString() implementation!
		
		// Test cases: 
		System.out.println(vec1); // Should print: { x=1.0000 y=2.0000 z=3.0000 }
		System.out.println(vec2); // Should print: { x=-3.0000 y=-2.0000 z=-1.0000 }
		System.out.println(vec3); // Should print: { x=-1.0000 y=-2.0000 z=3.0000 }
		System.out.println(vec4); // Should print: { x=-1.0000 y=-2.0000 z=3.0000 }
		System.out.println(vec1.sum(vec1));        // Should print: { x=2.0000 y=4.0000 z=6.0000 }
		System.out.println(vec1.sum(vec2));        // Should print: { x=-2.0000 y=0.0000 z=2.0000 }
		System.out.println(vec1.sum(vec3));        // Should print: { x=0.0000 y=0.0000 z=6.0000 }
		System.out.println(vec1.scalarMult(0.5));  // Should print: { x=0.5000 y=1.0000 z=1.5000 }
		System.out.println(vec2.scalarMult(-1.0)); // Should print: { x=3.0000 y=2.0000 z=1.0000 }
		System.out.println(vec1.sum(vec2.scalarMult(-1.0))); // Should print: { x=4.0000 y=4.0000 z=4.0000 }
		System.out.format("%01.3f\n", vec1.computeL2Norm());           // Should print: 3.742
		System.out.format("%01.3f\n", vec2.sum(vec3).computeL2Norm()); // Should print: 6.000
		//
		// If the following don't work, did you override equals()?  See Project 2 Vector and Matrix.
		System.out.println(vec3.equals(vec1)); // Should print: false
		System.out.println(vec3.equals(vec3)); // Should print: true
		System.out.println(vec3.equals(vec4)); // Should print: true
		System.out.println(vec1.sum(vec2).equals(vec2.sum(vec1))); // Should print: true
	}	
}
