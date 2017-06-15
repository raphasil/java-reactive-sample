package br.com.raphasil.reactive.example.sync;

import java.io.IOException;
import java.util.Iterator;
import java.util.logging.Logger;

import br.com.raphasil.reactive.example.BaseExample;

public class SyncExampleA extends BaseExample {
	
	public SyncExampleA(Logger logger) {		
		 super(logger);
	}
	
	public void loopBasic() {
		long startTime = System.nanoTime();
		
		logger.info("[BEGIN]");
		
		String dessert = "";
		
		Iterator<String> it = desserts.iterator();		
		while (it.hasNext()) {
			dessert = it.next();
			logger.info(dessert.toUpperCase());
		}
		
		logger.info("[END - "+ calcTime(startTime) +"]");	
	}
	
	public void loopIntermediate() {
		long startTime = System.nanoTime();
		
		logger.info("[BEGIN]");
				
		for (String dessert : desserts) {
			logger.info(dessert.toUpperCase());
		}
		
		logger.info("[END - "+ calcTime(startTime) +"]");	
	}
	
	public void loopAdvanced() {
		long startTime = System.nanoTime();
		
		logger.info("[BEGIN]");
				
		desserts.stream()
		.map(String::toUpperCase)
		.forEach(logger::info);
		
		logger.info("[END - "+ calcTime(startTime) +"]");
	}

	public static void main(String[] args) throws SecurityException, IOException {
		
		Logger logger = Logger.getLogger(SyncExampleA.class.getName());
		
		SyncExampleA simpleLoop = new SyncExampleA(logger);		
		simpleLoop.loopBasic();
		simpleLoop.loopIntermediate();
		simpleLoop.loopAdvanced();
		
		System.exit(0);
	}	

}
