package com.playtomic.tests.wallet.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.playtomic.tests.wallet.domain.UserId;
import com.playtomic.tests.wallet.domain.Wallet;
import com.playtomic.tests.wallet.infrastructure.jdbc.JdbcWalletRepository;
import com.playtomic.tests.wallet.infrastructure.jdbc.JdbcWalletRepositoryAdapter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.UUID;

import static com.playtomic.tests.util.Constants.CARD_NUMBER;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class WalletControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JdbcWalletRepository walletRepository;

    @Autowired
    private JdbcWalletRepositoryAdapter walletRepositoryAdapter;

    @InjectMocks
    private WalletController walletController;

    private static final String WALLET_ID = "a1d6f054-4e3c-44f3-9f24-b5e5c312e776";
    private static final String USER_ID = "d7b87957-52f0-49d9-9887-0e92a55c314d";
    private static final BigDecimal BALANCE = BigDecimal.valueOf(100.0);

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        walletRepository.deleteAll();
        walletRepositoryAdapter.insert(mockWallet());
    }

    @Test
    void shouldGetWalletWhenItExists() throws Exception {
        mockMvc.perform(get("/wallet/{id}", WALLET_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(WALLET_ID))
                .andExpect(jsonPath("$.userId").value(USER_ID))
                .andExpect(jsonPath("$.balance").value(BALANCE));
    }

    @Test
    void shouldGetWalletWhenItDoesNotExist() throws Exception {
        mockMvc.perform(get("/wallet/{id}", UUID.randomUUID().toString()))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldTopUpWallet() throws Exception {
        BigDecimal topUpAmount = BigDecimal.valueOf(50.0);

        WalletController.TopUpRequest topUpRequest = new WalletController.TopUpRequest(CARD_NUMBER, topUpAmount);
        mockMvc.perform(put("/wallet/{id}", WALLET_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(topUpRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(WALLET_ID))
                .andExpect(jsonPath("$.userId").value(USER_ID))
                .andExpect(jsonPath("$.balance").value(BALANCE.add(topUpAmount)));
    }

    private Wallet mockWallet() {
        return new Wallet(
                Wallet.Id.of(WALLET_ID),
                UserId.of(USER_ID),
                BALANCE
        );
    }
}
