package org.tools.hqlbuilder.annotations;

import java.io.BufferedWriter;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.persistence.Entity;
import javax.tools.Diagnostic;
import javax.tools.FileObject;
import javax.tools.StandardLocation;

/**
 * @see http://deors.wordpress.com/2011/10/08/annotation-processors/
 * @see http://deors.wordpress.com/2011/10/31/annotation-generators/
 */
@SupportedAnnotationTypes("javax.persistence.Entity")
@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class HibernateCfgEntityProcessor extends AbstractProcessor {
    public HibernateCfgEntityProcessor() {
        super();
    }

    /**
     * 
     * @see javax.annotation.processing.AbstractProcessor#process(java.util.Set, javax.annotation.processing.RoundEnvironment)
     */
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        try {
            FileObject fileObject = processingEnv.getFiler().createResource(StandardLocation.CLASS_OUTPUT, "", "hibernate.cfg.xml");
            BufferedWriter hibernateCfgXml = new BufferedWriter(fileObject.openWriter());
            hibernateCfgXml.write("<!DOCTYPE hibernate-configuration PUBLIC");
            hibernateCfgXml.newLine();
            hibernateCfgXml.write("\"-//Hibernate/Hibernate Configuration DTD 3.0//EN\"");
            hibernateCfgXml.newLine();
            hibernateCfgXml.write("\"http://www.hibernate.org/dtd/hibernate-configuration-3.0.dtd\">");
            hibernateCfgXml.newLine();

            for (Element e : roundEnv.getElementsAnnotatedWith(Entity.class)) {
                Entity entity = e.getAnnotation(Entity.class);
                String message = "annotation found in " + e.getSimpleName() + " with entity " + entity.name();
                processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, message);
                if (e.getKind() == ElementKind.CLASS) {
                    TypeElement classElement = (TypeElement) e;
                    hibernateCfgXml.write("<mapping class=\"" + classElement.getQualifiedName().toString() + "\"/>");
                    hibernateCfgXml.newLine();
                }
            }
            hibernateCfgXml.write("<hibernate-configuration>");
            hibernateCfgXml.newLine();
            hibernateCfgXml.write("<session-factory>");
            hibernateCfgXml.newLine();
            hibernateCfgXml.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, String.valueOf(ex));
        }
        return false;
    }
}
