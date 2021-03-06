package com.ngdb.web.model;

import java.util.Collections;
import java.util.List;

import org.apache.tapestry5.OptionModel;
import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.internal.OptionModelImpl;
import org.apache.tapestry5.internal.SelectModelImpl;
import org.apache.tapestry5.ioc.internal.util.CollectionFactory;

import com.ngdb.entities.reference.Box;

public class BoxList extends ModelList {

	private SelectModelImpl selectModel;

	public BoxList(List<Box> boxes) {
		Collections.sort(boxes);
		List<OptionModel> options = CollectionFactory.newList();
		for (Box box : boxes) {
			options.add(new OptionModelImpl(box.getName(), box));
		}
		selectModel = new SelectModelImpl(null, options);
	}

	@Override
	protected SelectModel getSelectModel() {
		return selectModel;
	}

}
