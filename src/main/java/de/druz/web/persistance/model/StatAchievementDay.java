package de.druz.web.persistance.model;

import java.util.Date;

import javax.persistence.Entity;

import org.hibernate.annotations.NaturalId;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
public class StatAchievementDay extends AbstractPersistable<Long> {

	private static final long serialVersionUID = 7609774221620881665L;

	@NaturalId
	private Date date;
	private Integer count;
	
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
	
}