package io.skymind.pathmind;

import io.skymind.pathmind.services.ModelService;
import io.skymind.pathmind.services.PolicyFileService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Record2;
import org.jooq.Table;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static io.skymind.pathmind.data.db.Tables.*;

@Slf4j
@Component
// TODO: remove this file along with MODEL_FILE, POLICY_SNAPSHOT and POLICY_SNAPSHOT tables,
// TODO: when the log message yelled : `Migration of binary content is finished`
public class DbToAwsS3FileMigrationBackgroundRunner implements CommandLineRunner {

    private final DSLContext ctx;
    private final ModelService modelService;
    private final PolicyFileService policyFileService;

    public DbToAwsS3FileMigrationBackgroundRunner(DSLContext ctx, ModelService modelService, PolicyFileService policyFileService) {
        this.ctx = ctx;
        this.modelService = modelService;
        this.policyFileService = policyFileService;
    }

    public void doMigrate(Table<? extends Record> table,
                          Supplier<Record2<Long, byte[]>> recordExtractor,
                          BiConsumer<Long, byte[]> processBinaryContentOperation,
                          Consumer<Long> confirmSuccessOperation) {

        final String tableName = table.getName();

        while (true) {
            Record2<Long, byte[]> record = recordExtractor.get();
            if (record == null) {
                log.info("{}: All binary records had been migrated", tableName);
                break;
            }
            final Long id = record.value1();
            final byte[] content = record.value2();

            try {
                if (ArrayUtils.isNotEmpty(content)) {
                    log.debug("{}: Uploading binary content for {}", tableName, id);
                    processBinaryContentOperation.accept(id, content);
                }
                confirmSuccessOperation.accept(id);
                log.debug("{}: Binary content processed", id);
            } catch (Exception e) {
                log.error("{}: Failed to process binary content {}. Retry", tableName, id, e);
            }
        }
    }

    @Override
    public void run(String... args) throws Exception {
        CompletableFuture<Void> migrateModelFiled = CompletableFuture.runAsync(() ->
                doMigrate(MODEL_FILE,
                        () -> ctx.select(MODEL_FILE.MODEL_ID, MODEL_FILE.FILE)
                                .from(MODEL_FILE)
                                .innerJoin(MODEL).on(MODEL.ID.eq(MODEL_FILE.MODEL_ID))
                                .where(MODEL.MODEL_FILE_MOVED_TO_S3.isFalse())
                                .limit(1)
                                .fetchOne(),
                        modelService::saveModelFile,
                        (id) -> ctx.update(MODEL)
                                .set(MODEL.MODEL_FILE_MOVED_TO_S3, true)
                                .where(MODEL.ID.eq(id)).execute()
                )
        );
        CompletableFuture<Void> migratePolicyFiles = CompletableFuture.runAsync(() ->
                doMigrate(POLICY_FILE,
                        () -> ctx.select(POLICY_FILE.POLICY_ID, POLICY_FILE.FILE)
                                .from(POLICY_FILE)
                                .innerJoin(POLICY).on(POLICY.ID.eq(POLICY_FILE.POLICY_ID))
                                .where(POLICY.POLICY_FILE_MOVED_TO_S3.isFalse())
                                .limit(1)
                                .fetchOne(),
                        policyFileService::savePolicyFile,
                        (id) -> ctx.update(POLICY)
                                .set(POLICY.POLICY_FILE_MOVED_TO_S3, true)
                                .where(POLICY.ID.eq(id)).execute()
                )
        );
        CompletableFuture<Void> migratePolicySnapshots = CompletableFuture.runAsync(() ->
                doMigrate(POLICY_SNAPSHOT,
                        () -> ctx.select(POLICY_SNAPSHOT.POLICY_ID, POLICY_SNAPSHOT.SNAPSHOT)
                                .from(POLICY_SNAPSHOT)
                                .innerJoin(POLICY).on(POLICY.ID.eq(POLICY_SNAPSHOT.POLICY_ID))
                                .where(POLICY.POLICY_SNAPSHOT_MOVED_TO_S3.isFalse())
                                .limit(1)
                                .fetchOne(),
                        policyFileService::saveCheckpointFile,
                        (id) -> ctx.update(POLICY)
                                .set(POLICY.POLICY_SNAPSHOT_MOVED_TO_S3, true)
                                .where(POLICY.ID.eq(id)).execute()
                )
        );
        CompletableFuture.allOf(migrateModelFiled, migratePolicyFiles, migratePolicySnapshots).join();
        log.info("Migration of binary content is finished");
    }
}
