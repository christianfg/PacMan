package org.example.canvasdemo;

public class Goldcoin {
    public int goldy;
    public int goldx;
    public boolean taken;

    public int getGoldy() {
        return goldy;
    }

    public int getGoldx() {
        return goldx;
    }

    public void setGoldy(int goldy) {
        this.goldy = goldy;
    }

    public void setGoldx(int goldx) {
        this.goldx = goldx;
    }

    public Goldcoin(int goldy, int goldx, boolean taken) {
        this.goldy = goldy;
        this.goldx = goldx;
        this.taken = taken;
    }
}
