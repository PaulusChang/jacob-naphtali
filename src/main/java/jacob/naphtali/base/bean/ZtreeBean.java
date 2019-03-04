package jacob.naphtali.base.bean;

import java.util.LinkedList;
import java.util.List;

import jacob.naphtali.base.bean.util.ReflectUtils;
import jacob.naphtali.base.bean.util.StringUtils;

/**
 * Ztree结构
 * @author ChangJian
 * @date 2019-01-23
 */
public class ZtreeBean {
	
	private String id;
	private String pId;
	private String name;
	
	public ZtreeBean() {
		super();
	}

	public ZtreeBean(String id, String pId, String name) {
		super();
		this.id = id;
		this.pId = pId;
		this.name = name;
	}

	public static ZtreeBean getInstance(Object obj, String idField, String pIdField, String nameField) {
		ZtreeBean ztreeBean = new ZtreeBean();
		ztreeBean.id = StringUtils.toString(ReflectUtils.getFieldVal(obj, idField));
		if (null == ztreeBean.id) {
			return null;
		}
		if (!StringUtils.isEmpty(pIdField)) {
			ztreeBean.pId = StringUtils.toString(ReflectUtils.getFieldVal(obj, pIdField));
		}
		ztreeBean.name = StringUtils.toString(ReflectUtils.getFieldVal(obj, nameField));
		return ztreeBean;
	}
	
	public static ZtreeBean getInstance(ZtreeTransable transable) {
		if (null == transable) {
			return null;
		}
		return new ZtreeBean(transable.zid(), transable.zPid(), transable.zName());
	}
	
	public static List<ZtreeBean> getInstance(List<ZtreeTransable> transables) {
		if (null == transables) {
			return null;
		}
		List<ZtreeBean> ztreeBeans = new LinkedList<>();
		ZtreeBean ztreeBean;
		for (ZtreeTransable ztreeTransable : transables) {
			ztreeBean = getInstance(ztreeTransable);
			if (null == ztreeBean) {
				continue;
			}
			if (ztreeBeans.contains(ztreeBean)) {
				continue;
			}
			ztreeBeans.add(ztreeBean);
		}
		return ztreeBeans;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getpId() {
		return pId;
	}
	public void setpId(String pId) {
		this.pId = pId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ZtreeBean other = (ZtreeBean) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return StringUtils.beanString(this);
	}
	
}
