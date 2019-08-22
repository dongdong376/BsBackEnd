<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.jca.datadao.${myClass.className}Mapper">
	<!-- 查询所有设备区域信息 -->
	<select id="findAll${myClass.className}" resultMap="${myClass.className}Map" parameterType="map">
		SELECT
		area_id AS areaId,
		area_no AS areaNo,
		area_name AS areaName,
		area_parent_no AS areaParentNo,
		province AS province,
		city AS city,
		(SELECT property_name FROM t_f_property fp WHERE fp.secret_key=fa.`secret_key`) AS propertyNo,
		attributes AS attributes,
		address AS address,
		secret_key AS secretKey,
		is_down AS isDown,
		create_by AS createBy,
		create_time AS createTime,
		update_by AS updateBy,
		update_time AS updateTime
		FROM
		${myClass.tableName} fa WHERE 1=1
		<if test="param.key !=null and param.key !=''">
			and fa.secret_key=${r"#"}{param.key}
		</if>
		<if test="param.areaId !=null and param.areaId !=0">
			and fa.secret_key=${r"#"}{param.areaId}
		</if>
		<if test="param.ids !=null and param.ids !=''">
			and fa.area_id in
			<foreach collection="param.ids" item="id" open="(" close=")"
				separator=",">
				${r"#"}{id}
			</foreach>
		</if>
	</select>
	<!-- 删除 -->
	<!-- <delete id="removeAreaByArray" parameterType="list"> delete from t_f_area 
		where area_no in <foreach collection="ids" item="id" open="(" separator="," 
		close=")"> ${r"#"}{no} </foreach> </delete> -->
	<resultMap type="TFArea" id="TFAreaMap" autoMapping="true">
		<collection property="devices" autoMapping="true"
			fetchType="lazy" column="areaNo" ofType="TFDevice" select="findDevice">
			<id property="faceId" column="face_id" />
		</collection>
	</resultMap>
	<select id="findDevice" resultType="TFDevice">
		SELECT
		face_id AS
		faceId,face_no AS faceNo,face_name AS faceName,
		area_no AS
		areaNo,pc_no
		AS pcNo,secret_key AS secretKey,face_ip AS faceIp,
		face_mac AS
		faceMac,face_netmark AS faceNetmark,
		face_in_out AS
		faceInOut,is_online
		AS isOnline,state,face_firm AS faceFirm,
		face_platform AS
		facePlatform,face_system AS faceSystem,face_version AS
		faceVersion,
		remarks AS remarks,is_down AS isDown,create_by AS
		createBy,
		create_time
		AS createTime,update_by AS updateBy,update_time
		AS
		updateTime,property_no AS propertyNo
		FROM
		t_f_device fd
		where
		area_no=${r"#"}{areaNo}
	</select>
	<!-- <![CDATA[ and check_date>=${r"#"}{beginTime}]]> -->
	<update id="update${myClass.className}Info" parameterType="${myClass.className}">
		update ${myClass.tableName}
		<set>	
			<trim suffix="," suffixOverrides=",">
				<#list myClass.fieldList as field>    
			     	<if test="${myClass.className}.${field.fieldName}!=null and ${myClass.className}.${field.fieldName}!=''">
						${field.dataBaseFieldName}=${r"#"}{${myClass.className}.${field.fieldName}},
					</if>
	    		</#list> 
    		</trim> 
		</set>
		where 1=1
	</update>
</mapper>

