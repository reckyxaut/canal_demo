package com.goldsign.canal.app.jxky.dao;

import com.goldsign.canal.annotation.MyBatisDao;
import com.goldsign.canal.app.jxky.entity.Student;
import com.goldsign.canal.dao.CrudDao;

@MyBatisDao
public interface StudentDao extends CrudDao<Student> {

}
