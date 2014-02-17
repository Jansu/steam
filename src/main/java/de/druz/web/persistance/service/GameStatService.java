package de.druz.web.persistance.service;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.koraktor.steamcondenser.steam.community.GameStats;

import de.druz.web.persistance.model.GameStat;
import de.druz.web.persistance.repository.GameStatRepository;

@Service
@Transactional
public class GameStatService {
	
	@Inject
	private GameStatRepository repo;

	public GameStat createGameStat(GameStats gameStats) {
		GameStat stat = new GameStat();
		stat.set(gameStats);
		repo.save(stat);
		return stat;
	}

	public void update(GameStat stat) {
		repo.saveAndFlush(stat);
	}
	
	public List<GameStat> findAll() {
		return repo.findAll();
	}

	public Double getAvgAchievPerc() {
		return repo.getAvgAchievPerc();
	}
}
