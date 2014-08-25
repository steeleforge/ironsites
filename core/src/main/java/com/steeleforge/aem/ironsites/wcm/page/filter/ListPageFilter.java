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
package com.steeleforge.aem.ironsites.wcm.page.filter;

import java.util.ArrayList;
import java.util.List;

import com.day.cq.wcm.api.Page;
import com.day.cq.commons.Filter;

/**
 * ListPageFilter manages a collection of filters with which implementors
 * can iterate over to determine if a page is included. Fail fast is
 * recommended. For implementations refer to
 * {@link AndPageFilter#includes(Page)} and {@link OrPageFilter#includes(Page)}
 * 
 * @author David Steele
 */
public abstract class ListPageFilter implements Filter<Page> {
    private List<Filter<Page>> filters;

    /**
     * @param filters
     */
    public ListPageFilter(List<Filter<Page>> filters) {
        this.filters = filters;
    }
    
    public ListPageFilter() {
        this.filters = getFilters();
    }

    /**
     * Lazily instantiated accessor.
     * 
     * @return list of filters
     */
    public List<Filter<Page>> getFilters() {
        if (null == filters) {
            filters = new ArrayList<Filter<Page>>();
        }
        return filters;
    }

    /**
     * Do not mutate during {@link #includes(Page)} iteration.
     * 
     * @param filters
     */
    public void setFilters(List<Filter<Page>> filters) {
        this.filters = filters;
    }

    /**
     * Do not mutate during {@link #includes(Page)} iteration.
     * 
     * Refer to {@link List#add(Object)}
     * 
     * @param filter
     */
    public void addFilter(Filter<Page> filter) {
        getFilters().add(filter);
    }

    
    /**
     * Do not mutate during {@link #includes(Page)} iteration.
     * 
     * Refer to {@link List#remove(Object)}
     * 
     * @param filter
     */
    public void removeFilter(Filter<Page> filter) {
        getFilters().remove(filter);
    }
    
    /* (non-Javadoc)
     * @see com.day.cq.commons.Filter#includes(java.lang.Object)
     */
    public abstract boolean includes(Page page);
}
