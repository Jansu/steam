package de.druz.web.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.inject.Inject;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.github.koraktor.steamcondenser.exceptions.SteamCondenserException;
import com.github.koraktor.steamcondenser.steam.community.GameAchievement;
import com.github.koraktor.steamcondenser.steam.community.GameStats;
import com.github.koraktor.steamcondenser.steam.community.SteamGame;
import com.github.koraktor.steamcondenser.steam.community.SteamId;

import de.druz.web.SystemModuleConfiguration;
import de.druz.web.persistance.model.Achievement;
import de.druz.web.persistance.model.Game;
import de.druz.web.persistance.model.GameStat;
import de.druz.web.persistance.service.AchievementService;
import de.druz.web.persistance.service.GameService;
import de.druz.web.persistance.service.GameStatService;
import de.druz.web.service.ThirdPartyDataService;

@Controller
public class UpdateController {

	private static final Logger logger = LoggerFactory.getLogger(UpdateController.class);

	@Inject
	private GameService gameService;
	@Inject
	private GameStatService gameStatService;
	@Inject
	private AchievementService achievService;
	@Inject
	private SystemModuleConfiguration config;
	@Inject
	private ThirdPartyDataService thirdPartyService;

	
	@RequestMapping(method = { RequestMethod.GET }, value = "/update/recent", produces = { "application/json" })
	public String updateRecent(Locale locale, Model model) throws SteamCondenserException {
		SteamId id = SteamId.create(config.getSteamId());

		try {
			List<Integer> recentlyPlayedGames = getRecentlyPlayedGames(config.getSteamIdLong());
			for (Integer appId : recentlyPlayedGames) {
				SteamGame steamGame = id.getGames().get(appId);
				try {
					updateGame(id, steamGame);
				} catch (Exception e) {
					logger.error("Failed to update recently played game " + steamGame.getName(), e);
				}
			}
		} catch (Exception e) {
			logger.error("Failed to update recently played games", e);
		}
		
		return "home";
	}
	
	private List<Integer> getRecentlyPlayedGames(String steamId) throws JsonParseException, JsonMappingException, IOException {
		List<Integer> appIds = new ArrayList<Integer>();
		String url = String.format("http://api.steampowered.com/IPlayerService/GetRecentlyPlayedGames/v0001/?key=%s&steamid=%s&format=json", config.getApiKey(), steamId);

		ObjectMapper mapper = new ObjectMapper();
		JsonNode rootNode = mapper.readValue(new URL(url), JsonNode.class);

		Iterator<JsonNode> iter = rootNode.get("response").get("games").getElements();
		while (iter.hasNext()) {
			JsonNode jsonNode = (JsonNode) iter.next();
			Integer appId = jsonNode.findValue("appid").asInt(-1);
			if (appId >= 0) {
				appIds.add(appId);
			}
		}
		
		return appIds;
	}

	@RequestMapping(method = { RequestMethod.GET }, value = "/update/{appId}", produces = { "application/json" })
	public Game update(@PathVariable Integer appId, Locale locale, Model model) throws SteamCondenserException {
		SteamId id = SteamId.create(config.getSteamId());
		Game game = gameService.findByAppId(appId);
		
		Collection<SteamGame> steamGames = id.getGames().values();
		for (SteamGame steamGame : steamGames) {
			if (steamGame.getAppId() != game.getAppId()) {
				continue;
			}
			try {
				updateGame(id, steamGame);
			} catch (Exception e) {
				logger.error("Failed to load userstats for game " + steamGame.getName(), e);
				return null;
			}
		}
		
		return game;
	}
	
	@RequestMapping(value = "/update/all", method = RequestMethod.GET)
	public String updateAll(Locale locale, Model model) throws SteamCondenserException {
		SteamId id = SteamId.create(config.getSteamId());
		
		List<Game> games = new ArrayList<Game>();
		Collection<SteamGame> steamGames = id.getGames().values();
		for (SteamGame steamGame : steamGames) {
			try {
				updateGame(id, steamGame);
			} catch (Exception e) {
				logger.error("Failed to load userstats for game " + steamGame.getName(), e);
			}
		}

		model.addAttribute("id", id);
		model.addAttribute("games", games);
		
		return "home";
	}
	
