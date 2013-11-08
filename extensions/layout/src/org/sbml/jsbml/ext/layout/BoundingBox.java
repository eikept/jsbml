/*
 * $Id$
 * $URL$
 * ----------------------------------------------------------------------------
 * This file is part of JSBML. Please visit <http://sbml.org/Software/JSBML>
 * for the latest version of JSBML and more information about SBML.
 *
 * Copyright (C) 2009-2013 jointly by the following organizations:
 * 1. The University of Tuebingen, Germany
 * 2. EMBL European Bioinformatics Institute (EBML-EBI), Hinxton, UK
 * 3. The California Institute of Technology, Pasadena, CA, USA
 * 4. The University of California, San Diego, La Jolla, CA, USA
 * 
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation. A copy of the license agreement is provided
 * in the file named "LICENSE.txt" included with this software distribution
 * and also available online as <http://sbml.org/Software/JSBML/License>.
 * ----------------------------------------------------------------------------
 */
package org.sbml.jsbml.ext.layout;

import java.text.MessageFormat;
import java.util.Map;

import javax.swing.tree.TreeNode;

import org.apache.log4j.Logger;
import org.sbml.jsbml.AbstractNamedSBase;
import org.sbml.jsbml.UniqueNamedSBase;
import org.sbml.jsbml.util.ResourceManager;

/**
 * 
 * @author Nicolas Rodriguez
 * @author Sebastian Fr&ouml;lich
 * @author Andreas Dr&auml;ger
 * @author Clemens Wrzodek
 * @since 1.0
 * @version $Rev$
 */
public class BoundingBox extends AbstractNamedSBase implements UniqueNamedSBase {

	/**
	 * Generated serial version identifier.
	 */
	private static final long serialVersionUID = -6371039558611201798L;
  
  /**
   * A {@link Logger} for this class.
   */
  private static final Logger logger = Logger.getLogger(BoundingBox.class);

	/**
	 * 
	 */
	private Dimensions dimensions;

	/**
	 * 
	 */
	private Point position;

	/**
	 * 
	 */
	public BoundingBox() {
		super();
		init();
	}

	/**
	 * 
	 * @param boundingBox
	 */
	public BoundingBox(BoundingBox boundingBox) {
		super(boundingBox);
		if (boundingBox.isSetDimensions()) {
			this.dimensions = boundingBox.getDimensions().clone();
		}
		if (boundingBox.isSetPosition()) {
			this.position = boundingBox.getPosition().clone();
		}
	}

	/**
	 * 
	 * @param level
	 * @param version
	 */
	public BoundingBox(int level, int version) {
		super(level, version);
		init();
	}

	/**
	 * 
	 * @param id
	 */
	public BoundingBox(String id) {
		super(id);
		init();
	}

	/**
	 * 
	 * @param id
	 * @param level
	 * @param version
	 */
	public BoundingBox(String id, int level, int version) {
		super(id, level, version);
		init();
	}

	/* (non-Javadoc)
	 * @see org.sbml.jsbml.AbstractSBase#clone()
	 */
	@Override
	public BoundingBox clone() {
		return new BoundingBox(this);
	}

	/**
	 * Creates, sets and returns a {@link Dimensions}
	 *	
	 * @return new {@link Dimensions} object.
	 */
	public Dimensions createDimensions() {
		Dimensions d = new Dimensions();
		setDimensions(d);
		return d;
	}

	/**
	 * Creates, sets and returns {@link Dimensions} based on the
	 * given values.
	 * @param width
	 * @param height
	 * @param depth
	 * @return new {@link Dimensions} object.
	 */
	public Dimensions createDimensions(double width, double height, double depth) {
		Dimensions d = new Dimensions();
		d.setWidth(width);
		d.setHeight(height);
		d.setDepth(depth);
		setDimensions(d);
		return d;
	}

	/**
	 * Creates, sets and returns a {@link Position}
	 * 
	 * @return new {@link Position} object.
	 */
	public Position createPosition() {
		Position p = new Position();
		setPosition(p);
		return p;
	}

	/**
	 * Creates, sets and returns a {@link Position} based on the
	 * given values.
	 * @param x
	 * @param y
	 * @param z
	 * @return new {@link Position} object.
	 */
	public Position createPosition(double x, double y, double z) {
		Position p = new Position();
		p.setX(x);
		p.setY(y);
		p.setZ(z);
		setPosition(p);
		return p;
	}

