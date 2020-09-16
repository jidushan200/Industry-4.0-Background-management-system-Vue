package org.tsinghuatj.sys.service;

import org.tsinghuatj.framework.support.BusinessException;

public interface ISequenceProvideService {

    /**
     * 获取序列号
     * 
     * @param data
     * @param serialType
     * @return
     * @throws BusinessException
     */
    String getSerial(String data, String serialType) throws BusinessException;

    /**
     * 设置序列号
     * 
     * @param data
     * @param serialType
     * @param serial
     * @return
     * @throws BusinessException
     */
    String setSerial(String data, String serialType, String serial) throws BusinessException;

    /**
     * 获取递增序列号
     * 
     * @param data
     * @param serialType
     * @return
     * @throws BusinessException
     */
    String getSerialProgressiveIncrease(String data, String serialType) throws BusinessException;

}
