package com.ehr.service;

import java.util.List;

import org.apache.log4j.Logger;

import com.ehr.Level;
import com.ehr.User;
import com.ehr.UserDao;

public class UserService {
	private Logger LOG = Logger.getLogger(this.getClass());
	
	private UserDao userDao; //인터페이스 통해 만들어야함

	public void setUserDao(UserDao userDao) { //주입을 위한  setter
		this.userDao = userDao;
	}
	
	
	//최초 사용자 베이직 레벨
	public void add(User user) {
		if(null == user.gethLevel()){
			user.sethLevel(Level.BASIC);
		}
		userDao.add(user);
	}
	
	
	
//	//level upgrade
//	//1. 전체 사용자를 조회
//	//2. 대상자를 선별
//	// 2.1. BASIC사용자, 로그인cnt가 50이상이면 : BASIC -> SILVER
//	// 2.2. SILVER사용자, 추천cnt가 300이상이면 : SILVER -> GOLD
//	//3. 대상자 업그레이드
//	public void upgradeLevels() {
//		int upCnt = 0;
//		
//		
//		//1.전체 사용자를 조회
//		List<User> users = userDao.getAll();
//		for(User user : users) {
//			Boolean changed = null;
//			
//			// BASIC -> SILVER
//			if(user.gethLevel() == Level.BASIC && user.getLogin() >= 50) {
//				user.sethLevel(Level.SILVER);
//				changed = true;
//				
//			//	SILVER -> GOLD
//			}else if(user.gethLevel() == Level.SILVER && user.getRecommend() >= 30) {
//				user.sethLevel(Level.GOLD);
//				changed = true;
//			}else if(user.gethLevel() == Level.GOLD) {
//				changed = false;
//			}else {
//				changed = false;
//			}
//			
//			
//			if(changed == true) {
//				userDao.update(user);
//				upCnt++; //업뎃 됐는지 확인 (전역변수 확인해)
//			}
//			
//		}//--for
//		
//		LOG.debug("upCnt:"+upCnt);
//		
//	}
	
	
	private void upgradeLevel(User user) {
//		if(user.gethLevel() == Level.BASIC) {
//			user.sethLevel(Level.SILVER);
//		}else if(user.gethLevel() == Level.SILVER) {
//			user.sethLevel(Level.GOLD);
//		}
		user.upgradeLevel(); //VO부분에 기능을 만듦
		userDao.update(user);
		
	}
	
	
	
	
	/** 기능 뜯어내기 */
	//level upgrade
	//1. 전체 사용자를 조회
	//2. 대상자를 선별
	// 2.1. BASIC사용자, 로그인cnt가 50이상이면 : BASIC -> SILVER
	// 2.2. SILVER사용자, 추천cnt가 300이상이면 : SILVER -> GOLD
	//3. 대상자 업그레이드
	public void upgradeLevels() {
		int upCnt = 0;
		
		List<User> users =  userDao.getAll();
		
		for(User user : users) {
			if(canUpgradeLevel(user)==true) {
				upgradeLevel(user);
				upCnt++;
			}
		}//--for
		
		LOG.debug("=========================");
		LOG.debug("=upCnt="+upCnt);
		LOG.debug("=========================");
		
	}
	
	
	
	//업그레이드 대상여부 파악 : true
	private boolean canUpgradeLevel(User user) {
		Level currLevel = user.gethLevel();
		
		switch(currLevel) {
			case BASIC : return (user.getLogin() >= 50);
			case SILVER : return (user.getRecommend() >= 30);
			case GOLD : return false;
			default : throw new IllegalArgumentException("Unknown Level:"+currLevel);
		}
		
	}
	
	
	
	
}
