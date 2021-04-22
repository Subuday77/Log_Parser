package com.logParser.logParser.beans;

import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class Constants {
    public static final String[] KEYS = {"operatorId", "playerTokenAtLaunch", "uid", "token", "currency", "balance"};
    public static final String[] COLUMNS = {"Round ID", "Operator ID", "UID", "Game Type", "Table ID", "Seat ID", "Date",
            "Bet Type", "Bet", "Win", "Currency", "Balance", "Error Code", "Transaction ID", "Timestamp", "Order"};

    public static final BidiMap<Integer, String> BETTYPES;
    public static final HashMap<Integer, String> GAMEIDS;

    static {
        BETTYPES = new DualHashBidiMap<Integer, String>();
        BETTYPES.put(1, "Table Bet");
        BETTYPES.put(101, "Game Credit");
        BETTYPES.put(3, "TIP");
        BETTYPES.put(103, "TIP Credit");
        BETTYPES.put(4, "Insurance");
        BETTYPES.put(104, "Insurance Credit");
        BETTYPES.put(5, "Double");
        BETTYPES.put(105, "Double Credit");
        BETTYPES.put(6, "Split");
        BETTYPES.put(106, "Split Credit");
        BETTYPES.put(24, "Call");
        BETTYPES.put(124, "Call Credit");
        BETTYPES.put(27, "Jackpot");
        BETTYPES.put(127, "Jackpot Credit");
    }

    static {
        GAMEIDS = new HashMap<>();
        GAMEIDS.put(1, "Blackjack");
        GAMEIDS.put(2, "Baccarat");
        GAMEIDS.put(3, "Roulette");
        GAMEIDS.put(4, "Bet on Numbers");
        GAMEIDS.put(5, "Hybrid Blackjack");
        GAMEIDS.put(6, "Keno");
        GAMEIDS.put(7, "Automatic Roulette");
        GAMEIDS.put(8, "Wheel of Dice");
        GAMEIDS.put(9, "Sede");
        GAMEIDS.put(10, "American Blackjack");
        GAMEIDS.put(11, "American Hybrid Blackjack");
        GAMEIDS.put(12, "Unlimited Blackjack");
        GAMEIDS.put(13, "Lucky 7");
        GAMEIDS.put(14, "Sic BO");
        GAMEIDS.put(15, "Casino Holde’m");
        GAMEIDS.put(16, "Bet on Teen Patti");
        GAMEIDS.put(17, "Three Card Poker(NJ)/ Teen Patti");
        GAMEIDS.put(18, "Roulette with JP");
        GAMEIDS.put(19, "32 Cards");
        GAMEIDS.put(20, "Baccarat KO");
        GAMEIDS.put(21, "Baccarat Super 6");
        GAMEIDS.put(24, "Dragon Tiger");
        GAMEIDS.put(25, "No Commission Baccarat");
        GAMEIDS.put(26, "Baccarat Dragon Bonus");
        GAMEIDS.put(27, "Baccarat Queenco");
        GAMEIDS.put(28, "Baccarat Punto Banco");
        GAMEIDS.put(29, "Roulette Portomaso");
        GAMEIDS.put(30, "Bet on Roulette");
        GAMEIDS.put(31, "American Roulette");
        GAMEIDS.put(32, "Triple Roulette");
        GAMEIDS.put(38, "Andar Bahar");
        GAMEIDS.put(39, "OTT Andar Bahar");
        GAMEIDS.put(43, "One Day Teen Patty");
    }

}

