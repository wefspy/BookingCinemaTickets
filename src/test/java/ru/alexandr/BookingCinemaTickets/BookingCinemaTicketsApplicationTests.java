package ru.alexandr.BookingCinemaTickets;

import org.junit.jupiter.api.Test;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.mockito.Mockito.doNothing;

@SpringBootTest
class BookingCinemaTicketsApplicationTests {

	@MockBean
	private CommandLineRunner commandScanner;

	@Test
	void contextLoads() throws Exception {
		doNothing().when(commandScanner).run();
	}

}
