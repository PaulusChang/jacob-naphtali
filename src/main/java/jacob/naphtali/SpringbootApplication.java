package jacob.naphtali;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import jacob.naphtali.base.factory.BaseRepositoryFactoryBean;

@EnableJpaRepositories(basePackages = { "jacob.naphtali" }, repositoryFactoryBeanClass = BaseRepositoryFactoryBean.class// 指定自己的工厂类
)
@SpringBootApplication
public class SpringbootApplication {
	public static void main(String[] args) {
		SpringApplication.run(SpringbootApplication.class, args);
	}

}
