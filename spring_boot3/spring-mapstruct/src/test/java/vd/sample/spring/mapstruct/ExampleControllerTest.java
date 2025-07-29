package vd.sample.spring.mapstruct;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ExampleControllerTest {


//    @DisplayName("Should get successful response When saying hello world")
//    @Test
//    void shouldGetSuccessfulResponseWhenSayingHelloWorld(@Autowired MockMvc mvc) throws Exception {
//        var getRequest = MockMvcRequestBuilders.get("/example").accept(MediaType.TEXT_PLAIN);
//        var mockResponse = mvc.perform(getRequest)
//                .andExpect(status().isOk())
//                .andReturn();
//        assertThat(mockResponse.getResponse().getContentAsString())
//                .isNotBlank()
//                .isEqualTo("Hello World");
//
//    }

}