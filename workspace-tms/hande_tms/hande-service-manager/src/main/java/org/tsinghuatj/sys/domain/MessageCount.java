package org.tsinghuatj.sys.domain;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ApiModel
public class MessageCount implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer count;
	private Integer readFlag;

}
