package de.druz.web.persistance.repository;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import de.druz.web.persistance.model.StatAchievementDay;

public interface StatAchievementDayRepository extends JpaRepository<StatAchievementDay, Long> {
	
	@Query(nativeQuery=true, value="select date from StatAchievementDay order by date ASC limit 1")
	Date findFirstDate();

	@Query(nativeQuery=true, value="select date from StatAchievementDay order by date DESC limit 1")
	Date findLastDate();

	StatAchievementDay findByDate(Date date);

}