package com.seatsurfer;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class SeatSurferApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void loginOtpBookingAndAdminEndpointsWork() throws Exception {
        String today = LocalDate.now().toString();

        String loginResponse = mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                      \"username\": \"adi\",
                      \"password\": \"SeatSurfer!2026\"
                    }
                    """))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

        JsonNode loginJson = objectMapper.readTree(loginResponse);
        String challengeId = loginJson.get("challengeId").asText();
        String otpCode = loginJson.get("devOtpCode").asText();

        String verifyResponse = mockMvc.perform(post("/api/auth/verify-otp")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                      \"challengeId\": \"%s\",
                      \"code\": \"%s\"
                    }
                    """.formatted(challengeId, otpCode)))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

        String token = objectMapper.readTree(verifyResponse).get("token").asText();

        String availabilityResponse = mockMvc.perform(get("/api/public/floors/1/availability")
                .param("date", today)
                .param("viewerName", "Ion Test"))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

        JsonNode availabilityJson = objectMapper.readTree(availabilityResponse);
        long seatId = availabilityJson.get("seats").findValues("seatId").getFirst().asLong();

        String bookingResponse = mockMvc.perform(post("/api/public/bookings")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                      \"seatId\": %d,
                      \"bookingDate\": \"%s\",
                      \"bookedByName\": \"Ion Test\"
                    }
                    """.formatted(seatId, today)))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

        long bookingId = objectMapper.readTree(bookingResponse).get("id").asLong();

        mockMvc.perform(post("/api/public/bookings/{bookingId}/cancel", bookingId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                      \"requesterName\": \"Ion Test\"
                    }
                    """))
            .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/admin/floors")
                .header("Authorization", "Bearer " + token))
            .andExpect(status().isOk());
    }
}
