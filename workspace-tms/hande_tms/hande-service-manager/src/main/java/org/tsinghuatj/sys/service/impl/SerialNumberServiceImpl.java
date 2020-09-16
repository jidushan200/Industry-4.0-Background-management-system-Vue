package org.tsinghuatj.sys.service.impl;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.Validate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.tsinghuatj.framework.support.BusinessException;
import org.tsinghuatj.framework.utils.CollectionUtils;
import org.tsinghuatj.framework.utils.DateFormatUtils;
import org.tsinghuatj.framework.utils.NumberUtils;
import org.tsinghuatj.framework.utils.StringUtils;
import org.tsinghuatj.sys.service.ISequenceProvideService;
import org.tsinghuatj.sys.service.ISerialNumberService;



@Service
public class SerialNumberServiceImpl implements ISerialNumberService {
    private @Resource ISequenceProvideService sequenceProvideService;
    private final String SERIALTYPE_ORDER = "__";
    private final Map<String, LinkedList<Long>> orderSequence = Collections.synchronizedMap(new HashMap<String, LinkedList<Long>>());
    private final Map<String, LinkedList<Long>> serialSequence = Collections.synchronizedMap(new HashMap<String, LinkedList<Long>>());

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public synchronized String nextOrderNo(Integer source) throws BusinessException {
        String data = DateFormatUtils.format(new Date(), "yyMMddHHmm");
        String key = data;
        if (!orderSequence.containsKey(key)) {
            orderSequence.put(key, new LinkedList<Long>());
        }
        if (CollectionUtils.isEmpty(orderSequence.get(key))) {
            String serial = sequenceProvideService.getSerial(data, SERIALTYPE_ORDER);
            sequenceProvideService.setSerial(data, SERIALTYPE_ORDER, String.valueOf(NumberUtils.toInt(serial) + 100));
            for (int i = 0; i < 100; i++) {
                orderSequence.get(key).offer(NumberUtils.toLong(serial) + i);
            }
        }

        String serial = String.valueOf(orderSequence.get(key).poll());
        return String.format("%s%s%s", source, data, StringUtils.leftPad(serial, 4, "0"));
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public synchronized String nextSerialNo(String prefix) throws BusinessException {
        Validate.isTrue(StringUtils.isNoneBlank(prefix));
        String data = DateFormatUtils.format(new Date(), "yyyyMMdd");
        String key = prefix + data;
        if (!serialSequence.containsKey(key)) {
            serialSequence.put(key, new LinkedList<Long>());
        }
        if (CollectionUtils.isEmpty(serialSequence.get(key))) {
            String serial = sequenceProvideService.getSerialProgressiveIncrease(data, prefix);
            sequenceProvideService.setSerial(data, prefix, String.valueOf(NumberUtils.toInt(serial) + 100));
            for (int i = 0; i < 100; i++) {
                serialSequence.get(key).offer(NumberUtils.toLong(serial) + i);
            }
        }

        String serial = String.valueOf(serialSequence.get(key).poll());
        return String.format("%s%s%s", prefix, data, StringUtils.leftPad(serial, 4, "0"));
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public LinkedList<String> nextSerialNoBatch(String prefix, int count) throws BusinessException {
        LinkedList<String> serialNOList = new LinkedList<>();
        for (int i = 0; i < count; i++) {
            serialNOList.add(nextSerialNo(prefix));
        }
        return serialNOList;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public LinkedList<String> nextOrderNoBatch(Integer source, int count) throws BusinessException {
        LinkedList<String> orderNOList = new LinkedList<>();
        for (int i = 0; i < count; i++) {
            orderNOList.add(nextOrderNo(source));
        }
        return orderNOList;
    }
}
