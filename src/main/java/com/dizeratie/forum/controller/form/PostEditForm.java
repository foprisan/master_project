package com.dizeratie.forum.controller.form;

import java.text.ParseException;
import java.util.Date;

import javax.validation.constraints.Pattern;

import com.dizeratie.forum.entity.type.Sex;

public class PostEditForm {
	  
	private String content;
	
	private Date lastUpdateDate;

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		content = content;
	}

	public Date getLastUpdateDate() {
		return lastUpdateDate;
	}

	public void setLastUpdateDate(Date lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}

	public PostEditForm() {
		super();
	}

	public PostEditForm(String content, Date lastUpdateDate) {
		super();
		content = content;
		this.lastUpdateDate = lastUpdateDate;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((content == null) ? 0 : content.hashCode());
		result = prime * result + ((lastUpdateDate == null) ? 0 : lastUpdateDate.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PostEditForm other = (PostEditForm) obj;
		if (content == null) {
			if (other.content != null)
				return false;
		} else if (!content.equals(other.content))
			return false;
		if (lastUpdateDate == null) {
			if (other.lastUpdateDate != null)
				return false;
		} else if (!lastUpdateDate.equals(other.lastUpdateDate))
			return false;
		return true;
	}


}


