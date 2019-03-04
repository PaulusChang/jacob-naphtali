package jacob.naphtali.base.bean;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

/**
 * 分页时做请求入口参数
 * @author ChangJian
 * @date 2019-01-18
 */
public class PageRequestBean {

	private Integer pageSize;
	private Integer pageNo;
	
	public PageRequestBean() {
		super();
	}

	public PageRequestBean(Integer pageSize, Integer pageNo) {
		super();
		this.pageSize = pageSize;
		this.pageNo = pageNo;
	}

	public PageRequest toPageRequest(Sort sort) {
		return PageRequest.of(pageNo, pageSize, sort);
	}
	
	public Integer getPageSize() {
		return pageSize;
	}
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
	public Integer getPageNo() {
		return pageNo;
	}
	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
	}
	
}
