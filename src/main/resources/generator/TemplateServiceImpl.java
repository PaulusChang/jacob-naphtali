package ${superPackage}.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ${basePackage}.repository.BaseRepository;
import ${basePackage}.service.impl.BaseServiceImpl;
import ${superPackage}.entity.${ClassName};
import ${superPackage}.repository.${ClassName}Repository;
import ${superPackage}.service.${ClassName}Service;

@Service
public class ${ClassName}ServiceImpl extends BaseServiceImpl<${ClassName}> implements ${ClassName}Service {

	@Autowired
	private ${ClassName}Repository repository;

	@Override
	public BaseRepository<${ClassName}> baseRepository() {
		return repository;
	}
}
