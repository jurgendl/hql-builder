package org.tools.hqlbuilder.annotations;

import java.io.BufferedWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
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
import javax.persistence.Transient;
import javax.tools.Diagnostic;
import javax.tools.FileObject;

/**
 * @see http://deors.wordpress.com/2011/10/08/annotation-processors/
 * @see http://deors.wordpress.com/2011/10/31/annotation-generators/
 */
@SupportedAnnotationTypes("javax.persistence.Entity")
@SupportedSourceVersion(SourceVersion.RELEASE_5)
public class HibernateEntityPropertiesProcessor extends AbstractProcessor {
    private boolean run = false;

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        System.out.println("HibernateEntityPropertiesProcessor: start processing");
        try {
            if (!run) {
                run = true;
                for (Element elem : roundEnv.getElementsAnnotatedWith(javax.persistence.Entity.class)) {
                    try {
                        if (elem.getKind() == ElementKind.CLASS) {
                            TypeElement classElement = (TypeElement) elem;
                            PackageElement packageElement = (PackageElement) classElement.getEnclosingElement();
                            String className = classElement.getQualifiedName() + "Properties";
                            log(className);
                            FileObject fileObject = processingEnv.getFiler().createSourceFile(className);
                            BufferedWriter bw = new BufferedWriter(fileObject.openWriter());
                            bw.append("package ");
                            bw.append(packageElement.getQualifiedName().toString());
                            bw.append(";");
                            bw.newLine();
                            bw.append("public interface ");
                            bw.append(classElement.getSimpleName() + "Properties");
                            bw.append("{");
                            bw.newLine();

                            Class<?> forName = Class.forName(classElement.getQualifiedName().toString());
                            for (Field field : forName.getDeclaredFields()) {
                                if (field.isSynthetic()) {
                                    continue;
                                }
                                int modifiers = field.getModifiers();
                                if (Modifier.isStatic(modifiers)) {
                                    continue;
                                }
                                if (Modifier.isTransient(modifiers)) {
                                    continue;
                                }
                                if (field.getAnnotation(Transient.class) != null) {
                                    continue;
                                }

                                bw.append("public static final String ");
                                bw.append(field.getName().toUpperCase());
                                bw.append(" = \"");
                                bw.append(field.getName());
                                bw.append("\";");
                                bw.newLine();
                            }

                            bw.append("}");
                            bw.newLine();
                            bw.close();
                        }
                    } catch (Exception ex) {
                        log(String.valueOf(ex));
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "HibernateEntityPropertiesProcessor: " + String.valueOf(ex));
        }
        System.out.println("HibernateEntityPropertiesProcessor: end processing");
        return false;
    }

    private void log(String message) {
        System.out.println(message);
        processingEnv.getMessager().printMessage(Diagnostic.Kind.MANDATORY_WARNING, message);
    }
}
