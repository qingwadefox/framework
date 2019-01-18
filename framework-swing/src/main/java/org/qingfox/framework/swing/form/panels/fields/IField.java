package org.qingfox.framework.swing.form.panels.fields;

import java.io.Serializable;

public interface IField<T> extends Cloneable {

	public T getValue();

	public void setValue(T value);

}
