/*
 * $Id$
 * $URL$
 *
 *
 *==================================================================================
 * Copyright (c) 2009 the copyright is held jointly by the individual
 * authors. See the file AUTHORS for the list of authors.
 *
 * This file is part of jsbml, the pure java SBML library. Please visit
 * http://sbml.org for more information about SBML, and http://jsbml.sourceforge.net/
 * to get the latest version of jsbml.
 *
 * jsbml is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 2.1 of the License, or
 * (at your option) any later version.
 *
 * jsbml is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with jsbml.  If not, see <http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html>.
 *
 *===================================================================================
 *
 */
package org.sbml.jsbml.util.compilers;

import java.util.IllegalFormatException;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import org.sbml.jsbml.ASTNode;
import org.sbml.jsbml.Compartment;
import org.sbml.jsbml.FunctionDefinition;
import org.sbml.jsbml.NamedSBaseWithDerivedUnit;
import org.sbml.jsbml.SBMLException;
import org.sbml.jsbml.SpeciesReference;
import org.sbml.jsbml.util.StringTools;

/**
 * This class creates C-like strings that represent the content of
 * {@link ASTNode}s. These can be used to save equations in SBML with older than
 * Level 2.
 * 
 * @author Andreas Dr&auml;ger
 * 
 * @opt attributes
 * @opt types
 * @opt visibility
 */
public class TextFormula extends StringTools implements ASTNodeCompiler {

	/**
	 * Basic method which links several elements with a mathematical operator.
	 * All empty StringBuffer object are excluded.
	 * 
	 * @param operator
	 * @param elements
	 * @return
	 */
	private static final StringBuffer arith(char operator, Object... elements) {
		List<Object> vsb = new Vector<Object>();
		for (Object sb : elements) {
			if (sb != null && sb.toString().length() > 0) {
				vsb.add(sb);
			}
		}
		StringBuffer equation = new StringBuffer();
		if (vsb.size() > 0) {
			equation.append(vsb.get(0));
		}
		Character op = Character.valueOf(operator);
		for (int count = 1; count < vsb.size(); count++) {
			append(equation, op, vsb.get(count));
		}
		return equation;
	}

	/**
	 * 
	 * @param sb
	 * @return
	 */
	public static final StringBuffer brackets(Object sb) {
		return concat(Character.valueOf('('), sb, Character.valueOf(')'));
	}

	/**
	 * Tests whether the String representation of the given object contains any
	 * arithmetic symbols and if the given object is already sorrounded by
	 * brackets.
	 * 
	 * @param something
	 * @return True if either brackets are set around the given object or the
	 *         object does not contain any symbols such as +, -, *, /.
	 */
	private static boolean containsArith(Object something) {
		boolean arith = false;
		String d = something.toString();
		if (d.length() > 0) {
			char c;
			for (int i = 0; (i < d.length()) && !arith; i++) {
				c = d.charAt(i);
				arith = ((c == '+') || (c == '-') || (c == '*') || (c == '/'));
			}
		}
		return arith;
	}

	/**
	 * Returns the difference of the given elements as StringBuffer.
	 * 
	 * @param subtrahents
	 * @return
	 */
	public static final StringBuffer diff(Object... subtrahents) {
		if (subtrahents.length == 1) {
			return brackets(concat(Character.valueOf('-'), subtrahents));
		}
		return brackets(arith('-', subtrahents));
	}

	/**
	 * Returns a fraction with the given elements as numerator and denominator.
	 * 
	 * @param numerator
	 * @param denominator
	 * @return
	 */
	public static final StringBuffer frac(Object numerator, Object denominator) {
		return brackets(arith('/',
				(containsArith(numerator) ? brackets(numerator) : numerator),
				containsArith(denominator) ? brackets(denominator)
						: denominator));
	}

	/**
	 * Returns the id of a PluginSpeciesReference object's belonging species as
	 * an object of type StringBuffer.
	 * 
	 * @param ref
	 * @return
	 */
	protected static final StringBuffer getSpecies(SpeciesReference ref) {
		return new StringBuffer(ref.getSpecies());
	}

