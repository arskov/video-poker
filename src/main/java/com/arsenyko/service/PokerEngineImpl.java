package com.arsenyko.service;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Very simple implementation of the Video Poker Engine.
 * <br/>
 * 
 * <table class="wikitable">
 * <tbody><tr><th>Hand</th><th>1 credit</th><th>2 credits</th><th>3 credits</th><th>4 credits</th><th>5 credits</th></tr>
 * <tr><td>Royal Flush</td><td>250</td><td>500</td><td>750</td><td>1000</td><td>4000</td></tr>
 * <tr><td>Straight Flush</td><td>50</td><td>100</td><td>150</td><td>200</td><td>250</td></tr>
 * <tr><td>Four of a kind</td><td>25</td><td>50</td><td>75</td><td>100</td><td>125</td></tr>
 * <tr><td>Full House</td><td>9</td><td>18</td><td>27</td><td>36</td><td>45</td></tr>
 * <tr><td>Flush</td><td>6</td><td>12</td><td>18</td><td>24</td><td>30</td></tr>
 * <tr><td>Straight</td><td>4</td><td>8</td><td>12</td><td>16</td><td>20</td></tr>
 * <tr><td>Three of a kind</td><td>3</td><td>6</td><td>9</td><td>12</td><td>15</td></tr>
 * <tr><td>Two Pair</td><td>2</td><td>4</td><td>6</td><td>8</td><td>10</td></tr>
 * <tr><td>Jacks or Better</td><td>1</td><td>2</td><td>3</td><td>4</td><td>5</td></tr>
 * <tr><td>Theoretical Return</td><td>98.4%</td><td>98.4%</td><td>98.4%</td><td>98.4%</td><td>99.5%</td></tr>
 * </tbody></table>
 *
 * @author Arseny Kovalchuk
 * 
 * 
 */
public class PokerEngineImpl implements PokerEngine {
    
    private static Logger log = Logger.getLogger(PokerEngineImpl.class.getName());
    
    private static final String[] SUITS = new String[] {"&clubs;", "&diams;", "&hearts;", "&spades;"};
    private static final String[] RANKS = new String[] {"2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A"};
    
    private static final Map<String, Integer> SUIT_VALUE = new HashMap<>();
    private static final Map<String, Integer> RANK_VALUE = new HashMap<>();
    
    static {
        for (int i = 0; i < SUITS.length; i++) {
            SUIT_VALUE.put(SUITS[i], i);
        }
        for (int i = 0; i < RANKS.length; i++) {
            RANK_VALUE.put(RANKS[i], i);
        }
        
    }
    
    static class CardComparator<T extends Card> implements Comparator<T> {

        @Override
        public int compare(T card1, T card2) {
            int card1rankValue = RANK_VALUE.get(card1.getRank());
            int card2rankValue = RANK_VALUE.get(card2.getRank());
            
            if (card1rankValue == card2rankValue) {
                int card1suitValue = SUIT_VALUE.get(card1.getSuit());
                int card2suitValue = SUIT_VALUE.get(card2.getSuit());
                return card1suitValue - card2suitValue;
            } else {
                return card1rankValue - card2rankValue;
            }
        }
        
    }
    
    
    // according to table in JavaDoc (top, left)
    // for example 
    // coefficients[0][0] is Royal Flush for 1 credit
    // coefficients[0][4] is Royal Flush for 5 credits
    // coefficients[8][0] is Jacks or Better for 1 credit
    
    private static final String[] COMBINATION_NAME = new String[] {
        "Royal Flush", "Straight Flush", "Full House", "Flush", "Four of a kind", "Straight", "Three of a kind", "Two Pair", "Jacks or Better"
    };
    
    private static final int[][] COEFFICIENTS = new int[][] {
        new int[] {250, 500, 750, 1000, 4000},  // Royal Flush
        new int[] {50,  100, 150, 200,  250},   // Straight Flush
        new int[] {9,   18,  27,  36,   45},    // Full House
        new int[] {6,   12,  18,  24,   30},    // Flush
        new int[] {25,  50,  75,  100,  125},   // Four of a kind
        new int[] {4,   8,   12,  16,   20},    // Straight
        new int[] {3,   6,   9,   12,   15},    // Three of a kind
        new int[] {2,   4,   6,   8,    10},    // Two Pair
        new int[] {1,   2,   3,   4,    5},     // Jacks or Better
    };
    
    StoreService storeService = StoreServiceFactory.getInstance();
    
    static class CardHolder {
        
        int combination = -1;
        
        int[] rankCounter = new int[13];
        int[] suitCounter = new int[4];
        
