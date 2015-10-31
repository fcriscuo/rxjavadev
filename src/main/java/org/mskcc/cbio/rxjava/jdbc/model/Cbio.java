package org.mskcc.cbio.rxjava.jdbc.model;

import com.google.auto.value.AutoValue;
import com.sun.istack.internal.Nullable;

import java.util.Arrays;
import java.util.List;

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
 * Created by Fred Criscuolo on 8/5/15.
 * criscuof@mskcc.org
 */
public class Cbio {

    @AutoValue
    public abstract static class GeneticAlteration{
        GeneticAlteration() {}
        public static GeneticAlteration create (Integer profileId, Integer entrezId, List<String> valueList) {
            return new AutoValue_Cbio_GeneticAlteration(profileId, entrezId, valueList);
        }
        abstract Integer profileId();
        abstract Integer entrezId();
        abstract List<String> valueList();

    }
    @AutoValue
    public  abstract static class CancerStudy {
        CancerStudy() {}
        public  static CancerStudy create(String cancerStudyIdentifier, String typeOfCancer, String name ,
                                   String shortName, String description){
            return new AutoValue_Cbio_CancerStudy(cancerStudyIdentifier, typeOfCancer, name, shortName, description);
        }
        abstract String cancerStudyIdentifier();
        abstract String typeOfCancer();
        abstract String name();
        abstract String shortName();
        abstract String description();

    }


    public static void main (String...args){
        CancerStudy study = CancerStudy.create("identifier1", "lymph", "Cancer Study 1", "study1","A cancer study");
        System.out.println(study.toString());
        GeneticAlteration ga = GeneticAlteration.create(1,1000, Arrays.asList("1","0","-1"));
        System.out.println(ga.toString());
    }

}