	/**
	 * Returns the value of a PluginSpeciesReference object's stoichiometry
	 * either as a double or, if the stoichiometry has an integer value, as an
	 * int object.
	 * 
	 * @param ref
	 * @return
	 */
	protected static final double getStoichiometry(SpeciesReference ref) {
		double stoich = ref.getStoichiometry();
		return stoich;
	}

	/**
	 * Returns the basis to the power of the exponent as StringBuffer. Several
	 * special cases are treated.
	 * 
	 * @param basis
	 * @param exponent
	 * @return
	 */
	public static final StringBuffer pow(Object basis, Object exponent) {
		try {
			if (Double.parseDouble(exponent.toString()) == 0f) {
				return new StringBuffer("1");
			}
			if (Double.parseDouble(exponent.toString()) == 1f) {
				return basis instanceof StringBuffer ? (StringBuffer) basis
						: new StringBuffer(basis.toString());
			}
		} catch (NumberFormatException exc) {
		}
		String b = basis.toString();
		if (b.contains("*") || b.contains("-") || b.contains("+")
				|| b.contains("/") || b.contains("^")) {
			basis = brackets(basis);
		}
		String e = exponent.toString();
		if (e.contains("*") || e.contains("-") || e.contains("+")
				|| e.contains("/") || e.contains("^")) {
			exponent = brackets(e);
		}
		return arith('^', basis, exponent);
	}

	/**
	 * Returns the exponent-th root of the basis as StringBuffer.
	 * 
	 * @param exponent
	 * @param basis
	 * @return
	 * @throws IllegalFormatException
	 *             If the given exponent represents a zero.
	 */
	public static final StringBuffer root(Object exponent, Object basis)
			throws NumberFormatException {
		if (Double.parseDouble(exponent.toString()) == 0f) {
			throw new NumberFormatException(
					"Cannot extract a zeroth root of anything");
		}
		if (Double.parseDouble(exponent.toString()) == 1f) {
			return new StringBuffer(basis.toString());
		}
		return concat("root(", exponent, Character.valueOf(','), basis,
				Character.valueOf(')'));
	}

	/**
	 * 
	 * @param basis
	 * @return
	 */
	public static final StringBuffer sqrt(Object basis) {
		try {
			return root(Integer.valueOf(2), basis);
		} catch (IllegalFormatException e) {
			return pow(basis, frac(Integer.valueOf(1), Integer.valueOf(2)));
		}
	}

	/**
	 * Returns the sum of the given elements as StringBuffer.
	 * 
	 * @param summands
	 * @return
	 */
	public static final StringBuffer sum(Object... summands) {
		return brackets(arith('+', summands));
	}

