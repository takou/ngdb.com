package com.ngdb.web.pages.user;

import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;

import com.ngdb.entities.article.Article;
import com.ngdb.entities.article.Game;
import com.ngdb.entities.shop.ShopItem;
import com.ngdb.entities.shop.Wish;
import com.ngdb.entities.user.User;
import com.ngdb.web.pages.base.EvenOdd;
import com.ngdb.web.services.infrastructure.CurrentUser;

public class UserView {

	private User user;

	@Property
	private Wish wish;

	@Property
	private Article article;

	@Property
	private ShopItem shopItem;

	@Inject
	private CurrentUser currentUser;

	private EvenOdd evenOdd = new EvenOdd();

	void onActivate(User user) {
		this.user = user;
	}

	public String getViewPage() {
		if (wish.getArticle().getType().equals(Game.class)) {
			return "article/game/gameView";
		}
		return "article/hardware/hardwareView";
	}

	public User getUser() {
		if (user == null) {
			this.user = currentUser.getUserFromDb();
		}
		return user;
	}

	public String getRowClass() {
		return evenOdd.next();
	}

}
