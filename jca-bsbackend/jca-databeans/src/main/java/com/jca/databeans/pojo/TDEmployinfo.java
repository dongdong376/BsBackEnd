package com.jca.databeans.pojo;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import com.jca.datacommon.annotation.NoColumn;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description: 
 * @author:
 * @date: 
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TDEmployinfo implements Serializable {

        //
        private Long employId;
        //微信自增主键
        private Long wxEmployId;
        //
        private String employNo;
        //
        private String employName;
        //
        private Long cardNo;
        //
        private Integer groupId;
        //
        private Integer isAttend;
        //
        private Integer isDoor;
        //
        private String beginDateTime;
        //
        private String endDateTime;
        //
        private String inPassWord;
        //
        private String departmentName;
        //
        private String mailbox;
        //
        private String mobilePhone;
        //
        private String weiXinNo;
        //
        private String postName;
        //
        private Integer gender;
        //0未上传  1已上传
        private Integer isDown;
        //0未上传  1已上传
        private Integer isWxDown;
        //身份证|二维码
        private String iDCardNo;
        //有效刷卡次数
        private Integer playCardNumber;
        //已刷卡次数
        private Integer playCardSum;
        //`Birthday`,`Address`,`Police`,`Nation`
        private Integer updataCloud;
        //
        private String birthday;
        //
        private String police;
        //
        private String nation;
        //
        private String address;
        //
        private Integer updateState;
        //
        private String updateDateTime;
        //是否防潜回（0：否；1：是）
        private Integer isSubmarineBack;
        //人员在场的状态（0：在场；1：出场）
        private Integer onSiteState;
        //时段号
        private Integer timeNumber;
        //用户时段记录(格式11,25,2000,300,10,30,3,22。 月,日,月总次数,日总次数,时区1次数,时区2次数,时区3次数,时区4次数)
        private String cardTimeRecord;        
        //密钥
        private String secret_key;

}

