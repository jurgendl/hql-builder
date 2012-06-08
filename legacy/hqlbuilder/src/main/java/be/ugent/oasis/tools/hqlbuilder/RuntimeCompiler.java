package be.ugent.oasis.tools.hqlbuilder;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.concurrent.Callable;

/**
 * RuntimeCompiler
 * 
 * @author jdlandsh
 */
@SuppressWarnings("unchecked")
public class RuntimeCompiler {
    /** compilerMethod */
    private static Method compilerMethod = null;

    /** parentLoader */
    private static final ClassLoader parentLoader = RuntimeCompiler.class.getClassLoader();

    /** initException */
    private static Exception initException = null;

    /** workdir */
    private static File workdir = null;

    static {
        try {
            File jdklocation = new File(System.getProperty("java.home")).getParentFile();

            if (!jdklocation.exists()) {
                throw new FileNotFoundException(jdklocation.getAbsolutePath());
            }

            File tools = new File(jdklocation, "lib/tools.jar");

            if (!tools.exists()) {
                throw new FileNotFoundException(tools.getAbsolutePath());
            }

            URLClassLoader toolsloader = new URLClassLoader(new URL[] { tools.toURI().toURL() }, parentLoader);
            Class<?> compilerclass = toolsloader.loadClass("com.sun.tools.javac.Main");
            compilerMethod = compilerclass.getMethod("compile", String[].class, java.io.PrintWriter.class);

            File tmpdir = new File(System.getProperty("java.io.tmpdir"));
            workdir = new File(tmpdir, "JavaRuntimeCompiler");
            workdir.mkdir();
        } catch (Exception ex) {
            initException = ex;
        }
    }

    /**
     * init was successful
     * 
     * @return
     */
    public static boolean success() {
        return initException == null;
    }

    /**
     * evaluate java code
     * 
     * @param javacode
     * 
     * @return
     * 
     * @throws Exception
     */
    public static Object eval(String javacode) throws Exception {
        return eval(javacode, false, null);
    }

    /**
     * evaluate java code
     * 
     * @param javacode
     * @param log
     * @param logout
     * 
     * @return
     * 
     * @throws Exception
     * @throws RuntimeException
     */
    @SuppressWarnings("cast")
    public static Object eval(String javacode, boolean log, ByteArrayOutputStream logout) throws Exception {
        if (initException != null) {
            throw initException;
        }

        if (!javacode.startsWith("return ") && !javacode.endsWith(";")) {
            javacode = "return " + javacode + ";";
        }

        String key = "RuntimeClass";
        File javatemp = new File(workdir, key + ".java");
        File classtemp = new File(workdir, key + ".class");
        final String classname = javatemp.getName().replaceAll(".java", "");
        StringBuilder sb = new StringBuilder();
        String[] packages = {
                "java.io",
                "java.net",
                "java.text",
                "java.sql",
                "java.math",
                "java.util",
                "java.util.regex",
                "org.joda.time",
                "org.apache.commons.lang",
                "org.apache.commons.lang.builder" };

        for (String pack : packages) {
            sb.append("import ").append(pack).append(".*;\n");
        }

        sb.append("public class ").append(classname).append(" implements java.util.concurrent.Callable<Object> {\n");
        sb.append("public Object call() throws Exception {\n");
        sb.append(javacode).append("\n");
        sb.append("}\n");
        sb.append("}\n");

        if (log) {
            System.out.println(sb);
        }

        FileOutputStream out = new FileOutputStream(javatemp);
        out.write(sb.toString().getBytes());
        out.close();

        if (log) {
            System.out.println("java-file=" + javatemp);
        }

        // com.sun.tools.javac.Main.compile(new String[] { "-d", workdir.getAbsolutePath(), javatemp.getAbsolutePath() });
        ByteArrayOutputStream buffer = (logout == null) ? new ByteArrayOutputStream() : logout;
        PrintWriter pw = new PrintWriter(buffer);

        int status = (int) (Integer) compilerMethod.invoke(null,
                new Object[] { new String[] { "-d", workdir.getAbsolutePath(), javatemp.getAbsolutePath() }, pw });

        if (log) {
            System.out.println(new String(buffer.toByteArray()));
            System.out.println("status=" + status);
            System.out.println("class-file=" + classtemp);
        }

        if (!classtemp.exists()) {
            throw new RuntimeException(new String(buffer.toByteArray()));
        }

        URLClassLoader dynamicloader = new URLClassLoader(new URL[] { workdir.toURI().toURL() }, parentLoader);
        Class<Callable<Object>> callableclass = (Class<Callable<Object>>) dynamicloader.loadClass(classname);
        Callable<Object> callableinstance = callableclass.newInstance();
        Object returnvalue = callableinstance.call();

        javatemp.deleteOnExit();
        classtemp.deleteOnExit();

        return returnvalue;
    }
}
