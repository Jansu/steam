package de.druz.web.controller;

import java.util.ArrayList;
import java.util.Arrays;

import org.springframework.ui.Model;

public abstract class BaseController {

	protected void addSorterOptions(Model model, String ... strings) {
		model.addAttribute("sorterOptions", new ArrayList<String>(Arrays.asList(strings)));
	}
}
