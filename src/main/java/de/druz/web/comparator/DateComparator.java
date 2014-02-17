package de.druz.web.comparator;

import java.util.Comparator;
import java.util.Date;

public class DateComparator implements Comparator<Date> {

	@Override
	public int compare(Date o1, Date o2) {
		return o2.compareTo(o1);
	}
	
//	@Override
//	public int compare(Date o1, Date o2) {
//		if (o1 == null) {
//			if (o2 == null) {
//				return 0;
//			}
//			return -1;
//		} else if (o2 == null || o2.getGlobalUnlockPerc() == null) {
//			return 1;
//		}
//		return o2.getGlobalUnlockPerc().compareTo(o1.getGlobalUnlockPerc());
//	}
}
