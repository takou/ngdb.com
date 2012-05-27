package com.ngdb.entities;

import static org.hibernate.criterion.Order.asc;
import static org.hibernate.criterion.Restrictions.between;
import static org.hibernate.criterion.Restrictions.eq;

import java.util.Collection;
import java.util.Date;

import org.apache.tapestry5.ioc.annotations.Inject;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.ngdb.entities.article.Game;
import com.ngdb.entities.reference.Genre;
import com.ngdb.entities.reference.Origin;
import com.ngdb.entities.reference.Platform;
import com.ngdb.entities.reference.Publisher;

@SuppressWarnings("unchecked")
public class GameFactory {

	@Inject
	private Session session;

	public Collection<Game> findAllByReleaseDate(Date releaseDate) {
		return allGames().add(between("releaseDate", releaseDate, releaseDate)).list();
	}

	public Collection<Game> findAllByNgh(String ngh) {
		return allGames().add(eq("ngh", ngh)).list();
	}

	public Collection<Game> findAllByPlatform(Platform platform) {
		return allGames().add(eq("platform", platform)).list();
	}

	public Collection<Game> findAllByOrigin(Origin origin) {
		return allGames().add(eq("origin", origin)).list();
	}

	public Collection<Game> findAllByGenre(final Genre genre) {
		Criteria criteria = allGames();
		Predicate<Game> additionnalFilter = new Predicate<Game>() {
			@Override
			public boolean apply(Game game) {
				return game.isOfGenre(genre);
			}
		};
		return Collections2.filter(criteria.list(), additionnalFilter);
	}

	public Collection<Game> findAllByPublisher(Publisher publisher) {
		return allGames().add(eq("publisher", publisher)).list();
	}

	public Long getNumGames() {
		return (Long) session.createCriteria(Game.class).setProjection(Projections.rowCount()).uniqueResult();
	}

	public Collection<Game> findAll() {
		return allGames().list();
	}

	private Criteria allGames() {
		return session.createCriteria(Game.class).addOrder(asc("title"));
	}

}
