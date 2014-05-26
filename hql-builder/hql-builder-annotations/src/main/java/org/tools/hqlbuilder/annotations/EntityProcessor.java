package org.tools.hqlbuilder.annotations;

import java.io.BufferedWriter;
import java.io.IOException;
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
import javax.tools.JavaFileObject;
import javax.tools.StandardLocation;

/**
 * @see http://deors.wordpress.com/2011/10/08/annotation-processors/
 */
@SupportedAnnotationTypes("javax.persistence.Entity")
@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class EntityProcessor extends AbstractProcessor {
    public EntityProcessor() {
        super();
    }

    /**
     * 
     * @see javax.annotation.processing.AbstractProcessor#process(java.util.Set, javax.annotation.processing.RoundEnvironment)
     */
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (Element e : roundEnv.getElementsAnnotatedWith(Entity.class)) {
            Entity entity = e.getAnnotation(Entity.class);
            String message = "annotation found in " + e.getSimpleName() + " with entity " + entity.name();
            processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, message);
            if (e.getKind() == ElementKind.CLASS) {
                TypeElement classElement = (TypeElement) e;
                PackageElement packageElement = (PackageElement) classElement.getEnclosingElement();
                try {
                    JavaFileObject jfo = processingEnv.getFiler().createSourceFile(classElement.getQualifiedName() + "BeanInfo");
                    {
                        BufferedWriter bw = new BufferedWriter(jfo.openWriter());
                        bw.append("package ");
                        bw.append(packageElement.getQualifiedName());
                        bw.append(";");
                        bw.newLine();
                        bw.newLine();
                        bw.append("public class " + classElement.getSimpleName() + "BeanInfo {}");
                        bw.close();
                    }

                    FileObject rfo = processingEnv.getFiler().createResource(StandardLocation.CLASS_OUTPUT, packageElement.getQualifiedName(),
                            classElement.getSimpleName() + "Resource");
                    {
                        BufferedWriter bw = new BufferedWriter(rfo.openWriter());
                        bw.append("package ");
                        bw.append(packageElement.getQualifiedName());
                        bw.append(";");
                        bw.newLine();
                        bw.newLine();
                        bw.close();
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                    processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, String.valueOf(ex));
                }
            }
        }
        return true; // no further processing of this annotation type
    }
}
