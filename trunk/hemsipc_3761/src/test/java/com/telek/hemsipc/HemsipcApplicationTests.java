package com.telek.hemsipc;

import java.io.IOException;

import org.junit.Test;
import org.springframework.stereotype.Component;

import com.telek.hemsipc.server.FirmwareUpdateServer;

@Component
public class HemsipcApplicationTests {

    @Test
    public void contextLoads() throws IOException {
		new FirmwareUpdateServer("85D02A61C1AB494B3740C570F8A9E95F","006f6953d04c4fe5a5417fb39a7254a5").run();
    }
}
