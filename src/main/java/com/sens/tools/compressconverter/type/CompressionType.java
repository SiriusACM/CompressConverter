package com.sens.tools.compressconverter.type;

public enum CompressionType {

    ZIPMIN("Zip no compression"),
    ZIPMAX("Zip maximal compresson");

    private String label;

    CompressionType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
