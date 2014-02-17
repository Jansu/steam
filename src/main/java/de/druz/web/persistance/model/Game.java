package de.druz.web.persistance.model;

import java.util.Set;
import java.util.SortedSet;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.Sort;
import org.hibernate.annotations.SortType;
import org.springframework.data.jpa.domain.AbstractPersistable;

import com.github.koraktor.steamcondenser.steam.community.SteamGame;

import de.druz.web.comparator.AchievementPercComparator;

@Entity
public class Game extends AbstractPersistable<Long> {

	private static final long serialVersionUID = 1206245976840924106L;
	
	@NaturalId
	private Integer appId;
	private String iconUrl;
	private String logoUrl;
	private String logoThumbnailUrl;
	private String shopUrl;
	private String name;
	private String shortName;
	
	@OneToOne(fetch = FetchType.EAGER)
	private GameStat stats;
	
	@OneToMany(fetch = FetchType.EAGER, mappedBy="game", cascade = CascadeType.ALL)
	@Sort(type = SortType.COMPARATOR, comparator = AchievementPercComparator.class) 
	private SortedSet<Achievement> achievements;
	
	public Game set(SteamGame steamGame) {
		this.appId = steamGame.getAppId();
		this.iconUrl = steamGame.getIconUrl();
		this.logoUrl = steamGame.getLogoUrl();
		this.logoThumbnailUrl = steamGame.getLogoThumbnailUrl();
		this.shopUrl = steamGame.getStoreUrl();
		this.name = steamGame.getName();
		this.shortName = steamGame.getShortName();
		return this;
	}
	
	public Integer getAppId() {
		return appId;
	}
	public void setAppId(Integer appId) {
		this.appId = appId;
	}
	public String getIconUrl() {
		return iconUrl;
	}
	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}
	public String getLogoUrl() {
		return logoUrl;
	}
	public void setLogoUrl(String logoUrl) {
		this.logoUrl = logoUrl;
	}
	public String getLogoThumbnailUrl() {
		return logoThumbnailUrl;
	}
	public void setLogoThumbnailUrl(String logoThumbnailUrl) {
		this.logoThumbnailUrl = logoThumbnailUrl;
	}
	public String getShopUrl() {
		return shopUrl;
	}
	public void setShopUrl(String shopUrl) {
		this.shopUrl = shopUrl;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getShortName() {
		return shortName;
	}
	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public GameStat getStats() {
		return stats;
	}

	public void setStats(GameStat stats) {
		this.stats = stats;
	}

	@Override
	public String toString() {
		return "Game [appId=" + appId + ", name=" + name + "]";
	}

	public SortedSet<Achievement> getAchievements() {
		return achievements;
	}

	public void setAchievements(SortedSet<Achievement> achievements) {
		this.achievements = achievements;
	}

}
