package de.druz.web.persistance.model;

import java.text.DecimalFormat;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

import org.springframework.data.jpa.domain.AbstractPersistable;

import com.github.koraktor.steamcondenser.steam.community.GameStats;

@Entity
public class GameStat extends AbstractPersistable<Long> {

	private static final long serialVersionUID = -9206867061979277436L;

	@OneToOne
	private Game game;
	
	private String hoursPlayed;
	private Float achievPerc;
	private Integer achievDone;
	private String url;
	private Integer playerCount;
	private Integer totalPlaytime;
	private Integer metaScore;
	private Float userMetaScore;
	private Integer minutesToBeat;
	private Integer minutesToBeatCompletely;
	private String howLongToBeatUrl;
	private String metacriticUrl;
	private Boolean beaten;
	private Date beatenDate;
	
	public GameStat set(GameStats stat) {
		this.hoursPlayed = stat.getHoursPlayed();
		this.achievPerc = stat.getAchievementsPercentage();
		this.achievDone = stat.getAchievementsDone();
		this.url= stat.getBaseUrl();
//		this.achivements = stat.getAchievements()
		return this;
	}

	public String getHoursPlayed() {
		return hoursPlayed;
	}

	public void setHoursPlayed(String hoursPlayed) {
		this.hoursPlayed = hoursPlayed;
	}

	public Float getAchievPerc() {
		return achievPerc;
	}

	public void setAchievPerc(Float achievPerc) {
		this.achievPerc = achievPerc;
	}

	public Integer getAchievDone() {
		return achievDone;
	}

	public void setAchievDone(Integer achievDone) {
		this.achievDone = achievDone;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Integer getPlayerCount() {
		return playerCount;
	}

	public void setPlayerCount(Integer playerCount) {
		this.playerCount = playerCount;
	}

	public Integer getTotalPlaytime() {
		return totalPlaytime;
	}

	public String getTotalPlaytimeFormatted() {
		if (totalPlaytime == null) {
			return null;
		}
		DecimalFormat df = new DecimalFormat("00");
		return totalPlaytime/60 + ":" + df.format(totalPlaytime % 60);
	}
	
	public void setTotalPlaytime(Integer totalPlaytime) {
		this.totalPlaytime = totalPlaytime;
	}

	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}

	public Integer getMetaScore() {
		return metaScore;
	}

	public void setMetaScore(Integer metaScore) {
		this.metaScore = metaScore;
	}

	public Float getUserMetaScore() {
		return userMetaScore;
	}

	public void setUserMetaScore(Float userMetaScore) {
		this.userMetaScore = userMetaScore;
	}

	public Integer getMinutesToBeatCompletely() {
		return minutesToBeatCompletely;
	}

	public void setMinutesToBeatCompletely(Integer minutesToBeatCompletely) {
		this.minutesToBeatCompletely = minutesToBeatCompletely;
	}

	public Integer getMinutesToBeat() {
		return minutesToBeat;
	}

	public void setMinutesToBeat(Integer minutesToBeat) {
		this.minutesToBeat = minutesToBeat;
	}

	public String getHowLongToBeatUrl() {
		return howLongToBeatUrl;
	}

	public void setHowLongToBeatUrl(String howLongToBeatUrl) {
		this.howLongToBeatUrl = howLongToBeatUrl;
	}

	public String getMetacriticUrl() {
		return metacriticUrl;
	}

	public void setMetacriticUrl(String metacriticUrl) {
		this.metacriticUrl = metacriticUrl;
	}

	public Boolean getBeaten() {
		return beaten;
	}

	public void setBeaten(Boolean beaten) {
		this.beaten = beaten;
	}

	public Date getBeatenDate() {
		return beatenDate;
	}

	public void setBeatenDate(Date beatenDate) {
		this.beatenDate = beatenDate;
	}

	/**
	 * toggles beaten flag
	 * @return new value: true if was false previously, false otherwise
	 */
	public Boolean toggleBeaten() {
		if (null == beaten) {
			beaten = true;
		} else {
			beaten = !beaten;
		}
		if (beaten == true) {
			beatenDate = new Date();
		}
		return beaten;
	}
}
