package io.skymind.pathmind.services.project;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.objectweb.asm.*;

/*To read and find PathmindHelper qualifiedClassName from class files using ASM*/
@Slf4j
public class ByteCodeAnalyzer extends ClassVisitor {
    public String qualifiedClassName;
    List<String> pathmindHelperClasses = new ArrayList<>();
    List<AnyLogicModelInfo> models = new ArrayList<>();
    List<String> observationClasses = new ArrayList<>();
    List<String> actionClasses = new ArrayList<>();
    List<String> rewardClasses = new ArrayList<>();
    List<String> configurationClasses = new ArrayList<>();

    public ByteCodeAnalyzer() {
        super(Opcodes.ASM7);
    }

    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        if (AnyLogicModelInfo.isSupportedExperiment(superName)) {
            models.add(new AnyLogicModelInfo(name, superName));
        }
        this.qualifiedClassName = name;
    }

    public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
        return null;
    }

    public void visitInnerClass(String name, String outerName, String innerName, int access) {
        if (!this.qualifiedClassName.contains("$") && innerName != null) {
            if (AnyLogicModelInfo.isObservations(innerName)) {
                observationClasses.add(name);
            } else if (AnyLogicModelInfo.isActions(innerName)) {
                actionClasses.add(name);
            } else if (AnyLogicModelInfo.isReward(innerName)) {
                rewardClasses.add(name);
            } else if (AnyLogicModelInfo.isConfig(innerName)) {
                configurationClasses.add(name);
            }
        }
    }

    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        if (name.equals("createRoot") && access == Opcodes.ACC_PUBLIC) {
            models.stream().filter(m -> m.getExperimentClass().equals(this.qualifiedClassName))
                .forEach(m -> m.setMainAgentClass(Type.getReturnType(desc).getClassName()));
        }
        return null;
    }

    /*To find and add PathmindHelper instance variables in the format of QualifiedClassName#instanceMemberName*/
    public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
        if (AnyLogicModelInfo.isHelperClass(Type.getType(desc).getClassName())) {
            this.pathmindHelperClasses.add(this.qualifiedClassName + "##" + name);
        }
        return null;
    }

    public void visitEnd() {
    }

    /*To iterate and read all class files*/
    public void byteParser(List<String> classFiles, AnylogicFileCheckResult anylogicFileCheckResult) throws IOException {
        for (String classFile : classFiles) {
            if (classFile.endsWith("model.properties")) {
                FileUtils.readLines(new File(classFile), Charset.defaultCharset()).stream()
                    .filter(line -> line.startsWith("ReinforcementLearningPlatform"))
                    .findFirst()
                    .ifPresent(line -> anylogicFileCheckResult.setRlPlatform(line));
                continue;
            }
            try (InputStream inputStream = new FileInputStream(classFile)) {
                ClassReader cr = new ClassReader(inputStream);
                cr.accept(this, 0);
            } catch (IOException e) {
                log.error("error while reading classes", e);

            }
        }

        anylogicFileCheckResult.getDefinedHelpers().addAll(pathmindHelperClasses);
        anylogicFileCheckResult.getModelInfos().addAll(models);
    }

}
