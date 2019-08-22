package com.jca.dataquery.service.inoutrecord;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.jca.databeans.pojo.TFArea;
import com.jca.databeans.pojo.TFEmployInfo;
import com.jca.databeans.pojo.TFEventRecord;
import com.jca.databeans.pojo.TempEntity;
import com.jca.datacommon.base.BaseServiceImpl;
import com.jca.datacommon.log.MethodLog;
import com.jca.datacommon.web.form.PageForm;
import com.jca.datadao.TFAreaMapper;
import com.jca.datadao.TFEmployInfoMapper;
import com.jca.datadao.TFEventRecordMapper;
import com.jca.datatool.DateUtil;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TFEventRecordServiceImpl extends BaseServiceImpl<TFEventRecordMapper, TFEventRecord>
		implements TFEventRecordService {

	@Resource
	private TFEventRecordMapper tFEventRecordMapper;
	@Resource
	private TFEmployInfoMapper  tFEmployInfoMapper;
	@Resource
	private TFAreaMapper tFAreaMapper;

	@Override
	public TFEventRecord save(TFEventRecord e) {
		// TODO Auto-generated method stub
		return null;
	}
	@MethodLog(value="修改记录信息")
	@Override
	public TFEventRecord updateById(TFEventRecord e) {
		Integer result=tFEventRecordMapper.updateByPrimaryKeySelective(e);
		if(result>0)
			return tFEventRecordMapper.selectOneByCriteria(e);
		return null;
	}

	@Override
	public Boolean deleteById(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean deleteByCondition(TFEventRecord e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long countByCondition(TFEventRecord e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TFEventRecord> listAll(String orderBy) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TFEventRecord> listAll() {
		// TODO Auto-generated method stub
		return tFEventRecordMapper.selectRecordDetail();
	}

	@Override
	public List<TFEventRecord> listByCondition(TFEventRecord e) {
		return tFEventRecordMapper.updateRecord(e.getPropertyName());
	}

	@Override
	public Page<TFEventRecord> listByCondition(TFEventRecord e, PageForm pageForm) {
		return null;
	}

	@Override
	public TFEventRecord getById(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TFEventRecord getByCondition(TFEventRecord e) {
		// TODO Auto-generated method stub
		return null;
	}

	@MethodLog(value = "查询记录")
	@Override
	public List<TFEventRecord> findAllOrOneRecord(TFEventRecord e, Integer currentPage) {
		PageHelper.startPage(currentPage, 10);
		List<TFEventRecord> records = tFEventRecordMapper.selectAllRecord(e);
		for (Iterator iterator = records.iterator(); iterator.hasNext();) {
			TFEventRecord tfEventRecord = (TFEventRecord) iterator.next();
			tfEventRecord.setJpgPath(tfEventRecord.getJpgPath().replace("\\\\", "\\"));
		}
		return records;
	}

	@Data
	class TempRecord {
		private String areaName;
		private Integer inSum;
		private Integer outSum;
	}

	@MethodLog(value = "统计记录")
	@Override
	public Map<String, Object> countRecordInfo(String type, String name) {
		List<TFArea> areas = tFAreaMapper.countRecordInfo(type, name);
		String area[] = new String[areas.size()];
		Integer in[] = new Integer[areas.size()];
		Integer out[] = new Integer[areas.size()];
		int i = 0;
		for (TFArea tfArea : areas) {
			area[i] = tfArea.getAreaName();
			in[i] = tfArea.getInSum();
			out[i] = tfArea.getOutSum();
			i++;
		}
		Map<String, Object> map = new HashMap<>();
		map.put("in", in);
		map.put("out", out);
		map.put("area", area);
		return map;
	}

	@MethodLog("当天情况")
	@Override
	public TempEntity todayOryesterdayFlux(String name) {
		TempEntity entity = tFEventRecordMapper.StrangenessOrResidentSumFlux(name);
		return entity;
	}

	@MethodLog("统计情况")
	@Override
	public List<TempEntity> findAllSituation(String name) {
		return tFEventRecordMapper.selectSumFlux(name);
	}

	@Override
	public List<TFEmployInfo> selectCurrentTime(String type,String name) {
		return tFEmployInfoMapper.selectisPresentRecord(Integer.valueOf(type),name);
	}

	//@Scheduled(cron = "0/5 * * * * ? ")
	@Override
	public void TimerInsert() {
		Random random = new Random();
		String[] prono = new String[] { "龙华", "龙胜", "清湖" };
		Integer[] inOrOut = new Integer[] { 1, 2 };			
		try {
			String date=DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss");
			int i=1;
			int max=(random.nextInt(10)+1);
			do{
				Integer result = tFEventRecordMapper
						.insert(TFEventRecord.builder().employNo("5691").employName("test").secretKey("test1")
								.jpgPath("2576\\20190803\\20190803163430_546.jpg").propertyName(prono[random.nextInt(3)])
								.recordDateTime(date)
								.faceInOut(inOrOut[random.nextInt(2)]).build());
				log.info(result > 0 ? "插入成功!" : "插入失败!");
				System.out.println("插入"+i+"条完成");
				i++;
			}while(i<=max);
			System.out.println("总插入"+(i-1)+"条");
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	//@Scheduled(cron = "0/12 * * * * ? ")
	@Override
	public void TimerSelect() {
		List<TFEventRecord> list= tFEventRecordMapper.selectCurrentTime();
		System.out.println("30秒之内插入了==>");
		for (TFEventRecord r : list) {
			System.out.println(r.getRecordDateTime());
		}
	}
	
		//@Scheduled(cron = "0/5 * * * * ? ")
		@Override
		public void TimerUpdate() {				
			try {
				tFEmployInfoMapper.updateTFEmployInfoInfo(TFEmployInfo.builder().createBy("1").updateBy("1").updateTime(DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss")).build());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

}
