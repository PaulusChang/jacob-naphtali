package jacob.naphtali.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import jacob.naphtali.SpringbootApplication;
import jacob.naphtali.user.entity.Order;
import jacob.naphtali.user.entity.Role;
import jacob.naphtali.user.repository.OrderRepository;
import jacob.naphtali.user.service.RoleService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SpringbootApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GenerateDataTest {
	
	@Autowired
	private RoleService roleService;
	
	@Autowired
	private OrderRepository orderRepository;

	/**
	 * 执行此方法，生成测试数据 generateUserRole() 要最后调用
	 * @author ChangJian
	 * @date 2019年2月25日
	 */
	@Test
	public void generateTestData() {
		generateRole();
	}
	

	/**
	 * 生成测试的role数据
	 * @author ChangJian
	 * @date 2019年2月25日
	 */
	@Test
	public void generateRole() {
		Role role;
		String name;
		String value;
		String description;
		for (int i = 0; i < 5; i++) {
			name = "name" + i;
			value = "value" + i;
			description = "description" + i;
			role = new Role(name, value, description);
			roleService.save(role);
			System.out.println(role);
		}
	}
	

	/**
	 * 生成测试的role数据
	 * @author ChangJian
	 * @date 2019年2月25日
	 */
	@Test
	public void generateOrder() {
		Order order = new Order();
		order.setOrderId(1L);
		order.setUserId(1L);
		orderRepository.save(order);
	}
	
	public static void main(String[] args) {
	}
	

}
