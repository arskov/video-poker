package com.arsenyko.service;
/**
 * 
 * @author Arseny Kovalchuk
 *
 */
public class PokerEngineFactory {
    
    private final static PokerEngine engine = new PokerEngineImpl();
    
    private PokerEngineFactory() {}
    
    public static PokerEngine getEngineInstance() {
        return engine;
    }

}
