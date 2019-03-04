package jacob.naphtali.user.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import jacob.naphtali.base.bean.NameEntity;

@Entity
@Table(name = "nap_role")
public class Role extends NameEntity {

	private String value;
	@Column(length = 127)
	private String description;
	
	public Role() {
		super();
	}
	public Role(String name, String value, String description) {
		super();
		this.name = name;
		this.value = value;
		this.description = description;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

}
