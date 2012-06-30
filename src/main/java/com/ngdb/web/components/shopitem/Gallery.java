package com.ngdb.web.components.shopitem;

import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.json.JSONObject;

import com.ngdb.entities.article.element.Picture;
import com.ngdb.entities.article.element.ShopItemPictures;
import com.ngdb.entities.shop.ShopItem;

public class Gallery {

	@Property
	@Parameter(allowNull = false)
	private ShopItem shopItem;

	@Property
	private Picture picture;

	public JSONObject getParams() {
		return new JSONObject("maxHeight", "600px", "slideshow", "true", "slideshowSpeed", "5000");
	}

	public ShopItemPictures getPictures() {
		return shopItem.getPictures();
	}

	public String getSmallPictureUrl() {
		return picture.getUrl("small");
	}

}
