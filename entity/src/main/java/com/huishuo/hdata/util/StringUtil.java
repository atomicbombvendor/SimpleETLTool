package com.huishuo.hdata.util;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;

public class StringUtil {

    /**
     * Split str to list with #{,}; if str is null or empty, return null
     * @param str str need to split
     * @return String List
     */
    public static List<String> splitString(String str){

        if (StringUtils.isEmpty(str)){
            return null;
        }

        return Arrays.asList(str.split(","));
    }

    /**
     * Split str to list with #{,}; if str is null or empty, return null
     * @param str str need to split
     * @return String List
     */
    public static List<String> splitStringObject(Object str){

        if (str == null || StringUtils.isEmpty(str.toString())){
            return null;
        }

        return Arrays.asList(str.toString().split(","));
    }
}
