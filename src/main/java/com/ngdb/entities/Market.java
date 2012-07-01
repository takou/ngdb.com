package com.ngdb.entities;

import static org.hibernate.criterion.Order.desc;
import static org.hibernate.criterion.Projections.count;
import static org.hibernate.criterion.Restrictions.eq;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.annotations.Symbol;
import org.hibernate.Criteria;
import org.hibernate.Session;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.ngdb.entities.article.Article;
import com.ngdb.entities.reference.Origin;
import com.ngdb.entities.reference.Platform;
import com.ngdb.entities.shop.ShopItem;
import com.ngdb.entities.user.User;
import com.ngdb.web.services.MailService;
import com.ngdb.web.services.infrastructure.CurrentUser;

public class Market {

	@Inject
	private Session session;

	@Inject
	private MailService mailService;

	@Inject
	@Symbol("host.url")
	private String hostUrl;

	@Inject
	private CurrentUser currentUser;

	public List<ShopItem> findAllItemsSold() {
		return allShopItems().add(eq("sold", true)).list();
	}

	public List<ShopItem> findAllItemsForSale() {
		return allShopItems().add(eq("sold", false)).list();
	}

	private Criteria allShopItems() {
		return session.createCriteria(ShopItem.class).addOrder(desc("modificationDate"));
	}

	public List<ShopItem> findLastForSaleItems(int count) {
		return session.createQuery("SELECT si FROM ShopItem si WHERE si.sold = false ORDER BY modificationDate DESC").setCacheable(true).setCacheRegion("cacheCount").setMaxResults(count).list();
	}

	public Long getNumForSaleItems() {
		return (Long) session.createCriteria(ShopItem.class).setProjection(count("id")).add(eq("sold", false)).setCacheable(true).setCacheRegion("cacheCount").uniqueResult();
	}

	public Long getNumSoldItems() {
		return (Long) session.createCriteria(ShopItem.class).setProjection(count("id")).add(eq("sold", true)).setCacheable(true).setCacheRegion("cacheCount").uniqueResult();
	}

	public void potentialBuyer(ShopItem shopItem, User potentialBuyer) {
		shopItem.addPotentialBuyer(potentialBuyer);
		Map<String, String> params = new HashMap<String, String>();
		User seller = shopItem.getSeller();
		params.put("recipient", seller.getLogin());
		params.put("potentialBuyer", potentialBuyer.getLogin());
		params.put("shopItemTitle", shopItem.getTitle());
		params.put("shopItemUrl", hostUrl + "market.shopitem/" + shopItem.getId());
		params.put("potentialBuyerEmail", potentialBuyer.getEmail());
		mailService.sendMail(seller, "potential_buyer", params);
	}

	public void remove(ShopItem shopItem) {
		session.delete(shopItem);
	}

	public Collection<ShopItem> findAllByOriginAndPlatform(final Origin origin, final Platform platform) {
		Collection<ShopItem> list = allShopItems().list();
		list = Collections2.filter(list, new Predicate<ShopItem>() {
			@Override
			public boolean apply(ShopItem input) {
				Article article = input.getArticle();
				Origin articleOrigin = article.getOrigin();
				Platform articlePlatform = article.getPlatform();
				return articleOrigin.equals(origin) && articlePlatform.equals(platform);
			}
		});
		return list;
	}

	public String getPriceOf(com.ngdb.entities.shop.ShopItem shopItem) {
		if (currentUser.isFrench()) {
			return shopItem.getPriceInEuros() + " €";
		}
		return "$" + shopItem.getPriceInEuros();
	}
}
