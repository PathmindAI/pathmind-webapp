package io.skymind.pathmind.api.domain.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.skymind.pathmind.api.conf.security.PathmindApiUser;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/me")
public class MeController {

    @Data
    @Builder
    public static class MeResponse {
        @JsonProperty("name")
        private String name;
    }

    @GetMapping({"/", ""})
    public MeResponse me(@AuthenticationPrincipal PathmindApiUser principal) {
        return MeResponse.builder().name(principal.getName()).build();
    }

}
