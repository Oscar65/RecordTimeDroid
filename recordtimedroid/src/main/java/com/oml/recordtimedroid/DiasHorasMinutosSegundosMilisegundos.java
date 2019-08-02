package com.oml.recordtimedroid;

class DiasHorasMinutosSegundosMilisegundos {
    private long dias;
    private long horas;
    private long minutos;
    private long segundos;
    private long milisegundos;

    DiasHorasMinutosSegundosMilisegundos() {
        dias = 0;
        horas = 0;
        minutos = 0;
        segundos = 0;
        milisegundos = 0;
    }

    long getDias() {
        return dias;
    }

    void setDias(long dias) {
        this.dias = dias;
    }

    long getHoras() {
        return horas;
    }

    void setHoras(long horas) {
        this.horas = horas;
    }

    long getMinutos() {
        return minutos;
    }

    void setMinutos(long minutos) {
        this.minutos = minutos;
    }

    long getSegundos() {
        return segundos;
    }

    void setSegundos(long segundos) {
        this.segundos = segundos;
    }

    long getMilisegundos() {
        return milisegundos;
    }

    void setMilisegundos(long milisegundos) {
        this.milisegundos = milisegundos;
    }
}