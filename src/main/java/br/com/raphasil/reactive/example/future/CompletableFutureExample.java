package br.com.raphasil.reactive.example.future;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.logging.Logger;

import br.com.raphasil.reactive.example.BaseExample;

public class CompletableFutureExample extends BaseExample {

	class SupplierExample implements Supplier<String> {
		@Override
		public String get() {
			String info = "SupplierExample ".concat(getRandomDessert());
			try {
				Thread.sleep(15);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			logger.info(info);
			return info;
		}
	}

	class FunctionExampleOne implements Function<String, String> {
		@Override
		public String apply(String s) {
			String info = "FunctionExampleOne ".concat(getRandomDessert());
			try {
				Thread.sleep(15);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			logger.info(info);
			return s.concat(" ").concat(info);
		}
	}

	class FunctionExampleTwo implements Function<String, String> {
		@Override
		public String apply(String s) {
			String info = "FunctionExampleTwo ".concat(getRandomDessert());
			try {
				Thread.sleep(15);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			logger.info(info);
			return s.concat(" ").concat(info);
		}
	}

	public CompletableFutureExample(Logger logger) {
		super(logger);
	}

	public void startSequencial() throws Exception {

		long startTime = System.nanoTime();

		ExecutorService exec = Executors.newSingleThreadExecutor();
		CompletableFuture<String> supplyAsync = CompletableFuture.supplyAsync(new SupplierExample(), exec);
		CompletableFuture<String> funcOne = supplyAsync.thenApply(new FunctionExampleOne());
		CompletableFuture<String> funcTwo = funcOne.thenApply(new FunctionExampleTwo());
		CompletableFuture<String> funcThree = funcTwo.thenApply(s -> {
			String info = "FunctionExampleThree ".concat(getRandomDessert());
			logger.info(info);
			return s.concat(" ").concat(info);
		});

		logger.info("before get, supplyAsync, isDone: " + supplyAsync.isDone());
		logger.info("before get, funcOne, isDone: " + funcOne.isDone());
		logger.info("before get, funcTwo, isDone: " + funcTwo.isDone());
		logger.info("before get, funcThree, isDone: " + funcThree.isDone());

		String result = funcThree.get(); // block

		logger.info("after get, supplyAsync, isDone: " + supplyAsync.isDone());
		logger.info("after get, funcOne, isDone: " + funcOne.isDone());
		logger.info("after get, funcTwo, isDone: " + funcTwo.isDone());
		logger.info("after get, funcThree, isDone: " + funcThree.isDone());

		logger.info("Sequencial [" + calcTime(startTime) + "] Result: " + result);

		shutDownExecutor(exec);
	}

	public static void main(String[] args) throws Exception {

		Logger logger = Logger.getLogger(CompletableFutureExample.class.getName());

		CompletableFutureExample completableFutureExample = new CompletableFutureExample(logger);
		completableFutureExample.startSequencial();

		System.exit(0);
	}

}
