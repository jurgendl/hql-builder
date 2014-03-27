package org.tools.hqlbuilder.servicecontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.tools.hqlbuilder.common.HqlService;
import org.tools.hqlbuilder.common.QueryParameter;

@Controller
public class PojoController {
    private static final String VIEW_ID = "marshallingView";

    @Autowired
    private HqlService hqlService;

    /**
     * @see http://localhost/hqlbuilder/rest/get/Pojo/1
     */
    @RequestMapping(method = RequestMethod.GET, value = "/get/{pojo}/{id}")
    public ModelAndView getEmployee(@PathVariable String pojo, @PathVariable Long id) {
        Object rv = hqlService.execute("from " + pojo + " where id=:id", new QueryParameter("id", "id", id)).getResults().get(0);
        return new ModelAndView(VIEW_ID, BindingResult.MODEL_KEY_PREFIX + "pojo", rv);
    }
}