package com.psychocoder.parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

public class MathParserCLI {

	private static String in = null;

	private static final String NEWLINE = "\n";

	private static String TITLE = "             __   _                "
			+ NEWLINE + "|V| __|_|_  |_   |_)___  _ _o _ __ " + NEWLINE
			+ "| |(_||_| | |__><|  |(/__>_>|(_)| |" + "\n\n" + NEWLINE
			+ "|_)_ ___ _ __                      " + NEWLINE
			+ "| (_||_>(/_|                       " + NEWLINE
			+ "\n                    By Psycho_Coder";

	private static final String USAGE = "Usage :Type any mathematical expression with the functions"
			+ "stated above in the description"
			+ NEWLINE
			+ "example : "
			+ "sin(2*x)*cos(x)" + NEWLINE;
	private static final String SEPERATOR = "--------------------";

	private static final String DESCRIPTION = "Description :"
			+ NEWLINE
			+ SEPERATOR
			+ "\n\nThis parser is a simple math expression parser that"
			+ NEWLINE
			+ "parses and evaluates various mathematical functions."
			+ NEWLINE
			+ "\nTriginometric functions : sin, cos, tan, arctan, arccos,arcsin, sec, cosec,cot "
			+ NEWLINE
			+ "\nLogarithms : ln is used for natural log, log10 is log base 10 and log2 is log base 2"
			+ NEWLINE
			+ "\nOther functions : exp for exponentiation, abs for absolute value, sqrt for square root"
			+ NEWLINE
			+ "\nNumbers like 45.3e-13 are also supported "
			+ "\n\nThis parser can also take functions of one variable like"
			+ NEWLINE
			+ "a math relation log10(x*sin(30*x)) with a variable x. Whenever such"
			+ NEWLINE
			+ "expressions are encountered you are asked to enter the value of x."
			+ NEWLINE
			+ "\nPlease Note :"
			+ NEWLINE
			+ SEPERATOR
			+ " \n\n1. Trigonometric functions work with radians and not degrees"
			+ NEWLINE
			+ "2. sin x is not valid but sin(x) or sin (x) is valid. Similarly its true for others too"
			+ NEWLINE
			+ "3. This parser also skips whitespaces and so it will work as long as "
			+ "parentheisation is in correct order" + NEWLINE + "\n"
			+ SEPERATOR + "\n\nType help, description, usage for assistence and exit to leave.";

	private static MathParser obj = null;

	private static void output(String args) {
		System.out.print(args);
	}

	private static void getDescription() {
		output(NEWLINE + DESCRIPTION + NEWLINE);
	}

	private static void usage() {
		output(NEWLINE + USAGE + NEWLINE);
	}

	private static void getHelp() {
		output("Help yourself :P" + NEWLINE);
	}

	protected void finalize() {
		obj = null;
	}

	private static void input() throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		in = br.readLine();
	}
	
	

	public static void main(String[] args) throws IOException {
		Scanner sc = new Scanner(System.in);
		output(TITLE + NEWLINE);
		getDescription();
		usage();
		char c = 'x';
		// String in = null;//
		// "sin(x*log10 (tan(cos (x + x ^ 10) - log2 (x))))*100";
		double valX = Double.MIN_VALUE;
		while (true) {
			output(NEWLINE + "$$>> ");
			input();
			try {
				// obj = (MathParser) createNewInstance(in);
				
				if (in.equalsIgnoreCase("usage")) {
					usage();
				} else if (in.equalsIgnoreCase("description")) {
					getDescription();
				} else if (in.equalsIgnoreCase("help")) {
					getHelp();
				} else if (in.equalsIgnoreCase("exit")) {
					output("Good bye. ^_^");
					break;
				} else {
					try {
						obj = new MathParser(in);
						if (in.indexOf(c) != -1) {
							output("$$>> Enter variable value :");
							valX = sc.nextDouble();
							output("Entered Value(x=" + valX + ")");
							System.out.print("\nThe given expression is : "
									+ MathParser.getExpr());
						}
						output(NEWLINE + "$$>> " + (obj.setValue(valX) + "\n"));
					} catch (IllegalArgumentException e) {
						output(e.getMessage().toString());
					}
				}
			} catch (IllegalArgumentException e1) {
				output(e1.getMessage().toString());
			}
		}
	}
}
