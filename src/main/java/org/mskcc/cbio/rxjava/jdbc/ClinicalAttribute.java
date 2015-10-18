package org.mskcc.cbio.rxjava.jdbc;

import org.apache.log4j.Logger;

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
 * Created by Fred Criscuolo on 6/9/15.
 * criscuof@mskcc.org
 */
public class ClinicalAttribute {
    private static final Logger logger = Logger.getLogger(ClinicalAttribute.class);
    private final String displayName;
    public ClinicalAttribute(String aName) {
        this.displayName=aName;
    }

    public String getDisplayName() { return this.displayName;}

   public String toString() {
       return "Display name: " +this.displayName;
   }
}
