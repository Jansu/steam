package de.druz.web.vo;

public class KeyValueVo {

	public String key;
	public Object value;
	
	public KeyValueVo(String key, Object value) {
		super();
		this.key = key;
		this.value = value;
	}
	
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public Object getValue() {
		return value;
	}
	public void setValue(Object value) {
		this.value = value;
	}
}
