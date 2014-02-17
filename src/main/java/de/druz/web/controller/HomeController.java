package de.druz.web.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.inject.Inject;

import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.github.koraktor.steamcondenser.exceptions.SteamCondenserException;
import com.github.koraktor.steamcondenser.steam.community.SteamId;

import de.druz.web.SystemModuleConfiguration;
import de.druz.web.comparator.DateComparator;
import de.druz.web.persistance.model.Achievement;
import de.druz.web.persistance.model.Game;
import de.druz.web.persistance.service.AchievementService;
import de.druz.web.persistance.service.GameService;
import de.druz.web.persistance.service.GameStatService;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController extends BaseController {

	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

	@Inject
	private GameService gameService;
	@Inject
	private GameStatService gameStatService;
	@Inject
	private AchievementService achievService;
	@Inject
	private SystemModuleConfiguration config;

	@RequestMapping(value = "/stats", method = RequestMethod.GET)
	public String stats(Locale locale, Model model) {
		System.out.println("Avg Achiev Perc: " + gameStatService.getAvgAchievPerc());
		return "stats";
	}
	
	@RequestMapping(value = "/timeline/{page}", method = RequestMethod.GET)
	public String timeline(@PathVariable Integer page, Locale locale, Model model) throws SteamCondenserException {
		List<Achievement> achievs = achievService.findAllOrderByTimestamp(page);

		Set<Date> days = new TreeSet<Date>(new DateComparator());
		Map<Date, List<Achievement>> data = new HashMap<Date, List<Achievement>>();
		for (Achievement achievement : achievs) {
			Date day = DateUtils.truncate(achievement.getTimestamp(), Calendar.DAY_OF_MONTH);
			days.add(day);
			if (data.get(day) == null) {
				List<Achievement> tempAchievs = new ArrayList<Achievement>();
				tempAchievs.add(achievement);
				data.put(day, tempAchievs);
			} else {
				data.get(day).add(achievement);
			}
		}
		model.addAttribute("days", days);
		model.addAttribute("data", data);
		return "timeline";
	}
	
	@RequestMapping(value = "/games", method = RequestMethod.GET)
	public String home(Locale locale, Model model) throws SteamCondenserException {
		
		List<Game> games = gameService.findAll();
		addSorterOptions(model, 
				"name", "Name", 
				"achievCount", "Achievements Count",
				"achievDone", "Achievements Done",
				"achievLeft", "Achievements Left",
				"achievPerc", "Achievements Percantage",
				"totalPlaytime", "Total Playtime",
				"hoursPlayed", "Playtime last 2 weeks",
				"minutesToBeat", "Time to beat",
				"minutesToBeatRemaining", "Remaining time to beat",
				"minutesToBeatCompletely", "Time to beat 100%",
				"minutesToBeatCompletelyRemaining", "Remaining time to beat 100%",
				"metaScore", "Metascore",
				"userMetaScore", "User Score"
				);
		model.addAttribute("games", games);
		try {
			SteamId id = SteamId.create(config.getSteamId());
			model.addAttribute("id", id);
		} catch (Exception e) {
			logger.error("Unable to create SteamId", e);
		}
		
		return "home";
	}

	@RequestMapping(value = "/games/easiestOverall", method = RequestMethod.GET)
	public String easiestOverallGames(Locale locale, Model model) throws SteamCondenserException {
		model.addAttribute("games", gameService.findEasiestToComplete());
		addSorterOptions(model, 
				"avgAchiev", "Avg. Achievement Difficulty", 
				"hardestAchiev", "Hardest Achievement",
				"achievDone", "Achievements Done");
		return "easiestGames";
	}

	@RequestMapping(value = "/games/easiest", method = RequestMethod.GET)
	public String easiestGames(Locale locale, Model model) throws SteamCondenserException {
		model.addAttribute("games", gameService.findEasiestToCompleteHonoringUnlocked().toArray());
		addSorterOptions(model, 
				"avgAchiev", "avg. Achievement Difficulty", 
				"hardestAchiev", "hardest Achievement");
		return "easiestGames";
	}
	
	@RequestMapping(value = "/game/{appId}", method = RequestMethod.GET)
	public String game(@PathVariable Integer appId, Locale locale, Model model) throws SteamCondenserException {
		model.addAttribute("game", gameService.findByAppId(appId));
		addSorterOptions(model, 
				"achievDate", "Unlock Date",
				"achievPerc", "Achievements Percantage");
		return "game";
	}
	
	@RequestMapping(value = "/achievements/easiest/{page}", method = RequestMethod.GET)
	public String easiestAchievements(@PathVariable Integer page, Locale locale, Model model) throws SteamCondenserException {
		PageRequest pageRequest = new PageRequest(page, 100);
		List<Achievement> easiestAchievs = achievService.findEasiestLocked(pageRequest);
		model.addAttribute("achievs", easiestAchievs );
		
		return "achievements";
	}
	
	@RequestMapping(value = "/achievements/hardest/{page}", method = RequestMethod.GET)
	public String hardestAchievements(@PathVariable Integer page, Locale locale, Model model) throws SteamCondenserException {
		PageRequest pageRequest = new PageRequest(page, 100);
		List<Achievement> easiestAchievs = achievService.findHardestUnlocked(pageRequest);
		model.addAttribute("achievs", easiestAchievs );
		
		return "achievements";
	}
}
