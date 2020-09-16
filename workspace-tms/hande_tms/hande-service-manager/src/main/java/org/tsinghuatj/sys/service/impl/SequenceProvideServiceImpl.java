package org.tsinghuatj.sys.service.impl;

import java.util.Random;

import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.tsinghuatj.framework.support.BusinessException;
import org.tsinghuatj.sys.service.ISequenceProvideService;



@Service
public class SequenceProvideServiceImpl implements ISequenceProvideService {
    private final Random random = new Random(1000);

    @Override
    @Cacheable(value = "cache-sequence", key = "#data + #serialType")
    @Transactional(propagation = Propagation.SUPPORTS)
    public String getSerial(String data, String serialType) throws BusinessException {
        return String.valueOf(random.nextInt(1000) + 1000);
    }

    @Override
    @CachePut(value = "cache-sequence", key = "#data + #serialType")
    @Transactional(propagation = Propagation.SUPPORTS)
    public String setSerial(String data, String serialType, String serial) throws BusinessException {
        return serial;
    }

    @Override
    @Cacheable(value = "cache-sequence", key = "#data + #serialType")
    @Transactional(propagation = Propagation.SUPPORTS)
    public String getSerialProgressiveIncrease(String data, String serialType) throws BusinessException {
        return String.valueOf(1);
    }
}
