package org.qingfox.framework.database.sequence;

public interface ISequenceCreate<T> {

	public T next();

	public T current();

	public T previous();
}
