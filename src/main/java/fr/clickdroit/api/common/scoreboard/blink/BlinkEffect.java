package fr.clickdroit.api.common.scoreboard.blink;

public class BlinkEffect {
    private int count = 0;

    private boolean back = false;

    private String text = "bonne-game";

    public void next() {
        if (this.count == 0) {
            this.text = "Game";
        } else if (this.count == 1) {
            this.text = "Game";
        } else if (this.count == 2) {
            this.text = "Game";
        } else if (this.count == 3) {
            this.text = "Game";
        } else if (this.count == 4) {
            this.text = "Game";
        } else if (this.count == 5) {
            this.text = "Game";
        } else if (this.count == 6) {
            this.text = "Game";
        } else if (this.count == 7) {
            this.text = "Game";
        } else if (this.count == 8) {
            this.text = "Game";
        } else if (this.count == 9) {
            this.text = "Game";
        } else if (this.count == 10) {
            this.text = "Game";
        } else if (this.count == 11) {
            this.text = "Game";
        }
        if (this.count == 12)
            this.back = true;
        if (this.count == -10) {
            this.back = false;
            this.count = 0;
        }
        if (!this.back) {
            this.count++;
        } else {
            this.count--;
        }
    }

    public String getText() {
        return this.text;
    }
}
