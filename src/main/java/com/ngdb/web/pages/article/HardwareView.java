package com.ngdb.web.pages.article;

import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.hibernate.annotations.CommitAfter;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.hibernate.Session;

import com.ngdb.entities.article.Article;
import com.ngdb.entities.article.Comment;
import com.ngdb.entities.article.Hardware;
import com.ngdb.entities.article.Note;
import com.ngdb.entities.article.Review;
import com.ngdb.entities.article.Tag;
import com.ngdb.entities.shop.Wish;
import com.ngdb.entities.user.CollectionObject;
import com.ngdb.entities.user.User;

public class HardwareView extends ArticleView {

	@Persist("entity")
	private Hardware hardware;

	@Property
	private Note property;

	@Property
	private Tag tag;

	@Property
	private Review review;

	@Property
	private Comment comment;

	@Property
	private String value;

	@Property
	private Note note;

	@Inject
	protected Session session;

	@Property
	private String commentText;

	@CommitAfter
	Object onActionFromCollection(Hardware hardware) {
		User currentUser = userService.getCurrentUser();
		CollectionObject collection = new CollectionObject(currentUser, hardware);
		session.merge(collection);
		return HardwareView.class;
	}

	@CommitAfter
	Object onActionFromWishList(Hardware hardware) {
		User currentUser = userService.getCurrentUser();
		Wish wish = new Wish(currentUser, hardware);
		session.merge(wish);
		return HardwareView.class;
	}

	public void onActivate(Hardware hardware) {
		this.hardware = hardware;
	}

	@CommitAfter
	public Object onSuccess() {
		User user = userService.getCurrentUser();
		session.merge(new Comment(commentText, user, getArticle()));
		return HardwareView.class;
	}

	@Override
	protected Article getArticle() {
		return hardware;
	}

	public void setHardware(Hardware hardware) {
		this.hardware = hardware;
	}

	public Hardware getHardware() {
		return hardware;
	}

}
