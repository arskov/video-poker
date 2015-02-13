package com.arsenyko.service;
/**
 * 
 * @author Arseny Kovalchuk
 *
 */
public interface StoreService {
    
    GameModel getGameModel(String userId);

    void storeGameModel(GameModel model, String userId);
    
    void reset(String userId);
    
    void updateCredit(UserInfo user, int credit);
    
    int currentCredit(UserInfo user);
    
}
