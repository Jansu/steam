package de.druz.web.persistance.service;

import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.inject.Inject;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.koraktor.steamcondenser.steam.community.SteamGame;

import de.druz.web.comparator.EasiestGameComparator;
import de.druz.web.persistance.model.Game;
import de.druz.web.persistance.repository.GameRepository;

@Service
@Transactional
public class GameService {
	
	@Inject
	private GameRepository repo;

	public Game createGame(SteamGame steamGame) {
		Game game = new Game();
		game.set(steamGame);
		repo.save(game);
		return game;
	}
	
	public void update(Game game) {
		repo.save(game);
	}
	
	public Game findByAppId(Integer appId) {
		return repo.findByAppId(appId);
	}

	public List<Game> findAll() {
		Sort sort = new Sort(Direction.DESC, "stats.hoursPlayed");
		return repo.findAll(sort );
	}
	
	public SortedSet<Game> findEasiestToComplete() {
		List games = repo.findEasiestToComplete();
		SortedSet<Game> set = new TreeSet(new EasiestGameComparator());
		set.addAll(games);
		return set;
	}
	
	public SortedSet<Game> findEasiestToCompleteHonoringUnlocked() {
		List games = repo.findEasiestToCompleteHonoringUnlocked();
		SortedSet<Game> set = new TreeSet(new EasiestGameComparator());
		set.addAll(games);
		return set;
	}

	public Long count() {
		return repo.count();
	}

	public Long countBeaten() {
		return repo.countBeaten(true);
	}

	public Long countPlayed() {
		return repo.countPlayed();
	}
}
