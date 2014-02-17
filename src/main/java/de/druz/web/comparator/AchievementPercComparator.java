package de.druz.web.comparator;

import java.util.Comparator;

import de.druz.web.persistance.model.Achievement;

public class AchievementPercComparator implements Comparator<Achievement> {

	@Override
	public int compare(Achievement o1, Achievement o2) {
		if (o1 == null || o1.getGlobalUnlockPerc() == null) {
			if (o2 == null || o2.getGlobalUnlockPerc() == null) {
				return o1.getId().compareTo(o2.getId());
			}
			return 1;
		} else if (o2 == null || o2.getGlobalUnlockPerc() == null) {
			return -1;
		}
		return o2.getGlobalUnlockPerc().compareTo(o1.getGlobalUnlockPerc());
	}

}
