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
package com.steeleforge.aem.ironsites.cache.service;

import java.util.Map;
import java.util.Set;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheStats;

/**
 * OSGi service used to access felix factory configured and ad-hoc instances of
 * Google Guava basic cache. A new cache is built when attempting to access an
 * instance of a cache not previously named/accessed. The Google Guava {@link <a href="http://docs.guava-libraries.googlecode.com/git/javadoc/com/google/common/cache/Cache.html">Cache</a>}
 * can be configured at build time with {@link <a href="http://docs.guava-libraries.googlecode.com/git/javadoc/com/google/common/cache/CacheBuilderSpec.html">specifications</a>}.
 *  
 * @author David Steele
 *
 */
public interface SimpleCacheService {
    /**
     * Known cache names/keys
     * 
     * @return set of cache keys
     */
    public Set<String> getCaches();
    
    /**
     * Access OSGi configured or application managed cache for name & options.
     * 
     * @param name cache key
     * @param spec {@link <a href="http://docs.guava-libraries.googlecode.com/git/javadoc/com/google/common/cache/CacheBuilderSpec.html">CacheBuilderSpec</a>}
     * @param stats true if recording statistics is desired
     * @return pre-existing or new cache
     */
    public Cache<Object, Object> getCache(String name, String spec, boolean stats);
    
    /**
     * Access OSGi configured or application managed cache by name.
     * 
     * @param name cache key
     * @return pre-existing or new cache
     */
    public Cache<Object, Object> getCache(String name);
    
    /**
     * Cache by name as Map.
     * 
     * @param name cache key
     * @return pre-existing/new cache or null
     */
    public Map<Object, Object> getCacheMap(String name);
    
    /**
     * Access cache statistics by name.
     * 
     * @param name cache key
     * @return cache statistics
     * @see <a href="http://docs.guava-libraries.googlecode.com/git/javadoc/com/google/common/cache/CacheStats.html">CacheStats</a>
     */
    public CacheStats getStats(String name);
}
