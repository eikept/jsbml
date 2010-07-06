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

import java.util.List;

import org.sbml.jsbml.ASTNode;
import org.sbml.jsbml.Compartment;
import org.sbml.jsbml.FunctionDefinition;
import org.sbml.jsbml.NamedSBaseWithDerivedUnit;
import org.sbml.jsbml.SBMLException;
import org.sbml.jsbml.Species;
import org.sbml.jsbml.Unit;
import org.sbml.jsbml.UnitDefinition;
import org.sbml.jsbml.Unit.Kind;

/**
 * A compiler for abstract syntax trees. This compiler evaluates the values
 * represented by {@link ASTNode}s. It defines how to perform mathematical or
 * other operations on these data types. Recursion can be performed as follows:
 * 
 * <pre>
 *   public ASTNodeValue doSomeThing(ASTNode ast) {
 *     ...
 *     ASTNodeValue child = ast.compile(this);
 *     ...
 *     return new ASTNodeValue(doSomeThing(child), this);
 *   }
 * </pre>
 * 
 * @author Andreas Dr&auml;ger
 * 
 * @opt attributes
 * @opt types
 * @opt visibility
 */
public interface ASTNodeCompiler {

	/**
	 * The absolute value represented by the given {@link ASTNode}.
	 * 
	 * @param value
	 *            Must be interpretable as a {@link Number}.
	 * @return
	 * @throws SBMLException
	 */
	public ASTNodeValue abs(ASTNode value) throws SBMLException;

	/**
	 * 
	 * @param values
	 * @return
	 * @throws SBMLException
	 */
	public ASTNodeValue and(List<ASTNode> values) throws SBMLException;

	/**
	 * 
	 * @param value
	 * @return
	 * @throws SBMLException
	 */
	public ASTNodeValue arccos(ASTNode value) throws SBMLException;

	/**
	 * 
	 * @param value
	 * @return
	 * @throws SBMLException
	 */
	public ASTNodeValue arccosh(ASTNode value) throws SBMLException;

	/**
	 * 
	 * @param value
	 * @return
	 * @throws SBMLException
	 */
	public ASTNodeValue arccot(ASTNode value) throws SBMLException;

	/**
	 * 
	 * @param value
	 * @return
	 * @throws SBMLException
	 */
	public ASTNodeValue arccoth(ASTNode value) throws SBMLException;

	/**
	 * 
	 * @param value
	 * @return
	 * @throws SBMLException
	 */
	public ASTNodeValue arccsc(ASTNode value) throws SBMLException;

	/**
	 * 
	 * @param value
	 * @return
	 * @throws SBMLException
	 */
	public ASTNodeValue arccsch(ASTNode value) throws SBMLException;

	/**
	 * 
	 * @param value
	 * @return
	 * @throws SBMLException
	 */
	public ASTNodeValue arcsec(ASTNode value) throws SBMLException;

	/**
	 * 
	 * @param value
	 * @return
	 * @throws SBMLException
	 */
	public ASTNodeValue arcsech(ASTNode value) throws SBMLException;

	/**
	 * 
	 * @param value
	 * @return
	 * @throws SBMLException
	 */
	public ASTNodeValue arcsin(ASTNode value) throws SBMLException;

	/**
	 * 
	 * @param value
	 * @return
	 * @throws SBMLException
	 */
	public ASTNodeValue arcsinh(ASTNode value) throws SBMLException;

	/**
	 * 
	 * @param value
	 * @return
	 * @throws SBMLException
	 */
	public ASTNodeValue arctan(ASTNode value) throws SBMLException;

	/**
	 * 
	 * @param value
	 * @return
	 * @throws SBMLException
	 */
	public ASTNodeValue arctanh(ASTNode value) throws SBMLException;

	/**
	 * 
	 * @param value
	 * @return
	 * @throws SBMLException
	 */
	public ASTNodeValue ceiling(ASTNode value) throws SBMLException;

	/**
	 * 
	 * @param c
	 * @return
	 */
	public ASTNodeValue compile(Compartment c);

