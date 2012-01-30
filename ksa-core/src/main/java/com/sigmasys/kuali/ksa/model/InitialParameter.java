package com.sigmasys.kuali.ksa.model;

public class InitialParameter implements Comparable<InitialParameter> {

	private String name;
	private String value;
	private boolean readOnly;

	public InitialParameter() {
	}

	public InitialParameter(String name, String value) {
		this(name, value, false);
	}

	public InitialParameter(String name, String value, boolean readOnly) {
		setName(name);
		setValue(value);
		setReadOnly(readOnly);
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
		return readOnly;
	}

	public void setReadOnly(Boolean readOnly) {
		this.readOnly = readOnly;
	}

	@Override
	public int compareTo(InitialParameter param) {
		if (getName() == null) {
			return -1;
		}
		return getName().compareTo(param.getName());
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (this.getClass() != obj.getClass()) {
			return false;
		}
		InitialParameter para = (InitialParameter) obj;
		if (this.getName() == null) {
			// When both are null the two entities are new, but not necessarily equal
			return para.getName() == null && super.equals(obj);
		}
		return this.getName().equals(para.getName());
	}

	@Override
	public int hashCode() {
		return 31 + ((getName() == null) ? super.hashCode() : getName().hashCode());
	}
	
	public InitialParameter copy() {
		InitialParameter d = new InitialParameter();
		d.setName(getName());
		d.setValue(getValue());
		d.setReadOnly(isReadOnly());
		return d;
	}

}

