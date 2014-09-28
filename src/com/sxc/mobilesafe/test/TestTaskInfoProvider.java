package com.sxc.mobilesafe.test;

import java.util.List;

import com.sxc.mobilesafe.domain.TaskInfo;
import com.sxc.mobilesafe.engine.TaskInfoProvider;

import android.test.AndroidTestCase;

public class TestTaskInfoProvider extends AndroidTestCase {

	public void testGetTaskInfos()throws Exception{
		List<TaskInfo> infos=TaskInfoProvider.getTsakInfos(getContext());
		for(TaskInfo info:infos){
			System.out.println(info.toString());
		}
	}
}
