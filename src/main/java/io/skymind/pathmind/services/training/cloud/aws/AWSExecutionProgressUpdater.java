package io.skymind.pathmind.services.training.cloud.aws;

import io.skymind.pathmind.db.dao.ExecutionProviderMetaDataDAO;
import io.skymind.pathmind.services.training.ExecutionProgressUpdater;
import io.skymind.pathmind.services.training.cloud.rescale.RescaleExecutionProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AWSExecutionProgressUpdater implements ExecutionProgressUpdater {

    private final AWSExecutionProvider provider;
    private final ExecutionProviderMetaDataDAO executionProviderMetaDataDAO;


    public AWSExecutionProgressUpdater(AWSExecutionProvider provider, ExecutionProviderMetaDataDAO executionProviderMetaDataDAO) {
        this.provider = provider;
        this.executionProviderMetaDataDAO = executionProviderMetaDataDAO;
    }

    @Override
    public void update() {
        log.info("kepricondebug aws");
    }
}
