package com.goldsign.canal.app.jxky.processor;

import java.util.Date;

import org.springframework.stereotype.Component;

import com.goldsign.canal.annotation.CanalProcessor;
import com.goldsign.canal.app.jxky.dao.StudentDao;
import com.goldsign.canal.app.jxky.entity.Student;
import com.goldsign.canal.processor.BaseProcessor;
import com.goldsign.canal.utils.Global;

@Component
@CanalProcessor(schema="canal_test",table="student")
public class StudentProcessor  extends BaseProcessor<Student,StudentDao> {

	@Override
	public Student convertObjLocal(Student entity) throws Exception {
		Global.LOGGER.debug("convert data before:{}",entity.toString());
		entity.setTmp1("tmp1");
		entity.setTmp2("test" + new Date().getTime());
		Global.LOGGER.debug("convert data after:{}",entity.toString());
		return entity;
	}
}
