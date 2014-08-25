package com.steeleforge.aem.ironsites.i18n;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

public enum I18nUtil {
    INSTANCE;
    private static final String TOKEN_RULE_DELIMITER = ";";
    private static final String TOKEN_REPLACEMENT_DELIMITER = "=";
    private static final String DEFAULT_FORMAT = "%s";
    

    /**
     * Interprets variable replacement as well as formatting; Object[] version
     * 
     * @param src source text
     * @param rules see {@link I18nUtil#interpolate(String, String)}
     * @param values
     * @return
     */
    public static String interpolateValues(String src, String rules, Object[] values) {
        return interpolate(swap(src, rules), values);
    }
    
    /**
     * Interprets variable replacement as well as formatting; varargs version
     * 
     * @param src source text
     * @param rules see {@link I18nUtil#interpolate(String, String)}
     * @param values
     * @return
     */
    public static String interpolate(String src, String rules, Object... values) {
        return interpolate(swap(src, rules), values);
    }
    
    /**
     * Supports MessageFormat varargs to replace source text tokens.
     * 
     * @param src source text
     * @param values replacement tokens
     * @return
     */
    public static String interpolate(String src, Object... values) {
        if (StringUtils.isBlank(src)) {
            return src;
        }
        // to behave similar to I18n#get
        return MessageFormat.format(StringUtils.replace(src, "'", "''"), values);
    }
    
    /**
     * Replace named tokens with MessageFormat indices.
     * 
     * @param src source text
     * @param variables array of variables to replace with indicees
     * @param indices array of indices to replace variables
     * @return
     */
    public static String interpolate(String src, String[] variables, String[] indices) {
        if (StringUtils.isBlank(src)) {
            return src;
        }
        return StringUtils.replaceEach(src, variables, indices);
    }
    
    /**
     * Replace named tokens with MessageFormat indices.
     * 
     * Expects replacement rule definition to be formatted as "foo=0;bar=1" etc
     * whitespace is ignored; strict interpretation of "right replaces left"
     * 
     * @param src source text
     * @param tokens rule definition; format "foo=0;bar=1"; ignores whitespace
     * @param format token format
     * @return
     */
    public static String swap(String src, String rules, String format) {
        if (StringUtils.isBlank(src)) {
            return src;
        }
        String fmt = (StringUtils.isNotBlank(format))? format : DEFAULT_FORMAT;
        List<String> indices = Collections.emptyList();
        List<String> variables = Collections.emptyList();
        if (StringUtils.isNotBlank(rules)) {
            // split by delimiter then populate indices & assignment lists
            // variables[idx] should be a variable name, whitespace stripped
            // indices[idx] should be an index such as 0, whitespace stripped
            // each will be wrapped with { }'s for MessageFormat#format
            String[] pairs;
            String[] parts; 
            pairs = StringUtils.split(rules, TOKEN_RULE_DELIMITER);
            if (pairs.length > 0) {
                indices = new ArrayList<String>();
                variables = new ArrayList<String>();
                for(String rule : pairs) {
                    parts = StringUtils.split(rule, TOKEN_REPLACEMENT_DELIMITER);
                    if (2 == ArrayUtils.getLength(parts)) {
                        variables.add(String.format(fmt, StringUtils.trim(parts[0])));
                        indices.add(String.format(fmt, StringUtils.trim(parts[1])));
                    }
                }
            }
            pairs = null;
            parts = null;
        }
        return interpolate(src, variables.toArray(new String[0]), indices.toArray(new String[0]));
    }
    

    /**
     * Interpolate with default java index format "{%s}"
     * 
     * @param src
     * @param rules
     * @see I18nUtil#interpolate(String, String, String)
     * @return
     */
    public static String swap(String src, String rules) {
        return swap(src, rules, DEFAULT_FORMAT);
    }
}
