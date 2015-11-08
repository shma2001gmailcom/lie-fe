/*
 * Copyright (c) 2014. Misha's property, all rights reserved.
 */

package org.misha.controller;

import org.misha.domain.EndoObject;
import org.misha.domain.PolynomialObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * front controller
 */
@Controller
@RequestMapping("/")
final class InputController {
    @Value("#{applicationProperties['welcome.message']}")
    private String welcomeMessage;
    @Value("#{applicationProperties['polynomial.message']}")
    private String polynomialMessage;
    @Value("#{applicationProperties['hall-base']}")
    private String hallBase;
    @Value("#{applicationProperties['endo.message']}")
    private String endoMessage;
    @Value("#{applicationProperties['expand.message']}")
    private String expandMessage;
    @Value("#{applicationProperties['fox.message']}")
    private String foxMessage;
    @Value("#{applicationProperties['jacobi.message']}")
    private String jacobiMessage;

    @RequestMapping(method = RequestMethod.GET)
    public String printPrompt(
            @ModelAttribute("polynomialObject") final PolynomialObject polynomialObject,
            @ModelAttribute("endoObject") final EndoObject endoObject, final ModelMap model
    ) {
        model.addAttribute("welcomeMessage", welcomeMessage);
        model.addAttribute("polynomialMessage", polynomialMessage);
        model.addAttribute("endoMessage", endoMessage);
        model.addAttribute("expandMessage", expandMessage);
        model.addAttribute("foxMessage", foxMessage);
        model.addAttribute("jacobiMessage", jacobiMessage);
        model.addAttribute("hallBase", hallBase);
        return "input";
    }
}