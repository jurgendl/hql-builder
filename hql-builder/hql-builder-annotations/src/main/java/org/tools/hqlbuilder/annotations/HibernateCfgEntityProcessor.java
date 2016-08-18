package org.tools.hqlbuilder.annotations;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.tools.FileObject;
import javax.tools.StandardLocation;

/**
 * @see http://deors.wordpress.com/2011/10/08/annotation-processors/
 * @see http://deors.wordpress.com/2011/10/31/annotation-generators/
 */
@SupportedAnnotationTypes("javax.persistence.Entity")
@SupportedSourceVersion(SourceVersion.RELEASE_5)
public class HibernateCfgEntityProcessor extends AbstractProcessor {
    private boolean run = false;

    @SuppressWarnings("deprecation")
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        System.out.println("HibernateCfgEntityProcessor: start processing");
        try {
            if (!run) {
                run = true;
                List<String> elements = new ArrayList<>();
                for (Element e : roundEnv.getElementsAnnotatedWith(javax.persistence.Entity.class)) {
                    javax.persistence.Entity entity = e.getAnnotation(javax.persistence.Entity.class);
                    String message = "HibernateCfgEntityProcessor: annotation found in " + e.getSimpleName() + " with entity " + entity.name();
                    log(message);
                    addToConfig(elements, e);
                }
                for (Element e : roundEnv.getElementsAnnotatedWith(javax.persistence.Table.class)) {
                    javax.persistence.Table entity = e.getAnnotation(javax.persistence.Table.class);
                    String message = "HibernateCfgEntityProcessor: annotation found in " + e.getSimpleName() + " with entity " + entity.name();
                    log(message);
                    addToConfig(elements, e);
                }
                for (Element e : roundEnv.getElementsAnnotatedWith(org.hibernate.annotations.Entity.class)) {
                    org.hibernate.annotations.Entity entity = e.getAnnotation(org.hibernate.annotations.Entity.class);
                    String message = "HibernateCfgEntityProcessor: annotation found in " + e.getSimpleName() + " with entity " + entity;
                    log(message);
                    addToConfig(elements, e);
                }
                for (Element e : roundEnv.getElementsAnnotatedWith(org.hibernate.annotations.Table.class)) {
                    org.hibernate.annotations.Table entity = e.getAnnotation(org.hibernate.annotations.Table.class);
                    String message = "HibernateCfgEntityProcessor: annotation found in " + e.getSimpleName() + " with entity " + entity;
                    log(message);
                    addToConfig(elements, e);
                }
                BufferedWriter writer = getWriter();
                addToConfig(writer, elements);
                endWriter();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "HibernateCfgEntityProcessor: " + String.valueOf(ex));
        }
        System.out.println("HibernateCfgEntityProcessor: end processing");
        return false;
    }

    private void log(String message) {
        System.out.println(message);
        processingEnv.getMessager().printMessage(Diagnostic.Kind.MANDATORY_WARNING, message);
    }

    private void addToConfig(List<String> elements, Element e) throws IOException {
        if (e.getKind() == ElementKind.CLASS) {
            TypeElement classElement = (TypeElement) e;
            String el = classElement.getQualifiedName().toString();
            if (!elements.contains(el)) {
                elements.add(el);
            }
        }
    }

    private void addToConfig(BufferedWriter writer, List<String> elements) throws IOException {
        for (String el : elements) {
            writer.write("<mapping class=\"" + el + "\"/>");
            writer.newLine();
        }
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
            FileObject fileObject = processingEnv.getFiler().createResource(StandardLocation.SOURCE_OUTPUT, "", "hibernate.cfg.xml");
            System.out.println(fileObject.toUri().toASCIIString());
            processingEnv.getMessager().printMessage(Diagnostic.Kind.MANDATORY_WARNING, fileObject.toUri().toASCIIString());
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
