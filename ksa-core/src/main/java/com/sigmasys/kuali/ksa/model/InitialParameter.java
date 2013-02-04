package com.sigmasys.kuali.ksa.model;

public class InitialParameter implements Comparable<InitialParameter> {

    private String name;
    private String value;
    private Boolean readOnly;
    private Boolean locked;

    public InitialParameter() {
    }

    public InitialParameter(String name, String value) {
        this(name, value, false, false);
    }

    public InitialParameter(String name, String value, boolean readOnly) {
        this(name, value, readOnly, false);
    }

    public InitialParameter(String name, String value, boolean readOnly, boolean locked) {
        setName(name);
        setValue(value);
        setReadOnly(readOnly);
        setLocked(locked);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isReadOnly() {
        return readOnly != null ? readOnly : false;
    }

    public void setReadOnly(Boolean readOnly) {
        this.readOnly = readOnly;
    }

    public Boolean isLocked() {
        return locked != null ? locked : false;
    }

    public void setLocked(Boolean locked) {
        this.locked = locked;
    }

    @Override
    public int compareTo(InitialParameter param) {
        if (getName() == null) {
            return -1;
        }
        return getName().toLowerCase().compareTo(param.getName().toLowerCase());
    }

}

