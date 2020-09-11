package org.startupframework;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WaitForService {

	static Logger log = LoggerFactory.getLogger(WaitForService.class);

	static public void waitForIt(String host, int port) throws InterruptedException {
		Socket sock = null;
		while (true) {
			try {
				sock = new Socket(host, port);
				sock.close();
				return;
			} catch (IOException ex) {
				log.warn("Wait for {}:{}", host, port);
				TimeUnit.SECONDS.sleep(1);
			}
		}
	}
}
