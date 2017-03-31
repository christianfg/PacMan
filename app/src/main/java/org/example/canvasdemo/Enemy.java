package org.example.canvasdemo;


public class Enemy {
    public int enemyx;
    public int enemyy;
    public boolean dead;
    public int directionCounter;

    public int getDirectionCounter() {
        return directionCounter;
    }


    public void directionCoundown()
    {
        directionCounter--;
    }

    public int getEnemyx() {
        return enemyx;
    }

    public void setEnemyx(int enemyx) {
        this.enemyx = enemyx;
    }

    public int getEnemyy() {
        return enemyy;
    }

    public void setEnemyy(int enemyy) {
        this.enemyy = enemyy;
    }

    public boolean isDead() {
        return dead;
    }

    public void setDead(boolean dead) {
        this.dead = dead;
    }

    public Enemy(int enemyx, int enemyy, boolean dead, int directionCounter) {
        this.enemyx = enemyx;
        this.enemyy = enemyy;
        this.dead = dead;
        this.directionCounter = directionCounter;
    }
}