	/**
	 * Returns the product of the given elements as StringBuffer.
	 * 
	 * @param factors
	 * @return
	 */
	public static final StringBuffer times(Object... factors) {
		return arith('*', factors);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.sbml.jsbml.util.compilers.ASTNodeCompiler#abs(org.sbml.jsbml.ASTNode)
	 */
	public ASTNodeValue abs(ASTNode node) throws SBMLException {
		return function("abs", node);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.sbml.jsbml.util.compilers.ASTNodeCompiler#and(java.util.List)
	 */
	public ASTNodeValue and(List<ASTNode> nodes) throws SBMLException {
		return logicalOperation(" and ", nodes);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.sbml.jsbml.util.compilers.ASTNodeCompiler#arccos(org.sbml.jsbml.ASTNode
	 * )
	 */
	public ASTNodeValue arccos(ASTNode node) throws SBMLException {
		return function("arccos", node);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.sbml.jsbml.util.compilers.ASTNodeCompiler#arccosh(org.sbml.jsbml.
	 * ASTNode)
	 */
	public ASTNodeValue arccosh(ASTNode node) throws SBMLException {
		return function("arccos", node);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.sbml.jsbml.util.compilers.ASTNodeCompiler#arccot(org.sbml.jsbml.ASTNode
	 * )
	 */
	public ASTNodeValue arccot(ASTNode node) throws SBMLException {
		return function("arccot", node);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.sbml.jsbml.util.compilers.ASTNodeCompiler#arccoth(org.sbml.jsbml.
	 * ASTNode)
	 */
	public ASTNodeValue arccoth(ASTNode node) throws SBMLException {
		return function("arccoth", node);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.sbml.jsbml.util.compilers.ASTNodeCompiler#arccsc(org.sbml.jsbml.ASTNode
	 * )
	 */
	public ASTNodeValue arccsc(ASTNode node) throws SBMLException {
		return function("arccsc", node);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.sbml.jsbml.util.compilers.ASTNodeCompiler#arccsch(org.sbml.jsbml.
	 * ASTNode)
	 */
	public ASTNodeValue arccsch(ASTNode node) throws SBMLException {
		return function("arccsch", node);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.sbml.jsbml.util.compilers.ASTNodeCompiler#arcsec(org.sbml.jsbml.ASTNode
	 * )
	 */
	public ASTNodeValue arcsec(ASTNode node) throws SBMLException {
		return function("arcsec", node);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.sbml.jsbml.util.compilers.ASTNodeCompiler#arcsech(org.sbml.jsbml.
	 * ASTNode)
	 */
	public ASTNodeValue arcsech(ASTNode node) throws SBMLException {
		return function("arcsech", node);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.sbml.jsbml.util.compilers.ASTNodeCompiler#arcsin(org.sbml.jsbml.ASTNode
	 * )
	 */
	public ASTNodeValue arcsin(ASTNode node) throws SBMLException {
		return function("arcsin", node);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.sbml.jsbml.util.compilers.ASTNodeCompiler#arcsinh(org.sbml.jsbml.
	 * ASTNode)
	 */
	public ASTNodeValue arcsinh(ASTNode node) throws SBMLException {
		return function("arcsinh", node);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.sbml.jsbml.util.compilers.ASTNodeCompiler#arctan(org.sbml.jsbml.ASTNode
	 * )
	 */
	public ASTNodeValue arctan(ASTNode node) throws SBMLException {
		return function("arctan", node);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.sbml.jsbml.util.compilers.ASTNodeCompiler#arctanh(org.sbml.jsbml.
	 * ASTNode)
	 */
	public ASTNodeValue arctanh(ASTNode node) throws SBMLException {
		return function("arctanh", node);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.sbml.jsbml.util.compilers.ASTNodeCompiler#ceiling(org.sbml.jsbml.
	 * ASTNode)
	 */
	public ASTNodeValue ceiling(ASTNode node) throws SBMLException {
		return function("ceil", node);
	}

	/**
	 * Creates brackets if needed.
	 * 
	 * @param nodes
	 * @return
	 * @throws SBMLException
	 */
	private String checkBrackets(ASTNode nodes) throws SBMLException {
		String term = nodes.compile(this).toString();
		if (nodes.isSum() || nodes.isDifference() || nodes.isUMinus()) {
			term = brackets(term).toString();
		}
		return term;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.sbml.jsbml.util.compilers.ASTNodeCompiler#compile(org.sbml.jsbml.
	 * Compartment)
	 */
	public ASTNodeValue compile(Compartment c) {
		return new ASTNodeValue(c.getId(), this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.sbml.jsbml.util.compilers.ASTNodeCompiler#compile(double,
	 * java.lang.String)
	 */
	public ASTNodeValue compile(double real, String units) {
		return new ASTNodeValue(toString(real), this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.sbml.jsbml.util.compilers.ASTNodeCompiler#compile(int,
	 * java.lang.String)
	 */
	public ASTNodeValue compile(int integer, String units) {
		return new ASTNodeValue(integer, this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.sbml.jsbml.util.compilers.ASTNodeCompiler#compile(org.sbml.jsbml.
	 * NamedSBaseWithDerivedUnit)
	 */
	public ASTNodeValue compile(NamedSBaseWithDerivedUnit variable) {
		return new ASTNodeValue(variable.getId(), this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.sbml.jsbml.util.compilers.ASTNodeCompiler#compile(java.lang.String)
	 */
	public ASTNodeValue compile(String name) {
		return new ASTNodeValue(name, this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.sbml.jsbml.util.compilers.ASTNodeCompiler#cos(org.sbml.jsbml.ASTNode)
	 */
	public ASTNodeValue cos(ASTNode node) throws SBMLException {
		return function("cos", node);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.sbml.jsbml.util.compilers.ASTNodeCompiler#cosh(org.sbml.jsbml.ASTNode
	 * )
	 */
	public ASTNodeValue cosh(ASTNode node) throws SBMLException {
		return function("cosh", node);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.sbml.jsbml.util.compilers.ASTNodeCompiler#cot(org.sbml.jsbml.ASTNode)
	 */
	public ASTNodeValue cot(ASTNode node) throws SBMLException {
		return function("cot", node);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.sbml.jsbml.util.compilers.ASTNodeCompiler#coth(org.sbml.jsbml.ASTNode
	 * )
	 */
	public ASTNodeValue coth(ASTNode node) throws SBMLException {
		return function("coth", node);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.sbml.jsbml.util.compilers.ASTNodeCompiler#csc(org.sbml.jsbml.ASTNode)
	 */
	public ASTNodeValue csc(ASTNode node) throws SBMLException {
		return function("csc", node);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.sbml.jsbml.util.compilers.ASTNodeCompiler#csch(org.sbml.jsbml.ASTNode
	 * )
	 */
	public ASTNodeValue csch(ASTNode node) throws SBMLException {
		return function("csch", node);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.sbml.jsbml.util.compilers.ASTNodeCompiler#delay(java.lang.String,
	 * org.sbml.jsbml.ASTNode, double, java.lang.String)
	 */
	public ASTNodeValue delay(String delayName, ASTNode x, double d,
			String timeUnits) throws SBMLException {
		return new ASTNodeValue(concat("delay(", x.compile(this), ", ",
				toString(d), ")").toString(), this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.sbml.jsbml.util.compilers.ASTNodeCompiler#eq(org.sbml.jsbml.ASTNode,
	 * org.sbml.jsbml.ASTNode)
	 */
	public ASTNodeValue eq(ASTNode left, ASTNode right) throws SBMLException {
		return new ASTNodeValue(relation(left, " == ", right), this);
	}

	/**
	 * 
	 * @param left
	 * @param symbol
	 * @param right
	 * @return
	 * @throws SBMLException
	 */
	private String relation(ASTNode left, String symbol, ASTNode right)
			throws SBMLException {
		return concat(left.compile(this), symbol, right.compile(this))
				.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.sbml.jsbml.util.compilers.ASTNodeCompiler#exp(org.sbml.jsbml.ASTNode)
	 */
	public ASTNodeValue exp(ASTNode node) throws SBMLException {
		return function("exp", node);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.sbml.jsbml.util.compilers.ASTNodeCompiler#factorial(org.sbml.jsbml
	 * .ASTNode)
	 */
	public ASTNodeValue factorial(ASTNode node) {
		return new ASTNodeValue(append(brackets(node), Character.valueOf('!'))
				.toString(), this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.sbml.jsbml.util.compilers.ASTNodeCompiler#floor(org.sbml.jsbml.ASTNode
	 * )
	 */
	public ASTNodeValue floor(ASTNode node) throws SBMLException {
		return function("floor", node);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.sbml.jsbml.util.compilers.ASTNodeCompiler#frac(org.sbml.jsbml.ASTNode
	 * , org.sbml.jsbml.ASTNode)
	 */
	public ASTNodeValue frac(ASTNode numerator, ASTNode denominator)
			throws SBMLException {
		return new ASTNodeValue(concat(checkBrackets(numerator),
				Character.valueOf('/'), checkBrackets(denominator)).toString(),
				this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.sbml.jsbml.util.compilers.ASTNodeCompiler#frac(int, int)
	 */
	public ASTNodeValue frac(int numerator, int denominator) {
		return new ASTNodeValue(concat(
				numerator < 0 ? brackets(compile(numerator, null)) : compile(
						numerator, null),
				Character.valueOf('/'),
				denominator < 0 ? brackets(compile(denominator, null))
						: compile(denominator, null)).toString(), this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.sbml.jsbml.util.compilers.ASTNodeCompiler#function(org.sbml.jsbml
	 * .FunctionDefinition, java.util.List)
	 */
	public ASTNodeValue function(FunctionDefinition func, List<ASTNode> nodes)
			throws SBMLException {
		return function(func.getName(), nodes);
	}

	/**
	 * 
	 * @param name
	 * @param nodes
	 * @return
	 * @throws SBMLException
	 */
	private ASTNodeValue function(String name, List<ASTNode> nodes)
			throws SBMLException {
		return new ASTNodeValue(concat(name, lambda(nodes)).toString(), this);
	}

	/**
	 * 
	 * @param name
	 * @param nodes
	 * @return
	 * @throws SBMLException
	 */
	private ASTNodeValue function(String name, ASTNode... nodes)
			throws SBMLException {
		LinkedList<ASTNode> l = new LinkedList<ASTNode>();
		for (ASTNode node : nodes) {
			l.add(node);
		}
		return new ASTNodeValue(concat(name, lambda(l)).toString(), this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.sbml.jsbml.util.compilers.ASTNodeCompiler#geq(org.sbml.jsbml.ASTNode,
	 * org.sbml.jsbml.ASTNode)
	 */
	public ASTNodeValue geq(ASTNode left, ASTNode right) throws SBMLException {
		return new ASTNodeValue(relation(left, " >= ", right), this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.sbml.jsbml.util.compilers.ASTNodeCompiler#getConstantE()
	 */
	public ASTNodeValue getConstantE() {
		return new ASTNodeValue(Character.toString('e'), this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.sbml.jsbml.util.compilers.ASTNodeCompiler#getConstantFalse()
	 */
	public ASTNodeValue getConstantFalse() {
		return new ASTNodeValue(false, this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.sbml.jsbml.util.compilers.ASTNodeCompiler#getConstantPi()
	 */
	public ASTNodeValue getConstantPi() {
		return new ASTNodeValue("pi", this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.sbml.jsbml.util.compilers.ASTNodeCompiler#getConstantTrue()
	 */
	public ASTNodeValue getConstantTrue() {
		return new ASTNodeValue(true, this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.sbml.jsbml.util.compilers.ASTNodeCompiler#getNegativeInfinity()
	 */
	public ASTNodeValue getNegativeInfinity() {
		return new ASTNodeValue(Double.NEGATIVE_INFINITY, this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.sbml.jsbml.util.compilers.ASTNodeCompiler#getPositiveInfinity()
	 */
	public ASTNodeValue getPositiveInfinity() {
		return new ASTNodeValue(Double.POSITIVE_INFINITY, this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.sbml.jsbml.util.compilers.ASTNodeCompiler#gt(org.sbml.jsbml.ASTNode,
	 * org.sbml.jsbml.ASTNode)
	 */
	public ASTNodeValue gt(ASTNode left, ASTNode right) throws SBMLException {
		return new ASTNodeValue(relation(left, " > ", right), this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.sbml.jsbml.util.compilers.ASTNodeCompiler#lambda(java.util.List)
	 */
	public ASTNodeValue lambda(List<ASTNode> nodes) throws SBMLException {
		StringBuffer lambda = new StringBuffer();
		for (int i = 0; i < nodes.size(); i++) {
			if (i > 0) {
				lambda.append(", ");
			}
			lambda.append(nodes.get(i).compile(this));
		}
		return new ASTNodeValue(brackets(lambda).toString(), this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.sbml.jsbml.util.compilers.ASTNodeCompiler#leq(org.sbml.jsbml.ASTNode,
	 * org.sbml.jsbml.ASTNode)
	 */
	public ASTNodeValue leq(ASTNode left, ASTNode right) throws SBMLException {
		return new ASTNodeValue(relation(left, " <= ", right), this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.sbml.jsbml.util.compilers.ASTNodeCompiler#ln(org.sbml.jsbml.ASTNode)
	 */
	public ASTNodeValue ln(ASTNode node) throws SBMLException {
		return function("log", node);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.sbml.jsbml.util.compilers.ASTNodeCompiler#log(org.sbml.jsbml.ASTNode)
	 */
	public ASTNodeValue log(ASTNode node) throws SBMLException {
		return function("log", node);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.sbml.jsbml.util.compilers.ASTNodeCompiler#log(org.sbml.jsbml.ASTNode,
	 * org.sbml.jsbml.ASTNode)
	 */
	public ASTNodeValue log(ASTNode left, ASTNode right) throws SBMLException {
		return function("log", left, right);
	}

	/**
	 * 
	 * @param operator
	 * @param nodes
	 * @return
	 * @throws SBMLException
	 */
	private ASTNodeValue logicalOperation(String operator, List<ASTNode> nodes)
			throws SBMLException {
		StringBuffer value = new StringBuffer();
		boolean first = true;
		for (ASTNode node : nodes) {
			if (!first) {
				value.append(operator);
			} else {
				first = true;
			}
			if (!node.isUnary()) {
				append(value, Character.valueOf('('), node.compile(this)
						.toString(), Character.valueOf(')'));
			} else {
				value.append(node.compile(this).toString());
			}
		}
		return new ASTNodeValue(value.toString(), this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.sbml.jsbml.util.compilers.ASTNodeCompiler#lt(org.sbml.jsbml.ASTNode,
	 * org.sbml.jsbml.ASTNode)
	 */
	public ASTNodeValue lt(ASTNode left, ASTNode right) throws SBMLException {
		return new ASTNodeValue(relation(left, " < ", right), this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.sbml.jsbml.util.compilers.ASTNodeCompiler#minus(java.util.List)
	 */
	public ASTNodeValue minus(List<ASTNode> nodes) throws SBMLException {
		StringBuffer minus = new StringBuffer();
		for (int i = 0; i < nodes.size(); i++) {
			if (i > 0) {
				minus.append('-');
			}
			minus.append(checkBrackets(nodes.get(i)));
		}
		return new ASTNodeValue(minus.toString(), this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.sbml.jsbml.util.compilers.ASTNodeCompiler#neq(org.sbml.jsbml.ASTNode,
	 * org.sbml.jsbml.ASTNode)
	 */
	public ASTNodeValue neq(ASTNode left, ASTNode right) throws SBMLException {
		return new ASTNodeValue(relation(left, " != ", right), this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.sbml.jsbml.util.compilers.ASTNodeCompiler#not(org.sbml.jsbml.ASTNode)
	 */
	public ASTNodeValue not(ASTNode node) throws SBMLException {
		return function("not", node);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.sbml.jsbml.util.compilers.ASTNodeCompiler#or(java.util.List)
	 */
	public ASTNodeValue or(List<ASTNode> nodes) throws SBMLException {
		return logicalOperation(" or ", nodes);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.sbml.jsbml.util.compilers.ASTNodeCompiler#piecewise(java.util.List)
	 */
	public ASTNodeValue piecewise(List<ASTNode> nodes) throws SBMLException {
		return function("piecewise", nodes);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.sbml.jsbml.util.compilers.ASTNodeCompiler#plus(java.util.List)
	 */
	public ASTNodeValue plus(List<ASTNode> nodes) throws SBMLException {
		StringBuffer plus = new StringBuffer();
		ASTNode node;
		for (int i = 0; i < nodes.size(); i++) {
			if (i > 0) {
				plus.append('+');
			}
			node = nodes.get(i);
			if (node.isDifference()) {
				plus.append(checkBrackets(node));
			} else {
				plus.append(node.compile(this));
			}
		}
		return new ASTNodeValue(plus.toString(), this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.sbml.jsbml.util.compilers.ASTNodeCompiler#pow(org.sbml.jsbml.ASTNode,
	 * org.sbml.jsbml.ASTNode)
	 */
	public ASTNodeValue pow(ASTNode left, ASTNode right) throws SBMLException {
		return function("pow", left, right);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.sbml.jsbml.util.compilers.ASTNodeCompiler#root(org.sbml.jsbml.ASTNode
	 * , org.sbml.jsbml.ASTNode)
	 */
	public ASTNodeValue root(ASTNode rootExponent, ASTNode radiant)
			throws SBMLException {
		return function("root", rootExponent, radiant);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.sbml.jsbml.util.compilers.ASTNodeCompiler#root(double,
	 * org.sbml.jsbml.ASTNode)
	 */
	public ASTNodeValue root(double rootExponent, ASTNode radiant)
			throws SBMLException {
		if (rootExponent == 2d) {
			return sqrt(radiant);
		}
		return new ASTNodeValue(concat("root(", rootExponent,
				radiant.compile(this), ")").toString(), this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.sbml.jsbml.util.compilers.ASTNodeCompiler#sec(org.sbml.jsbml.ASTNode)
	 */
	public ASTNodeValue sec(ASTNode node) throws SBMLException {
		return function("sec", node);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.sbml.jsbml.util.compilers.ASTNodeCompiler#sech(org.sbml.jsbml.ASTNode
	 * )
	 */
	public ASTNodeValue sech(ASTNode node) throws SBMLException {
		return function("sech", node);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.sbml.jsbml.util.compilers.ASTNodeCompiler#sin(org.sbml.jsbml.ASTNode)
	 */
	public ASTNodeValue sin(ASTNode node) throws SBMLException {
		return function("sin", node);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.sbml.jsbml.util.compilers.ASTNodeCompiler#sinh(org.sbml.jsbml.ASTNode
	 * )
	 */
	public ASTNodeValue sinh(ASTNode node) throws SBMLException {
		return function("sin", node);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.sbml.jsbml.util.compilers.ASTNodeCompiler#sqrt(org.sbml.jsbml.ASTNode
	 * )
	 */
	public ASTNodeValue sqrt(ASTNode node) throws SBMLException {
		return function("sqrt", node);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.sbml.jsbml.util.compilers.ASTNodeCompiler#symbolTime(java.lang.String
	 * )
	 */
	public ASTNodeValue symbolTime(String time) {
		return new ASTNodeValue(time, this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.sbml.jsbml.util.compilers.ASTNodeCompiler#tan(org.sbml.jsbml.ASTNode)
	 */
	public ASTNodeValue tan(ASTNode node) throws SBMLException {
		return function("tan", node);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.sbml.jsbml.util.compilers.ASTNodeCompiler#tanh(org.sbml.jsbml.ASTNode
	 * )
	 */
	public ASTNodeValue tanh(ASTNode node) throws SBMLException {
		return function("tanh", node);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.sbml.jsbml.util.compilers.ASTNodeCompiler#times(java.util.List)
	 */
	public ASTNodeValue times(List<ASTNode> nodes) throws SBMLException {
		Object n[] = new ASTNodeValue[nodes.size()];
		for (int i = 0; i < nodes.size(); i++) {
			ASTNode ast = nodes.get(i);
			n[i] = new ASTNodeValue(checkBrackets(ast).toString(), this);
		}
		return new ASTNodeValue(times(n).toString(), this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.sbml.jsbml.util.compilers.ASTNodeCompiler#uMinus(org.sbml.jsbml.ASTNode
	 * )
	 */
	public ASTNodeValue uMinus(ASTNode node) throws SBMLException {
		return new ASTNodeValue(concat(Character.valueOf('-'),
				checkBrackets(node)).toString(), this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.sbml.jsbml.util.compilers.ASTNodeCompiler#unknownValue()
	 */
	public ASTNodeValue unknownValue() throws SBMLException {
		throw new SBMLException(
				"cannot write unknown syntax tree nodes to a formula String");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.sbml.jsbml.util.compilers.ASTNodeCompiler#xor(java.util.List)
	 */
	public ASTNodeValue xor(List<ASTNode> nodes) throws SBMLException {
		return logicalOperation(" xor ", nodes);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.sbml.jsbml.util.compilers.ASTNodeCompiler#compile(double, int,
	 * java.lang.String)
	 */
	public ASTNodeValue compile(double mantissa, int exponent, String units) {
		return new ASTNodeValue(concat(mantissa, "*10^(", exponent, ")")
				.toString(), this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.sbml.jsbml.util.compilers.ASTNodeCompiler#getConstantAvogadro(java
	 * .lang.String)
	 */
	public ASTNodeValue getConstantAvogadro(String name) {
		return new ASTNodeValue("avogadro", this);
	}
}
