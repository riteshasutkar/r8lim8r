package entities;

import enums.Tier;

public class User {
    String id;
    Tier tier;
//    int tokens;

    public User(String id, Tier tier) {
        this.id = id;
        this.tier = tier;
    }

    public String getId() {
        return id;
    }

    public Tier getTier() {
        return tier;
    }

//    public int getTokens() {
//        return tokens;
//    }
//
//    public void setTokens(int tokens) {
//        this.tokens = tokens;
//    }
}
