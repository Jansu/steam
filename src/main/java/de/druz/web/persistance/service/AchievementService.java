package de.druz.web.persistance.service;

import java.math.BigInteger;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.koraktor.steamcondenser.steam.community.GameAchievement;

import de.druz.web.persistance.model.Achievement;
import de.druz.web.persistance.model.Game;
import de.druz.web.persistance.repository.AchievementRepository;

@Service
@Transactional
public class AchievementService {
	
	@Inject
	private AchievementRepository repo;

	public Achievement createAchievement(Game game, GameAchievement gameAchiev) {
		Achievement achiev = new Achievement();
		achiev.set(game, gameAchiev);
		repo.save(achiev);
		return achiev;
	}

	public void update(Achievement achiev) {
		repo.saveAndFlush(achiev);
	}
	
	public List<Achievement> findEasiestLocked(Pageable page) {
		return repo.findByUnlockedAndGlobalUnlockPercNotNullOrderByGlobalUnlockPercDesc(false, page);
	}

	public List<Achievement> findHardestUnlocked(Pageable page) {
		return repo.findByUnlockedAndGlobalUnlockPercNotNullOrderByGlobalUnlockPercAsc(true, page);
	}
	
	public List<Achievement> findAll() {
		return repo.findAll();
	}
	
	public List<Achievement> findAllOrderByTimestamp(Integer pageNumber) {
		Calendar cal = GregorianCalendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, 1);
		DateUtils.truncate(cal, Calendar.DAY_OF_MONTH);
		
		for (int i = 0; i < pageNumber; i++) {
			cal.add(Calendar.WEEK_OF_YEAR, -2);
		}
		
		Date end = cal.getTime();
		cal.add(Calendar.WEEK_OF_YEAR, -2);
		Date start = cal.getTime();
		return repo.findByTimestampAfterAndTimestampBeforeOrderByTimestampDesc(start, end);
	}
	
	public BigInteger countByHourOfDayUnlocked(String hour) {
		return repo.countByHourOfDayUnlocked(hour);
	}
	
	/**
	 * @param day numeric value: Sat=7, Son=1, Mon=2...
	 * @return the number of achievements which were unlocked on the given day of week
	 */
	public BigInteger countByDayOfWeekUnlocked(String day) {
		return repo.countByDayOfWeekUnlocked(day);
	}
	
	public BigInteger countByTimeUnlocked(Date start, Date end) {
		return repo.countByTimeUnlocked(start, end);
	}

	public Achievement findFirst() {
		return repo.findFirst();
	}

	public Long count() {
		return repo.count();
	}
	
	public BigInteger countByUnlocked(boolean unlocked) {
		return repo.countByUnlocked(unlocked);
	}

	public Long countByUnlockedGamesPlayed(boolean unlocked) {
		return repo.countByUnlockedGamesPlayed(unlocked);
	}
}