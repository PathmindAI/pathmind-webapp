package io.skymind.pathmind.services.project;


import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.objectweb.asm.*;

public class ClassPrinter extends ClassVisitor {

    List<String> qualifiedClasses = new ArrayList<String>();
    public String qualifiedClassName;


    public ClassPrinter() {
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
        if(desc.contains("PathmindHelper")) {
            this.qualifiedClasses.add(this.qualifiedClassName+"##"+name);
        }
        return null;
    }
    public void visitEnd() {
        System.out.println("}");
    }
    public List<String> byteParser(List<String> classFiles) {
        try {
            for (String classFile : classFiles) {
                InputStream in=new FileInputStream(classFile);
                ClassReader cr = new ClassReader(in);;
                cr.accept(this, 0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return this.qualifiedClasses;
    }

}
