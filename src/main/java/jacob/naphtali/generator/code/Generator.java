package jacob.naphtali.generator.code;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jacob.naphtali.base.bean.util.FileUtils;
import jacob.naphtali.base.bean.util.XmlUtils;
import jacob.naphtali.generator.code.CategoryList.Category;
import jacob.naphtali.user.entity.Role;

public class Generator {
	
	public static final String BASE_PACKAGE = "jacob.naphtali.base";
	public static final String SRC_MAIN_JAVA = "src/main/java/";
	
	public static boolean generate(Category category, Class<?> cls) {
		System.out.println("Generator: " + cls.getSimpleName() + category.getName() + "-start");
		File templateFile = new File("src/main/resources/generator/Template" + category.getName() + ".java");
		String content = FileUtils.getString(templateFile);
		Map<String, String> dataMap = getDataMap(cls);
		for (String key : dataMap.keySet()) {
			content = content.replace("${" + key + "}", dataMap.get(key));
		}
		File file = new File(SRC_MAIN_JAVA + dataMap.get("superPackage").replace('.', '/') + "/" + category.getPath() + "/" + dataMap.get("ClassName") + category.getName() + ".java");
		if (file.exists()) {
			System.out.println("Generator: " + cls.getSimpleName() + category.getName() + "-file exists");
			return false;
		}
		FileUtils.newFile(content, SRC_MAIN_JAVA + dataMap.get("superPackage").replace('.', '/') + "/" + category.getPath() + "/" + dataMap.get("ClassName") + category.getName() + ".java");
		System.out.println("Generator: " + cls.getSimpleName() + category.getName() + "-finish");
		return true;
	}
	
	public static void generate(CategoryList categoryList, Class<?> cls) {
		List<Category> categories = categoryList.getCategories();
		for (Category category : categories) {
			generate(category, cls);
		}
	}
	
	public static void generate(Category category, Package entityPackage) throws ClassNotFoundException {
		File packageFile = new File(SRC_MAIN_JAVA + entityPackage.getName().replace('.', '/'));
		for (String fileName : packageFile.list()) {
			Class<?> cls = Class.forName(entityPackage.getName() + "." + fileName.substring(0, (fileName.length() - ".java".length())));
			generate(category, cls);
		}
	}
	
	public static void generate(CategoryList categoryList, Package entityPackage) throws ClassNotFoundException {
		File packageFile = new File(SRC_MAIN_JAVA + entityPackage.getName().replace('.', '/'));
		List<Category> categories = categoryList.getCategories();
		for (String fileName : packageFile.list()) {
			Class<?> cls = Class.forName(entityPackage.getName() + "." + fileName.substring(0, (fileName.length() - ".java".length())));
			for (Category category : categories) {
				generate(category, cls);
			}
		}
	}
	
	private static Map<String, String> getDataMap(Class<?> cls) {
		Map<String, String> map = new HashMap<>();
		map.put("basePackage", BASE_PACKAGE);
		String[] folders = cls.getPackage().getName().split("\\.");
		String superPackage = "";
		for (int i = 0; i < folders.length - 1; i++) {
			superPackage += folders[i] + ".";
		}
		superPackage = superPackage.substring(0, superPackage.length() - 1);
		map.put("superPackage", superPackage);
		map.put("ClassName", cls.getSimpleName());
		return map;
	}
	
	public static void main(String[] args) throws ClassNotFoundException {
		String xml = FileUtils.getString("src/main/resources/generator/conf/template-category.xml");
		CategoryList categoryList = XmlUtils.xmlToBean(CategoryList.class, xml);
		categoryList.init();
		generate(categoryList, Role.class);
	}
}