	/**
	 * Creates an {@link ASTNodeValue} that represents a real number in
	 * scientific notation, i.e., mantissa * 10^exponent, and the given units.
	 * 
	 * @param mantissa
	 *            The number to be multiplied with ten to the power of the given
	 *            exponent.
	 * @param exponent
	 *            The exponent for the multiplier ten.
	 * @param units
	 *            The identifier of the units object associated with the number
	 *            represented by this element. Can be null if no units have been
	 *            defined.
	 * @return
	 */
	public ASTNodeValue compile(double mantissa, int exponent, String units);

	/**
	 * 
	 * @param real
	 * @param units
	 *            A String representing the {@link Unit} of the given number.
	 *            This can be the identifier of a {@link UnitDefinition} in the
	 *            model or a literal in {@link Kind}. Can be null if no units
	 *            have been defined.
	 * @return
	 */
	public ASTNodeValue compile(double real, String units);

	/**
	 * 
	 * @param integer
	 * @param units
	 *            A String representing the {@link Unit} of the given number.
	 *            This can be the identifier of a {@link UnitDefinition} in the
	 *            model or a literal in {@link Kind}. Can be null if no units
	 *            have been defined.
	 * @return
	 */
	public ASTNodeValue compile(int integer, String units);

	/**
	 * 
	 * @param variable
	 * @return
	 * @throws SBMLException
	 */
	public ASTNodeValue compile(NamedSBaseWithDerivedUnit variable)
			throws SBMLException;

	/**
	 * A compiler will also have to deal with a name. The meaning of this can be
	 * various. For instance, the name may refer to a {@link Species} in the
	 * system. In case of numerical computation, the {@link ASTNodeCompiler}
	 * must create an {@link ASTNodeValue} representing the current value of
	 * this {@link Species}.
	 * 
	 * @param name
	 * @return
	 */
	public ASTNodeValue compile(String name);

	/**
	 * 
	 * @param value
	 * @return
	 * @throws SBMLException
	 */
	public ASTNodeValue cos(ASTNode value) throws SBMLException;

	/**
	 * 
	 * @param value
	 * @return
	 * @throws SBMLException
	 */
	public ASTNodeValue cosh(ASTNode value) throws SBMLException;

	/**
	 * 
	 * @param value
	 * @return
	 * @throws SBMLException
	 */
	public ASTNodeValue cot(ASTNode value) throws SBMLException;

	/**
	 * 
	 * @param value
	 * @return
	 * @throws SBMLException
	 */
	public ASTNodeValue coth(ASTNode value) throws SBMLException;

	/**
	 * 
	 * @param value
	 * @return
	 * @throws SBMLException
	 */
	public ASTNodeValue csc(ASTNode value) throws SBMLException;

	/**
	 * 
	 * @param value
	 * @return
	 * @throws SBMLException
	 */
	public ASTNodeValue csch(ASTNode value) throws SBMLException;

	/**
	 * Evaluate delay functions.
	 * 
	 * @param delayName
	 *            the name of this delay function.
	 * @param x
	 * @param d
	 * @param timeUnits
	 *            the units for the delay.
	 * @return
	 * @throws SBMLException
	 */
	public ASTNodeValue delay(String delayName, ASTNode x, double d,
			String timeUnits) throws SBMLException;

	/**
	 * Equal.
	 * 
	 * @param left
	 * @param right
	 * @return
	 * @throws SBMLException
	 */
	public ASTNodeValue eq(ASTNode left, ASTNode right) throws SBMLException;

	/**
	 * 
	 * @param value
	 * @return
	 * @throws SBMLException
	 */
	public ASTNodeValue exp(ASTNode value) throws SBMLException;

	/**
	 * 
	 * @param value
	 * @return
	 * @throws SBMLException
	 */
	public ASTNodeValue factorial(ASTNode value) throws SBMLException;

	/**
	 * 
	 * @param value
	 * @return
	 * @throws SBMLException
	 */
	public ASTNodeValue floor(ASTNode value) throws SBMLException;

