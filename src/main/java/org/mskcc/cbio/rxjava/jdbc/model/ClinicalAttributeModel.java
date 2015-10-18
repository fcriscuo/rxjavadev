package org.mskcc.cbio.rxjava.jdbc.model;

/**
 * Copyright (c) 2015 Memorial Sloan-Kettering Cancer Center.
 * <p>
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY, WITHOUT EVEN THE IMPLIED WARRANTY OF
 * MERCHANTABILITY OR FITNESS FOR A PARTICULAR PURPOSE.  The software and
 * documentation provided hereunder is on an "as is" basis, and
 * Memorial Sloan-Kettering Cancer Center
 * has no obligations to provide maintenance, support,
 * updates, enhancements or modifications.  In no event shall
 * Memorial Sloan-Kettering Cancer Center
 * be liable to any party for direct, indirect, special,
 * incidental or consequential damages, including lost profits, arising
 * out of the use of this software and its documentation, even if
 * Memorial Sloan-Kettering Cancer Center
 * has been advised of the possibility of such damage.
 * <p>
 * Created by Fred Criscuolo on 10/12/15.
 * criscuof@mskcc.org
 */
public class ClinicalAttributeModel {
    private final String attributeId;
    private final String displayName;
    private final String description;
    private final String datatype;

    public ClinicalAttributeModel(String attributeId, String displayName, String description, String datatype) {
        this.attributeId = attributeId;
        this.displayName = displayName;
        this.description = description;

        this.datatype = datatype;
    }
    public String getAttributeId() {
        return attributeId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }

    public String getDatatype() {
        return datatype;
    }
}
