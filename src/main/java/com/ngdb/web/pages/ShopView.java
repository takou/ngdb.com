package com.ngdb.web.pages;

import static org.apache.commons.lang.StringUtils.isNotBlank;
import static org.hibernate.criterion.Order.desc;
import static org.hibernate.criterion.Restrictions.eq;

import java.util.Collection;

import org.apache.commons.lang.StringUtils;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.hibernate.Criteria;
import org.hibernate.Session;

import com.ngdb.entities.Article;
import com.ngdb.entities.ShopItem;
import com.ngdb.entities.User;
import com.ngdb.web.Category;

public class ShopView {

	@Property
	private ShopItem shopItem;

	@Property
	private Collection<ShopItem> shopItems;

	@Inject
	private Session session;

	@Persist
	private Category category;

	private Long id;

	void onActivate(String category, String value) {
		this.category = Category.none;
		if (isNotBlank(category)) {
			this.category = Category.valueOf(Category.class, category);
			if (StringUtils.isNumeric(value)) {
				id = Long.valueOf(value);
			}
		}
	}

	@SetupRender
	public void init() {
		Criteria criteria = createCriteria();
		this.shopItems = criteria.list();
	}

	private Criteria createCriteria() {
		Criteria criteria = session.createCriteria(ShopItem.class).addOrder(desc("modificationDate"));
		switch (category) {
		case byArticle:
			Article article = (Article) session.load(Article.class, id);
			criteria = criteria.add(eq("article", article)).add(eq("sold", false));
			break;
		case byUser:
			User user = (User) session.load(User.class, id);
			criteria = criteria.add(eq("seller", user)).add(eq("sold", false));
			break;
		case bySoldDate:
			criteria = criteria.add(eq("sold", true));
			break;
		case none:
			criteria = criteria.add(eq("sold", false));
			break;
		}
		return criteria;
	}

	public void setCategory(Category category) {
		this.category = category;
	}
}
