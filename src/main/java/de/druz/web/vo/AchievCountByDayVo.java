package de.druz.web.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import de.druz.web.persistance.model.StatAchievementDay;

public class AchievCountByDayVo implements Serializable {
	
	private static final long serialVersionUID = -5722481014283426412L;
	
	private Integer firstYear;
	private Integer lastYear;
	private List<StatAchievementDay> days = new ArrayList<StatAchievementDay>();
	
	public Integer getFirstYear() {
		return firstYear;
	}
	public void setFirstYear(Integer firstYear) {
		this.firstYear = firstYear;
	}
	public Integer getLastYear() {
		return lastYear;
	}
	public void setLastYear(Integer lastYear) {
		this.lastYear = lastYear;
	}
	public List<StatAchievementDay> getDays() {
		return days;
	}
	public void setDays(List<StatAchievementDay> days) {
		this.days = days;
	}
	
}
