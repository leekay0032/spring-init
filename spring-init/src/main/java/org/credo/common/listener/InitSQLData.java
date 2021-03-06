package org.credo.common.listener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.shiro.crypto.hash.Sha512Hash;
import org.credo.model.Person;
import org.credo.repository.PersonRepository;
import org.credo.repository.UserRepository;
import org.credo.security.model.User;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class InitSQLData implements ServletContextListener
{
	@Override
	public void contextInitialized(ServletContextEvent sce)
	{
		WebApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(sce.getServletContext());
		PersonRepository personRepository = webApplicationContext.getBean(PersonRepository.class);
		personRepository.deleteAll();

		List<Person> personList = new ArrayList<>();
		for (int i = 0; i < 100; i++)
		{
			String chars = "abcdefghijklmnopqrstuvwxyz";
			String name = String.valueOf(chars.charAt((int) (Math.random() * 26)));
			Person person = new Person();
			person.setName(name);
			person.setAge((i % 100) + 1);
			personList.add(person);
		}
		personRepository.save(personList);
		
		UserRepository userRepository = webApplicationContext.getBean(UserRepository.class);
		userRepository.deleteAll();
		
		User user=new User();
		user.setName("zhaoqianjava@foxmail.com");
		byte[] passwordSalt=UUID.randomUUID().toString().getBytes();
		user.setPasswordSalt(passwordSalt);
		user.setPassword("123");
		String passwordHash = new Sha512Hash(user.getPassword(), user.getName()+new String(passwordSalt),99).toString();
		user.setPasswordHash(passwordHash);
		userRepository.save(user);
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce)
	{
	}

}
