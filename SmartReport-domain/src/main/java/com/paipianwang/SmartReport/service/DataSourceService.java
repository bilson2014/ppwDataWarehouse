package com.paipianwang.SmartReport.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.paipianwang.SmartReport.dao.DataSourceDao;
import com.paipianwang.SmartReport.data.jdbc.BaseService;
import com.paipianwang.SmartReport.domain.po.DataSourcePo;

@Service
public class DataSourceService extends BaseService<DataSourceDao, DataSourcePo>{

	@Autowired
	public DataSourceService(DataSourceDao dao) {
		super(dao);
	}

}
