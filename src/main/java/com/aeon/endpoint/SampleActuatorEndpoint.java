package com.aeon.endpoint;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.springframework.boot.actuate.endpoint.mvc.AbstractMvcEndpoint;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Arrays;
import java.util.List;

/**
 * Created by roshane on 3/5/17.
 */
@Component
public class SampleActuatorEndpoint extends AbstractMvcEndpoint {

    public SampleActuatorEndpoint() {
        super("/sample", false, true);
    }

    @RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getSampleResponse() {
        SampleResponse sampleResponse = new SampleResponse("active-users", Arrays.asList("roshane", "akila", "manoj"));
        return ResponseEntity.ok(sampleResponse);
    }

    @JsonPropertyOrder(alphabetic = true)
    public static class SampleResponse {

        @JsonProperty("info")
        private String info;
        @JsonProperty("values")
        private List<String> values;

        public SampleResponse(String info, List<String> values) {
            this.info = info;
            this.values = values;
        }
    }
}
