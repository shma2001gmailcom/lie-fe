package org.misha.controller;

import org.apache.commons.lang3.StringUtils;
import org.misha.views.PolynomialObject;
import org.misha.service.ExpandService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * author: misha
 * Date: 5/26/14
 * Time: 10:20 PM
 */
@Controller
@RequestMapping("/")
final class ExpandController {
    private final ExpandService expandService;
    @Value("#{applicationProperties['input.error']}")
    private String inputError;
    @Inject
    public ExpandController(@Named("expandService") ExpandService expandService) {
        this.expandService = expandService;
    }

    /**
     * /input.jsp/form2/@action
     */
    @RequestMapping("/expand")
    public String printResult(
            @ModelAttribute("polynomialObject") final PolynomialObject polynomialObject,
            final ModelMap model
    ) {
        final String given = polynomialObject.getValue().trim();
        final String answer = expandService.expand(given);
        if (StringUtils.isEmpty(answer)) {
            model.addAttribute("error", inputError);
        } else {
            model.addAttribute("given", given);
            model.addAttribute("answer", answer);
        }
        return "expand";
    }
}
