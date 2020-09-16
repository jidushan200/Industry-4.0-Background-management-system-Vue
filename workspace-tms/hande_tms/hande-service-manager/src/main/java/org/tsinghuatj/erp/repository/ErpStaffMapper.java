package org.tsinghuatj.erp.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.tsinghuatj.erp.domain.ErpStaff;

@Mapper
@Repository
public interface ErpStaffMapper {
	

	ErpStaff selectByStaffNumber(@Param("staffNumber") String staffNumber);
}
