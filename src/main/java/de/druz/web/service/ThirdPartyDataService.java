package de.druz.web.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.util.Locale;
import java.util.regex.Pattern;

import javax.inject.Inject;

import org.jsoup.Jsoup;
import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import de.druz.web.persistance.model.Game;
import de.druz.web.persistance.model.GameStat;
import de.druz.web.persistance.service.GameService;
import de.druz.web.persistance.service.GameStatService;

@Service
public class ThirdPartyDataService {

	// private static final String HOW_LONG_TO_BEAT_URL =
	// "http://www.howlongtobeat.com/search.php?t=games&s=%s";
	private static final String HOW_LONG_TO_BEAT_URL = "http://www.howlongtobeat.com/search_main.php?t=games&page=1&sorthead=name&sortd=Normal&plat=PC&detail=1";
	private static final String METACRITC_URL = "http://www.metacritic.com/game/pc/%s";

	private static final Pattern NONLATIN = Pattern.compile("[^\\w-]");
	private static final Pattern WHITESPACE = Pattern.compile("[\\s]");

	private static final Logger logger = LoggerFactory.getLogger(ThirdPartyDataService.class);

	@Inject
	private GameStatService gameStatService;
	@Inject
	private GameService gameService;

	public void updateMetaScore(Game game) throws IOException {
		if (game == null) {
			return;
		}
		logger.info("Starting Update of Metascores for {}", game);
		GameStat stat = game.getStats();
		if (stat == null) {
			stat = new GameStat();
			stat.setGame(game);
		}

		String url = String.format(METACRITC_URL, slugify(game.getName().toLowerCase()));

		Document doc = Jsoup.connect(url).get();
		Elements metaScoreEle = doc.getElementsByAttributeValue("property",
				"v:average");
		if (null != metaScoreEle) {
			String metaScore = metaScoreEle.text();
			if (StringUtil.isNumeric(metaScore)) {
				stat.setMetaScore(Integer.parseInt(metaScore));
			} else {
				logger.warn("MetaScore was not numeric: " + metaScore + " for Url " + url);
			}
		}

		Element userScoreEle = doc.select(".section.product_scores .side_details .avguserscore .score_value").first();
		if (null != userScoreEle) {
			String userScore = userScoreEle.text();
			Float parsed = Float.parseFloat(userScore);
			if (null != parsed) {
				stat.setUserMetaScore(parsed);
			} else {
				logger.warn("User Score was not numeric: " + userScore);
			}
		}

		stat.setMetacriticUrl(url);
		gameStatService.update(stat);
		game.setStats(stat);
		gameService.update(game);
		logger.info("Ended Update of Metascores for {}", game);
	}

	public void updateHowLongToBeat(Game game) throws Exception {
//		if (game == null) {
//			return;
//		}
//		logger.info("Starting Update of HowLongToBeat for {}", game);
//		GameStat stat = game.getStats();
//		if (stat == null) {
//			stat = new GameStat();
//			stat.setGame(game);
//		}
//
//		String name = cleanName(game.getName());
//		try {
//			updateHowLongToBeat(name, stat);
//		} catch (Exception e) {
//			if (game.getName().contains(":")) {
//				logger.warn("Failed to update HowLongToBeat for game " + name + ", trying again with cleaned name");
//				name = cleanName(game.getName().replaceAll(":.*", ""));
//				updateHowLongToBeat(name, stat);
//			} else {
//				throw e;
//			}
//		}
//
//		gameStatService.update(stat);
//		game.setStats(stat);
//		gameService.update(game);
//		logger.info("Ended Update of HowLongToBeat for {}", game);
	}

	private void updateHowLongToBeat(String name, GameStat stat) throws IOException {
		String url = HOW_LONG_TO_BEAT_URL;

		Document doc = Jsoup.connect(url).data("queryString", name )
				.post();
//		logger.info(doc.html());
		Element firstSearchResult = doc.select(".gamelist_details").first();
		String mainStory = firstSearchResult
				.getElementsMatchingOwnText("Main Story").first()
				.siblingElements().first().text();
		String complete = firstSearchResult
				.getElementsMatchingOwnText("Completionist").first()
				.siblingElements().first().text();

		if (!StringUtil.isBlank(mainStory)) {
			Integer minutesToBeat = parseMinutes(mainStory);
			stat.setMinutesToBeat(minutesToBeat);
		}

		if (!StringUtil.isBlank(complete)) {
			Integer minutesToBeat = parseMinutes(complete);
			stat.setMinutesToBeatCompletely(minutesToBeat);
//		} else if (null != stat.getMinutesToBeat()) {
//			stat.setMinutesToBeatCompletely(stat.getMinutesToBeat());
		}

		Element linkEle = doc.select(".gamelist_details a").first();
		String detailUrl = linkEle.attr("abs:href");;
		stat.setHowLongToBeatUrl(detailUrl);
	}

	private String cleanName(String name) {
		return name.replaceAll(" - ", " ").replaceAll("&", " ").replaceAll(":", " ");
	}

	private Integer parseMinutes(String timeString) {
		Integer minutesToBeat = 0;
		if (timeString.contains("Mins")) {
			String cleaned = timeString.replaceAll("Mins", "").replaceAll("Min", "").trim();
			minutesToBeat = Integer.parseInt(cleaned);
		} else {
			boolean add30Min = timeString.contains("½");
			String cleaned = timeString.replaceAll("Hours", "").replaceAll("Hour", "").replaceAll("½", "").trim();
			minutesToBeat = Integer.parseInt(cleaned) * 60;
			if (add30Min) {
				minutesToBeat += 30;
			}
		}
		return minutesToBeat;
	}

	private String urify(String name) {
		try {
			return URLEncoder.encode(name, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			logger.error("Failed to URL encode " + name, e);
		}
		return name;
	}

	public static String slugify(String input) {
		String nowhitespace = WHITESPACE.matcher(input).replaceAll("-");
		String normalized = Normalizer.normalize(nowhitespace, Form.NFD);
		String slug = NONLATIN.matcher(normalized).replaceAll("");
		return slug.toLowerCase(Locale.ENGLISH);
	}
}
