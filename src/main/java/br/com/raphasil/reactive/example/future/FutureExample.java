package br.com.raphasil.reactive.example.future;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Logger;

import br.com.raphasil.reactive.example.BaseExample;

public class FutureExample extends BaseExample {

	class CallableExample implements Callable<String> {
		
		private final String name;
		private final int time;
		
		public CallableExample() {
			this("CallableExample");
		}
		
		public CallableExample(String name) {
			this(name, random.nextInt(30) + 10);
		}
		
		public CallableExample(String name, int time) {
			this.name = name;
			this.time = time;
		}

		@Override
		public String call() throws Exception {
			String info = name.concat(" ").concat(getRandomDessert());
			Thread.sleep(time);
			logger.info(info);
			return info;
		}

	}	

	public FutureExample(Logger logger) {
		super(logger);
	}

	private void runExampleOneSequecial(ExecutorService exec) throws Exception {
		long startTime = System.nanoTime();

		logger.info("BEGIN");

		Future<String> future = exec.submit(new CallableExample());

		logger.info("before get, isDone: " + future.isDone());

		String result = future.get(); // block

		logger.info("after get, isDone: " + future.isDone() + " result: " + result);

		logger.info("[END - " + calcTime(startTime) + "]");
	}

	private void runExampleTwoSequencial(ExecutorService exec) throws Exception {
		long startTime = System.nanoTime();

		logger.info("BEGIN");
		
		Callable<String> callable = () -> {
			String info = "CallableExample ".concat(getRandomDessert());
			Thread.sleep(15);
			logger.info(info);
			return info;			
		};

		Future<String> future = exec.submit(callable);

		while (!future.isDone() && !future.isCancelled()) { // until done
			logger.info("yielding");
			Thread.yield();
		}

		logger.info("[END - " + calcTime(startTime) + "]");
	}

	public void startSequencial() throws Exception {
		ExecutorService exec = Executors.newSingleThreadExecutor();
		runExampleOneSequecial(exec);
		runExampleTwoSequencial(exec);		

		shutDownExecutor(exec);
	}
	
	private void runExampleInvokeAllParallel(ExecutorService exec) throws Exception {
		long startTime = System.nanoTime();

		logger.info("BEGIN");
		
		List<Callable<String>> callables = Arrays.asList(
				new CallableExample("task1"),
				new CallableExample("task2"),
				new CallableExample("task3")
			);

		exec.invokeAll(callables)
		.stream()
		.map(fut -> {
			try {
				return fut.get();
			} catch (InterruptedException | ExecutionException e) {
				throw new IllegalStateException(e);
			}
		})
		.forEach(logger::info);
		
		logger.info("[END - " + calcTime(startTime) + "]");
	}
	
	private void runExampleInvokeAnyParallel(ExecutorService exec) throws Exception {
		long startTime = System.nanoTime();

		logger.info("BEGIN");
		
		List<Callable<String>> callables = Arrays.asList(
				new CallableExample("task1", 15),
				new CallableExample("task2", 5),
				new CallableExample("task3", 18)
			);

		String result = exec.invokeAny(callables);
		
		logger.info("[END - " + calcTime(startTime) + "], result: " + result);
	}
	
	public void startParalell() throws Exception {
		ExecutorService exec = Executors.newWorkStealingPool();
		runExampleInvokeAllParallel(exec);
		runExampleInvokeAnyParallel(exec);
		shutDownExecutor(exec);
	}

	public static void main(String[] args) throws Exception {
		Logger logger = Logger.getLogger(FutureExample.class.getName());
		FutureExample futureExample = new FutureExample(logger);
		futureExample.startSequencial();
		futureExample.startParalell();

		System.exit(0);
	}

}
