package org.tsinghuatj.framework.web.support.binder;

import java.beans.PropertyEditorSupport;

import org.tsinghuatj.framework.utils.NumberUtils;
import org.tsinghuatj.framework.utils.StringUtils;

public class DoubleEditor extends PropertyEditorSupport {

	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		if (StringUtils.isNotBlank(text)) {
			setValue(NumberUtils.toDouble(text, 0.00));
		}
	}
}