	/* (non-Javadoc)
	 * @see org.sbml.jsbml.AbstractNamedSBase#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object object) {		
		// Check all child elements recursively in super class first:
		boolean equals = super.equals(object);

		if (equals) {
			// Cast is possible because super class checks the class attributes
			BoundingBox bb = (BoundingBox) object;

			// compare position and dimensions 
			equals &= bb.isSetPosition() == isSetPosition();
			if (equals && isSetPosition()) {
				equals &=  bb.getPosition().equals(getPosition());
			}

			equals &= bb.isSetDimensions() == isSetDimensions();
			if (equals && isSetDimensions()) {
				equals &=  bb.getDimensions().equals(getDimensions());
			}

		}
		return equals;
	}

	/* (non-Javadoc)
	 * @see org.sbml.jsbml.AbstractSBase#getChildAt(int)
	 */
	@Override
	public TreeNode getChildAt(int index) {
		if (index < 0) {
			throw new IndexOutOfBoundsException(Integer.toString(index));
		}
		int count = super.getChildCount(), pos = 0;
		if (index < count) {
			return super.getChildAt(index);
		} else {
			index -= count;
		}
		if (isSetPosition()) {
			if (pos == index) {
				return getPosition();
			}
			pos++;
		}
		if (isSetDimensions()) {
			if (pos == index) {
				return getDimensions();
			}
			pos++;
		}
		throw new IndexOutOfBoundsException(MessageFormat.format(
				"Index {0,number,integer} >= {1,number,integer}",
				index, +((int) Math.min(pos, 0))));
	}

	/* (non-Javadoc)
	 * @see org.sbml.jsbml.AbstractSBase#getChildCount()
	 */
	@Override
	public int getChildCount() {
		int count = super.getChildCount();
		if (isSetPosition()) {
			count++;
		}
		if (isSetDimensions()) {
			count++;
		}
		return count;
	}

	/**
	 * 
	 * @return
	 */
	public Dimensions getDimensions() {
		return dimensions;
	}

	/**
	 * 
	 * @return
	 */
	public Point getPosition() {
		return position;
	}

	/* (non-Javadoc)
	 * @see org.sbml.jsbml.AbstractNamedSBase#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 7;
		int hashCode = super.hashCode();
		hashCode += prime * getId().hashCode();
		return hashCode;
	}

	/**
	 * 
	 */
	private void init() {
		addNamespace(LayoutConstants.namespaceURI);
	}

	/* (non-Javadoc)
	 * @see org.sbml.jsbml.NamedSBase#isIdMandatory()
	 */
	public boolean isIdMandatory() {
		return false;
	}

	/**
	 * @return
	 */
	public boolean isSetDimensions() {
		return dimensions != null;
	}

	/**
	 * @return
	 */
	public boolean isSetPosition() {
		return position != null;
	}

	/**
	 * 
	 * @param dimensions
	 */
	public void setDimensions(Dimensions dimensions) {
		if (this.dimensions != null) {
			Dimensions oldValue = this.dimensions;
			this.dimensions = null;
			oldValue.fireNodeRemovedEvent();
		}
		this.dimensions = dimensions;
		registerChild(this.dimensions);
	}


	/**
	 * 
	 * @param point
	 */
	public void setPosition(Point point) {
		Point oldValue = this.position;
		this.position = point;
		if (oldValue != null) {
			oldValue.fireNodeRemovedEvent();
		}
		if (this.position != null) {
			if (! (this.position instanceof Position)) {
				this.position = new Position(this.position);
			}
			this.position.fireNodeAddedEvent();
		}
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuffer builder = new StringBuffer();
		builder.append("BoundingBox [");
		if (isSetId()) {
			builder.append("id=");
			builder.append(getId());
		}
		if (isSetName()) {
			if (isSetId()) {
				builder.append(", ");
			}
			builder.append("name=");
			builder.append(getName());
			if (isSetPosition() || isSetDimensions()) {
				builder.append(", ");
			}
		}
		if (isSetPosition()) {
			builder.append(position);
			if (isSetDimensions()) {
				builder.append(", ");
			}
		}
		if (isSetDimensions()) {
			builder.append(dimensions);
		}
		builder.append("]");
		return builder.toString();
	}

	/* (non-Javadoc)
	 * @see org.sbml.jsbml.AbstractNamedSBase#writeXMLAttributes()
	 */
	@Override
	public Map<String, String> writeXMLAttributes() {
		Map<String, String> attributes = super.writeXMLAttributes();

		if (isSetId()) {
			attributes.remove("id");
			attributes.put(LayoutConstants.shortLabel + ":id", getId());
		}
		if (isSetName()) {
		  attributes.remove("name");
		  logger.warn(MessageFormat.format(
		    ResourceManager.getBundle("org.sbml.jsbml.resources.cfg.Messages").getString("UNDEFINED_ATTRIBUTE"),
		    "name", getLevel(), getVersion(), getElementName()));
		  // TODO: This must be generally solved. Here we have an SBase with ID but without name!
		}

		return attributes;
	}

}
