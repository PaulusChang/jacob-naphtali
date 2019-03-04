package jacob.naphtali.user.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jacob.naphtali.base.repository.BaseRepository;
import jacob.naphtali.base.service.impl.BaseServiceImpl;
import jacob.naphtali.user.entity.Role;
import jacob.naphtali.user.repository.RoleRepository;
import jacob.naphtali.user.service.RoleService;

@Service
public class RoleServiceImpl extends BaseServiceImpl<Role> implements RoleService {

	@Autowired
	private RoleRepository repository;

	@Override
	public BaseRepository<Role> baseRepository() {
		return repository;
	}
}
