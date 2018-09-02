package com.iheart.jmssample;

public class Memory1Mb {
    private byte[] array;

    public Memory1Mb(){
        this.array = new byte[1024*1024];
    }
}