	/**
	 * Fraction of two {@link ASTNode}s
	 * 
	 * @param numerator
	 * @param denominator
	 * @return
	 * @throws SBMLException
	 */
	public ASTNodeValue frac(ASTNode numerator, ASTNode denominator)
			throws SBMLException;

	/**
	 * A fraction of two int values.
	 * 
	 * @param numerator
	 * @param denominator
	 * @return
	 */
	public ASTNodeValue frac(int numerator, int denominator)
			throws SBMLException;

	/**
	 * 
	 * @param functionDefinition
	 * @param args
	 *            Values to be inserted into the parameter list of the
	 *            function's body.
	 * @return
	 * @throws SBMLException
	 */
	public ASTNodeValue function(FunctionDefinition functionDefinition,
			List<ASTNode> args) throws SBMLException;

	/**
	 * Greater equal.
	 * 
	 * @param left
	 * @param right
	 * @return
	 * @throws SBMLException
	 */
	public ASTNodeValue geq(ASTNode left, ASTNode right) throws SBMLException;

	/**
	 * Creates an {@link ASTNodeValue} that represent's Avogadro's number.
	 * Optionally, the compiler may associate the given name with this number.
	 * 
	 * @param name
	 *            An optional name for Avogadro's number.
	 * @return
	 */
	public ASTNodeValue getConstantAvogadro(String name);

	/**
	 * 
	 * @return
	 */
	public ASTNodeValue getConstantE();

	/**
	 * 
	 * @return
	 */
	public ASTNodeValue getConstantFalse();

	/**
	 * 
	 * @return
	 */
	public ASTNodeValue getConstantPi();

	/**
	 * 
	 * @return
	 */
	public ASTNodeValue getConstantTrue();

	/**
	 * 
	 * @return
	 * @throws SBMLException
	 */
	public ASTNodeValue getNegativeInfinity() throws SBMLException;

	/**
	 * 
	 * @return
	 */
	public ASTNodeValue getPositiveInfinity();

	/**
	 * Greater than.
	 * 
	 * @param left
	 * @param right
	 * @return
	 * @throws SBMLException
	 */
	public ASTNodeValue gt(ASTNode left, ASTNode right) throws SBMLException;

	/**
	 * The body of a {@link FunctionDefinition}.
	 * 
	 * @param values
	 *            Place holders for arguments.
	 * @return
	 * @throws SBMLException
	 */
	public ASTNodeValue lambda(List<ASTNode> values) throws SBMLException;

	/**
	 * Less equal.
	 * 
	 * @param left
	 * @param right
	 * @return
	 * @throws SBMLException
	 */
	public ASTNodeValue leq(ASTNode left, ASTNode right) throws SBMLException;

	/**
	 * Natural logarithm.
	 * 
	 * @param value
	 * @return
	 * @throws SBMLException
	 */
	public ASTNodeValue ln(ASTNode value) throws SBMLException;

	/**
	 * Logarithm of the given value to base 10.
	 * 
	 * @param value
	 * @return
	 * @throws SBMLException
	 */
	public ASTNodeValue log(ASTNode value) throws SBMLException;

	/**
	 * Logarithm of the given value to the given base.
	 * 
	 * @param base
	 * @param value
	 * @return
	 * @throws SBMLException
	 */
	public ASTNodeValue log(ASTNode base, ASTNode value) throws SBMLException;

	/**
	 * Less than.
	 * 
	 * @param left
	 * @param right
	 * @return
	 * @throws SBMLException
	 */
	public ASTNodeValue lt(ASTNode left, ASTNode right) throws SBMLException;

	/**
	 * Subtraction.
	 * 
	 * @param values
	 * @return
	 * @throws SBMLException
	 */
	public ASTNodeValue minus(List<ASTNode> values) throws SBMLException;

	/**
	 * Not equal.
	 * 
	 * @param left
	 * @param right
	 * @return
	 * @throws SBMLException
	 */
	public ASTNodeValue neq(ASTNode left, ASTNode right) throws SBMLException;

