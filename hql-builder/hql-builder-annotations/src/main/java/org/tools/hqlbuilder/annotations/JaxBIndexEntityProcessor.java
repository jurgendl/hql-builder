package org.tools.hqlbuilder.annotations;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @see http://deors.wordpress.com/2011/10/08/annotation-processors/
 * @see http://deors.wordpress.com/2011/10/31/annotation-generators/
 */
@SupportedAnnotationTypes("javax.persistence.Entity")
@SupportedSourceVersion(SourceVersion.RELEASE_5)
public class JaxBIndexEntityProcessor extends AbstractProcessor {
    private Map<String, BufferedWriter> jaxbIndices = new HashMap<>();

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        System.out.println("JaxBIndexEntityProcessor: start processing");
        try {
            for (Element e : roundEnv.getElementsAnnotatedWith(XmlRootElement.class)) {
                XmlRootElement entity = e.getAnnotation(XmlRootElement.class);
                String message = "JaxBIndexEntityProcessor: annotation found in " + e.getSimpleName() + " with entity " + entity.name();
                log(message);
                if (e.getKind() == ElementKind.CLASS) {
                    TypeElement classElement = (TypeElement) e;
                    PackageElement packageElement = (PackageElement) classElement.getEnclosingElement();
                    BufferedWriter jaxbIndex = getJaxbIndex(packageElement.getQualifiedName().toString());
                    jaxbIndex.write(classElement.getSimpleName().toString());
                    jaxbIndex.newLine();
                }
            }
            for (BufferedWriter jaxbIndex : jaxbIndices.values()) {
                jaxbIndex.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "JaxBIndexEntityProcessor: " + String.valueOf(ex));
        }
        System.out.println("JaxBIndexEntityProcessor: end processing");
        return false;
    }

    private void log(String message) {
        System.out.println(message);
        processingEnv.getMessager().printMessage(Diagnostic.Kind.MANDATORY_WARNING, message);
    }

    public BufferedWriter getJaxbIndex(String packageName) throws IOException {
        BufferedWriter fileObjectWriter = jaxbIndices.get(packageName);
        if (fileObjectWriter == null) {
            FileObject fileObject = processingEnv.getFiler().createResource(StandardLocation.SOURCE_OUTPUT, packageName, "jaxb.index");
            System.out.println(fileObject.toUri().toASCIIString());
            processingEnv.getMessager().printMessage(Diagnostic.Kind.MANDATORY_WARNING, fileObject.toUri().toASCIIString());
            fileObjectWriter = new BufferedWriter(fileObject.openWriter());
            jaxbIndices.put(packageName, fileObjectWriter);
        }
        return fileObjectWriter;
    }
}
