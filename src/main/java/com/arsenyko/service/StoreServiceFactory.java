package com.arsenyko.service;
/**
 * 
 * @author Arseny Kovalchuk
 *
 */
public class StoreServiceFactory {
    
    private StoreServiceFactory() {}
    
    private static final StoreService instance = new StoreServiceImpl();
    
    public static StoreService getInstance() {
        return instance;
    }

}