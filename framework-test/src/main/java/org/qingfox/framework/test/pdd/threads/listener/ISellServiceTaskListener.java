/**
 * 
 */
package org.qingfox.framework.test.pdd.threads.listener;

import org.qingfox.framework.test.pdd.threads.SellServiceTask;

/**
 * @author Administrator
 *
 */
public interface ISellServiceTaskListener {

	public void onTaskStateChange(SellServiceTask task, Integer checkState, Integer checkSuccess, Integer checkFailure, Integer publicState, Integer publicSuccess, Integer publicFailure, Long lastTaskTime);
}
