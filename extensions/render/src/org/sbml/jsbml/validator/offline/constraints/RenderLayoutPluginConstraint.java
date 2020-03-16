/*
 * ----------------------------------------------------------------------------
 * This file is part of JSBML. Please visit <http://sbml.org/Software/JSBML>
 * for the latest version of JSBML and more information about SBML.
 *
 * Copyright (C) 2009-2018 jointly by the following organizations:
 * 1. The University of Tuebingen, Germany
 * 2. EMBL European Bioinformatics Institute (EBML-EBI), Hinxton, UK
 * 3. The California Institute of Technology, Pasadena, CA, USA
 * 4. The University of California, San Diego, La Jolla, CA, USA
 * 5. The Babraham Institute, Cambridge, UK
 * 
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation. A copy of the license agreement is provided
 * in the file named "LICENSE.txt" included with this software distribution
 * and also available online as <http://sbml.org/Software/JSBML/License>.
 * ----------------------------------------------------------------------------
 */
package org.sbml.jsbml.validator.offline.constraints;

import java.util.Set;

import org.sbml.jsbml.JSBML;
import org.sbml.jsbml.ext.layout.Layout;
import org.sbml.jsbml.ext.render.ListOfLocalRenderInformation;
import org.sbml.jsbml.ext.render.RenderConstants;
import org.sbml.jsbml.ext.render.RenderLayoutPlugin;
import org.sbml.jsbml.validator.SBMLValidator.CHECK_CATEGORY;
import org.sbml.jsbml.validator.offline.ValidationContext;
import org.sbml.jsbml.validator.offline.constraints.helper.DuplicatedElementValidationFunction;
import org.sbml.jsbml.validator.offline.constraints.helper.UnknownCoreAttributeValidationFunction;
import org.sbml.jsbml.validator.offline.constraints.helper.UnknownElementValidationFunction;
import org.sbml.jsbml.validator.offline.constraints.helper.UnknownPackageAttributeValidationFunction;
import org.sbml.jsbml.validator.offline.constraints.helper.UnknownPackageElementValidationFunction;
import org.sbml.jsbml.xml.XMLNode;


/**
 * Defines validation rules (as {@link ValidationFunction} instances) for the
 * {@link RenderLayoutPlugin} class.
 * 
 * @author David Emanuel Vetter
 */
public class RenderLayoutPluginConstraint extends AbstractConstraintDeclaration {

  @Override
  public void addErrorCodesForCheck(Set<Integer> set, int level, int version,
    CHECK_CATEGORY category, ValidationContext context) {
    switch(category) {
    case GENERAL_CONSISTENCY:
      addRangeToSet(set, RENDER_20301, RENDER_20307);
      break;
    default:
      break;
    }
  }


  @Override
  public void addErrorCodesForAttribute(Set<Integer> set, int level,
    int version, String attributeName, ValidationContext context) {
    // TODO Auto-generated method stub
  }


  @Override
  public ValidationFunction<?> getValidationFunction(int errorCode,
    ValidationContext context) {
    ValidationFunction<RenderLayoutPlugin> func = null;
    switch(errorCode) {
    case RENDER_20301:
      func = new ValidationFunction<RenderLayoutPlugin>() {

        @Override
        public boolean check(ValidationContext ctx, RenderLayoutPlugin rlp) {
          Layout extendedLayout = (Layout) rlp.getExtendedSBase();
          return new DuplicatedElementValidationFunction<Layout>(
            RenderConstants.shortLabel + ":"
              + RenderConstants.listOfLocalRenderInformation).check(ctx,
                extendedLayout)
            && new UnknownPackageElementValidationFunction<Layout>(
              RenderConstants.shortLabel).check(ctx, extendedLayout);
        }
      };
      break;
      
    case RENDER_20302:
      func = new ValidationFunction<RenderLayoutPlugin>() {

        @Override
        public boolean check(ValidationContext ctx, RenderLayoutPlugin t) {
          if(t.isSetListOfLocalRenderInformation()) {
            // TODO 2020/03: isSet will ignore empty List (i.e. empty-list is
            // considered not set) -> Figure out how to check whether empty
            // (getList... will set the list)
            return !t.getListOfLocalRenderInformation().isEmpty();
          }
          // Need not be set:
          return true;
        }
      };
      break;
      
    case RENDER_20303:
      func = new ValidationFunction<RenderLayoutPlugin>() {

        @Override
        public boolean check(ValidationContext ctx, RenderLayoutPlugin rlp) {
          if(rlp.isSetListOfLocalRenderInformation()) {
            return new UnknownElementValidationFunction<ListOfLocalRenderInformation>().check(
              ctx, rlp.getListOfLocalRenderInformation());
          }
          return true;
        }
      };
      break;
      
    // Core attributes on ListOfLocalRenderInformation
    case RENDER_20304:
      func = new ValidationFunction<RenderLayoutPlugin>() {

        @Override
        public boolean check(ValidationContext ctx, RenderLayoutPlugin rlp) {
          if(rlp.isSetListOfLocalRenderInformation()) {
            return new UnknownCoreAttributeValidationFunction<ListOfLocalRenderInformation>().check(
              ctx, rlp.getListOfLocalRenderInformation());
          }
          return true;
        }
      };
      break;
      
    // Package attributes on ListOfLocalRenderInformation
    case RENDER_20305:
      func = new ValidationFunction<RenderLayoutPlugin>() {

        @Override
        public boolean check(ValidationContext ctx, RenderLayoutPlugin rlp) {
          if(rlp.isSetListOfLocalRenderInformation()) {
            return new UnknownPackageAttributeValidationFunction<ListOfLocalRenderInformation>(
              RenderConstants.shortLabel).check(ctx,
                rlp.getListOfLocalRenderInformation());
          }
          return true;
        }
      };
      break;
      
    case RENDER_20306:
      func=new ValidationFunction<RenderLayoutPlugin>() {

        @Override
        public boolean check(ValidationContext ctx, RenderLayoutPlugin rlp) {

          if (rlp.isSetListOfLocalRenderInformation()) {
            ListOfLocalRenderInformation lris = rlp.getListOfLocalRenderInformation();
            // a) Check that versionMajor is nonnegative, if it is a correct number
            if (lris.isSetVersionMajor()) {
              return lris.getVersionMajor() >= 0;
            } else if (lris.getUserObject(JSBML.UNKNOWN_XML) != null) {
              XMLNode unknown = (XMLNode) lris.getUserObject(JSBML.UNKNOWN_XML);
              // If the versionMajor is found in the unknown-object: There was a
              // numberformat-exception, and the file is invalid (return false);
              // If it isn't, the index will be -1 and the file is not invalid
              return unknown.getAttrIndex(RenderConstants.versionMajor) == -1;
            }
          }
                    
          return true;
        }
      };
      break;
      
    case RENDER_20307:
      func=new ValidationFunction<RenderLayoutPlugin>() {

        @Override
        public boolean check(ValidationContext ctx, RenderLayoutPlugin rlp) {

          if (rlp.isSetListOfLocalRenderInformation()) {
            ListOfLocalRenderInformation lris = rlp.getListOfLocalRenderInformation();
            if (lris.isSetVersionMinor()) {
              return lris.getVersionMinor() >= 0;
            } else if (lris.getUserObject(JSBML.UNKNOWN_XML) != null) {
              XMLNode unknown = (XMLNode) lris.getUserObject(JSBML.UNKNOWN_XML);
              return unknown.getAttrIndex(RenderConstants.versionMinor) == -1;
            }
          }
                    
          return true;
        }
      };
      break;    
    }
    
    return func;
  }
}
