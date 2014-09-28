package com.sxc.mobilesafe.test;

import java.util.List;
import java.util.Random;

import com.sxc.mobilesafe.db.BlackNumberDBOpenHelper;
import com.sxc.mobilesafe.db.dao.BlackNumberDao;
import com.sxc.mobilesafe.domain.BlackNumberInfo;

import android.test.AndroidTestCase;

public class TestBlackNumberDB extends AndroidTestCase {

	public void testCreatDB()throws Exception{
		BlackNumberDBOpenHelper helper=new BlackNumberDBOpenHelper(getContext());
		helper.getWritableDatabase();
	}
	public void testAdd() throws Exception{
		BlackNumberDao dao=new BlackNumberDao(getContext());
		long basenumber=13673600001L;
		Random rand=new Random();
		for(int i=0;i<100;i++){
		dao.add(String.valueOf(basenumber+i),String.valueOf(rand.nextInt(3)+1));
		}
	}
	public void testFindAll(){
		BlackNumberDao dao=new BlackNumberDao(getContext());
		List<BlackNumberInfo> infos= dao.findAll();
		for(BlackNumberInfo info:infos){
			System.out.println(info.toString());
		}
	}
	public void testUpdate() throws Exception{
		BlackNumberDao dao=new BlackNumberDao(getContext());
		dao.update("110","2");
	}
	public void testdelete() throws Exception{
		BlackNumberDao dao=new BlackNumberDao(getContext());
		dao.delete("110");
	}
	public void testFind() throws Exception{
		BlackNumberDao dao=new BlackNumberDao(getContext());
		boolean result=dao.find("110");
		assertEquals(true, result);
	}
}
