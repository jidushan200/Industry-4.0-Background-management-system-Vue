package ${basepackage}.${namespace}.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ApiModel
public class ${table.className} implements Serializable {
	
	private static final long serialVersionUID = 1L;

	<#list table.pkColumns as column>
	/**
	 * @Fields ${column.sqlName} : ${column.columnAlias!}
	 */
	@ApiModelProperty(name = "${column.columnNameLower}", notes = "${column.columnAlias!}")
	private ${column.javaType} ${column.columnNameLower};
	</#list>
	<#list table.notPkColumns as column>
	/**
	 * @Fields ${column.sqlName} : ${column.columnAlias!}
	 */
	@ApiModelProperty(name = "${column.columnNameLower}", notes = "${column.columnAlias!}")
	private ${column.javaType} ${column.columnNameLower};
	</#list>
	
	@Override
    public String toString() {
        return  "${table.className} " + 
                <#list table.pkColumns as column>
                    "[${column.columnNameLower}=" + ${column.columnNameLower} + "]"
                </#list>
                <#list table.notPkColumns as column>
                   + ",[${column.columnNameLower}=" + ${column.columnNameLower} + "]"
                </#list>;
	}
}

