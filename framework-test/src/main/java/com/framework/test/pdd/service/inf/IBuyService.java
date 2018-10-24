/**
 * 
 */
package com.framework.test.pdd.service.inf;

import java.util.List;

import com.framework.test.pdd.entity.OrderEntity;

/**
 * @author zhengwei
 *
 */
public interface IBuyService extends ILoginService {

	public void buy(OrderEntity order) throws Exception;

	public List<OrderEntity> getOrderList(List<OrderEntity> orderList) throws Exception;

}
