package org.mskcc.cbio.util;

import java.util.ArrayList;
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
 * Created by Fred Criscuolo on 7/24/15.
 * criscuof@mskcc.org
 */
public class UnderscoreFormatter {

    private static final String BLANK = " ";
    private static final String UNDER_SCORE = "_";

    public static String formatString(String s){
        if(null != s && s.length()>0){
            return s.replaceAll(BLANK,UNDER_SCORE).trim();
        }
        return s;
    }

    public static List<String>  formatStringList(List<String> listSting){
        List<String> formattedList = new ArrayList();
        for(String s : listSting) {
            formattedList.add(formatString(s));
        }
        return formattedList;
    }
    public static String formatTSVString (String tsvString){
        String retString = "";
        if(null != tsvString && tsvString.length()>0){
          for(String s: formatStringList(Arrays.asList(tsvString.split("\t")))){
              retString += s +"\t";
          }
           retString.substring(0,retString.length()-1);
        }
        return retString;
    }

    public static void  main(String...args){
        String s1 = "ABC DEFG";
        List<String>sList = Arrays.asList("a b", "d e", "f g h i");
        String s3 = "QW ERT\tABC 987\tXX YY zz\ttwo words";

        System.out.println("Original: " +s1 +" ++Formatted: " +formatString(s1));
        System.out.println("Original: " +sList +" ++Formatted: " +formatStringList(sList));
        System.out.println("Original: " +s3 +" ++Formatted: " +formatTSVString(s3));
    }
}
