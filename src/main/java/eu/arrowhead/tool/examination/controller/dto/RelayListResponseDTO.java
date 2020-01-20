package eu.arrowhead.tool.examination.controller.dto;

import java.io.Serializable;
import java.util.List;

public class RelayListResponseDTO implements Serializable {
	
	//=================================================================================================
	// members
	
	private static final long serialVersionUID = 2424981884919746719L;
	
	private List<RelayResponseDTO> data;
	private long count;
	
	//=================================================================================================
	// methods
	
	//-------------------------------------------------------------------------------------------------
	public RelayListResponseDTO() {}

	//-------------------------------------------------------------------------------------------------
	public RelayListResponseDTO(final List<RelayResponseDTO> data, final long count) {
		this.data = data;
		this.count = count;
	}

	//-------------------------------------------------------------------------------------------------
	public List<RelayResponseDTO> getData() { return data; }
	public long getCount() { return count; }

	//-------------------------------------------------------------------------------------------------
	public void setData(final List<RelayResponseDTO> data) { this.data = data; }
	public void setCount(final long count) { this.count = count; }

}
