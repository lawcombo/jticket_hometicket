package com.bluecom.ticketing.domain;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ApiParamDTO  implements Serializable {

	@JsonProperty(value = "Param")	
	Object Param;
}
