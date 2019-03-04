package ${superPackage}.repository;

import org.springframework.stereotype.Repository;

import ${basePackage}.repository.BaseRepository;
import ${superPackage}.entity.${ClassName};

@Repository
public interface ${ClassName}Repository extends BaseRepository<${ClassName}> {

}
