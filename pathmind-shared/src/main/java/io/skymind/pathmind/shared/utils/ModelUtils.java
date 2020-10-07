package io.skymind.pathmind.shared.utils;

import io.skymind.pathmind.shared.constants.InvalidModelType;
import io.skymind.pathmind.shared.data.Model;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.regex.Pattern;

import static io.skymind.pathmind.shared.utils.ZipUtils.entryContentExtractor;

@Slf4j
public class ModelUtils {
    private static final Predicate<String> LIB_MODEL_MATCH = Pattern.compile("^(lib/)?model[0-9]*\\.jar", Pattern.CASE_INSENSITIVE).asMatchPredicate();

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
    
    public static Optional<InvalidModelType> checkIfModelIsInvalid(Model model) {
        return InvalidModelType.getEnumFromValue(model.getInvalidModel());
    }

    public static boolean isValidModel(Model model) {
        return InvalidModelType.getEnumFromValue(model.getInvalidModel()).isEmpty();
    }

    public static boolean isModelFile(String filename) {
        return ModelUtils.LIB_MODEL_MATCH.test(filename);
    }

}
