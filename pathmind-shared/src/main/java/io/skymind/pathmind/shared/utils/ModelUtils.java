package io.skymind.pathmind.shared.utils;

import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.regex.Pattern;

import io.skymind.pathmind.shared.constants.InvalidModelType;
import io.skymind.pathmind.shared.data.Model;
import io.skymind.pathmind.shared.data.Observation;
import io.skymind.pathmind.shared.data.RewardVariable;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

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

    public static List<Observation> convertToObservations(List<String> observationNames, List<String> observationTypes) {
        Map<String, Observation> auxObservations = new LinkedHashMap<>();
        for (int i = 0; i < observationNames.size(); i++) {
            String name = observationNames.get(i);
            String type = observationTypes.get(i);
            if (VariableParserUtils.isArray(name)) {
                String correctName = VariableParserUtils.removeArrayIndexFromVariableName(name);
                if (auxObservations.containsKey(correctName)) {
                    Observation obs = auxObservations.get(correctName);
                    obs.setMaxItems(obs.getMaxItems() + 1);
                } else {
                    Observation obs = new Observation();
                    obs.setVariable(correctName);
                    obs.setDataTypeEnum(VariableParserUtils.observationType(name, type));
                    obs.setArrayIndex(auxObservations.size());
                    obs.setMaxItems(1);
                    auxObservations.put(correctName, obs);
                }
            } else {
                Observation obs = new Observation();
                obs.setVariable(name);
                obs.setDataTypeEnum(VariableParserUtils.observationType(name, type));
                obs.setArrayIndex(auxObservations.size());
                auxObservations.put(name, obs);
            }
        }
        return new ArrayList<>(auxObservations.values());
    }

    public static List<RewardVariable> convertToRewardVariables(long modelId, List<String> rewardVariableNames, List<String> rewardVariableTypes) {
        List<RewardVariable> rewardVariables = new ArrayList<>();
        for (int i = 0; i < rewardVariableNames.size(); i++) {
            RewardVariable rv = new RewardVariable();
            rv.setArrayIndex(i);
            rv.setModelId(modelId);
            rv.setName(rewardVariableNames.get(i));
            rv.setDataType(rewardVariableTypes.get(i));
            rewardVariables.add(rv);
        }
        return rewardVariables;
    }


}
