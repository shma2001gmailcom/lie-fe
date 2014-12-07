package org.misha.controller;

import org.apache.commons.lang3.StringUtils;
import org.misha.domain.EndoObject;
import org.misha.service.EndoService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * author: misha
 * Date: 5/22/14
 * Time: 8:54 PM
 */
@Controller
@RequestMapping("/")
final class EndoController {
    @Inject
    @Named("endoService")
    private EndoService service;

    @Value("#{applicationProperties['input.error']}")
    private String inputError;

    /**
     * /input.jsp/form1/@action
     */

    @RequestMapping("/endo-result")
    public String printResult(
            @ModelAttribute("endoObject") final EndoObject endoObject,
            @ModelAttribute("serviceName") final String serviceName, final ModelMap model
    ) {
        model.addAttribute("serviceName", "endoService");
        final String given = endoObject.getValue();
        final String answer = service.getProductOf(given);
        if (StringUtils.isEmpty(answer)) {
            model.addAttribute("error", inputError);
        } else {
            model.addAttribute("answer", given + "<br> = " + answer);
        }
        return "endo-result";
    }
}
