package jacob.naphtali.base.repository.impl;

import javax.persistence.EntityManager;

import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import jacob.naphtali.base.bean.BaseEntity;
import jacob.naphtali.base.repository.BaseRepository;

public class BaseRepositoryImpl<T extends BaseEntity> extends SimpleJpaRepository<T, String> implements BaseRepository<T> {

    @SuppressWarnings("unused")
	private final EntityManager entityManager;
    
	public BaseRepositoryImpl(Class<T> domainClass, EntityManager em) {
		super(domainClass, em);
		entityManager = em;
	}

	public BaseRepositoryImpl(JpaEntityInformation<T, String> entityInformation, EntityManager entityManager) {
		super(entityInformation, entityManager);
		this.entityManager = entityManager;
	}
}
