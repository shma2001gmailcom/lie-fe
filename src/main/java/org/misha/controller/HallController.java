package org.misha.controller;

import org.apache.commons.lang3.StringUtils;
import org.misha.views.PolynomialObject;
import org.misha.service.HallService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Author: mshevelin
 * Date: 5/19/14
 * Time: 11:48 AM
 */

@Controller
@RequestMapping("/")
final class HallController {
    private final HallService service;
    @Value("#{applicationProperties['input.error']}")
    private String inputError;
    @Inject
    public HallController(@Named("hallService") HallService service) {
        this.service = service;
    }

    /**
     * /input.jsp/form/@action
     */
    @RequestMapping("/hall-result")
    public String printResult(
            @ModelAttribute("polynomialObject") final PolynomialObject polynomialObject,
            final ModelMap model
    ) {
        polynomialObject.correctLeadingSign();
        final String given = polynomialObject.getValue().trim();
        final String answer = service.getHall(given);
        if (StringUtils.isEmpty(answer)) {
            model.addAttribute("error", inputError);
        } else {
            model.addAttribute("answer", given + " = " + answer);
        }
        return "hall-result";
    }
}
