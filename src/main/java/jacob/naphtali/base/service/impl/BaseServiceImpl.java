package jacob.naphtali.base.service.impl;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.LinkedList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import jacob.naphtali.base.bean.BaseEntity;
import jacob.naphtali.base.bean.BaseSpecification;
import jacob.naphtali.base.bean.OrderBean;
import jacob.naphtali.base.bean.PageRequestBean;
import jacob.naphtali.base.bean.util.ReflectUtils;
import jacob.naphtali.base.bean.util.StringUtils;
import jacob.naphtali.base.repository.BaseRepository;
import jacob.naphtali.base.service.BaseService;

@Service
public abstract class BaseServiceImpl<T extends BaseEntity> implements BaseService<T> {

	public abstract BaseRepository<T> baseRepository();

	protected Specification<T> specification(T t) {
		return new BaseSpecification<T>(t);
	}
	
	protected Sort getSort(T t) {
		List<Field> fields = ReflectUtils.listFieldsByType(t.getClass(), OrderBean.class);
		List<Order> orders = new LinkedList<>();
		for (Field field : fields) {
			OrderBean orderBean = (OrderBean) ReflectUtils.getFieldVal(t, field);
			if (null == orderBean) {
				continue;
			}
			orders.add(orderBean.toOrder());
		}
		if (orders.isEmpty()) {
			return Sort.unsorted();
		}
		return Sort.by(orders);
	}

	@Override
	public void save(T t) {
		t.init();
		baseRepository().save(t);
	}

	@Override
	public T getOne(String id) {
		if (StringUtils.isEmpty(id)) {
			return null;
		}
		return baseRepository().getOne(id);
	}

	@Override
	public List<T> findAll() {
		return findAll(null);
	}

	@Override
	public List<T> findAll(T t) {
		if (null == t) {
			t = newTinstance();
		}
		return baseRepository().findAll(specification(t), getSort(t));
	}

	@Override
	public Page<T> findAll(T t, PageRequestBean pageRequestBean) {
		if (null == t) {
			t = newTinstance();
		}
		return baseRepository().findAll(specification(t), pageRequestBean.toPageRequest(getSort(t)));
	}
	
	@SuppressWarnings("unchecked")
	private Class<T> getTclass() {
		return (Class<T>)((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[0];
	}
	
	private T newTinstance() {
		return (T) ReflectUtils.newInstance(getTclass());
	}

}
