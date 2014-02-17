package de.druz.web.persistance.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import de.druz.web.persistance.model.Game;

public interface GameRepository extends JpaRepository<Game, Long> {

	Game findByAppId(Integer appId);

	@Query(value="select game, avg(a.globalUnlockPerc), min(a.globalUnlockPerc) from Achievement a where a.globalUnlockPerc is not null group by a.game order by a.globalUnlockPerc DESC order by min(a.globalUnlockPerc) desc")
	List findEasiestToComplete();

	@Query(value="select game, avg(a.globalUnlockPerc), min(a.globalUnlockPerc) from Achievement a where a.globalUnlockPerc is not null and a.unlocked = false group by a.game order by a.globalUnlockPerc DESC order by min(a.globalUnlockPerc) desc")
	List findEasiestToCompleteHonoringUnlocked();

	@Query(value="select count(*) from Game g where g.stats.beaten = ?1")
	Long countBeaten(Boolean beaten);
	
	@Query(value="select count(*) from Game g where g.stats.totalPlaytime != null and g.stats.totalPlaytime > 0")
	Long countPlayed();
}
