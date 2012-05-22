package com.ngdb.web.pages.shop;

import java.util.List;

import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.ioc.annotations.Inject;

import com.ngdb.entities.shop.Wish;
import com.ngdb.web.services.domain.WishService;

public class WishListView {

	@Property
	private Wish wish;

	@Property
	private List<Wish> wishes;

	@Inject
	private WishService wishService;

	@SetupRender
	public void setupRender() {
		this.wishes = wishService.findAll();
	}
}
