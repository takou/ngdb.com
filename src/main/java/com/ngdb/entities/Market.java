package com.ngdb.entities;

import static org.hibernate.criterion.Order.desc;
import static org.hibernate.criterion.Projections.count;
import static org.hibernate.criterion.Restrictions.eq;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.annotations.Symbol;
import org.hibernate.Criteria;
import org.hibernate.Session;

import com.ngdb.entities.shop.ShopItem;
import com.ngdb.entities.user.User;
import com.ngdb.web.services.MailService;

public class Market {

	@Inject
	private Session session;

	@Inject
	private MailService mailService;

	@Inject
	@Symbol("host.url")
	private String hostUrl;

	public List<ShopItem> findAllItemsSold() {
		return allShopItems().add(eq("sold", true)).list();
	}

	public List<ShopItem> findAllItemsForSale() {
		return allShopItems().add(eq("sold", false)).list();
	}

	private Criteria allShopItems() {
		return session.createCriteria(ShopItem.class).setCacheable(true).addOrder(desc("modificationDate"));
	}

	public List<ShopItem> findLastForSaleItems() {
		return session.createQuery("SELECT si FROM ShopItem si WHERE si.sold = false ORDER BY modificationDate DESC").setCacheable(true).setMaxResults(3).list();
	}

	public Long getNumForSaleItems() {
		return (Long) session.createCriteria(ShopItem.class).setCacheable(true).setProjection(count("id")).add(eq("sold", false)).uniqueResult();
	}

	public Long getNumSoldItems() {
		return (Long) session.createCriteria(ShopItem.class).setCacheable(true).setProjection(count("id")).add(eq("sold", true)).uniqueResult();
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

}
