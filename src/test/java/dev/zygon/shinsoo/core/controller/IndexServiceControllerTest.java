package dev.zygon.shinsoo.core.controller;

import dev.zygon.shinsoo.message.UserStatus;
import dev.zygon.shinsoo.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.ws.rs.core.Response;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IndexServiceControllerTest {

    private UserStatus status;

    @Mock
    private UserService service;

    @InjectMocks
    private IndexServiceController controller;

    @BeforeEach
    void setup() {
        status = new UserStatus();
    }

    @Test
    void whenIndexIsCalledUserStatusIsRetrievedFromService() {
        when(service.session())
                .thenReturn(status);

        Response response = controller.index();

        assertEquals(status, response.getEntity());

        verify(service, times(1)).session();
    }
}
