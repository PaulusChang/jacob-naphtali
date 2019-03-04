package jacob.naphtali.base.bean;

import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.NullHandling;
import org.springframework.data.domain.Sort.Order;

import jacob.naphtali.base.bean.util.StringUtils;

/**
 * 排序用
 * @author ChangJian
 * @date 2019-01-17
 */
public class OrderBean {
	
	private Direction direction;
	private String property;
	private Boolean ignoreCase;
	private NullHandling nullHandling;
	
	public OrderBean() {
		super();
	}

	public OrderBean(Direction direction, String property) {
		super();
		this.direction = direction;
		this.property = property;
	}

	public Order toOrder() {
		if (StringUtils.isEmpty(property)) {
			return null;
		}
		if (null == direction) {
			direction = Direction.ASC;
		}
		if (null == ignoreCase) {
			ignoreCase = false;
		}
		if (null == nullHandling) {
			nullHandling = NullHandling.NATIVE;
		}
		Order order = new Order(direction, property, nullHandling);
		if (ignoreCase) {
			order = order.ignoreCase();
		}
		return order;
	}

	public Direction getDirection() {
		return direction;
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
	}

	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}

	public Boolean getIgnoreCase() {
		return ignoreCase;
	}

	public void setIgnoreCase(Boolean ignoreCase) {
		this.ignoreCase = ignoreCase;
	}

	public NullHandling getNullHandling() {
		return nullHandling;
	}

	public void setNullHandling(NullHandling nullHandling) {
		this.nullHandling = nullHandling;
	}

}
