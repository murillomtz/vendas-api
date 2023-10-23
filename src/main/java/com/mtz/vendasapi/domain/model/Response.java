package com.mtz.vendasapi.domain.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public class Response<T> extends RepresentationModel<Response<T>>{
	
	private int statusCode;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private T data;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private List<?> content;
	private long timeStamp;
	@JsonInclude(JsonInclude.Include.NON_DEFAULT)
	private int size;
	@JsonInclude(JsonInclude.Include.NON_DEFAULT)
	private int totalPages;
	@JsonInclude(JsonInclude.Include.NON_DEFAULT)
	private long totalElementes;
	@JsonInclude(JsonInclude.Include.NON_DEFAULT)
	private int numberOfElements;
	@JsonInclude(JsonInclude.Include.NON_DEFAULT)
	private int pageNumber;
	
	public Response(){
		this.timeStamp = System.currentTimeMillis();
	}
}
