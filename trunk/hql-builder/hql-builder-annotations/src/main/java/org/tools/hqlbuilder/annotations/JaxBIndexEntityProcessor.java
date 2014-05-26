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
public class JaxBIndexEntityProcessor extends AbstractProcessor {
    public JaxBIndexEntityProcessor() {
        super();
    }

    private Map<String, BufferedWriter> jaxbIndices = new HashMap<String, BufferedWriter>();

    /**
     * 
     * @see javax.annotation.processing.AbstractProcessor#process(java.util.Set, javax.annotation.processing.RoundEnvironment)
     */
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        try {
            for (Element e : roundEnv.getElementsAnnotatedWith(Entity.class)) {
                Entity entity = e.getAnnotation(Entity.class);
                String message = "annotation found in " + e.getSimpleName() + " with entity " + entity.name();
                processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, message);
                if (e.getKind() == ElementKind.CLASS) {
                    TypeElement classElement = (TypeElement) e;
                    PackageElement packageElement = (PackageElement) classElement.getEnclosingElement();
                    BufferedWriter jaxbIndex = getJaxbIndex(packageElement.getQualifiedName().toString());
                    jaxbIndex.write(classElement.getQualifiedName().toString());
                    jaxbIndex.newLine();
                }
            }
            for (BufferedWriter jaxbIndex : jaxbIndices.values()) {
                jaxbIndex.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, String.valueOf(ex));
        }
        return false;
    }

    public BufferedWriter getJaxbIndex(String packageName) throws IOException {
        BufferedWriter fileObjectWriter = jaxbIndices.get(packageName);
        if (fileObjectWriter == null) {
            FileObject fileObject = processingEnv.getFiler().createResource(StandardLocation.CLASS_OUTPUT, packageName, "jaxb.index");
            fileObjectWriter = new BufferedWriter(fileObject.openWriter());
            jaxbIndices.put(packageName, fileObjectWriter);
        }
        return fileObjectWriter;
    }
}
