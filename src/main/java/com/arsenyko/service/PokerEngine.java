package com.arsenyko.service;
/**
 * 
 * @author Arseny Kovalchuk
 *
 */
public interface PokerEngine {

    GameModel load(UserInfo user);
    
    GameModel deal(UserInfo user);

    void toggleHold(UserInfo user, int card);

    void bet(UserInfo user, int bet);

    void reset(UserInfo user);

}
