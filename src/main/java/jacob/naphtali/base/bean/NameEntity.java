package jacob.naphtali.base.bean;

import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class NameEntity extends BaseEntity {

	protected String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
