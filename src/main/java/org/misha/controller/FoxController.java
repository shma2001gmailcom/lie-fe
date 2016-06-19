package org.misha.controller;

import org.apache.commons.lang3.StringUtils;
import org.misha.views.PolynomialObject;
import org.misha.service.FoxService;
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
 * Time: 10:57 PM
 */
@Controller
@RequestMapping("/")
final class FoxController {
    @Inject
    @Named("foxService")
    private FoxService foxService;

    @Value("#{applicationProperties['input.error']}")
    private String inputError;

    /**
     * /input.jsp/form3/@action
     */
    @RequestMapping("/fox")
    public String printResult(
            @ModelAttribute("polynomialObject") final PolynomialObject polynomialObject,
            final ModelMap model
    ) {
        final String given = polynomialObject.getValue().trim();
        final String answer = foxService.fox(given);
        if (StringUtils.isEmpty(answer)) {
            model.addAttribute("error", inputError);
        } else {
            model.addAttribute("answer", "The Fox Derivative of " + given + "\n is \n" + answer);
        }
        return "fox";
    }
}
