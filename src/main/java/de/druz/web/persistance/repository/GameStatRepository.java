package de.druz.web.persistance.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import de.druz.web.persistance.model.GameStat;

public interface GameStatRepository extends JpaRepository<GameStat, Long> {

	@Query(nativeQuery= true, value="select avg(achievPerc) from GameStat where achievPerc > 0.00001")
	Double getAvgAchievPerc();

}
