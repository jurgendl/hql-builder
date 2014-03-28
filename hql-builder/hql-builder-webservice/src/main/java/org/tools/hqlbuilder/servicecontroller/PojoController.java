package org.tools.hqlbuilder.servicecontroller;

import java.io.StringReader;
import java.util.List;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.tools.hqlbuilder.common.HqlService;
import org.tools.hqlbuilder.common.QueryParameter;

@Controller
public class PojoController {
    /** spring bean id */
    private static final String VIEW_ID = "marshallingView";

    @Autowired
    private HqlService hqlService;

    @Autowired
    private Jaxb2Marshaller jaxb2Mashaller;

    @SuppressWarnings("unchecked")
    protected <T> List<T> result(String pojo, Long id) {
        return (List<T>) hqlService.execute("from " + pojo + " where id=:id", new QueryParameter("id", "id", id)).getResults();
    }

    public Object unmarshall(String body) {
        Source source = new StreamSource(new StringReader(body));
        Object entity = jaxb2Mashaller.unmarshal(source);
        return entity;
    }

    /**
     * @see [get] http://localhost:80/hqlbuilder/rest/get/{pojo}/{id}
     */
    @RequestMapping(method = RequestMethod.GET, value = "/get/{pojo}/{id}")
    public <T> ModelAndView get(@PathVariable String pojo, @PathVariable Long id) {
        List<T> results = result(pojo, id);
        return new ModelAndView(VIEW_ID, BindingResult.MODEL_KEY_PREFIX + pojo, results.size() == 0 ? null : results.get(0));
    }

    /**
     * @see [delete] http://localhost:80/hqlbuilder/rest/delete/{pojo}/{id}
     */
    @RequestMapping(method = RequestMethod.DELETE, value = "/delete/{pojo}/{id}")
    public <T> void delete(@PathVariable String pojo, @PathVariable Long id) {
        List<T> results = result(pojo, id);
        hqlService.delete(results.get(0));
    }

    /**
     * @see [put] http://localhost:80/hqlbuilder/rest/put/{pojo}/{id} [body]
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/put/{pojo}/{id}")
    public void put(@SuppressWarnings("unused") @PathVariable String pojo, @SuppressWarnings("unused") @PathVariable Long id, @RequestBody String body) {
        Object entity = unmarshall(body);
        hqlService.save(entity);
    }

    /**
     * @see [post] http://localhost:80/hqlbuilder/rest/post/{pojo}/{id} [body]
     */
    @RequestMapping(method = RequestMethod.POST, value = "/post/{pojo}/{id}")
    public void post(@SuppressWarnings("unused") @PathVariable String pojo, @SuppressWarnings("unused") @PathVariable Long id,
            @RequestBody String body) {
        Object entity = unmarshall(body);
        hqlService.save(entity);
    }
}