/*
 * Copyright (c) 2014. Misha's property, all rights reserved.
 */

package org.misha.controller;

import org.apache.commons.lang3.StringUtils;
import org.misha.domain.EndoObject;
import org.misha.repository.MonomialService;
import org.misha.service.JacobiService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Author: mshevelin
 * Date: 5/30/14
 * Time: 4:56 PM
 */
@Controller
@RequestMapping("/")
final class JacobiController {
    private static final String JACOBI_MATRIX_OF = "Jacobi matrix for endomorphism ";
    @Inject
    @Named("jacobiService")
    private JacobiService service;

    @Value("#{applicationProperties['input.error']}")
    private String inputError;

    @Inject
    private MonomialService monomialService;
    /**
     * /input.jsp/form4/@action
     */
    @RequestMapping("/jacobi-result")
    public String printResult(
            @ModelAttribute("endoObject") final EndoObject endoObject, final ModelMap model
    ) {
        final String monomial = monomialService.findById(25L).toString();
        model.addAttribute("monomial", monomial);
        final String given = endoObject.getValue();
        final String answer = service.foxToHtml(given);
        if (StringUtils.isEmpty(answer)) {
            model.addAttribute("error", inputError);
        } else {
            model.addAttribute("answer", JACOBI_MATRIX_OF + given + " is <br>" + answer);
            model.addAttribute("txtAnswer", JACOBI_MATRIX_OF + given + " is \n\n" + service.foxToTxt(given)
            );
        }
        return "jacobi-result";
    }
}
