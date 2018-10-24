package com.framework.swing.form.panels.fields;

import java.io.Serializable;

public interface IField<T> extends Cloneable {

	public T getValue();

	public void setValue(T value);

}
