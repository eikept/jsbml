/*
 * $Id: MathContainer.java 366 2010-10-06 15:38:11Z andreas-draeger $
 * $URL: https://jsbml.svn.sourceforge.net/svnroot/jsbml/trunk/src/org/sbml/jsbml/MathContainer.java $
 *
 * 
 *==================================================================================
 * Copyright (c) 2009 The jsbml team.
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

package org.sbml.jsbml;

import java.util.Map;

import javax.swing.tree.TreeNode;

import org.sbml.jsbml.text.parser.ParseException;
import org.sbml.jsbml.util.StringTools;

/**
 * Base class for all the SBML components which contain MathML nodes.
 * 
 * @author Andreas Dr&auml;ger
 * @author marine
 */
public abstract class AbstractMathContainer extends AbstractSBase implements
		MathContainer {

	/**
	 * Generated serial version identifier.
	 */
	private static final long serialVersionUID = -6630349025482311163L;

	/**
	 * The math formula as an abstract syntax tree.
	 */
	private ASTNode math;

	/**
	 * Creates a MathContainer instance. By default, the math object is null.
	 */
	public AbstractMathContainer() {
		super();
		math = null;
	}

	/**
	 * Creates a MathContainer instance from a given MathContainer.
	 * 
	 * @param sb an <code>AbstractMathContainer</code> object to clone
	 */
	public AbstractMathContainer(AbstractMathContainer sb) {
		super(sb);
		if (sb.isSetMath()) {
			setMath(sb.getMath().clone());
		} else {
			this.math = null;
		}
	}

	/**
	 * Creates a MathContainer instance from an ASTNode, level and version. By
	 * default, the math is null.
	 * 
	 * @param math the ASTNode representing the math.
	 * @param level the SBML level
	 * @param version the SBML version
	 */
	public AbstractMathContainer(ASTNode math, int level, int version) {
		super(level, version);
		if (math != null) {
			setMath(math.clone());
		} else {
			this.math = null;
		}
	}

	/**
	 * Creates a MathContainer instance from a level and version. By default,
	 * the formula, math and mathBuffer are null.
	 * 
	 * @param level the SBML level
	 * @param version the SBML version
	 */
	public AbstractMathContainer(int level, int version) {
		super(level, version);
		math = null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.sbml.jsbml.AbstractSBase#clone()
	 */
	public abstract AbstractMathContainer clone();

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.sbml.jsbml.SBaseWithDerivedUnit#containsUndeclaredUnits()
	 */
	public boolean containsUndeclaredUnits() {
		return isSetMath() ? math.containsUndeclaredUnits() : false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.sbml.jsbml.AbstractSBase#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object o) {
		if (o.getClass().getName().equals(this.getClass().getName())) {
			AbstractMathContainer c = (AbstractMathContainer) o;
			if ((c.isSetMath() && !isSetMath())
					|| (!c.isSetMath() && isSetMath())) {
				return false;
			}
			boolean equal = super.equals(o);
			if (c.isSetMath() && isSetMath()) {
				equal &= getMath().equals(c.getMath());
			}
			return equal;
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.tree.TreeNode#getAllowsChildren()
	 */
	@Override
	public boolean getAllowsChildren() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.sbml.jsbml.AbstractSBase#getChildAt(int)
	 */
	@Override
	public TreeNode getChildAt(int index) {
		int children = getChildCount();
		if (index >= children) {
			throw new IndexOutOfBoundsException(index + " >= " + children);
		}
		int pos = 0;
		if (isSetMath()) {
			if (index == pos) {
				return getMath();
			}
			pos++;
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.tree.TreeNode#getChildCount()
	 */
	@Override
	public int getChildCount() {
		return isSetMath() ? 1 : 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.sbml.jsbml.SBaseWithDerivedUnit#getDerivedUnitDefinition()
	 */
	public UnitDefinition getDerivedUnitDefinition() {
		UnitDefinition ud = null;
		if (isSetMath()) {
			try {
				ud = math.deriveUnit();
			} catch (SBMLException e) {
				// Doesn't matter. We'll simply return an undefined unit.
			}
		}
		if (ud != null) {
			Model m = getModel();
			if (m != null)
				for (UnitDefinition u : m.getListOfUnitDefinitions()) {
					if (UnitDefinition.areEquivalent(u, ud)) {
						ud = u;
						break;
					}
				}
		} else {
			ud = new UnitDefinition(getLevel(), getVersion());
			ud.addUnit(ud.createUnit());
		}
		return ud;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.sbml.jsbml.SBaseWithDerivedUnit#getDerivedUnits()
	 */
	@SuppressWarnings("deprecation")
	public String getDerivedUnits() {
		UnitDefinition ud = getDerivedUnitDefinition();
		Model m = getModel();
		if (m != null) {
			if (m.getUnitDefinition(ud.getId()) != null)
				return ud.getId();
		}
		if (ud.getNumUnits() == 1) {
			Unit u = ud.getUnit(0);
			if (u.getOffset() == 0 && u.getMultiplier() == 1
					&& u.getScale() == 0 && u.getExponent() == 1)
				return u.getKind().toString();
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.sbml.jsbml.MathContainer#getFormula()
	 */
	@Deprecated
	public String getFormula() {
		try {
			return isSetMath() ? getMath().toFormula() : "";
		} catch (SBMLException e) {
			return "invalid";
		}
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.sbml.jsbml.MathContainer#getMath()
	 */
	public ASTNode getMath() {
		return math;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.sbml.jsbml.MathContainer#getMathMLString()
	 */
	public String getMathMLString() {
		if (isSetMath()) {
			return math.toMathML();
		}
		return "";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.sbml.jsbml.MathContainer#isSetMath()
	 */
	public boolean isSetMath() {
		return math != null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.sbml.jsbml.element.SBase#readAttribute(String attributeName,
	 * String prefix, String value)
	 */
	@Override
	public boolean readAttribute(String attributeName, String prefix,
			String value) {
		boolean isAttributeRead = super.readAttribute(attributeName, prefix,
				value);

		if (!isAttributeRead) {
			if (attributeName.equals("formula")) {
				try {
					setFormula(value);
					return true;
				} catch (ParseException exc) {
					throw new IllegalArgumentException(exc);
				}
			}
		}
		return isAttributeRead;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.sbml.jsbml.MathContainer#setFormula(java.lang.String)
	 */
	@Deprecated
	public void setFormula(String formula) throws ParseException {
		setMath(ASTNode.parseFormula(formula));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.sbml.jsbml.MathContainer#setMath(org.sbml.jsbml.ASTNode)
	 */
	public void setMath(ASTNode math) {
		ASTNode oldMath = this.math;
		this.math = math;
		ASTNode.setParentSBMLObject(math, this);
		firePropertyChange(SBaseChangedEvent.math, oldMath, math);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.sbml.jsbml.AbstractSBase#toString()
	 */
	@Override
	public String toString() {
		if (isSetMath()) {
			try {
				return math.toString();
			} catch (Throwable exc) {
			}
		}
		return StringTools.firstLetterLowerCase(getElementName());
	}

	/*
	 * (non-Javadoc)
	 * @see org.sbml.jsbml.MathContainer#unsetFormula()
	 */
	@Deprecated
	public void unsetFormula() {
		unsetMath();
	}

	/*
	 * (non-Javadoc)
	 * @see org.sbml.jsbml.MathContainer#unsetMath()
	 */
	public void unsetMath() {
		setMath(null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.sbml.jsbml.element.SBase#writeXMLAttributes()
	 */
	@Override
	public Map<String, String> writeXMLAttributes() {
		Map<String, String> attributes = super.writeXMLAttributes();
		if (isSetMath() && isSetLevel() && (getLevel() < 2)) {
			attributes.put("formula", getFormula());
		}
		return attributes;
	}
}
