package eu.arrowhead.tool.examination.controller.dto;

import java.io.Serializable;
import java.util.List;

public class CloudWithRelaysListResponseDTO implements Serializable {

	//=================================================================================================
	// members
	
	private static final long serialVersionUID = -413677609682231182L;
	
	private List<CloudWithRelaysResponseDTO> data;
	private long count;
	
	//=================================================================================================
	// methods
	
	//-------------------------------------------------------------------------------------------------
	public CloudWithRelaysListResponseDTO() {}
	
	//-------------------------------------------------------------------------------------------------
	public CloudWithRelaysListResponseDTO(final List<CloudWithRelaysResponseDTO> data, final long count) {
		this.data = data;
		this.count = count;
	}

	//-------------------------------------------------------------------------------------------------
	public List<CloudWithRelaysResponseDTO> getData() { return data; }
	public long getCount() { return count; }

	//-------------------------------------------------------------------------------------------------
	public void setData(final List<CloudWithRelaysResponseDTO> data) { this.data = data; }
	public void setCount(final long count) { this.count = count; }	
}
