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

package org.sbml.jsbml;

import java.util.HashMap;

import org.sbml.jsbml.Unit.Kind;
import org.sbml.jsbml.util.StringTools;

/**
 * Represents the unitDefinition XML element of a SBML file.
 * 
 * @author Andreas Dr&auml;ger
 * @author marine
 * 
 * @opt attributes
 * @opt types
 * @opt visibility
 * @composed 1..* ListOf 1 Unit
 */
public class UnitDefinition extends AbstractNamedSBase {

	/**
	 * Predefined unit for area.
	 */
	public static final UnitDefinition area(int level, int version) {
		return getPredefinedUnit("area", level, version);
	}

	/**
	 * <p>
	 * Predicate returning true or false depending on whether two UnitDefinition
	 * objects are equivalent.
	 * </p>
	 * <p>
	 * For the purposes of performing this comparison, two UnitDefinition
	 * objects are considered equivalent when they contain equivalent list of
	 * Unit objects. Unit objects are in turn considered equivalent if they
	 * satisfy the predicate Unit.areEquivalent(). The predicate tests a subset
	 * of the objects's attributes.
	 * </p>
	 * 
	 * @param ud1
	 *            the first UnitDefinition object to compare
	 * @param ud2
	 *            the second UnitDefinition object to compare
	 * @return true if all the Unit objects in ud1 are equivalent to the Unit
	 *         objects in ud2, false otherwise.
	 * @see areIdentical
	 * @see Unit.areEquivalent
	 */
	public static boolean areEquivalent(UnitDefinition ud1, UnitDefinition ud2) {
		UnitDefinition ud1clone = ud1.clone().simplify();
		UnitDefinition ud2clone = ud2.clone().simplify();
		if (ud1clone.getNumUnits() == ud2clone.getNumUnits()) {
			boolean equivalent = true;
			for (int i = 0; i < ud1clone.getNumUnits(); i++)
				equivalent &= Unit.areEquivalent(ud1clone.getUnit(i), ud2clone
						.getUnit(i));
			return equivalent;
		}
		return false;
	}

	/**
	 * <p>
	 * Predicate returning true or false depending on whether two UnitDefinition
	 * objects are identical.
	 * </p>
	 * <p>
	 * For the purposes of performing this comparison, two UnitDefinition
	 * objects are considered identical when they contain identical lists of
	 * Unit objects. Pairs of Unit objects in the lists are in turn considered
	 * identical if they satisfy the predicate Unit.areIdentical(). The
	 * predicate compares every attribute of the Unit objects.
	 * </p>
	 * 
	 * @param ud1
	 *            the first UnitDefinition object to compare
	 * @param ud2
	 *            the second UnitDefinition object to compare
	 * @return true if all the Unit objects in ud1 are identical to the Unit
	 *         objects of ud2, false otherwise.
	 */
	public static boolean areIdentical(UnitDefinition ud1, UnitDefinition ud2) {
		UnitDefinition ud1clone = ud1.clone().simplify();
		UnitDefinition ud2clone = ud2.clone().simplify();
		if (ud1clone.getNumUnits() == ud2clone.getNumUnits()) {
			boolean identical = true;
			for (int i = 0; i < ud1clone.getNumUnits(); i++) {
				identical &= Unit.areIdentical(ud1clone.getUnit(i), ud2clone
						.getUnit(i));
			}
			return identical;
		}
		return false;
	}

	/**
	 * 
	 * @param id
	 * @param level
	 * @param version
	 * @return
	 */
	private static final UnitDefinition getPredefinedUnit(String id, int level,
			int version) {
		id = id.toLowerCase();
		Unit u = new Unit(level, version);
		UnitDefinition ud = new UnitDefinition(id, level, version);
		if (id.equals("substance")) {
			u.setKind(Unit.Kind.MOLE);
		} else if (id.equals("volume")) {
			u.setKind(Unit.Kind.LITRE);
		} else if (id.equals("area")) {
			u.setKind(Unit.Kind.METRE);
			u.setExponent(2);
		} else if (id.equals("length")) {
			u.setKind(Unit.Kind.METRE);
		} else if (id.equals("time")) {
			u.setKind(Unit.Kind.SECOND);
			u.setSBOTerm(345);
			ud.setSBOTerm(345);
		} else {
			throw new IllegalArgumentException(
					"no predefined unit available for " + id);
		}
		ud.setName("Predefined unit " + id);
		ud.addUnit(u);
		return ud;
	}

