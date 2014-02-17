package de.druz.web.comparator;

import java.util.Comparator;

public class EasiestGameComparator implements Comparator<Object[]> {

	@Override
	public int compare(Object[] arg0, Object[] arg1) {
		return ((Double) arg0[1]) < ((Double) arg1[1])? 1 : ((Double) arg0[1]) == ((Double) arg1[1])? 0 : -1;
	}

}