	@RequestMapping(value = "/update/allMetaScores", method = RequestMethod.GET)
	public String updateAllMetaScores(Locale locale, Model model) throws SteamCondenserException {
		List<Game> games = gameService.findAll();
		for (Game game : games) {
			try {
				thirdPartyService.updateMetaScore(game);
			} catch (Exception e) {
				logger.error("Failed to get Metascores for Game " + game.getName(), e);
			}
		}
		return "home";
	}

	@RequestMapping(value = "/update/allHowLongToBeat", method = RequestMethod.GET)
	public String updateAllHowLongToBeat(Locale locale, Model model) throws SteamCondenserException {
		List<Game> games = gameService.findAll();
		for (Game game : games) {
			try {
				thirdPartyService.updateHowLongToBeat(game);
			} catch (Exception e) {
				logger.error("Failed to get How Long to Beat Data for Game " + game.getName(), e);
			}
		}
		return "home";
	}
	
	private void updateGame(SteamId id, SteamGame steamGame)
			throws SteamCondenserException {
		Game game = gameService.findByAppId(steamGame.getAppId());
		if (null == game) {
			game = gameService.createGame(steamGame);
		} else {
			game.set(steamGame);
		}

		logger.info("Fetching Userstats for Game {} - {}.",steamGame.getName(), steamGame);
		
//		steamGame.clearCache();
		GameStats gameStats = steamGame.getUserStats(config.getSteamId());
		GameStat stat = game.getStats();
		if (stat == null ) {
			stat = new GameStat(); 
		}
		game.setStats(stat);
		if (null != gameStats) {
			try {
				stat.set(gameStats);
				List<GameAchievement> gameAchievs = gameStats.getAchievements();
				if (0 < gameAchievs.size()) {
					SortedSet<Achievement> achievs = game.getAchievements();
					if (achievs == null) {
						achievs = new TreeSet<Achievement>();
					}
					Map<String, Double> globalStatsTemp = gameAchievs.get(0).getGlobalPercentages(steamGame.getAppId());
					Map<String, Double> globalStats = new HashMap<String, Double>();
					for (String key : globalStatsTemp.keySet()) {
						globalStats.put(key.toLowerCase(), globalStatsTemp.get(key));
					}
					
					for (GameAchievement gameAchievement : gameAchievs) {
						Achievement achiev = null;
						for (Achievement achievement : achievs) {
							if (achievement.getApiName().equals(gameAchievement.getApiName())) {
								achiev = achievement;
								break;
							}
						}
						if (achiev == null) {
							achiev = achievService.createAchievement(game, gameAchievement);
						}
						achiev.set(game, gameAchievement);
						achievs.add(achiev);
					}
					for (Achievement achiev : achievs) {
						Double perc = globalStats.get(achiev.getApiName().toLowerCase());
//						if (perc == null) {
//							perc = globalStats.get(achiev.getApiName().toUpperCase());
//						}
						achiev.setGlobalUnlockPerc(perc);
						achievService.update(achiev);
					}
					game.setAchievements(achievs);
				}
			} catch (Exception e) {
				logger.error("Failed to load achievements for game " + steamGame.getName(), e);
			}
		}
		stat.setTotalPlaytime(id.getTotalPlaytime(steamGame.getAppId()));
		gameStatService.update(stat);
		gameService.update(game);
		
//		try {
//			thirdPartyService.updateMetaScore(game);
//		} catch (IOException e) {
//			logger.error("Failed to update Metascores for game " + steamGame.getName(), e);
//		}
		try {
			thirdPartyService.updateHowLongToBeat(game);
		} catch (Exception e) {
			logger.error("Failed to update HowLongToBeat for game " + steamGame.getName(), e);
		}
	}
}