	/**
	 * Predefined unit for length.
	 */
	public static final UnitDefinition length(int level, int version) {
		return getPredefinedUnit("length", level, version);
	}

	/**
	 * Returns a string that expresses the unit definition represented by this
	 * UnitDefinition object.
	 * 
	 * @param ud
	 *            the UnitDefinition object
	 * @return a string expressing the unit definition
	 */
	public static String printUnits(UnitDefinition ud) {
		return printUnits(ud, false);
	}

	/**
	 * Returns a string that expresses the unit definition represented by this
	 * UnitDefinition object.
	 * 
	 * @param ud
	 *            the UnitDefinition object
	 * @param compact
	 *            boolean indicating whether the compact form should be used
	 *            (defaults to false)
	 * @return a string expressing the unit definition
	 */
	public static String printUnits(UnitDefinition ud, boolean compact) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < ud.getNumUnits(); i++) {
			Unit unit = ud.getUnit(i);
			if (i > 0) {
				sb.append(' ');
			}
			if (compact) {
				sb.append(unit.toString());
			} else {
				sb.append(unit.getKind().getName().toLowerCase());
				sb.append(String.format(
						" (exponent = %d, multiplier = %s, scale = %d)", unit
								.getExponent(), StringTools.toString(unit
								.getMultiplier()), unit.getScale()));
			}
		}
		return sb.toString();
	}

	/**
	 * Orders alphabetically the Unit objects within the ListOfUnits of a
	 * UnitDefinition.
	 * 
	 * @param ud
	 *            the UnitDefinition object whose units are to be reordered.
	 */
	public static void reorder(UnitDefinition ud) {
		ListOf<Unit> units = new ListOf<Unit>(ud.getLevel(), ud.getVersion());
		ListOf<Unit> orig = ud.getListOfUnits();
		int i, j;
		for (i = orig.size() - 1; i >= 0; i--) {
			Unit u = orig.remove(i);
			j = 0;
			while (0 < u.getKind().compareTo(units.get(j).getKind()))
				j--;
			units.add(Math.max(0, j - 1), u);
		}
		ud.setListOfUnits(units);
	}

	/**
	 * Predefined unit for substance.
	 */
	public static final UnitDefinition substance(int level, int version) {
		return getPredefinedUnit("substance", level, version);
	}

	/**
	 * Predefined unit for time.
	 */
	public static final UnitDefinition time(int level, int version) {
		return getPredefinedUnit("time", level, version);
	}

	/**
	 * Predefined unit for volume.
	 */
	public static final UnitDefinition volume(int level, int version) {
		return getPredefinedUnit("volume", level, version);
	}

	/**
	 * Represents the 'listOfUnit' XML subelement of a UnitDefinition.
	 */
	private ListOf<Unit> listOfUnits;

	/**
	 * Creates an UnitDefinition instance. By default, the listOfUnit is null.
	 */
	public UnitDefinition() {
		super();
		this.listOfUnits = null;
		if (isSetLevel() && getLevel() < 3) {
			initDefaults();
		}
	}

	public UnitDefinition(int level, int version) {
		super(level, version);
	}

	/**
	 * Creates an UnitDefinition instance from an id, level and version. By
	 * default, the listOfUnit is null.
	 * 
	 * @param id
	 * @param level
	 * @param version
	 */
	public UnitDefinition(String id, int level, int version) {
		super(id, level, version);
		listOfUnits = null;
		if (isSetLevel() && getLevel() < 3) {
			initDefaults();
		}
	}

	/**
	 * Creates an UnitDefinition instance from an id, level and version. By
	 * default, the listOfUnit is null.
	 * 
	 * @param id
	 * @param name
	 * @param level
	 * @param version
	 */
	public UnitDefinition(String id, String name, int level, int version) {
		super(id, name, level, version);
		listOfUnits = null;
		if (isSetLevel() && getLevel() < 3) {
			initDefaults();
		}
	}

	/**
	 * Creates an UnitDefinition instance from a given UnitDefinition.
	 * 
	 * @param nsb
	 */
	public UnitDefinition(UnitDefinition nsb) {
		super(nsb);
		if (nsb.isSetListOfUnits()) {
			setListOfUnits((ListOf<Unit>) nsb.getListOfUnits().clone());
		} else {
			listOfUnits = null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.sbml.jsbml.AbstractSBase#addChangeListener(org.sbml.jsbml.
	 * SBaseChangedListener)
	 */
	@Override
	public void addChangeListener(SBaseChangedListener l) {
		super.addChangeListener(l);
		if (!isSetListOfUnits())
			listOfUnits = new ListOf<Unit>(getLevel(), getVersion());
		listOfUnits.addChangeListener(l);
	}

	/**
	 * Adds an Unit to this UnitDefinition.
	 * 
	 * @param u
	 */
	public void addUnit(Unit u) {
		if (!isSetListOfUnits()) {
			this.listOfUnits = new ListOf<Unit>();
			setThisAsParentSBMLObject(this.listOfUnits);
		}
		setThisAsParentSBMLObject(u);
		listOfUnits.add(u);
		u.parentSBMLObject = this;
		stateChanged();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.sbml.jsbml.element.AbstractSBase#clone()
	 */
	@Override
	public UnitDefinition clone() {
		return new UnitDefinition(this);
	}

	public Unit createUnit() {
		Unit unit = new Unit(level, version);
		addUnit(unit);

		return unit;
	}

	/**
	 * Devides this unit definition by the second unit definition.
	 * 
	 * @param definition
	 */
	public UnitDefinition divideBy(UnitDefinition definition) {
		if (definition.isSetListOfUnits()) {
			for (Unit unit1 : definition.getListOfUnits()) {
				Unit unit = unit1.clone();
				unit.setExponent(unit1.getExponent() * -1);
				addUnit(unit);
			}
		}
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.sbml.jsbml.element.SBase#equals(Object o)
	 */
	public boolean equals(Object o) {
		if (o instanceof UnitDefinition) {
			UnitDefinition u = (UnitDefinition) o;
			boolean equal = super.equals(o);
			equal &= isSetListOfUnits() == u.isSetListOfUnits();
			if (equal && isSetListOfUnits()) {
				equal &= getListOfUnits().equals(u.getListOfUnits());
			}
			return equal;
		}
		return false;
	}

	/**
	 * 
	 * @return the listOfUnits of this UnitDefinition. Can be null if it is not
	 *         set.
	 */
	public ListOf<Unit> getListOfUnits() {
		return listOfUnits;
	}

	/**
	 * 
	 * @return the number of Unit.
	 */
	public int getNumUnits() {
		if (isSetListOfUnits()) {
			return listOfUnits.size();
		}
		return 0;
	}

	/**
	 * Returns a specific Unit instance belonging to this UnitDefinition.
	 * 
	 * @param i
	 *            an integer, the index of the Unit to be returned.
	 * @return the ith Unit of this UnitDefinition
	 */
	public Unit getUnit(int i) {
		if (isSetListOfUnits()) {
			return listOfUnits.get(i);
		}
		return null;
	}

	/**
	 * Initialises the default values of this UnitDefinition.
	 */
	public void initDefaults() {
		if (!isSetListOfUnits()) {
			this.listOfUnits = new ListOf<Unit>(getLevel(), getVersion());
			setThisAsParentSBMLObject(listOfUnits);
		}
	}

	/**
	 * 
	 * @return true if the listOfUnits is not null.
	 */
	public boolean isSetListOfUnits() {
		return this.listOfUnits != null;
	}

	/**
	 * 
	 * @return true if this UnitDefinition is a variant of Area
	 */
	public boolean isVariantOfArea() {
		if (isSetListOfUnits()) {
			if (listOfUnits.size() == 1) {
				Unit unit = listOfUnits.get(0);
				return unit.isVariantOfArea();
			}
		}
		return false;
	}

	/**
	 * Convenience function for testing if a given unit definition is a variant
	 * of the predefined unit identifier 'length'.
	 * 
	 * @param two
	 * @return true if this UnitDefinition is a variant of the predefined unit
	 *         length, meaning metres with only abritrary variations in scale or
	 *         multiplier values; false otherwise.
	 */
	public boolean isVariantOfLength() {
		if (isSetListOfUnits()) {
			if (listOfUnits.size() == 1) {
				Unit unit = listOfUnits.get(0);
				return unit.isVariantOfLength();
			}
		}
		return false;
	}

	/**
	 * Convenience function for testing if a given unit definition is a variant
	 * of the predefined unit identifier 'substance'.
	 * 
	 * @return true if this UnitDefinition is a variant of the predefined unit
	 *         substance, meaning moles or items (and grams or kilograms from
	 *         SBML Level 2 Version 2 onwards) with only abritrary variations in
	 *         scale or multiplier values; false otherwise.
	 */
	public boolean isVariantOfSubstance() {
		if (isSetListOfUnits()) {
			if (listOfUnits.size() == 1) {
				Unit unit = listOfUnits.get(0);
				return unit.isVariantOfSubstance();
			}
		}
		return false;
	}

	/**
	 * 
	 * @return
	 */
	public boolean isVariantOfSubstancePerArea() {
		if (isSetListOfUnits()) {
			if (listOfUnits.size() == 2) {
				;
				if (getUnit(0).isVariantOfSubstance()) {
					Unit two = getUnit(1).clone();
					two.setExponent(two.getExponent() * -1);
					return two.isVariantOfArea();
				} else if (getUnit(1).isVariantOfSubstance()) {
					Unit one = getUnit(0).clone();
					one.setExponent(one.getExponent() * -1);
					return one.isVariantOfArea();
				}
			}
		}
		return false;
	}

	/**
	 * 
	 * @return true if this UnitDefinition is a variant of substance per length.
	 */
	public boolean isVariantOfSubstancePerLength() {
		if (isSetListOfUnits()) {
			if (listOfUnits.size() == 2) {
				Unit unit = listOfUnits.get(0);
				Unit unit2 = listOfUnits.get(1);
				if (unit.isVariantOfSubstance()) {
					Unit two = listOfUnits.get(1).clone();
					two.setExponent(two.getExponent() * -1);
					return two.isVariantOfLength();
				} else if (unit2.isVariantOfSubstance()) {
					Unit one = listOfUnits.get(0).clone();
					one.setExponent(one.getExponent() * -1);
					return one.isVariantOfLength();
				}
			}
		}
		return false;
	}

	/**
	 * 
	 * @return true if this UnitDefinition is a variant of substance per volume.
	 */
	public boolean isVariantOfSubstancePerVolume() {
		if (isSetListOfUnits()) {
			if (listOfUnits.size() == 2) {
				Unit unit = listOfUnits.get(0);
				Unit unit2 = listOfUnits.get(1);
				if (unit.isVariantOfSubstance()) {
					Unit two = listOfUnits.get(1).clone();
					two.setExponent(two.getExponent() * -1);
					return two.isVariantOfVolume();
				} else if (unit2.isVariantOfSubstance()) {
					Unit one = listOfUnits.get(0).clone();
					one.setExponent(one.getExponent() * -1);
					return one.isVariantOfVolume();
				}
			}
		}
		return false;
	}

	/**
	 * Convenience function for testing if a given unit definition is a variant
	 * of the predefined unit identifier 'volume'.
	 * 
	 * @return true if this UnitDefinition is a variant of the predefined unit
	 *         volume, meaning litre or cubic metre with only abritrary
	 *         variations in scale or multiplier values; false otherwise.
	 */
	public boolean isVariantOfVolume() {
		if (isSetListOfUnits()) {
			if (listOfUnits.size() == 1) {
				Unit unit = listOfUnits.get(0);
				return unit.isVariantOfVolume();
			}
		}
		return false;
	}

	/**
	 * Multiplies this unit with the given unit definition, i.e., adds a clone
	 * of each unit object in the list of units of the given definition to the
	 * list of this unit.
	 * 
	 * @param definition
	 * @return
	 */
	public UnitDefinition multiplyWith(UnitDefinition definition) {
		if (isSetListOfUnits()) {
			for (Unit unit : definition.getListOfUnits()) {
				addUnit(unit.clone());
			}
		}
		return this;
	}

	/**
	 * Raises this unit definition by the power of the given exponent, i.e., the
	 * exponents of every unit contained by this unit definition are multiplied
	 * with the given exponent.
	 * 
	 * @param exponent
	 */
	public void raiseByThePowerOf(int exponent) {
		if (isSetListOfUnits()) {
			Unit u;
			for (int i = listOfUnits.size() - 1; i >= 0; i--) {
				u = listOfUnits.get(i);
				u.setExponent(u.getExponent() * exponent);
				if (u.getExponent() == 0) {
					listOfUnits.remove(i);
				}
			}
		}
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

		return isAttributeRead;
	}

	/**
	 * Removes the nth Unit object from this UnitDefinition object and returns a
	 * pointer to it.
	 * 
	 * The caller owns the returned object and is responsible for deleting it.
	 * 
	 * @param i
	 *            the index of the Unit object to remove
	 * @return the Unit object removed. As mentioned above, the caller owns the
	 *         returned item. NULL is returned if the given index is out of
	 *         range.
	 */
	public Unit removeUnit(int i) {
		if (isSetListOfUnits()) {
			Unit u = listOfUnits.remove(i);
			if (u != null) {
				u.sbaseRemoved();
			}
			return u;
		}
		return null;
	}

	/**
	 * Sets the listOfUnits of this UnitDefinition. Automatically sets the
	 * parentSBML object of the list to this UnitDefinition instance.
	 * 
	 * @param listOfUnits
	 */
	public void setListOfUnits(ListOf<Unit> listOfUnits) {
		this.listOfUnits = listOfUnits;
		setThisAsParentSBMLObject(this.listOfUnits);
		this.listOfUnits.setSBaseListType(ListOf.Type.listOfUnits);
		stateChanged();
	}

	/**
	 * Simplifies the UnitDefinition so that any Unit objects occurring within
	 * the ListOfUnits occurs only once.
	 * 
	 * @return a pointer to the simplified unit definition.
	 */
	public UnitDefinition simplify() {
		if (isSetListOfUnits()) {
			reorder(this);
			for (int i = getNumUnits() - 2; i >= 0; i--) {
				Unit u = getUnit(i); // current unit
				Unit s = getUnit(i + 1); // successor unit
				if (Unit.Kind.areEquivalent(u.getKind(), s.getKind())
						|| u.getKind() == Kind.DIMENSIONLESS
						|| s.getKind() == Kind.DIMENSIONLESS) {
					Unit.merge(u, removeUnit(i + 1));
					if (u.isDimensionless() && i == 0 && getNumUnits() > 1) {
						Unit.merge(getUnit(i + 1), removeUnit(i));
					}
				}
			}
		}
		return this;
	}

	/**
	 * Creates an HTML string representation of this UnitDefinition.
	 * 
	 * @return
	 */
	public String toHTML() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < getNumUnits(); i++) {
			Unit unit = getUnit(i);
			if (i > 0) {
				sb.append(" &#8901; ");
			}
			sb.append(unit.toHTML());
		}
		return sb.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.sbml.jsbml.element.SBase#writeXMLAttributes()
	 */
	@Override
	public HashMap<String, String> writeXMLAttributes() {
		HashMap<String, String> attributes = super.writeXMLAttributes();

		return attributes;
	}
}
