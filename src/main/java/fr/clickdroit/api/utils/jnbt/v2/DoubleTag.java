package fr.clickdroit.api.utils.jnbt.v2;

public class DoubleTag extends Tag {
    private final double value;

    public DoubleTag(double value) {
        this.value = value;
    }

    public Double getValue() {
        return Double.valueOf(this.value);
    }

    public String toString() {
        return "TAG_Double(" + this.value + ")";
    }
}
