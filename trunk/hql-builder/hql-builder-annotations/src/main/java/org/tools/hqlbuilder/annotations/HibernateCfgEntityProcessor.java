package org.tools.hqlbuilder.annotations;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Writer;
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
    private boolean run = false;

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        System.out.println("HibernateCfgEntityProcessor: start processing");
        try {
            if (!run) {
                run = true;
                BufferedWriter writer = getWriter();
                for (Element e : roundEnv.getElementsAnnotatedWith(Entity.class)) {
                    Entity entity = e.getAnnotation(Entity.class);
                    String message = "HibernateCfgEntityProcessor: annotation found in " + e.getSimpleName() + " with entity " + entity.name();
                    System.out.println(message);
                    processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, message);
                    if (e.getKind() == ElementKind.CLASS) {
                        TypeElement classElement = (TypeElement) e;
                        writer.write("<mapping class=\"" + classElement.getQualifiedName().toString() + "\"/>");
                        writer.newLine();
                    }
                }
                endWriter();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "HibernateCfgEntityProcessor: " + String.valueOf(ex));
        }
        System.out.println("HibernateCfgEntityProcessor: end processing");
        return false;
    }

    private void endWriter() throws IOException {
        hibernateCfgXml.write("</session-factory>");
        hibernateCfgXml.newLine();
        hibernateCfgXml.write("</hibernate-configuration>");
        hibernateCfgXml.newLine();
        hibernateCfgXml.close();
    }

    private BufferedWriter hibernateCfgXml = null;

    private BufferedWriter getWriter() throws IOException {
        if (hibernateCfgXml == null) {
            FileObject fileObject = processingEnv.getFiler().createResource(StandardLocation.CLASS_OUTPUT, "", "hibernate.cfg.xml");
            Writer writer = fileObject.openWriter();
            hibernateCfgXml = new BufferedWriter(writer);
            hibernateCfgXml.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            hibernateCfgXml.newLine();
            hibernateCfgXml.write("<!DOCTYPE hibernate-configuration PUBLIC");
            hibernateCfgXml.newLine();
            hibernateCfgXml.write("\"-//Hibernate/Hibernate Configuration DTD 3.0//EN\"");
            hibernateCfgXml.newLine();
            hibernateCfgXml.write("\"http://www.hibernate.org/dtd/hibernate-configuration-3.0.dtd\">");
            hibernateCfgXml.newLine();
            hibernateCfgXml.write("<hibernate-configuration>");
            hibernateCfgXml.newLine();
            hibernateCfgXml.write("<session-factory>");
            hibernateCfgXml.newLine();
        }
        return hibernateCfgXml;
    }
}
