package jacob.naphtali.generator.code;

import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import jacob.naphtali.base.bean.util.StringUtils;

@XmlRootElement(name = "categories")
public class CategoryList {
	
	private List<Category> categories;
	
	public void init() {
		if (null == categories) {
			return;
		}
		for (Category category : categories) {
			category.init();
		}
	}
	
	@XmlElement(name = "category")
	public List<Category> getCategories() {
		return categories;
	}

	public void setCategories(List<Category> categories) {
		this.categories = categories;
	}

	public static class Category {

		private String name;
		private String path;
		private String template;
		
		public Category() {
			super();
		}
		public Category(String name, String path, String template) {
			super();
			this.name = name;
			this.path = path;
			this.template = template;
		}
		
		public void init() {
			if (StringUtils.isEmpty(name)) {
				return;
			}
			if (StringUtils.isEmpty(path)) {
				StringBuilder sb = new StringBuilder();
				for (int i = 0; i < name.length(); i++) {
					if (StringUtils.isUpper(name.charAt(i))) {
						sb.append('/');
					}
					sb.append(name.charAt(i));
				}
				path = sb.toString();
				if (path.startsWith("/")) {
					path = path.substring(1);
				}
				path = path.toLowerCase();
			}
			if (StringUtils.isEmpty(template)) {
				template = "Template" + name;
			}
		}
		
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getPath() {
			return path;
		}
		public void setPath(String path) {
			this.path = path;
		}
		public String getTemplate() {
			return template;
		}
		public void setTemplate(String template) {
			this.template = template;
		}
		@Override
		public String toString() {
			return "Category [name=" + name + ", path=" + path + ", template=" + template + "]";
		}
		
	}
	
	public static CategoryList testXml() {
		Category category = new Category();
		category.name  = "name";
		CategoryList categoryList = new CategoryList();
		List<Category> categories = new LinkedList<>();
		categories.add(category);
		categories.add(new Category("name", "path", "template"));
		categoryList.setCategories(categories);
		return categoryList;
	}

}
