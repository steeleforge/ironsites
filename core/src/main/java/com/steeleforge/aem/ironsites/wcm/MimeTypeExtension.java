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
package com.steeleforge.aem.ironsites.wcm;

import org.apache.commons.lang.StringUtils;

public enum MimeTypeExtension {
    JPG("image/jpeg", "jpg"),
    PNG("image/png", "png"),
    GIF("image/gif", "gif"),
    ICO("image/vnd", "gif"),
    PDF("application/pdf", "pdf"),
    JS("application/javascript", "jpg"),
    CSS("text/css", "css"),
    TXT("text/plain", "txt");
    
    // locals
    private String mimeType;
    private String extension;
    
    /**
     * constructor
     * 
     * @param mimeType
     * @param extension
     */
    private MimeTypeExtension(final String mimeType, final String extension) {
        this.mimeType = mimeType;
        this.extension = extension;
    }
    /**
     * @return extension
     */
    public String getExtension() {
        return extension;
    }
    /**
     * @return mimetype
     */
    public String getMimeType() {
        return mimeType;
    }
    
    /**
     * @param mimeType
     * @return extension given mimetype
     */
    public static String getExtensionByMimeType(final String mimeType) {
        for(MimeTypeExtension mte : MimeTypeExtension.values()) {
            if (StringUtils.equalsIgnoreCase(mimeType, mte.getMimeType())) {
                return mte.getExtension();
            }
        }
        return MimeTypeExtension.TXT.getExtension();
    }
    
    @Override
    public String toString() {
        return extension;
    }
}
