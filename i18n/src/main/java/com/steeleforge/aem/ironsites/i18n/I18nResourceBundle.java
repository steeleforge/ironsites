/** 
 * This is free and unencumbered software released into the public domain.
 * 
 * Anyone is free to copy, modify, publish, use, compile, sell, or
 * distribute this software, either in source code form or as a compiled
 * binary, for any purpose, commercial or non-commercial, and by any
 * means.
 * 
 * In jurisdictions that recognize copyright laws, the author or authors
 * of this software dedicate any and all copyright interest in the
 * software to the public domain. We make this dedication for the benefit
 * of the public at large and to the detriment of our heirs and
 * successors. We intend this dedication to be an overt act of
 * relinquishment in perpetuity of all present and future rights to this
 * software under copyright law.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS BE LIABLE FOR ANY CLAIM, DAMAGES OR
 * OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 * 
 * For more information, please refer to <http://unlicense.org/>
 */
package com.steeleforge.aem.ironsites.i18n;

import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.adapter.Adaptable;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.wrappers.ValueMapDecorator;

/**
 * I18nResourceBundle will return local content or a fallback ResourceBundle
 * which should be based sling:basename.
 */
public class I18nResourceBundle extends ResourceBundle implements Adaptable {
    private final ValueMap content;
    private final ResourceBundle bundle;

    public I18nResourceBundle(ValueMap content, ResourceBundle bundle) {
        this.content = content;
        this.bundle = bundle;
        setParent(bundle);
    }

    @Override
    protected Object handleGetObject(String key) {
        if (null != content && content.keySet().contains(key)) {
            return content.get(key, StringUtils.EMPTY);
        }
        if (null != bundle && bundle.keySet().contains(key)) {
            return bundle.getObject(key);
        }
        return null;
    }

    @Override
    public Enumeration<String> getKeys() {
        Set<String> names = new HashSet<String>();
        if (null != parent) {
            names.addAll(Collections.list(parent.getKeys()));
        }
        if (null != bundle) {
            names.addAll(Collections.list(bundle.getKeys()));
        }
        if (null != content && null != content.keySet()) {
            names.addAll(content.keySet());
        }
        return Collections.enumeration(names);
    }

    /* (non-Javadoc)
     * @see org.apache.sling.api.adapter.Adaptable#adaptTo(java.lang.Class)
     */
    @SuppressWarnings("unchecked")
    public <AdapterType> AdapterType adaptTo(Class<AdapterType> clazz) {
        if (clazz == ValueMap.class) {
            ValueMap vm = new ValueMapDecorator(new HashMap<String, Object>());
            for(String key : Collections.list(getKeys())) {
                vm.put(key, getString(key));
            }
            return (AdapterType) vm;
        }
        return null;
    }
}