        Card[] cards = new Card[5];
        
        void addCard(Card card) {
            rankCounter[RANK_VALUE.get(card.getRank())]++;
            suitCounter[SUIT_VALUE.get(card.getSuit())]++;
        }
        
        int getCombination() {
            if (checkRoyalFlush()) {
                return combination;
            } else if (checkStraightFlush()) {
                return combination;
            } else if (checkFullHouse()) {
                return combination;
            } else if (checkFlush()) {
                return combination;
            } else if (checkFourOfAKind()) {
                return combination;
            } else if (checkStraight()) {
                return combination;
            } else if (checkThreeOfAKind()) {
                return combination;
            } else if (checkTwoPair()) {
                return combination;
            } else if (checkOnePair()) {
                return combination;
            } else {
                return combination;
            }
        }
        
        private boolean checkRoyalFlush() {
            for (int i = 0; i < suitCounter.length; i++) {
                if (suitCounter[i] == 5) {
                    // one suit
                    // starting from 10 til A
                    if (rankCounter[8] == 1 && rankCounter[9] == 1 && rankCounter[10] == 1 && rankCounter[11] == 1 && rankCounter[12] == 1) {
                        combination = 0;
                        return true;
                    }
                }
            }
            return false;
        }

        private boolean checkStraightFlush() {
            for (int i = 0; i < suitCounter.length; i++) {
                if (suitCounter[i] == 5) {
                    for (int j = 0; j <= 7; j++) {
                        if (rankCounter[j] == 1 && rankCounter[j + 1] == 1 && rankCounter[j + 2] == 1 && rankCounter[j + 3] == 1 && rankCounter[j + 4] == 1) {
                            combination = 1;
                            return true;
                        }
                    }
                }
            }
            return false;
        }
        

        private boolean checkFullHouse() {
            for (int i = 0; i < rankCounter.length; i++) {
                if (rankCounter[i] == 3) {
                    for (int j = 0; j < rankCounter.length; j++) {
                        if (j == i)
                            continue;
                        if (rankCounter[j] == 2) {
                            combination = 2;
                            return true;
                        }
                    }
                }
            }
            return false;
        }

        private boolean checkFlush() {
            // we may detect it earlier in checkRoyalFlush or checkStraightFlush
            // yeah, it's not optimized!
            for (int i = 0; i < suitCounter.length; i++) {
                if (suitCounter[i] == 5) {
                    combination = 3;
                    return true;
                }
            }
            return false;
        }
        
        private boolean checkFourOfAKind() {
            for (int i = 0; i < rankCounter.length; i++) {
                if (rankCounter[i] == 4) {
                    combination = 4;
                    return true;
                }
            }
            return false;
        }
        
        private boolean checkStraight() {
            for (int j = 0; j <= 7; j++) {
                if (rankCounter[j] == 1 && rankCounter[j + 1] == 1 && rankCounter[j + 2] == 1 && rankCounter[j + 3] == 1 && rankCounter[j + 4] == 1) {
                    combination = 5;
                    return true;
                }
            }
            return false;
                
        }

        private boolean checkThreeOfAKind() {
            for (int i = 0; i < rankCounter.length; i++) {
                if (rankCounter[i] == 3) {
                    combination = 6;
                    return true;
                }
            }
            return false;        
        }

        private boolean checkTwoPair() {
            for (int i = 0; i < rankCounter.length; i++) {
                if (rankCounter[i] == 2) {
                    for (int j = 0; j < rankCounter.length; j++) {
                        if (j == i)
                            continue;
                        if (rankCounter[j] == 2) {
                            combination = 7;
                            return true;
                        }
                    }
                }
            }
            return false;
        }

        private boolean checkOnePair() {
            for (int i = 9; i < rankCounter.length; i++) {
                if (rankCounter[i] == 2) {
                    combination = 8;
                    return true;
                }
            }
            return false;
        }

        boolean[] getWonCards() {
            boolean[] wonCards = new boolean[5];
            // TODO : identify cards which win!
            return wonCards;
        }

        public String getText(int index) {
            if (index >= 0 && index < COMBINATION_NAME.length) {
                return COMBINATION_NAME[index];
            } else {
                return null;
            }
        }
        
    }
    
    
    @Override
    public GameModel load(UserInfo user) {
        
        if (log.isLoggable(Level.FINE)) {
            log.fine(String.format("Current user ID is %s", user.getEmail()));
        }
        
        GameModel model = storeService.getGameModel(user.getUserId());
        if (model == null) {
            model = createNewDefault(user.getUserId());
        }
        //dealModelCards(model);
        storeService.storeGameModel(model, user.getUserId());
        return model;
    }

