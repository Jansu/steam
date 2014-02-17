package de.druz.web.persistance.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

import org.springframework.data.jpa.domain.AbstractPersistable;

import com.github.koraktor.steamcondenser.steam.community.GameAchievement;

import de.druz.web.comparator.AchievementPercComparator;

@Entity
public class Achievement extends AbstractPersistable<Long> implements Comparable<Achievement> {

	private static final long serialVersionUID = 551631159160357527L;
	
	@ManyToOne(fetch=FetchType.EAGER)
	private Game game;
	
	private String name;
	private String apiName;
	private String description;
	private String iconClosedUrl;
	private String iconOpenUrl;
	private Date timestamp;
	private boolean unlocked;
	private Double globalUnlockPerc;
	
	public Achievement set(Game game, GameAchievement achiev) {
		this.game = game;
		this.setName(achiev.getName());
		this.setApiName(achiev.getApiName());
		this.setDescription(achiev.getDescription());
		this.setIconClosedUrl(achiev.getIconClosedURL());
		this.setIconOpenUrl(achiev.getIconOpenURL());
		this.setTimestamp(achiev.getTimestamp());
		this.setUnlocked(achiev.isUnlocked());
//		achiev.getGlobalPercentages(appId)
		return this;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getApiName() {
		return apiName;
	}

	public void setApiName(String apiName) {
		this.apiName = apiName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getIconClosedUrl() {
		return iconClosedUrl;
	}

	public void setIconClosedUrl(String iconClosedUrl) {
		this.iconClosedUrl = iconClosedUrl;
	}

	public String getIconOpenUrl() {
		return iconOpenUrl;
	}

	public void setIconOpenUrl(String iconOpenUrl) {
		this.iconOpenUrl = iconOpenUrl;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public boolean isUnlocked() {
		return unlocked;
	}

	public void setUnlocked(boolean unlocked) {
		this.unlocked = unlocked;
	}

	public Double getGlobalUnlockPerc() {
		return globalUnlockPerc;
	}

	public void setGlobalUnlockPerc(Double globalUnlockPerc) {
		this.globalUnlockPerc = globalUnlockPerc;
	}

	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}

	@Override
	public int compareTo(Achievement o) {
		AchievementPercComparator comp = new AchievementPercComparator();
		return comp.compare(this, o);
	}
}