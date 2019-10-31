package io.skymind.pathmind.services.project;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.*;

/*To read and find PathmindHelper qualifiedClassName from class files using ASM*/
public class ByteCodeAnalyzer extends ClassVisitor {
    private static final Logger log = LogManager.getLogger(ByteCodeAnalyzer.class);
    public String qualifiedClassName;
    List<String> qualifiedClasses = new ArrayList<String>();


    public ByteCodeAnalyzer() {
        super(Opcodes.ASM7);
    }

    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        this.qualifiedClassName = name;
    }

    public void visitSource(String source, String debug) {
    }

    public void visitOuterClass(String owner, String name, String desc) {
    }

    public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
        return null;
    }

    public void visitAttribute(Attribute attr) {
    }

    public void visitInnerClass(String name, String outerName, String innerName, int access) {
    }

    /*To find and add PathmindHelper instance variables in the format of QualifiedClassName#instanceMemberName*/
    public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
        if (desc.contains("PathmindHelper")) {
            this.qualifiedClasses.add(this.qualifiedClassName + "##" + name);
        }
        return null;
    }

    public void visitEnd() {
    }

    /*To iterate and read all class files*/
    public List<String> byteParser(List<String> classFiles) throws IOException {

        for (String classFile : classFiles) {

            try (InputStream inputStream = new FileInputStream(classFile)) {
                ClassReader cr = new ClassReader(inputStream);
                cr.accept(this, 0);
            } catch (IOException e) {
                log.error("error while reading classes", e);

            }
        }
        return this.qualifiedClasses;
    }

}
