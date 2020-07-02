package io.skymind.pathmind.shared.utils;

import io.skymind.pathmind.shared.data.Model;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.nio.file.Paths;
import java.time.LocalDateTime;

import static io.skymind.pathmind.shared.utils.ZipUtils.entryContentExtractor;

@Slf4j
public class ModelUtils {

    public static String NONTUPLE_ERROR_MESSAGE = "Model needs to be updated. You can take a look at <a target='_blank' href='http://help.pathmind.com/en/articles/4219921-converting-models-to-support-tuples'>this article</a> for upgrade instructions.";
    
    private ModelUtils() {
        throw new IllegalAccessError("Static usage only");
    }

    public static Model generateNewDefaultModel() {
        Model model = new Model();
        model.setName(Model.DEFAULT_INITIAL_MODEL_NAME);
        model.setDateCreated(LocalDateTime.now());
        return model;
    }

    public static void extractAndSetPackageName(Model model) {
        byte[] modelJar = getModelJar(model.getFile());
        String packageName = getPackageNameInModelJar(modelJar);
        model.setPackageName(packageName);
    }

    private static String getPackageNameInModelJar(byte[] modelJar) {
        return ZipUtils.processZipEntryInFile(modelJar, s -> s.endsWith("Main.class"),
                (zipInputStream, zipEntry) -> StringUtils.trimToEmpty(Paths.get(zipEntry.getName()).getParent().toString())
        );
    }

    private static byte[] getModelJar(byte[] projectFile) {
        return ZipUtils.processZipEntryInFile(projectFile, s -> s.endsWith("model.jar"), entryContentExtractor());
    }
    
    public static boolean isTupleModel(Model model) {
        return model.getActionTupleSize() > 0;
    }

}
