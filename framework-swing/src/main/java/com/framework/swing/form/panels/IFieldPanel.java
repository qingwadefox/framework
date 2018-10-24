package com.framework.swing.form.panels;


public interface IFieldPanel<T, F> {

	public T getValue();

	public void setValue(T value);

	public String getCode();

	public void setCode(String code);

	public String getName();

	public void setName(String name);

	public F getComponent();
	
	public void clear();

}
