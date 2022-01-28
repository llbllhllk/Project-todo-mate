package ToDoMate.ToDoMate.controller;

import ToDoMate.ToDoMate.domain.Member;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
@WebAppConfiguration
class FindControllerTest {

    protected MockHttpSession session=new MockHttpSession();
    protected MockHttpServletRequest request;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    WebApplicationContext context;
    @Autowired
    private ObjectMapper objectMapper;


    @Test
    public void 인증번호확인() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();

        session.setAttribute("certification", "check_test");
        JSONObject input = new JSONObject();
        input.put("certification", "check_test");

        mockMvc.perform(MockMvcRequestBuilders.post("/validCertification")
                .flashAttr("certification", input))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }





}