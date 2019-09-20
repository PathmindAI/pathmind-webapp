package io.skymind.pathmind.services.project;


import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.*;

public class ByteCodeAnalyzer extends ClassVisitor {
    private static final Logger log = LogManager.getLogger(ByteCodeAnalyzer.class);

    List<String> qualifiedClasses = new ArrayList<String>();
    public String qualifiedClassName;


    public ByteCodeAnalyzer() {
        super(Opcodes.ASM7);
    }

    public void visit(int version, int access, String name,
                      String signature, String superName, String[] interfaces) {
        this.qualifiedClassName = name;
    }

    public void visitSource(String source, String debug) {
    }

    public void visitOuterClass(String owner, String name, String desc) {
    }

    public AnnotationVisitor visitAnnotation(String desc,
                                             boolean visible) {
        return null;
    }

    public void visitAttribute(Attribute attr) {
    }

    public void visitInnerClass(String name, String outerName,
                                String innerName, int access) {
    }

    public FieldVisitor visitField(int access, String name, String desc,
                                   String signature, Object value) {
        if (desc.contains("PathmindHelper")) {
            this.qualifiedClasses.add(this.qualifiedClassName + "##" + name);
        }
        return null;
    }

    public void visitEnd() {
    }

    public List<String> byteParser(List<String> classFiles) throws IOException {
        for (String classFile : classFiles) {
            try (InputStream inputStream = new FileInputStream(classFile)) {
                ClassReader cr = new ClassReader(inputStream);
                cr.accept(this, 0);
            } catch (IOException e) {
                log.error((CharSequence) e.getMessage(), e);

            }
        }
        return this.qualifiedClasses;
    }

}
