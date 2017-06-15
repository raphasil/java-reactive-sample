package br.com.raphasil.reactive.example;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class BaseExample {
	
	static {
		
		try {
			LogManager.getLogManager().readConfiguration(BaseExample.class.getResourceAsStream("/logging.properties"));
		} catch (SecurityException | IOException e) {
			e.printStackTrace();
		}
		
	}
	
	protected final List<String> desserts = Arrays.asList(
			"Biscuits / cookies",
			"Cakes",
			"Chocolates",
			"Candies",
			"Custards",
			"Puddings",
			"Deep-fried desserts",
			"Frozen desserts",
			"Jellied desserts",
			"Pastries",
			"Pies",
			"Cobblers",
			"Clafoutis",
			"Sweet soups"
	);
	
	protected final Random random = new Random();
	
	protected final Logger logger;
	
	public BaseExample(Logger logger) {		
		 this.logger = logger;
	}
	
	/**
	 * List of Desserts
	 * @return
	 */
	public List<String> getDesserts() {
		return desserts;
	}
	
	/**
	 * Get random dessert
	 * @return
	 */
	protected String getRandomDessert() {
		int pos = random.nextInt(desserts.size());
    	String dessert = desserts.get(pos);
    	return dessert;
	}
	
	/**
	 *  Utility to compute the time in nanoseconds
	 * @param startTime
	 * @return
	 */
	protected long calcTime(long startTime) {
		long endTime = System.nanoTime();
		return TimeUnit.MILLISECONDS.convert(endTime - startTime, TimeUnit.NANOSECONDS);		
	}
	
	protected void shutDownExecutor(ExecutorService exec) {
		
		try {
			logger.info("attempt to shutdown executor");
			exec.shutdown();
			exec.awaitTermination(5, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			logger.warning("tasks interrupted");
		} finally {
			if (!exec.isTerminated()) {
				logger.warning("cancel non-finished tasks");
			}
			exec.shutdownNow();
			logger.info("shutdown finished");
		}
		
	}

}
