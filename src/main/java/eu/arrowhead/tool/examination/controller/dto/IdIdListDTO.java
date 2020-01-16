package eu.arrowhead.tool.examination.controller.dto;

import java.io.Serializable;
import java.util.List;

public class IdIdListDTO implements Serializable {
	
	//=================================================================================================
	// members
	
	private static final long serialVersionUID = 7852566397409625879L;
	
	private Long id;
	private List<Long> idList;
	
	//=================================================================================================
	// methods
	
	//-------------------------------------------------------------------------------------------------
	public IdIdListDTO() {}
	
	//-------------------------------------------------------------------------------------------------
	public IdIdListDTO(final Long id, final List<Long> idList) {
		this.id = id;
		this.idList = idList;
	}

	//-------------------------------------------------------------------------------------------------
	public Long getId() { return id; }
	public List<Long> getIdList() { return idList; }

	//-------------------------------------------------------------------------------------------------
	public void setId(final Long id) { this.id = id; }
	public void setIdList(final List<Long> idList) { this.idList = idList; }	
}
