package io.skymind.pathmind.api.domain.user.dto;

import io.skymind.pathmind.api.domain.project.ProjectController;
import io.skymind.pathmind.api.domain.project.service.ProjectService;
import io.skymind.pathmind.db.dao.ModelDAO;
import io.skymind.pathmind.db.dao.ObservationDAO;
import io.skymind.pathmind.db.dao.ProjectDAO;
import io.skymind.pathmind.db.dao.RewardVariableDAO;
import io.skymind.pathmind.db.dao.UserDAO;
import io.skymind.pathmind.services.ModelService;
import io.skymind.pathmind.services.model.analyze.ModelFileVerifier;
import io.skymind.pathmind.services.project.rest.ModelAnalyzerApiClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.web.servlet.MockMvc;

import static io.skymind.pathmind.api.conf.security.PathmindApiAuthenticationProcessingFilter.HEADER_API_TOKEN_NAME;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;


@WebMvcTest(ProjectController.class)
public class ProjectControllerTest {

    @MockBean
    UserDAO userDAO;
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void trimPmTokenHeaderValue() throws Exception {
        // no spaces trimmed
        this.mockMvc.perform(get("/")
                .header(HEADER_API_TOKEN_NAME, "123")
        );
        verify(userDAO).findByApiKey(eq("123"));
        reset(userDAO);

        // both sides spaces trimmed
        this.mockMvc.perform(get("/")
                .header(HEADER_API_TOKEN_NAME, "     123    ")
        );
        verify(userDAO).findByApiKey(eq("123"));
        reset(userDAO);

        // both sides spaces trimmed no inner spaces affected
        this.mockMvc.perform(get("/")
                .header(HEADER_API_TOKEN_NAME, " 1 2 3 ")
        );
        verify(userDAO).findByApiKey(eq("1 2 3"));
        reset(userDAO);
    }

    @TestConfiguration
    public static class TestConfig {
        @Bean
        public ProjectService projectService() {
            return mock(ProjectService.class);
        }

        @Bean
        public ProjectDAO projectDAO() {
            return mock(ProjectDAO.class);
        }

        @Bean
        public ModelDAO modelDAO() {
            return mock(ModelDAO.class);
        }

        @Bean
        public ModelService modelService() {
            return mock(ModelService.class);
        }

        @Bean
        public RewardVariableDAO rewardVariableDAO() {
            return mock(RewardVariableDAO.class);
        }

        @Bean
        public ObservationDAO observationDAO() {
            return mock(ObservationDAO.class);
        }

        @Bean
        public ModelAnalyzerApiClient maClient() {
            return mock(ModelAnalyzerApiClient.class);
        }

        @Bean
        public ModelFileVerifier modelFileVerifier() {
            return mock(ModelFileVerifier.class);
        }
    }

}