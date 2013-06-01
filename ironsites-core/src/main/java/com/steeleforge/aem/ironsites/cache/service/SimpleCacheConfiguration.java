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

/**
 * OSGi configuration factory for Google Guava {@link <a href="http://docs.guava-libraries.googlecode.com/git/javadoc/com/google/common/cache/Cache.html">Cache</a>} creation.
 * 
 * @author David Steele
 */
public interface SimpleCacheConfiguration {
	/**
	 * Cache name.
	 * 
	 * @return cache name
	 */
	public String getName();
	
	/**
	 * Cache spec
	 * 
	 * @return cache specfication String
	 * @see <a href="http://docs.guava-libraries.googlecode.com/git/javadoc/com/google/common/cache/CacheBuilderSpec.html">CacheBuilderSpec</a>
	 */
	public String getSpecs();
	
	/**
	 * Cache statistics tracking enabled/disabled
	 * 
	 * @return cache stat toggle state
	 */
	public Boolean isRecordStats();
}
