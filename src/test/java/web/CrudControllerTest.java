package web;

import Application.App;
import data.service.OrderService;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Repeat;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@ContextConfiguration(classes = App.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CrudControllerTest {
    private MockMvc mockMvc;
    private static List<String> savedId = new ArrayList<>();

    @Autowired
    private WebApplicationContext context;

    @Before
    public void setup(){
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    public void cleanUp(){
        OrderService service = (OrderService)context.getBean("orderServiceImp");
        service.deleteAll();
        assertEquals(service.count(), 0);
    }


    @Test
    @Repeat(2)
    public void saveContent() throws Exception{
        MvcResult result =
                mockMvc.perform(MockMvcRequestBuilders.post("/rest/orders/")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"customer\": \"customer\", \"items\": [{\"productName\": \"product\", \"quantity\": \"3\", \"price\": \"2.33\"}]}")
                    .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.customer").exists())
                .andExpect(jsonPath("$.items").exists())
                .andExpect(jsonPath("$.customer").value("customer"))
                .andExpect(status().is(HttpStatus.CREATED.value()))
                .andReturn();

        Arrays.stream(result.getResponse().getContentAsString().split("\""))
                .skip(3)
                .limit(1)
                .findAny()
                .ifPresent(savedId::add);
    }

    @Test
    public void verifyContent() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/rest/orders")
        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    public void verifyContentById() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/rest/orders/" + savedId.get(0))
        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(savedId.get(0)))
                .andExpect(jsonPath("$.customer").value("customer"))
                .andExpect(jsonPath("$.items[:1].productName").value("product"))
                .andExpect(jsonPath("$.items[:1].quantity").value(3.0))
                .andExpect(jsonPath("$.items[:1].price").value(2.33));

        mockMvc.perform(MockMvcRequestBuilders.get("/rest/orders/" + savedId.get(1))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(savedId.get(1)))
                .andExpect(jsonPath("$.customer").value("customer"))
                .andExpect(jsonPath("$.items[:1].productName").value("product"))
                .andExpect(jsonPath("$.items[:1].quantity").value(3.0))
                .andExpect(jsonPath("$.items[:1].price").value(2.33));
    }

    @Test
    public void verifyContentCannotBeFound() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/rest/orders/0123")
        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.message").value("order [0123] not found"));
    }

    @Test
    public void verifyContentCanBeUpdated() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/rest/orders/" + savedId.get(0))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.items[1:]").doesNotExist());

        mockMvc.perform(MockMvcRequestBuilders.patch("/rest/orders/"+ savedId.get(0))
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"productName\": \"newProduct\", \"quantity\": \"1\", \"price\": \"12.32\"}]}")
                    .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.items[1:2]").exists())
                .andExpect(jsonPath("$.items[1:2].productName").value("newProduct"))
                .andExpect(jsonPath("$.items[1:2].quantity").value(1.0))
                .andExpect(jsonPath("$.items[1:2].price").value(12.32));
    }

    @Test
    public void verifyContentShouldBeDeleted() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/rest/orders")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)));

        mockMvc.perform(MockMvcRequestBuilders.delete("/rest/orders/" + savedId.get(1))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)));

        mockMvc.perform(MockMvcRequestBuilders.delete("/rest/orders/" + savedId.get(0))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(0)));
    }
}
