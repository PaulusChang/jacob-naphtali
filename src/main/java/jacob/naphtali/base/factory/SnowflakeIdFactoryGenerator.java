package jacob.naphtali.base.factory;

import java.io.Serializable;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import io.shardingsphere.core.keygen.DefaultKeyGenerator;

public class SnowflakeIdFactoryGenerator implements IdentifierGenerator {
	
	private static final DefaultKeyGenerator generator;
	
	static {
		generator = new DefaultKeyGenerator();
	}

	@Override
	public Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException {
		return generator.generateKey();
	}
}
