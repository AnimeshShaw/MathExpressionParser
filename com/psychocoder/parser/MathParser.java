package com.psychocoder.parser;

import java.util.Stack;

/**
 * @author psychocoder
 * 
 */
public class MathParser {

	/**
	 * @param exprsn
	 *            The original exprsn of the expression, as passed to the
	 *            constructor .
	 * @param code
	 *            A translated version of the expression, containing stack
	 *            operations that compute the value of the expression.
	 * @param stack
	 *            A stack to be used during the evaluation of the expression.
	 * @param constants
	 *            An array containing all the constants found in the expression.
	 */
	private String exprsn;
	private byte[] code;
	private Stack<Double> stack;
	private double[] constants;
	private static String expr;

	/**
	 * @Constructor This constructor calls the main parsing method that parses
	 *              the user defined string.
	 * @param expr
	 *            the math expression to be evaluated. This is given by the user
	 *            as an input
	 */
	public MathParser(String expr) {
		MathParser.setExpr(expr);
		parseMathExpression(expr);

	}

	/**
	 * This method returns the expression that was given by the user
	 * 
	 * @return Return the original expression string of this expression. This is
	 *         the same string that was provided in the constructor.
	 */
	public static String getExpr() {
		return expr;
	}

	/**
	 * Sets the user defined function to another variable for parsing
	 * 
	 * @param exprsn
	 *            Contains the math expression given by the user
	 */
	public static void setExpr(String exprsn) {
		expr = exprsn;
	}

	/**
	 * @param x
	 * @return Return the value of this expression, when the variable x has the
	 *         specified value. If the expression is undefined for the specified
	 *         value of x, then Double.NaN is returned.
	 */
	public double setValue(double x) {
		return evaluateExpression(x);
	}

	/**
	 * @param byte values for code array; values >= 0 are indices into constants
	 *        array
	 */
	private static final byte PLUS = -1, MINUS = -2, MULTI = -3, DIVIDE = -4,
			POWER = -5, SIN = -6, COS = -7, TAN = -8, COT = -9, SEC = -10,
			COSEC = -11, ARCSIN = -12, ARCCOS = -13, ARCTAN = -14, EXP = -15,
			LN = -16, LOG10 = -17, LOG2 = -18, ABS = -19, SQRT = -20,
			UNARYMINUS = -21, VARIABLE = -22;

	/**
	 * @param functionNames
	 *            names of standard functions, used during parsing
	 */
	private static String[] functionNames = { "sin", "cos", "tan", "cot",
			"sec", "cosec", "arcsin", "arccos", "arctan", "exp", "ln", "log10",
			"log2", "abs", "sqrt" };

	/**
	 * @param variable
	 * @return evaluate this expression for this value of the variable
	 */
	private double evaluateExpression(double variable) { //
		try {
			for (int i = 0; i < codeSize; i++) {
				if (code[i] >= 0) {
					stack.push(constants[code[i]]);
				} else if (code[i] >= POWER) {
					double y = stack.pop();
					double x = stack.pop();
					double result = Double.NaN;
					switch (code[i]) {
					case PLUS:
						result = x + y;
						break;
					case MINUS:
						result = x - y;
						break;
					case MULTI:
						result = x * y;
						break;
					case DIVIDE:
						result = x / y;
						break;
					case POWER:
						result = Math.pow(x, y);
						break;
					}
					if (Double.isNaN(result)) {
						return result;
					}
					stack.push(result);
				} else if (code[i] == VARIABLE) {
					stack.push(variable);
				} else {
					double x = stack.pop();
					double result = Double.NaN;
					switch (code[i]) {
					case SIN:
						result = Math.sin(x);
						break;
					case COS:
						result = Math.cos(x);
						break;
					case TAN:
						result = Math.tan(x);
						break;
					case COT:
						result = Math.cos(x) / Math.sin(x);
						break;
					case SEC:
						result = 1.0 / Math.cos(x);
						break;
					case COSEC:
						result = 1.0 / Math.sin(x);
						break;
					case ARCSIN:
						if (Math.abs(x) <= 1.0) {
							result = Math.asin(x);
						}
						break;
					case ARCCOS:
						if (Math.abs(x) <= 1.0) {
							result = Math.acos(x);
						}
						break;
					case ARCTAN:
						result = Math.atan(x);
						break;
					case EXP:
						result = Math.exp(x);
						break;
					case LN:
						if (x > 0.0) {
							result = Math.log(x);
						}
						break;
					case LOG2:
						if (x > 0.0) {
							result = Math.log(x) / Math.log(2);
						}
						break;
					case LOG10:
						if (x > 0.0) {
							result = Math.log(x) / Math.log(10);
						}
						break;
					case ABS:
						result = Math.abs(x);
						break;
					case SQRT:
						if (x >= 0.0) {
							result = Math.sqrt(x);
						}
						break;
					case UNARYMINUS:
						result = -x;
						break;
					}
					if (Double.isNaN(result)) {
						return result;
					}
					stack.push(result);

				}
			}
		} catch (Exception e) {
			return Double.NaN;
		}
		if (Double.isInfinite(stack.peek())) {
			return Double.NaN;
		} else {
			return stack.peek();
		}
	}

	/**
	 * data for use during parsing
	 */
	private int pos = 0, constantCt = 0, codeSize = 0;

	/**
	 * @param message
	 *            called when an exception occurs during parsing
	 */
	private void exception(String message) {
		throw new IllegalArgumentException("parseMathExpression exception:  "
				+ message + "  (Position = " + pos + ".)");
	}

