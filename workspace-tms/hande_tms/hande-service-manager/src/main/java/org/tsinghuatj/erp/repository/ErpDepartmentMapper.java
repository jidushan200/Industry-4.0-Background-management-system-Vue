package org.tsinghuatj.erp.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.tsinghuatj.erp.domain.ErpDepartment;

@Mapper
@Repository
public interface ErpDepartmentMapper {

	ErpDepartment selectByDepartmentNumber(@Param("departmentCode") String departmentCode);

}
