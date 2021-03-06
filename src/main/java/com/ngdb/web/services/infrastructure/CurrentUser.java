package com.ngdb.web.services.infrastructure;

import java.util.Collection;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.ApplicationStateManager;
import org.apache.tapestry5.services.Request;
import org.hibernate.Session;
import org.tynamo.security.services.SecurityService;

import com.ngdb.entities.Population;
import com.ngdb.entities.article.Article;
import com.ngdb.entities.article.element.Comment;
import com.ngdb.entities.article.element.Review;
import com.ngdb.entities.article.element.Tag;
import com.ngdb.entities.shop.ShopItem;
import com.ngdb.entities.shop.Wish;
import com.ngdb.entities.user.CollectionObject;
import com.ngdb.entities.user.Shop;
import com.ngdb.entities.user.User;

public class CurrentUser {

	@Inject
	private ApplicationStateManager applicationStateManager;

	@Inject
	private SecurityService securityService;

	@Inject
	private Population population;

	@Inject
	private Request request;

	@Inject
	private Session session;

	public User login(String login, String password) {
		Subject currentUser = securityService.getSubject();
		doLogout(currentUser);

		UsernamePasswordToken token = new UsernamePasswordToken(login, password);
		token.setRememberMe(true);
		currentUser.login(token);

		User user = population.findByLogin(login);
		init(user);
		return user;
	}

	public void logout() {
		doLogout(securityService.getSubject());
	}

	private void doLogout(Subject currentUser) {
		if (securityService.isAuthenticated()) {
			currentUser.logout();
			try {
				request.getSession(false).invalidate();
			} catch (Exception e) {
			}
		}
	}

	private <T> void store(Class<T> valueType, T storedValue) {
		applicationStateManager.set(valueType, storedValue);
	}

	private <T> T get(Class<T> valueType) {
		return applicationStateManager.getIfExists(valueType);
	}

	public boolean isLogged() {
		return securityService.isAuthenticated();
	}

	private void init(User user) {
		store(User.class, user);
	}

	public User getUser() {
		return get(User.class);
	}

	private boolean isLoggedUser(User user) {
		if (isAnonymous()) {
			return false;
		}
		return user.getLogin().equals(getUser().getLogin());
	}

	public Long getUserId() {
		return getUser().getId();
	}

	public String getUsername() {
		return getUser().getLogin();
	}

	public Shop getShop() {
		return getUser().getShop();
	}

	public boolean isAnonymous() {
		return !isLogged();
	}

	public boolean canAddToCollection(Article article) {
		if (isAnonymous()) {
			return false;
		}
		return getUserFromDb().canAddInCollection(article);
	}

	public boolean canRemoveFromCollection(Article article) {
		if (isAnonymous()) {
			return false;
		}
		return getUserFromDb().canRemoveFromCollection(article);
	}

	public void addToCollection(Article article) {
		CollectionObject collectionObject = getUserFromDb().addInCollection(article);
		session.merge(collectionObject);
	}

	public void removeFromCollection(Article article) {
		User userFromDb = getUserFromDb();
		userFromDb.removeFromCollection(article);
	}

	public boolean canWish(Article article) {
		if (isAnonymous()) {
			return false;
		}
		return getUserFromDb().canWish(article);
	}

	public boolean canUnwish(Article article) {
		if (isAnonymous()) {
			return false;
		}
		return getUserFromDb().canUnwish(article);
	}

	public void wish(Article article) {
		Wish wish = getUserFromDb().addToWishes(article);
		session.merge(wish);
	}

	public void unwish(Article article) {
		User userFromDb = getUserFromDb();
		userFromDb.removeFromWishes(article);
	}

	public boolean canSell(Article article) {
		return isLogged();
	}

	public int getNumArticlesInCollection() {
		return getUserFromDb().getNumArticlesInCollection();
	}

	public User getUserFromDb() {
		return ((User) session.load(User.class, getUser().getId()));
	}

	public int getNumArticlesInWishList() {
		return getUserFromDb().getNumArticlesInWishList();
	}

	public void buy(ShopItem shopItem) {
		shopItem.addPotentialBuyer(getUserFromDb());
	}

	public Collection<? extends Article> getGamesInMuseum() {
		return getUserFromDb().getCollection().getGames();
	}

	public Collection<? extends Article> getHardwaresInMuseum() {
		return getUserFromDb().getCollection().getHardwares();
	}

	public int getNumArticlesInShop() {
		return getUserFromDb().getNumArticlesInShop();
	}

	public boolean canMarkAsSold(ShopItem shopItem) {
		return isLogged() && getUserFromDb().canMarkAsSold(shopItem);
	}

	public boolean canRemove(ShopItem shopItem) {
		return isLogged() && getUserFromDb().canRemove(shopItem);
	}

	public boolean canEdit(ShopItem shopItem) {
		return canMarkAsSold(shopItem);
	}

	public boolean isSeller(ShopItem shopItem) {
		if (shopItem == null) {
			return false;
		}
		return isLoggedUser(shopItem.getSeller());
	}

	public boolean canBuy(ShopItem shopItem) {
		boolean currentUserIsPotentialBuyer = isLogged() && shopItem.isNotAlreadyWantedBy(getUser());
		boolean currentUserIsNotTheSeller = !isSeller(shopItem);
		return currentUserIsNotTheSeller && currentUserIsPotentialBuyer;
	}

	public void addCommentOn(Article article, String commentText) {
		User user = getUser();
		session.merge(new Comment(commentText, user, article));
	}

	public void addTagOn(Article article, String tagText) {
		Tag tag = new Tag(tagText, article);
		session.persist(tag);
		getArticleFromDb(article).addTag(tag);
	}

	public void addReviewOn(Article article, String label, String url, String mark) {
		Review review = new Review(label, url, mark, article);
		session.persist(review);
		getArticleFromDb(article).addReview(review);
	}

	private Article getArticleFromDb(Article article) {
		return (Article) session.load(Article.class, article.getId());
	}

	public boolean isFrench() {
		return isFrench(request.getLocale());
	}

	private boolean isFrench(Locale locale) {
		String country = StringUtils.defaultString(locale.getCountry());
		String language = StringUtils.defaultString(locale.getLanguage());
		return country.equalsIgnoreCase("fr") || language.equalsIgnoreCase("fr");
	}

}