	/**
	 * call after code[] is computed
	 * 
	 * @return Stack usage
	 */
	private int computeStackUsage() {
		int size = 0;
		int max = 0;
		for (int i = 0; i < codeSize; i++) {
			if (code[i] >= 0 || code[i] == VARIABLE) {
				size++;
				if (size > max) {
					max = size;
				}
			} else if (code[i] >= POWER) {
				size--;
			}
		}
		return max;
	}

	/**
	 * @return return next char in data or 0 if data is all used
	 */
	private char next() {
		if (pos >= exprsn.length()) {
			return 0;
		} else {
			return exprsn.charAt(pos);
		}
	}

	/**
	 * skipSpaces over white space in data
	 */
	private void skipSpaces() { //
		while (Character.isWhitespace(next())) {
			pos++;
		}
	}

	/**
	 * remaining routines do a standard recursive parseMathExpression of the
	 * expression
	 */

	private void parseExpression() {
		boolean neg = false;
		skipSpaces();
		if (next() == '+' || next() == '-') {
			neg = (next() == '-');
			pos++;
			skipSpaces();
		}
		parseTerm();
		if (neg) {
			code[codeSize++] = UNARYMINUS;
		}
		skipSpaces();
		while (next() == '+' || next() == '-') {
			char op = next();
			pos++;
			parseTerm();
			code[codeSize++] = (op == '+') ? PLUS : MINUS;
			skipSpaces();
		}
	}

	private void parseTerm() {
		parseFactor();
		skipSpaces();
		while (next() == '*' || next() == '/') {
			char op = next();
			pos++;
			parseFactor();
			code[codeSize++] = (op == '*') ? MULTI : DIVIDE;
			skipSpaces();
		}
	}

	private void parseFactor() {
		parsePrimary();
		skipSpaces();
		while (next() == '^') {
			pos++;
			parsePrimary();
			code[codeSize++] = POWER;
			skipSpaces();
		}
	}

	private void parsePrimary() {
		skipSpaces();
		char ch = next();
		if (ch == 'x' || ch == 'X') {
			pos++;
			code[codeSize++] = VARIABLE;
		} else if (Character.isLetter(ch)) {
			parseWord();
		} else if (Character.isDigit(ch) || ch == '.') {
			parseNumber();
		} else if (ch == '(') {
			pos++;
			parseExpression();
			skipSpaces();
			if (next() != ')') {
				exception("Expected right parenthesis.");
			}
			pos++;
		} else if (ch == ')') {
			exception("Unmatched right parenthesis. Check your expression.");
		} else if (ch == '+' || ch == '-' || ch == '*' || ch == '/'
				|| ch == '^') {
			exception("Operator '" + ch + "' found in an unexpected position.");
		} else if (ch == 0) {
			exception("Unexpected breakdown during parsing, reached end of data in the middle of an expression.");
		} else {
			exception("Illegal character '" + ch + "' found in data.");
		}
	}

	private void parseWord() {
		String w = "";
		while (Character.isLetterOrDigit(next())) {
			w += next();
			pos++;
		}
		w = w.toLowerCase();
		for (int i = 0; i < functionNames.length; i++) {
			if (w.equals(functionNames[i])) {
				skipSpaces();
				if (next() != '(') {
					exception("Function name '"
							+ w
							+ "' must be followed by its parameter in parentheses.");
				}
				pos++;
				parseExpression();
				skipSpaces();
				if (next() != ')') {
					exception("Missing right parenthesis after parameter of function '"
							+ w + "'.");
				}
				pos++;
				code[codeSize++] = (byte) (SIN - i);
				return;
			}
		}
		exception("Unknown character or string '" + w + "' found in data.");
	}

	private void parseNumber() {
		String w = "";
		while (Character.isDigit(next())) {
			w += next();
			pos++;
		}
		if (next() == '.') {
			w += next();
			pos++;
			while (Character.isDigit(next())) {
				w += next();
				pos++;
			}
		}
		if (w.equals(".")) {
			exception("Improper number found, consisting of decimal point only.");
		}
		if (next() == 'E' || next() == 'e') {
			w += next();
			pos++;
			if (next() == '+' || next() == '-') {
				w += next();
				pos++;
			}
			if (!Character.isDigit(next())) {
				exception("Illegal number found, with no digits in its exponent.");
			}
			while (Character.isDigit(next())) {
				w += next();
				pos++;
			}
		}
		double d = Double.NaN;
		try {
			d = Double.valueOf(w).doubleValue();
		} catch (Exception e) {
		}
		if (Double.isNaN(d)) {
			exception("Illegal number '" + w + "' found in data.");
		}
		code[codeSize++] = (byte) constantCt;
		constants[constantCt++] = d;
	}

	/**
	 * Produce all the data that represents the expression internally
	 * 
	 * @param exprsn
	 *            the mathematical expression to be evaluated
	 * @throws IllegalArgumentException
	 */
	public void parseMathExpression(String exprsn) {
		if (exprsn == null || exprsn.trim().equals("")) {
			exception("No data provided");
		}
		this.exprsn = exprsn;
		code = new byte[exprsn.length()];
		constants = new double[exprsn.length()];
		parseExpression();
		skipSpaces();
		if (next() != 0) {
			exception("Extra unwanted data found after the end of the expression.");
		}
		int stackSize = computeStackUsage();
		stack = new Stack<>();
		stack.setSize(stackSize);
		byte[] c = new byte[codeSize];
		System.arraycopy(code, 0, c, 0, codeSize);
		code = c;
		double[] A = new double[constantCt];
		System.arraycopy(constants, 0, A, 0, constantCt);
		constants = A;
	}
}
