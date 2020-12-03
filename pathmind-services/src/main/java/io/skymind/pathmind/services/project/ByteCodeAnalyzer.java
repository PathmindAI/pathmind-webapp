package io.skymind.pathmind.services.project;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Opcodes;

/*To read and find PathmindHelper qualifiedClassName from class files using ASM*/
@Slf4j
public class ByteCodeAnalyzer extends ClassVisitor {
    public String qualifiedClassName;
    List<String> qualifiedClasses = new ArrayList<String>();


    public ByteCodeAnalyzer() {
        super(Opcodes.ASM7);
    }

    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        log.info("kepricondebug100 : {}", name);
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
        log.info("kepricondebug1 : desc : {}, signature : {}, name : {}", desc, signature, name);
        if (desc.contains("PathmindHelper")) {
            log.info("kepricondebug2 : desc : {}, quialifiedClassName : {}, name : {}", desc, qualifiedClassName, name);
            this.qualifiedClasses.add(this.qualifiedClassName + "##" + name);
        }
        return null;
    }

    public void visitEnd() {
    }

    /*To iterate and read all class files*/
    public List<String> byteParser(List<String> classFiles) throws IOException {

        log.info("kepricondebug00 : {}", classFiles);
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
