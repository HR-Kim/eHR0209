package com.ehr;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ehr.service.UserService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/test-applicationContext.xml")
public class UserServiceTest {
	private Logger LOG = Logger.getLogger(this.getClass());
	
	@Autowired //location에서 파일에서 객체를 찾아 자동으로 매칭시켜줌
	UserService userService;
	
	@Autowired //location에서 파일에서 객체를 찾아 자동으로 매칭시켜줌
	UserDao userDao;
	
	List<User> users;
	
	
	//대상 데이터 생성
	@Before
	public void setUp() {
		users = Arrays.asList(
				new User("j01_126","강슬기01","1234",Level.BASIC,49,0,"glwlzkwp@naver.com","2019/08/23")
				,new User("j02_126","강슬기02","1234",Level.BASIC,50,0,"glwlzkwp@naver.com","2019/08/23") //BASIC -> SILVER
				,new User("j03_126","강슬기03","1234",Level.SILVER,50,29,"glwlzkwp@naver.com","2019/08/23")
				,new User("j04_126","강슬기04","1234",Level.SILVER,50,30,"glwlzkwp@naver.com","2019/08/23") //SILVER -> GOLD
				,new User("j05_126","강슬기05","1234",Level.GOLD,99,99,"glwlzkwp@naver.com","2019/08/23")
				);
	}
	
	
	//최초 사용자 등록 처리 : Level == null 이면 BASIC처리
	@Test
	@Ignore
	public void add() {
		//1. 전체삭제
		//2. Level 있는 사람
		//3. Level == null
		//4. 등업

		//1. 전체삭제
		userDao.deleteAll();
		
		//2. Level 있는 사람
		User userExistLevel = users.get(4);  //셋팅해준 값 활용
		
		//3. Level == null
		User userNoLevel = users.get(0); //셋팅해준 값 활용
		userNoLevel.sethLevel(null); //널로 셋팅해줌
		
		userService.add(userExistLevel); //서비스에 add 메소드 따로 만들어줌 (Level 검사하는)
		//Level == null -> Level.BASIC
		userService.add(userNoLevel); //서비스에 add 메소드 따로 만들어줌 (Level 검사하는)
		
		//4. 등록 데이턱 get
		User userExistLevelRead = userDao.get(userExistLevel.getU_id()); //GOLD 예상
		User userNoLevelRead = userDao.get(userNoLevel.getU_id()); //BASIC 예상
		
		//5. 확인
		assertThat(userExistLevelRead.gethLevel(), is(userExistLevel.gethLevel()));
		assertThat(userNoLevelRead.gethLevel(), is(Level.BASIC));
		
		
//		내가 짠 로직, 어느 부분에서 막혔는지 봐봐		
//		//최초 사용자등록
//		//베이직으로 업그레이드
//		//업그레이드 확인
//		
//		
//		//최초 사용자등록
//		User user = new User();
//		userDao.add(user);
//		
//		
//		//베이직으로 셋팅
//		
//		user.setName("강슬기U");
//		user.setPasswd("1234U");
//		user.sethLevel(Level.GOLD);
//		user.setLogin(99);
//		user.setRecommend(999);
//		user.setEmail("Uzz@hanmail.net");
//		
//		
//		user.setU_id("");
//		user.sethLevel(Level.BASIC);
//		this.userService.upgradeLevels();
//		
//		
//		//업그레이드 확인
//		checkUser(user.gethLevel(),Level.BASIC);
		
	}
	
	
	
	@Test
	//@Ignore
	public void upgreadeLevel() {
		//1.전체삭제
		//2.users데이터 등록
		//3.upgradeLevels 호출
		//4. 02,04 업그레이드 대상
		
		//1.
		userDao.deleteAll();
		
		//2.
		for(User user : users) {
			userDao.add(user);
		}
		
		//3.
		this.userService.upgradeLevels();
		
		
		//4.
		checkUser(users.get(0),Level.BASIC);
		checkUser(users.get(1),Level.SILVER); //업그레이드 될 것 예상
		checkUser(users.get(2),Level.SILVER);
		checkUser(users.get(3),Level.GOLD); //업그레이드 될 것 예상
		checkUser(users.get(4),Level.GOLD);
		
		
	}
	
	
	private void checkUser(User user, Level expectedLevel) {
		
		User userUpdate = userDao.get(user.getU_id());
		assertThat(userUpdate.gethLevel(), is(expectedLevel));
		
	}
	
	
	
	@Test
	public void serviceBean() {
		assertThat(this.userService, is(notNullValue())); //검증
		assertThat(this.userDao, is(notNullValue())); //검증
		LOG.info("----------------------------");
		LOG.info("-userService-"+userService);
		LOG.info("-userDao-"+userDao);
		LOG.info("----------------------------");
	}
	
	
	
	
	
}
