package com.jca.datadao;

import com.jca.databeans.pojo.TDEmployinfo;
import com.jca.datacommon.base.BaseSqlProvider;

public interface TDEmployinfoMapper extends BaseSqlProvider<TDEmployinfo> {
	TDEmployinfo selectOneTdEmp(TDEmployinfo employinfo);
}
