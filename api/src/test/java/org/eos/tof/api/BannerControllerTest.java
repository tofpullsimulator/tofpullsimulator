package org.eos.tof.api;

import org.eos.tof.common.BannerFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = {BannerController.class})
@AutoConfigureMockMvc
@ComponentScan(basePackages = {"org.eos.tof.common"})
@EnableAutoConfiguration
class BannerControllerTest {

    @Autowired
    private BannerFactory bannerFactory;
    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldCreate() throws Exception {
        mockMvc.perform(post("/api/v1/banners")
                        .contentType("application/json")
                        .content("""
                                {
                                    "spec": "Yulan",
                                    "mode": "normal",
                                    "theory": true
                                }"""))
                .andExpect(status().isOk());
    }

    @Test
    void shouldRecreate() throws Exception {
        var banner = bannerFactory.getObject();
        MockHttpSession session = new MockHttpSession();
        session.setAttribute(BannerController.ATTR_BANNER, banner);

        mockMvc.perform(post("/api/v1/banners")
                        .session(session)
                        .contentType("application/json")
                        .content("""
                                {
                                    "spec": "Yulan",
                                    "mode": "normal",
                                    "theory": true
                                }"""))
                .andExpect(status().isOk());
    }

    @Test
    void shouldInternalErrorWhenBadSpec() throws Exception {
        mockMvc.perform(post("/api/v1/banners")
                        .contentType("application/json")
                        .content("""
                                {
                                    "spec": "invalid",
                                    "mode": "normal",
                                    "theory": true
                                }"""))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldInternalErrorWhenBadMode() throws Exception {
        mockMvc.perform(post("/api/v1/banners")
                        .contentType("application/json")
                        .content("""
                                {
                                    "spec": "Yulan",
                                    "mode": "invalid",
                                    "theory": true
                                }"""))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldPull() throws Exception {
        var banner = bannerFactory.getObject();
        MockHttpSession session = new MockHttpSession();
        session.setAttribute(BannerController.ATTR_BANNER, banner);

        mockMvc.perform(get("/api/v1/banners")
                        .session(session)
                        .queryParam("amount", "1"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldBadRequestWhenNoBanner() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute(BannerController.ATTR_BANNER, null);
        mockMvc.perform(get("/api/v1/banners")
                        .session(session)
                        .queryParam("amount", "1"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldInternalErrorWhenBadAmount() throws Exception {
        var banner = bannerFactory.getObject();
        MockHttpSession session = new MockHttpSession();
        session.setAttribute(BannerController.ATTR_BANNER, banner);

        mockMvc.perform(get("/api/v1/banners")
                        .session(session)
                        .queryParam("amount", "invalid"))
                .andExpect(status().isInternalServerError());
    }
}


