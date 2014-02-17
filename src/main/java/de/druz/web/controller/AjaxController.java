package de.druz.web.controller;

import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.koraktor.steamcondenser.exceptions.SteamCondenserException;

import de.druz.web.SystemModuleConfiguration;
import de.druz.web.persistance.model.Game;
import de.druz.web.persistance.service.AchievementService;
import de.druz.web.persistance.service.GameService;
import de.druz.web.persistance.service.GameStatService;
import de.druz.web.persistance.service.StatAchievementDayService;
import de.druz.web.vo.AchievCountByDayVo;
import de.druz.web.vo.KeyValueVo;

@Controller
@RequestMapping("/ajax")
public class AjaxController {

	private static final Logger logger = LoggerFactory.getLogger(AjaxController.class);

	@Inject
	private GameService gameService;
	@Inject
	private GameStatService gameStatService;
	@Inject
	private AchievementService achievService;
	@Inject
	private StatAchievementDayService statAchievDayService;
	@Inject
	private SystemModuleConfiguration config;
	
	@RequestMapping(method = { RequestMethod.GET }, value = "/toggleBeaten/{appId}", produces = { "application/json" })
	@ResponseBody
	public Boolean toggleBeaten(@PathVariable Integer appId, Locale locale, Model model) throws SteamCondenserException {
		Game game = gameService.findByAppId(appId);

		logger.info("Toggling beaten flag for {}", game);
		game.getStats().toggleBeaten();
		gameStatService.update(game.getStats());
		
		return game.getStats().getBeaten();
	}

	@RequestMapping(method = { RequestMethod.GET }, value = "/stat/gameCount", produces = { "application/json" })
	@ResponseBody
	public Object gameCount(Locale locale, Model model) throws SteamCondenserException {
		Long count = gameService.count();
		Long beaten = gameService.countBeaten();
		Long played = gameService.countPlayed();
		
		ArrayList<KeyValueVo> data = new ArrayList<KeyValueVo>();
		data.add(new KeyValueVo("beaten", beaten));
		data.add(new KeyValueVo("played", played - beaten));
		data.add(new KeyValueVo("unplayed", count - played));
		return data;
	}

	@RequestMapping(method = { RequestMethod.GET }, value = "/stat/achievCount", produces = { "application/json" })
	@ResponseBody
	public ArrayList<KeyValueVo> achievCount(Locale locale, Model model) throws SteamCondenserException {
		Long count = achievService.count();
		BigInteger unlocked = achievService.countByUnlocked(true);
		Long lockedGamesPlayed = achievService.countByUnlockedGamesPlayed(false);
		Long locked = count - unlocked.longValue();
		ArrayList<KeyValueVo> data = new ArrayList<KeyValueVo>();
//		data.add(new KeyValueVo("count", count));
		data.add(new KeyValueVo("unlocked", unlocked));
		data.add(new KeyValueVo("locked Games Played", lockedGamesPlayed));
		data.add(new KeyValueVo("locked", locked-lockedGamesPlayed));
		return data;
	}

	@RequestMapping(method = { RequestMethod.GET }, value = "/stat/achievCountByDay", produces = { "application/json" })
	@ResponseBody
	public AchievCountByDayVo achievCountByDay(Locale locale, Model model) throws SteamCondenserException {
		AchievCountByDayVo data = new AchievCountByDayVo();
		
		data.setDays(statAchievDayService.findAll());
		statAchievDayService.updateAchievCountByDay();
		Date firstDate = statAchievDayService.findFirstDate();
		Date lastDate = statAchievDayService.findLastDate();
		Calendar cal = GregorianCalendar.getInstance();
		cal.setTime(firstDate);
		data.setFirstYear(cal.get(Calendar.YEAR));
		cal.setTime(lastDate);
		data.setLastYear(cal.get(Calendar.YEAR));
		
		return data;
	}

	@RequestMapping(method = { RequestMethod.GET }, value = "/stat/achievCountByDayOfWeek", produces = { "application/json" })
	@ResponseBody
	public List<BigInteger> achievCountByDayOfWeek(Locale locale, Model model) throws SteamCondenserException {

		NumberFormat format = DecimalFormat.getInstance();
		format.setMinimumIntegerDigits(2);
		List<BigInteger> achievsByDay = new ArrayList<BigInteger>();
		for (Integer day = 1; day < 8; day++) {
			achievsByDay.add(achievService.countByDayOfWeekUnlocked(format.format(day)));
		}

		return achievsByDay;
	}	

	@RequestMapping(method = { RequestMethod.GET }, value = "/stat/achievCountByHourOfDay", produces = { "application/json" })
	@ResponseBody
	public List<BigInteger> achievCountByHourOfDay(Locale locale, Model model) throws SteamCondenserException {

		NumberFormat format = DecimalFormat.getInstance();
		format.setMinimumIntegerDigits(2);

		List<BigInteger> achievsByHour = new ArrayList<BigInteger>();
		for (Integer hour = 0; hour < 24; hour++) {
			achievsByHour.add(achievService.countByHourOfDayUnlocked(format.format(hour)));
		}

		return achievsByHour;
	}
	
}