	/**
	 * 
	 * @param value
	 *            This value must be interpretable as a {@link Boolean}.
	 * @return
	 * @throws SBMLException
	 */
	public ASTNodeValue not(ASTNode value) throws SBMLException;

	/**
	 * 
	 * @param values
	 *            These values must be interpretable as a {@link Boolean}.
	 * @return
	 * @throws SBMLException
	 */
	public ASTNodeValue or(List<ASTNode> values) throws SBMLException;

	/**
	 * 
	 * @param values
	 * @return
	 * @throws SBMLException
	 */
	public ASTNodeValue piecewise(List<ASTNode> values) throws SBMLException;

	/**
	 * 
	 * @param values
	 * @return
	 * @throws SBMLException
	 */
	public ASTNodeValue plus(List<ASTNode> values) throws SBMLException;

	/**
	 * 
	 * @param base
	 * @param exponent
	 * @return
	 * @throws SBMLException
	 */
	public ASTNodeValue pow(ASTNode base, ASTNode exponent)
			throws SBMLException;

	/**
	 * 
	 * @param rootExponent
	 * @param radiant
	 * @return
	 * @throws SBMLException
	 */
	public ASTNodeValue root(ASTNode rootExponent, ASTNode radiant)
			throws SBMLException;

	/**
	 * 
	 * @param rootExponent
	 * @param radiant
	 * @return
	 * @throws SBMLException
	 */
	public ASTNodeValue root(double rootExponent, ASTNode radiant)
			throws SBMLException;

	/**
	 * 
	 * @param value
	 * @return
	 * @throws SBMLException
	 */
	public ASTNodeValue sec(ASTNode value) throws SBMLException;

	/**
	 * 
	 * @param value
	 * @return
	 * @throws SBMLException
	 */
	public ASTNodeValue sech(ASTNode value) throws SBMLException;

	/**
	 * 
	 * @param value
	 * @return
	 * @throws SBMLException
	 */
	public ASTNodeValue sin(ASTNode value) throws SBMLException;

	/**
	 * 
	 * @param value
	 * @return
	 * @throws SBMLException
	 */
	public ASTNodeValue sinh(ASTNode value) throws SBMLException;

	/**
	 * Square root.
	 * 
	 * @param radiant
	 *            This value must be interpretable as a {@link Number}.
	 * @return
	 * @throws SBMLException
	 */
	public ASTNodeValue sqrt(ASTNode radiant) throws SBMLException;

	/**
	 * The simulation time.
	 * 
	 * @param time
	 *            The name of the time symbol.
	 * @return An {@link ASTNodeValue} that represents the current time.
	 */
	public ASTNodeValue symbolTime(String time);

	/**
	 * 
	 * @param value
	 * @return
	 * @throws SBMLException
	 */
	public ASTNodeValue tan(ASTNode value) throws SBMLException;

	/**
	 * 
	 * @param value
	 * @return
	 * @throws SBMLException
	 */
	public ASTNodeValue tanh(ASTNode value) throws SBMLException;

	/**
	 * Product of all given {@link ASTNode}s.
	 * 
	 * @param values
	 *            These values must be interpretable to {@link Number}.
	 * @return
	 * @throws SBMLException
	 */
	public ASTNodeValue times(List<ASTNode> values) throws SBMLException;

	/**
	 * Unary minus, i.e., negation of the given {@link ASTNode}.
	 * 
	 * @param value
	 * @return
	 * @throws SBMLException
	 */
	public ASTNodeValue uMinus(ASTNode value) throws SBMLException;

	/**
	 * Dealing with a malformed {@link ASTNode}.
	 * 
	 * @return
	 * @throws SBMLException
	 */
	public ASTNodeValue unknownValue() throws SBMLException;

	/**
	 * Exclusive or.
	 * 
	 * @param values
	 *            It must be possible to evaluate the given values to
	 *            {@link Boolean}.
	 * @return
	 * @throws SBMLException
	 */
	public ASTNodeValue xor(List<ASTNode> values) throws SBMLException;
}
