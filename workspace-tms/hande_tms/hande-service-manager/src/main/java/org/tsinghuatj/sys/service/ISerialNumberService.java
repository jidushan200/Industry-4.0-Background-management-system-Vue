package org.tsinghuatj.sys.service;

import java.util.LinkedList;

import org.tsinghuatj.framework.support.BusinessException;

/**
 * 序列号生成服务
 * 
 */
public interface ISerialNumberService {
    /**
     * 获取订单编号
     * 
     * @param source
     *            终端来源
     * @return
     * @throws BusinessException
     */
    String nextOrderNo(Integer source) throws BusinessException;

    /**
     * 获取流水编号
     * 
     * @param prefix
     * 
     * @param serialType
     * @return
     * @throws BusinessException
     */
    String nextSerialNo(String prefix) throws BusinessException;

    /**
     * 批量获取流水号
     * 
     * @param count
     * @return
     * @throws BusinessException
     */
    LinkedList<String> nextSerialNoBatch(String prefix, int count) throws BusinessException;

    /**
     * 批量生成订单号
     * 
     * @param source
     * @param count
     * @return
     * @throws BusinessException
     */
    LinkedList<String> nextOrderNoBatch(Integer source, int count) throws BusinessException;
}
