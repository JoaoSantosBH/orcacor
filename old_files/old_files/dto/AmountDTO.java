package com.jomar.senhorpintor.dto;

public class AmountDTO {
    private float litros;
    private float dif;

    public float getDif() {
        return dif;
    }

    public void setDif(float dif) {
        this.dif = dif;
    }

    public float getLitros() {
        return litros;
    }

    public void setLitros(float litros) {
        this.litros = litros;
    }

    private int lata18;
    private int lata36;
    private int lata09;

    public int getLata18() {
        return lata18;
    }

    public void setLata18(int lata18) {
        this.lata18 = lata18;
    }

    public int getLata36() {
        return lata36;
    }

    public void setLata36(int lata36) {
        this.lata36 = lata36;
    }

    public int getLata09() {
        return lata09;
    }

    public void setLata09(int lata09) {
        this.lata09 = lata09;
    }
}
