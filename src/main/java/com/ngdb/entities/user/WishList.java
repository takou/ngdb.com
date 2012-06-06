package com.ngdb.entities.user;

import static javax.persistence.FetchType.LAZY;

import java.util.Collections;
import java.util.Iterator;
import java.util.Set;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

import com.ngdb.entities.article.Article;
import com.ngdb.entities.shop.Wish;

@Embeddable
public class WishList implements Iterable<Wish> {

	@OneToMany(mappedBy = "wisher", fetch = LAZY)
	private Set<Wish> wishes;

	public boolean contains(Article article) {
		for (Wish wish : wishes) {
			Long searchId = article.getId();
			Long idInWishList = wish.getArticle().getId();
			if (searchId.equals(idInWishList)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public Iterator<Wish> iterator() {
		return wishes.iterator();
	}

	public Set<Wish> getWishes() {
		return Collections.unmodifiableSet(wishes);
	}

	public void addInWishList(Wish wish) {
		wishes.add(wish);
	}

	public int getNumWishes() {
		return wishes.size();
	}

}
