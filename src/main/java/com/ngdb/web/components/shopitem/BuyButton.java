package com.ngdb.web.components.shopitem;

import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.hibernate.annotations.CommitAfter;
import org.apache.tapestry5.ioc.annotations.Inject;

import com.ngdb.entities.Market;
import com.ngdb.entities.shop.ShopItem;
import com.ngdb.web.services.infrastructure.CurrentUser;

public class BuyButton {

	@Property
	@Parameter
	private ShopItem shopItem;

	@Inject
	private Market market;

	@Inject
	private CurrentUser currentUser;

	@Property
	@Parameter
	private boolean asButton;

	@CommitAfter
	Object onActionFromBuyButton() {
		return onActionFromBuy();
	}

	@CommitAfter
	Object onActionFromBuy() {
		market.potentialBuyer(shopItem, currentUser.getUser());
		return this;
	}

}
