package br.com.raphasil.reactive.example.sync;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import br.com.raphasil.reactive.example.BaseExample;

public class SyncExampleB extends BaseExample {
	
	public SyncExampleB(Logger logger) {		
		 super(logger);
	}
	
	public List<String> getDesserts() {
		return desserts;
	}
	
	public void loopBasic() {
		long startTime = System.nanoTime();
		
		logger.info("[BEGIN]");
		
		String dessert = "";
		
		Iterator<String> it = desserts.iterator();		
		while (it.hasNext()) {
			dessert = it.next();
			
			if( dessert.toUpperCase().startsWith("P") ) {
				logger.info(dessert.toUpperCase());				
			}
			
		}
		
		logger.info("[END - "+ calcTime(startTime) +"]");
	}
	
	public void loopIntermediate() {
		long startTime = System.nanoTime();
		
		logger.info("[BEGIN]");
				
		for (String dessert : desserts) {
			
			if( dessert.toUpperCase().startsWith("P") ) {
				logger.info(dessert.toUpperCase());				
			}			

		}
		
		logger.info("[END - "+ calcTime(startTime) +"]");	
	}
	
	public void loopAdvanced() {
		long startTime = System.nanoTime();
		
		logger.info("[BEGIN]");
				
		desserts.stream()
		.map(String::toUpperCase)
		// .peek( dessert -> logger.info(dessert))
		.filter(dessert -> dessert.startsWith("P"))
		.forEach(logger::info);
		
		logger.info("[END - "+ calcTime(startTime) +"]");	
	}

	public static void main(String[] args) throws SecurityException, IOException {
		
		Logger logger = Logger.getLogger(SyncExampleB.class.getName());
		
		SyncExampleB simpleLoop = new SyncExampleB(logger);		
		simpleLoop.loopBasic();
		simpleLoop.loopIntermediate();
		simpleLoop.loopAdvanced();
		
		System.exit(0);
	}	

}
