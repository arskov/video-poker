package com.arsenyko.service;

import java.io.Serializable;

/**
 * 
 * @author Arseny Kovalchuk
 *
 */
public class GameModel implements Serializable {
    

    private static final long serialVersionUID = 1L;
    
    public static final String DEFAULT = "Please Bet Something";
    public static final String DEAL = "Deal";
    public static final String NEXT = "Next";

    private String userId;
    private int credit = 100;
    private int step = 1;
    private int bet = 0;
    private int wonCombination = -1;
    private String stepText = DEFAULT;
    private String resultText;
    

    private Card[] hand;
    private boolean[] wonCards = new boolean[5];

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getCredit() {
        return credit;
    }

    public void setCredit(int credit) {
        this.credit = credit;
    }

    public Card[] getHand() {
        return hand;
    }

    public void setHand(Card[] hand) {
        this.hand = hand;
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public int getBet() {
        return bet;
    }

    public void setBet(int bet) {
        this.bet = bet;
    }

    public boolean[] getWonCards() {
        return wonCards;
    }

    public void setWonCards(boolean[] wonCards) {
        this.wonCards = wonCards;
    }

    public int getWonCombination() {
        return wonCombination;
    }

    public void setWonCombination(int wonCombination) {
        this.wonCombination = wonCombination;
    }

    public boolean isCanDeal() {
        return (this.step == 1 && this.bet > 0) || (this.step == 2);
    }

    public boolean isCanBet() {
        return this.step == 1;
    }

    public String getStepText() {
        return stepText;
    }

    public void setStepText(String stepText) {
        this.stepText = stepText;
    }

    public String getResultText() {
        return resultText;
    }

    public void setResultText(String resultText) {
        this.resultText = resultText;
    }

}
