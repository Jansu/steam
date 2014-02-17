package de.druz.web.persistance.repository;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import de.druz.web.persistance.model.Achievement;

public interface AchievementRepository extends JpaRepository<Achievement, Long> {

	List<Achievement> findByUnlockedAndGlobalUnlockPercNotNullOrderByGlobalUnlockPercDesc(boolean unlocked, Pageable page);
	
	List<Achievement> findByUnlockedAndGlobalUnlockPercNotNullOrderByGlobalUnlockPercAsc(boolean unlocked, Pageable page);
	
	List<Achievement> findByTimestampAfterAndTimestampBeforeOrderByTimestampDesc(Date start, Date end);
	
	@Query(nativeQuery= true, value="select count(*) from Achievement a where a.timestamp REGEXP CONCAT('[0-9]{4}-[0-9]{2}-[0-9]{2} ', ?1, ':[0-9]{2}:[0-9]{2}.*');")
	BigInteger countByHourOfDayUnlocked(String hour);
	
	@Query(nativeQuery= true, value="select count(*) from Achievement a where DAYOFWEEK(a.timestamp) = ?1")
	BigInteger countByDayOfWeekUnlocked(String day);

	@Query(nativeQuery= true, value="select count(*) from Achievement where timestamp >= ?1 and timestamp < ?2")
	BigInteger countByTimeUnlocked(Date start, Date end);

	@Query(nativeQuery= true, value="select count(*) from Achievement where unlocked = ?1")
	BigInteger countByUnlocked(boolean unlocked);

	@Query(nativeQuery= false, value="select count(*) from Achievement where unlocked = ?1 and game.stats.totalPlaytime > 0")
	Long countByUnlockedGamesPlayed(boolean unlocked);

	@Query(nativeQuery= true, value="select * from Achievement a where timestamp is not null order by timestamp ASC limit 1")
	Achievement findFirst();
}