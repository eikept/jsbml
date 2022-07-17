package org.sbml.jsbml.ext.comp.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Storage class of information of replaced info used in flattening the model
 * id: id of the replaced Element
 * idType: Type of id of replaced Element: id, metaId, port, unitID
 * modelId: model in which the Element is
 * conversionFactor: conversionFactor to transformElement
 * replacedElementPath: model path to replaced Element
 */
public class ReplacedElementInfo {

    String id;
    String modelId;
    IdType idType;
    String conversionFactor;
    List<String> replacedElementPath;

    public ReplacedElementInfo(String id, String modelId, IdType idType, String conversionFactor, List<String> replacedElementPath) {

        this.id = id;
        this.modelId = modelId;
        this.idType = idType;
        this.conversionFactor = conversionFactor;
        this.replacedElementPath = replacedElementPath;
    }


    public ReplacedElementInfo clone() {

        return new ReplacedElementInfo(this.id, this.modelId, this.idType, this.conversionFactor, new ArrayList<String>(replacedElementPath));
    }

    @Override
    public String toString() {
        return String.format("[ReplacedElementInfo id=%s, modelId=%s, idType=%s, convFactor=%s, replacedElementPath=%s]",
                this.id, this.modelId, this.idType.toString(), this.conversionFactor, this.replacedElementPath.toString());
    }

}
