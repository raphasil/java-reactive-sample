package br.com.raphasil.reactive.example.rx;

import java.util.logging.Logger;

import br.com.raphasil.reactive.example.BaseExample;
import rx.Observable;
import rx.Single;
import rx.Subscriber;
import rx.schedulers.Schedulers;

public class ObservableExample extends BaseExample {

	public ObservableExample(Logger logger) {
		super(logger);
	}
	
	private void runExampleOne() {
		long startTime = System.nanoTime();
		
		logger.info("BEGIN");
		Observable<String> observable = Observable.just(desserts.get(0));
		observable.subscribe(logger::info);
		logger.info("[END - " + calcTime(startTime) + "]");
	}
	
	private void runExampleTwo() {
		long startTime = System.nanoTime();
		
		logger.info("BEGIN");
		Observable<String> observable = Observable.just(desserts.get(0), desserts.get(1), desserts.get(2));
		observable.subscribe(logger::info);
		logger.info("[END - " + calcTime(startTime) + "]");	
        
	}
	
	private void runExampleThree() {
		long startTime = System.nanoTime();
		
		logger.info("BEGIN");
		Observable<String> observable = Observable.from(desserts);
		observable.subscribe(logger::info);
		logger.info("[END - " + calcTime(startTime) + "]");	
        
	}
	
	private void runExampleFour() {
		long startTime = System.nanoTime();
		
		logger.info("BEGIN");
		Observable<String> observable = Observable.from(desserts);
		observable
		.map( d -> {
			logger.info("map 1");
			StringBuffer sb = new StringBuffer(d);
			return sb.reverse().toString();
		})
		.map( d -> {
			logger.info("map 2");
			return d.substring(0, d.length() / 2);
		})
		.subscribe(logger::info);
		logger.info("[END - " + calcTime(startTime) + "]");	
        
	}
	
	private void runExampleFive() {
		long startTime = System.nanoTime();
		
		logger.info("BEGIN");
		
		Subscriber<Integer> subscriber = new Subscriber<Integer>() {
			
			@Override
			public void onStart() {
				logger.info("onStart");
		    }
			
			@Override
			public void onNext(Integer i) {
				logger.info("Next " + i);
			}
			
			@Override
			public void onError(Throwable e) {
				logger.warning("Error: " + e);
			}
			
			@Override
			public void onCompleted() {
				logger.info("Done");
			}
		};
		
		Observable<Integer> observable = Observable.range(1, 5); // .subscribeOn(Schedulers.io());		
		observable.subscribe(subscriber);
		logger.info("[END - " + calcTime(startTime) + "]");	
        
	}
	
	private void runExampleSix() {
		long startTime = System.nanoTime();
		
		logger.info("BEGIN");
		
		Observable.from(desserts)
				.skip(3)
				.take(2)
				.subscribe(logger::info);
		
		logger.info("[END - " + calcTime(startTime) + "]");	
        
	}
	
	private void runExampleSeven() {
		long startTime = System.nanoTime();
		
		logger.info("BEGIN");
		
		
		Observable.from(desserts)		
		.flatMap( d -> {
			logger.info("map 1");
			StringBuffer sb = new StringBuffer(d);
			return Observable.just(sb.reverse().toString()); //.subscribeOn(Schedulers.io());
		})
		.flatMap( d -> {
			logger.info("map 2");
			return Observable.just(d.substring(0, d.length() / 2)); // .subscribeOn(Schedulers.io());
		})	
		.subscribe(logger::info);				
		
		logger.info("[END - " + calcTime(startTime) + "]");	
        
	}
	
	private void runExampleEight() {
		long startTime = System.nanoTime();
		
		logger.info("BEGIN");
		
		
		Observable.from(desserts)
		.flatMapSingle( str -> {
			return Single.create( s -> {
				logger.info("map 1");
				StringBuffer sb = new StringBuffer(str);
				s.onSuccess(sb.reverse().toString());
			}).subscribeOn(Schedulers.io());
		})
		.flatMapSingle( str -> {
			return Single.create( s -> {
				logger.info("map 2");
				s.onSuccess(str.toString().substring(0, str.toString().length() / 2));
			}).subscribeOn(Schedulers.io());
		})
		.toList()
		.subscribe(list -> {
			for(Object str : list) {
				logger.info(str.toString());				
			}
		});				
		
		logger.info("[END - " + calcTime(startTime) + "]");	
        
	}
	
	private void runExampleNine() {
		long startTime = System.nanoTime();
		
		logger.info("BEGIN");
		
		
		Observable.from(desserts)
		.flatMapSingle( str -> {
			return Single.create( s -> {
				String result = str.concat(" - map 1");
				logger.info(result);
				s.onSuccess(result);
			}).subscribeOn(Schedulers.io());
		})
		.flatMapSingle( str -> {
			return Single.create( s -> {
				String result = str.toString().concat(" - map 2");
				logger.info(result);
				s.onSuccess(result);
			}).subscribeOn(Schedulers.io());
		})
		.toList()
		.subscribe(list -> {
			for(Object str : list) {
				logger.info(str.toString());				
			}
		});				
		
		logger.info("[END - " + calcTime(startTime) + "]");	
        
	}

	public static void main(String[] args) throws Exception {
		Logger logger = Logger.getLogger(ObservableExample.class.getName());
		ObservableExample observableExample = new ObservableExample(logger);
		
		observableExample.runExampleOne();	
		observableExample.runExampleTwo();
		observableExample.runExampleThree();
		observableExample.runExampleFour();
		observableExample.runExampleFive();
		observableExample.runExampleSix();
		observableExample.runExampleSeven();		
		observableExample.runExampleEight();
		observableExample.runExampleNine();
		
		Thread.sleep(10000);
		
	}

}
