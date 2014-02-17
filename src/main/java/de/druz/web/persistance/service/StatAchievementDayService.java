package de.druz.web.persistance.service;

import java.math.BigInteger;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.druz.web.persistance.model.Achievement;
import de.druz.web.persistance.model.StatAchievementDay;
import de.druz.web.persistance.repository.StatAchievementDayRepository;

@Service
@Transactional
public class StatAchievementDayService {

	private static final Logger logger = LoggerFactory.getLogger(StatAchievementDayService.class);
	
	@Inject
	private StatAchievementDayRepository repo;

	@Inject
	private AchievementService achievService;

	public StatAchievementDay createStatAchievementDay(Date date, Integer count) {
		logger.info("Creating StatAchievementDay with date {}: {}", date, count);
		StatAchievementDay stat = new StatAchievementDay();
		stat.setCount(count);
		stat.setDate(DateUtils.truncate(date, Calendar.DAY_OF_MONTH));
		repo.saveAndFlush(stat);
		return stat;
	}

	public void update(StatAchievementDay stat) {
		repo.saveAndFlush(stat);
	}
	
	public List<StatAchievementDay> findAll() {
		return repo.findAll(new Sort(Direction.ASC, "date"));
	}

	public Date findFirstDate() {
		return repo.findFirstDate();
	}

	public Date findLastDate() {
		return repo.findLastDate();
	}

	public StatAchievementDay findByDate(Date date) {
		return repo.findByDate(date);
	}

	public void updateAchievCountByDay() {
		Date firstDate = findFirstDate();
		Date lastDate = findLastDate();

		if (firstDate == null) {
			Achievement a = achievService.findFirst();
			StatAchievementDay stat = createStatAchievementDay(a.getTimestamp(), countAchievByDay(a.getTimestamp()).intValue());
			firstDate = stat.getDate();
		}

		if (lastDate == null) {
			lastDate = firstDate;
		}
		
		Date startOfToday = DateUtils.truncate(new Date(), Calendar.DAY_OF_MONTH);
		while (lastDate.before(startOfToday)) {
			StatAchievementDay stat = findByDate(lastDate); 
			if (stat == null) {
				stat = createStatAchievementDay(lastDate, countAchievByDay(lastDate).intValue());
			}
			lastDate = DateUtils.addDays(lastDate, 1);
		}
	}
	
	public BigInteger countAchievByDay(Date day) {
		day = DateUtils.truncate(day, Calendar.DAY_OF_MONTH);
		return achievService.countByTimeUnlocked(day, DateUtils.addDays(day, 1));
	}
}