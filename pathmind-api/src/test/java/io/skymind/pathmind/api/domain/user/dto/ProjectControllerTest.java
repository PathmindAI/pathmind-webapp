package io.skymind.pathmind.api.domain.user.dto;

import io.skymind.pathmind.api.domain.project.ProjectController;
import io.skymind.pathmind.db.dao.ProjectDAO;
import io.skymind.pathmind.db.dao.UserDAO;
import io.skymind.pathmind.services.TrainingService;
import io.skymind.pathmind.services.experiment.ExperimentService;
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

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    UserDAO userDAO;

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
        public ExperimentService experimentService() {
            return mock(ExperimentService.class);
        }
        @Bean
        public ProjectDAO projectDAO() {
            return mock(ProjectDAO.class);
        }
        @Bean
        public ModelAnalyzerApiClient maClient() {
            return mock(ModelAnalyzerApiClient.class);
        }
        @Bean
        public TrainingService trainingService() {
            return mock(TrainingService.class);
        }
    }

}