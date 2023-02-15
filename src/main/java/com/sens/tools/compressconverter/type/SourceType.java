package com.sens.tools.compressconverter.type;

import java.security.AllPermission;

public enum SourceType {

    ZIPFILE("Zip file source only",".zip"),
    SEVENZFILE("7z file source only",".7z"),
    ALL("All files",".*");

    private final String extension;
    private String label;

    SourceType(final String label, final String extension) {
        this.label = label;
        this.extension = extension;
    }

    public String getLabel() {
        return label;
    }

    public String getExtension() {
        return extension;
    }
}
