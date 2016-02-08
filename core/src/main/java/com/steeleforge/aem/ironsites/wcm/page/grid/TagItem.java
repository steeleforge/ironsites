package com.steeleforge.aem.ironsites.wcm.page.grid;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;

import com.day.cq.tagging.Tag;

/**
 * Tag Item POJO
 * 
 * @author david
 */
public class TagItem {
	// statics
	public static TagItem getInstance(Tag tag) {
		TagItem ti = null;
		if (null != tag) {
			ti = new TagItem();
			ti.setName(tag.getName());
			ti.setTitle(tag.getTitle());
			ti.setDescription(tag.getDescription());
        	Iterator<Tag> children = tag.listChildren();
        	if (null != children) {
	        	while(children.hasNext()) {
	        		ti.addItem(TagItem.getInstance(children.next()));
	        	}
        	}
		}
		return ti;
	}
	
    // locals
	private String name = StringUtils.EMPTY;
	private String title = StringUtils.EMPTY;
    private String description = StringUtils.EMPTY;
    private List<TagItem> items = Collections.emptyList();
    
    // constructor
    public TagItem() {
        this.items = new ArrayList<TagItem>();
    }

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<TagItem> getItems() {
		return items;
	}

	public void addItem(final TagItem item) {
		this.items.add(item);
	}
	
	public void removeItem(final TagItem item) {
		if (this.items.contains(item)) {
			this.items.remove(item);
		}
	}
}
