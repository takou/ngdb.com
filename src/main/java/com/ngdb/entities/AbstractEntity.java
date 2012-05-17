package com.ngdb.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PreUpdate;

@MappedSuperclass
public abstract class AbstractEntity {

	@Column(name = "creation_date", nullable = false)
	private Date creationDate;

	@Column(name = "modification_date", nullable = false)
	private Date modificationDate;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	public AbstractEntity() {
		creationDate = modificationDate = new Date();
	}

	@PreUpdate
	public void preUpdate() {
		modificationDate = new Date();
	}

	public Long getId() {
		return id;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public Date getModificationDate() {
		return modificationDate;
	}

}
