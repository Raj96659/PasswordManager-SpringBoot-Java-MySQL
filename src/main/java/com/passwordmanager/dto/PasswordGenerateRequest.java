package com.passwordmanager.dto;

public class PasswordGenerateRequest {

    private int length;
    private boolean useUpper;
    private boolean useLower;
    private boolean useNumbers;
    private boolean useSpecial;
    private boolean excludeSimilar;
    private int count;


    public PasswordGenerateRequest() {
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public boolean isUseUpper() {
        return useUpper;
    }

    public void setUseUpper(boolean useUpper) {
        this.useUpper = useUpper;
    }

    public boolean isUseLower() {
        return useLower;
    }

    public void setUseLower(boolean useLower) {
        this.useLower = useLower;
    }

    public boolean isUseNumbers() {
        return useNumbers;
    }

    public void setUseNumbers(boolean useNumbers) {
        this.useNumbers = useNumbers;
    }

    public boolean isUseSpecial() {
        return useSpecial;
    }

    public void setUseSpecial(boolean useSpecial) {
        this.useSpecial = useSpecial;
    }

    public boolean isExcludeSimilar() {
        return excludeSimilar;
    }

    public void setExcludeSimilar(boolean excludeSimilar) {
        this.excludeSimilar = excludeSimilar;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}