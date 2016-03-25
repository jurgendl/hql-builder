package org.tools.hqlbuilder.service;

import java.util.HashMap;
import java.util.Map;

import org.hibernate.cfg.Configuration;
import org.hibernate.dialect.function.StandardSQLFunction;
import org.hibernate.type.Type;
import org.jhaws.common.lang.ObjectWrapper;

public class AdditionalSqlFunctionsPostProcessor implements ConfigurationPostProcessor {
    private Map<String, String> standardSQLFunctions = new HashMap<String, String>();

    private Map<String, Type> types;

    /**
     * @see org.tools.hqlbuilder.service.ConfigurationPostProcessor#postProcessConfiguration(org.hibernate.cfg.Configuration)
     */
    @Override
    public void postProcessConfiguration(Configuration config) {
        for (Map.Entry<String, String> simpleSqlFunction : standardSQLFunctions.entrySet()) {
            String name = simpleSqlFunction.getKey();
            String type = simpleSqlFunction.getValue();
            if (type == null || "null".equals(type)) {
                type = "object";
            }
            config.addSqlFunction(name.toLowerCase(), new StandardSQLFunction(name, getTypes().get(simpleSqlFunction.getValue())));
        }
    }

    public Map<String, String> getStandardSQLFunctions() {
        return this.standardSQLFunctions;
    }

    public void setStandardSQLFunctions(Map<String, String> standardSQLFunctions) {
        this.standardSQLFunctions = standardSQLFunctions;
    }

    @SuppressWarnings("unchecked")
    public Map<String, Type> getTypes() {
        if (types == null) {
            try {
                types = (Map<String, Type>) new ObjectWrapper(Class.forName("org.hibernate.type.TypeFactory")).get("BASIC_TYPES");
            } catch (Exception ex) {
                try {
                    types = (Map<String, Type>) new ObjectWrapper(Class.forName("org.hibernate.type.BasicTypeRegistry").newInstance())
                            .get("registry");
                } catch (Exception ex2) {
                    //
                }
            }
        }
        return this.types;
    }
}