    @Override
    public GameModel deal(UserInfo user) {
        
        if (log.isLoggable(Level.FINE)) {
            log.fine(String.format("Current user ID is %s", user.getEmail()));
        }
        
        GameModel model = storeService.getGameModel(user.getUserId());
        
        if (model == null || model.getStep() == 3) {
            int credit = model == null ? 100 : model.getCredit();
            //storeService.updateCredit(userId, credit);
            model = createNewDefault(user.getUserId());
            model.setBet(0);
            model.setStep(1);
            model.setStepText(GameModel.DEFAULT);
            model.setCredit(credit);
        } else if (model.getStep() == 1) {
            model.setStep(2);
            model.setStepText(GameModel.DEAL);
            dealModelCards(model);
        } else if (model.getStep() == 2) {
            dealModelCards(model);
            checkWin(model, user);
            model.setStep(3);
            model.setBet(0);
            model.setStepText(GameModel.NEXT);

        }
        
        storeService.storeGameModel(model, user.getUserId());
        return model;
    }

    @Override
    public void toggleHold(UserInfo user, int card) {
        if (0 <= card && card <= 4) {
            GameModel model = storeService.getGameModel(user.getUserId());
            if (model.getStep() == 2) {
                model.getHand()[card].setHeld(!model.getHand()[card].isHeld());
                storeService.storeGameModel(model, user.getUserId());
            } else {
                throw new RuntimeException("Cannot hold!");
            }
        } else {
            throw new RuntimeException("Cannot hold!");
        }
    }


    @Override
    public void bet(UserInfo user, int bet) {
        if (1 <= bet && bet <= 5) {
            GameModel model = storeService.getGameModel(user.getUserId());
            
            if (model.isCanBet()) {
                model.setBet(bet);
                storeService.storeGameModel(model, user.getUserId());
            } else {
                throw new RuntimeException("Cannot bet!");
            }
        } else {
            throw new RuntimeException("Cannot bet!");
        }
    }
    
    protected void checkWin(GameModel model, UserInfo user) {
        // Didn't think on *right* optimized way.
        // Just straightforward check for combinations.
        CardHolder holder = new CardHolder();
        for (Card card : model.getHand()) {
            card.setHeld(false);
            holder.addCard(card);
        }
        
        int combination = holder.getCombination();
        int credit = model.getCredit() - model.getBet();
        if (combination > 0) {
            int bonus = model.getBet() > 0 ? COEFFICIENTS[combination][model.getBet() - 1] : 0;
            credit += bonus;
            model.setResultText(String.format("You Win %s! %s", bonus, holder.getText(holder.getCombination())));
        } else {
            model.setResultText("Lose :(");
        }
        model.setCredit(credit);
        model.setWonCombination(combination);
        model.setWonCards(holder.getWonCards());
        
        storeService.updateCredit(user, credit);
    }

    protected void dealModelCards(GameModel model) {
        Card[] cards = model.getHand();
        
        //Random randomSuit = new Random();
        //Random randomRank = new Random();
        
        ThreadLocalRandom random = ThreadLocalRandom.current();
        
        Set<Card> doNotRepeat = new HashSet<>();
        
        for(int i = 0; i < cards.length; i++) {
            doNotRepeat.add(cards[i]);
        }
        
        for (int i = 0; i < cards.length; i++) {
            
            if (cards[i].isHeld())
                continue;
            Card card = null;
            String suit = null;
            String rank = null;
            do {
                card = new Card();
                suit = SUITS[random.nextInt(4)];
                rank = RANKS[random.nextInt(13)]; 
                card.setSuit(suit);
                card.setRank(rank);
            } while (doNotRepeat.contains(card));
            
            // css class = card rank-q hearts
            StringBuilder sb = new StringBuilder();
            sb.append("card ").append("rank-").append(rank.toLowerCase()).append(" ").append(suit.substring(1, suit.length() - 1));
            card.setClazz(sb.toString());
            cards[i] = card;
            doNotRepeat.add(card);
        }        
    }
    
    public static GameModel createNewDefault(String userId) {
        
        GameModel model = new GameModel();
        model.setUserId(userId);
        model.setHand(new Card[5]);

        for (int i = 0; i < 5; i++) {
            Card card = null;
            card = new Card();
            StringBuilder sb = new StringBuilder();
            sb.append("card ").append("back");
            card.setClazz(sb.toString());
            model.getHand()[i] = card;
        }
        
        return model;
    }

    @Override
    public void reset(UserInfo user) {
        storeService.reset(user.getUserId());
    }

}
